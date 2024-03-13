package core

import chisel3._
import chisel3.util._

import bus._
import common._
import common.Consts._

import difftest._

class Core (implicit val p: CoreConfig) extends Module {
    val io = IO(new Bundle {
        val ireq  = Output(new IBusReq)
        val iresp = Input(new IBusResp)
        val dreq  = Output(new DBusReq)
        val dresp = Input(new DBusResp)
        val trint = Input(Bool())
        val swint = Input(Bool())
        val exint = Input(Bool())
    })

    io.ireq := DontCare
    io.dreq := DontCare

    val this_pc = RegInit(PC_START)
    when (io.iresp.data_ok) {
        this_pc := this_pc + 4.U
    }

    io.ireq.valid := true.B
    io.ireq.addr  := this_pc
    val fetch_valid = io.iresp.addr_ok && io.iresp.data_ok
    val this_instr  = io.iresp.data

    if (!p.FPGAPlatform) {
        // ----------------- InstrCommit -----------------
        val diff_instr = Module(new DifftestInstrCommit)
        diff_instr.io := DontCare
        diff_instr.io.clock  := clock
        diff_instr.io.coreid := 0.U
        diff_instr.io.index  := 0.U
        diff_instr.io.valid := fetch_valid
        diff_instr.io.pc    := this_pc
        diff_instr.io.instr := this_instr
        // --------------- ArchIntRegState ---------------
        val diff_regs = Module(new DifftestArchIntRegState)
        diff_regs.io := DontCare
        diff_regs.io.clock := clock
        diff_regs.io.coreid := 0.U
        // ------------------ CSRState -------------------
        val diff_csrs = Module(new DifftestCSRState)
        diff_csrs.io := DontCare
        diff_csrs.io.clock := clock
        diff_csrs.io.coreid := 0.U
        diff_csrs.io.priviledgeMode := 3.U
        // ------------------ TrapEvent ------------------
        val diff_trap = Module(new DifftestTrapEvent)
        diff_trap.io := DontCare
        diff_trap.io.clock := clock
        diff_trap.io.coreid := 0.U
    }
}
