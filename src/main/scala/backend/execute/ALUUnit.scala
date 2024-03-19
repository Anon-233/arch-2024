package backend

import chisel3._
import chisel3.util._

import common._
import common.Consts._

class ALUUnit extends AbsExeUnit(
    is_alu = true,
    is_br  = false,
    is_mem = false,
) {
    io.fu_code := FU_ALU

    val uop = io.req.bits.uop
    val alu_op1 = MuxLookup(uop.op1_sel, 0.U)(Seq(
        OP1_RS1 -> io.req.bits.rs1_data,
        OP1_PC  -> uop.pc,
    ))
    val alu_op2 = MuxLookup(uop.op2_sel, 0.U)(Seq(
        OP2_RS2  -> io.req.bits.rs2_data,
        OP2_IMM  -> uop.imm,
        OP2_NEXT -> 4.U,
    ))
    val alu_out = MuxLookup(uop.op_func, 0.U)(Seq(
        FN_ADD  -> (alu_op1 + alu_op2),
        FN_SUB  -> (alu_op1 - alu_op2),
        FN_AND  -> (alu_op1 & alu_op2),
        FN_OR   -> (alu_op1 | alu_op2),
        FN_XOR  -> (alu_op1 ^ alu_op2),
    ))

    io.resp.valid     := io.req.valid
    io.resp.bits.uop  := uop
    io.resp.bits.data := alu_out
}
