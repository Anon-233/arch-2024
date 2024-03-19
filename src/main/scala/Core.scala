package core

import chisel3._
import chisel3.util._

import bus._
import common._
import common.Consts._

import frontend._
import backend._

import difftest._

class Core (implicit val p: CoreConfig) extends Module {
    val io = IO(new Bundle {
        val ibus  = new IBusIO
        val dbus  = new DBusIO
        val trint = Input(Bool())
        val swint = Input(Bool())
        val exint = Input(Bool())
    })

    val iregfile = Module(new RegisterFile()(p))

    val ifu = Module(new Frontend)
    io.ibus <> ifu.io.ibus

    val idu = Module(new DecodeUnit)
    idu.io.in_uop := ifu.io.core.uop
    val dec_uop = idu.io.dec_uop

    val rru = Module(new RegisterReadUnit)
    rru.io.dec_uop := dec_uop
    iregfile.io.rports <> rru.io.rports
    val exe_req = rru.io.exe_req

    val exe_units = Seq(
        Module(new ALUUnit),
        Module(new BrUnit ),
        Module(new MemUnit),
    )
    val wb_arb = Module(new Arbiter(new ExeUnitResp, exe_units.size))

    exe_units.map { u =>
        u.io.req.valid := exe_req.valid && exe_req.bits.uop.fu_code === u.io.fu_code
        u.io.req.bits  := exe_req.bits
        wb_arb.io.in(exe_units.indexOf(u)) <> u.io.resp
    }
    ifu.io.core.redirect := exe_units(1).io.redirect
    ifu.io.core.stall    := exe_units(2).io.stall
    io.dbus <> exe_units(2).io.dbus

    wb_arb.io.out.ready   := true.B
    val exe_resp = wb_arb.io.out

    // writeback
    iregfile.io.wports(0).wen  := exe_resp.valid && exe_resp.bits.uop.rf_wen
    iregfile.io.wports(0).addr := exe_resp.bits.uop.ldst
    iregfile.io.wports(0).data := exe_resp.bits.data

    if (!p.FPGAPlatform) {
        // ----------------- InstrCommit -----------------
        val diff_instr = Module(new DifftestInstrCommit)
        diff_instr.io.clock    := clock
        diff_instr.io.coreid   := 0.U
        diff_instr.io.index    := 0.U
        diff_instr.io.valid    := RegNext(exe_resp.valid)
        diff_instr.io.pc       := RegNext(exe_resp.bits.uop.pc)
        diff_instr.io.instr    := RegNext(exe_resp.bits.uop.instr)
        diff_instr.io.skip     := false.B
        diff_instr.io.isRVC    := false.B
        diff_instr.io.scFailed := false.B
        diff_instr.io.wen      := RegNext(exe_resp.valid && exe_resp.bits.uop.rf_wen)
        diff_instr.io.wdata    := RegNext(exe_resp.bits.data)
        diff_instr.io.wdest    := RegNext(exe_resp.bits.uop.ldst)
        // ------------------ CSRState -------------------
        val diff_csrs = Module(new DifftestCSRState)
        diff_csrs.io := DontCare
        diff_csrs.io.clock  := clock
        diff_csrs.io.coreid := 0.U
        diff_csrs.io.priviledgeMode := 3.U
        // ------------------ TrapEvent ------------------
        val diff_trap = Module(new DifftestTrapEvent)
        diff_trap.io := DontCare
        diff_trap.io.clock  := clock
        diff_trap.io.coreid := 0.U
    }
}
