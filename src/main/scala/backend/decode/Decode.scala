package backend

import chisel3._
import chisel3.util._

import isa.Instructions._

import common._
import common.Consts._

import myutil._
import myutil.ImplicitCast.uintToBitPat

trait DecodeTable {
    val DC1 = BitPat.dontCare(1)
    def decode_default: List[BitPat] =
    //
    //       is val instr?
    //       |  micro-code                    is_br
    //       |     |                            | imm_sel
    //       |     |     func unit              |    |
    //       |     |      |     dst_type        |    |
    //       |     |      |     |    rs1_typ    |    |
    //       |     |      |     |    |          |    |
    //       |     |      |     |    | rs2_typ  |    |
    //       |     |      |     |    |    |     |    |
        List(N, uopNOP, FU_X, RT_X, DC1, DC1,   X,  IMM_X)

    val table: Array[(BitPat, List[BitPat])]
}

class CtrlSigs extends Bundle {
    val legal           = Bool()
    val uopc            = UInt(UOPC_SZ.W)
    val fu_code         = UInt(FUC_SZ.W)    // alu, mul, div, mem
    val dst_type        = UInt(1.W)
    val rs1_type        = UInt(1.W)
    val rs2_type        = UInt(1.W)
    val is_br           = Bool()
    val imm_sel         = UInt(3.W)

    def decode(instr: UInt, table: Iterable[(BitPat, List[BitPat])]) = {
        val decoder = DecodeLogic(instr, XDecode.decode_default, table)
        val sigs = Seq(
            legal, uopc, fu_code,
            dst_type, rs1_type, rs2_type,
            is_br, imm_sel
        )
        sigs zip decoder map { case (s, d) => s := d }
        this
    }
}

object XDecode extends DecodeTable  {
    val table: Array[(BitPat, List[BitPat])] = Array(
    //                    is val instr? 
    //                    | micro-code                               is_br
    //                    |    |                                       |  imm_sel
    //                    |    |       func_unit                       |     |
    //                    |    |          |    dst_type                |     |
    //                    |    |          |       |    rs1_typ         |     |
    //                    |    |          |       |       |            |     |
    //                    |    |          |       |       |    rs2_typ |     |
    //                    |    |          |       |       |       |    |     |
        NEMU_TRAP -> List(Y, uopNOP  , FU_ALU,  RT_X ,  RT_X ,  RT_X , N,  IMM_X),
        ADDI      -> List(Y, uopADDI , FU_ALU, RT_FIX, RT_FIX,  RT_X , N,  IMM_I),
        XORI      -> List(Y, uopXORI , FU_ALU, RT_FIX, RT_FIX,  RT_X , N,  IMM_I),
        ORI       -> List(Y, uopORI  , FU_ALU, RT_FIX, RT_FIX,  RT_X , N,  IMM_I),
        ANDI      -> List(Y, uopANDI , FU_ALU, RT_FIX, RT_FIX,  RT_X , N,  IMM_I),
        LUI       -> List(Y, uopLUI  , FU_ALU, RT_FIX,  RT_X ,  RT_X , N,  IMM_U),
        JAL       -> List(Y, uopJAL  , FU_BR , RT_FIX,  RT_X ,  RT_X , Y,  IMM_J),
        BEQ       -> List(Y, uopBEQ  , FU_BR , RT_X  , RT_FIX, RT_FIX, Y,  IMM_B),
        ADD       -> List(Y, uopADD  , FU_ALU, RT_FIX, RT_FIX, RT_FIX, N,  IMM_X),
        SUB       -> List(Y, uopSUB  , FU_ALU, RT_FIX, RT_FIX, RT_FIX, N,  IMM_X),
        AND       -> List(Y, uopAND  , FU_ALU, RT_FIX, RT_FIX, RT_FIX, N,  IMM_X),
        OR        -> List(Y, uopOR   , FU_ALU, RT_FIX, RT_FIX, RT_FIX, N,  IMM_X),
        XOR       -> List(Y, uopXOR  , FU_ALU, RT_FIX, RT_FIX, RT_FIX, N,  IMM_X),
        AUIPC     -> List(Y, uopAUIPC, FU_ALU, RT_FIX,  RT_X ,  RT_X , N,  IMM_U),
        JALR      -> List(Y, uopJALR , FU_BR , RT_FIX, RT_FIX,  RT_X , Y,  IMM_I),
        LD        -> List(Y, uopLD   , FU_MEM, RT_FIX, RT_FIX,  RT_X , N,  IMM_I),
        SD        -> List(Y, uopSD   , FU_MEM, RT_X  , RT_FIX, RT_FIX, N,  IMM_S)
    )
}

class DecodeUnitIO extends Bundle {
    val in_uop  = Flipped(Valid(new MicroOp))
    val dec_uop = Valid(new MicroOp)
}

class DecodeUnit extends Module {
    val io = IO(new DecodeUnitIO)

    val uop = WireInit(io.in_uop.bits)

    var decode_table = XDecode.table

    val instr = uop.instr
    val cs    = Wire(new CtrlSigs).decode(instr, decode_table)

    uop.uopc      := cs.uopc
    uop.fu_code   := cs.fu_code
    uop.ldst_type := cs.dst_type
    uop.lrs1_type := cs.rs1_type
    uop.lrs2_type := cs.rs2_type
    uop.ldst      := instr(11, 7)
    uop.lrs1      := instr(19, 15)
    uop.lrs2      := instr(24, 20)
    uop.imm       := MuxLookup(cs.imm_sel, 0.U)(Seq(
        IMM_I -> Cat(Fill(52, instr(31)), instr(31, 20)),
        IMM_S -> Cat(Fill(52, instr(31)), instr(31, 25), instr(11, 7)),
        IMM_B -> Cat(Fill(51, instr(31)), instr(31), instr(7), instr(30, 25), instr(11, 8), 0.U(1.W)),
        IMM_U -> Cat(Fill(32, instr(31)), instr(31, 12), Fill(12, 0.U)),
        IMM_J -> Cat(Fill(43, instr(31)), instr(31), instr(19, 12), instr(20), instr(30, 21), 0.U(1.W))
    ))

    io.dec_uop.valid := io.in_uop.valid
    io.dec_uop.bits  := uop
}
