package backend

import chisel3._
import chisel3.util._

import common._
import common.Consts._

class BrUnit extends AbsExeUnit(
    is_alu = false,
    is_br  = true,
    is_mem = false,
) {
    io.fu_code := FU_BR

    val uop = io.req.bits.uop
    val rs1 = io.req.bits.rs1_data

    val br_eq  = (io.req.bits.rs1_data === io.req.bits.rs2_data)
    val br_taken = MuxLookup(uop.br_type, false.B)(Seq(
        BR_BEQ  -> br_eq,
    ))
    val target = MuxLookup(uop.br_type, 0.U)(Seq(
        BR_BEQ  -> (uop.pc + uop.imm),
        BR_JAL  -> (uop.pc + uop.imm),
        BR_JALR -> (rs1 + uop.imm),
    ))

    io.resp.valid     := io.req.valid
    io.resp.bits.uop  := uop
    io.resp.bits.data := uop.pc + 4.U

    io.redirect.valid := br_taken || uop.is_jal || uop.is_jalr
    io.redirect.bits.target := target
}
