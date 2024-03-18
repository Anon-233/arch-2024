package frontend

import chisel3._
import chisel3.util._

import bus._
import common._
import common.Consts._

class Frontend2Core extends Bundle {
    val stall    = Input(Bool())
    val redirect = Flipped(Valid(new Redirect))
    val uop      = Valid(new MicroOp)
}

class Frontend extends Module {
    val io = IO(new Bundle {
        val ibus  = new IBusIO
        val core  = new Frontend2Core
    })

    val vpc = RegInit(PC_START)
// --------------------------------------------------
// ------------------ instr fetch -------------------
    io.ibus.req.valid := !reset.asBool && !io.core.stall
    io.ibus.req.addr  := vpc

    val instr_valid = io.ibus.resp.addr_ok && io.ibus.resp.data_ok
    val instr       = io.ibus.resp.data

    val vpc_reg   = RegEnable(vpc  , instr_valid)
    val instr_reg = RegEnable(instr, instr_valid)
// --------------------------------------------------
// ----------------- select next pc -----------------
    val vpc_nxt = WireInit(vpc)
    when (instr_valid) {
        vpc_nxt := vpc + 4.U
    }
    when (io.core.redirect.valid) {
        vpc_nxt := io.core.redirect.bits.target
    }
    vpc := vpc_nxt
// --------------------------------------------------
// ------------------- to core ----------------------
    io.core.uop := DontCare
    io.core.uop.valid      := instr_valid || io.core.stall
    io.core.uop.bits.pc    := Mux(io.core.stall, vpc_reg, vpc)
    io.core.uop.bits.instr := Mux(io.core.stall, instr_reg, instr)
// --------------------------------------------------
}
