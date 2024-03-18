package backend

import chisel3._
import chisel3.util._

import common._
import common.Consts._

import myutil.ImplicitCast.uintToBitPat

class FuncUnitCtrlSigs extends Bundle {
    val br_type        = UInt(BR_N.getWidth.W)
    val op_func        = Bits(FN_SZ.W)
    val op1_sel        = UInt(OP1_X.getWidth.W)
    val op2_sel        = UInt(OP2_X.getWidth.W)
    val rf_wen         = Bool()

    def decode(uopc: UInt, table: Iterable[(BitPat, List[BitPat])]) = {
        val decoder = DecodeLogic(uopc, AluDecode.default, table)
        val sigs = Seq(
            br_type, op_func,
            op1_sel, op2_sel, rf_wen
        )
        sigs zip decoder map {case(s, d) => s := d}
        this
    }
}

abstract trait FuncUnitDecodeConstants {
    val default: List[BitPat] = List[BitPat](BR_N, FN_ADD, OP1_X, OP2_X, N)
    val table: Array[(BitPat, List[BitPat])]
}

object AluDecode extends FuncUnitDecodeConstants {
    val table: Array[(BitPat, List[BitPat])] =
        Array[(BitPat, List[BitPat])](
            BitPat(uopADDI)  -> List(BR_N, FN_ADD, OP1_RS1 , OP2_IMM, Y),
            BitPat(uopXORI)  -> List(BR_N, FN_XOR, OP1_RS1 , OP2_IMM, Y),
            BitPat(uopORI)   -> List(BR_N, FN_OR , OP1_RS1 , OP2_IMM, Y),
            BitPat(uopANDI)  -> List(BR_N, FN_AND, OP1_RS1 , OP2_IMM, Y),
            BitPat(uopLUI)   -> List(BR_N, FN_ADD, OP1_ZERO, OP2_IMM, Y),
            BitPat(uopADD)   -> List(BR_N, FN_ADD, OP1_RS1 , OP2_RS2, Y),
            BitPat(uopSUB)   -> List(BR_N, FN_SUB, OP1_RS1 , OP2_RS2, Y),
            BitPat(uopAND)   -> List(BR_N, FN_AND, OP1_RS1 , OP2_RS2, Y),
            BitPat(uopOR)    -> List(BR_N, FN_OR , OP1_RS1 , OP2_RS2, Y),
            BitPat(uopXOR)   -> List(BR_N, FN_XOR, OP1_RS1 , OP2_RS2, Y),
            BitPat(uopAUIPC) -> List(BR_N, FN_ADD, OP1_PC  , OP2_IMM, Y),
        )
}

object BrDecode extends FuncUnitDecodeConstants {
    val table: Array[(BitPat, List[BitPat])] =
        Array[(BitPat, List[BitPat])](
            BitPat(uopJAL)  -> List(BR_JAL , FN_JAL , OP1_PC , OP2_NEXT, Y),
            BitPat(uopBEQ)  -> List(BR_BEQ , FN_BEQ , OP1_RS1, OP2_RS2 , N),
            BitPat(uopJALR) -> List(BR_JALR, FN_JALR, OP1_PC , OP2_NEXT, Y),
        )
}

object MemDecode extends FuncUnitDecodeConstants {
    val table: Array[(BitPat, List[BitPat])] =
        Array[(BitPat, List[BitPat])](
            BitPat(uopLD) -> List(BR_N, FN_LD, OP1_RS1, OP2_IMM, Y),
            BitPat(uopSD) -> List(BR_N, FN_SD, OP1_RS1, OP2_IMM, N),
        )
}

class FuncUnitDecode extends Module {
    val io = IO(new Bundle {
        val in_uop  = Input(new MicroOp)
        val out_uop = Output(new MicroOp)
    })

    val uop = WireInit(io.in_uop)

    var dec_table = AluDecode.table
    dec_table ++= BrDecode.table
    dec_table ++= MemDecode.table

    val rrd_cs = Wire(new FuncUnitCtrlSigs).decode(uop.uopc, dec_table)

    uop.br_type := rrd_cs.br_type
    uop.op_func := rrd_cs.op_func
    uop.op1_sel := rrd_cs.op1_sel
    uop.op2_sel := rrd_cs.op2_sel
    uop.rf_wen  := rrd_cs.rf_wen

    io.out_uop := uop
}
