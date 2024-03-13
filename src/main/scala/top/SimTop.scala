package top

import chisel3._
import chisel3.util._

import bus._
import ram._
import core._
import common._

import difftest._

class SimTop extends Module {
    val io = IO(new Bundle {
        val logCtrl = new LogCtrlIO
        val perfInfo = new PerfInfoIO
        val uart = new UARTIO
    })
    io.logCtrl  := DontCare
    io.perfInfo := DontCare
    io.uart     := DontCare

    val ireq   = Wire(new IBusReq)
    val iresp  = Wire(new IBusResp)
    val dreq   = Wire(new DBusReq)
    val dresp  = Wire(new DBusResp)
    val icreq  = Wire(new CBusReq)
    val icresp = Wire(new CBusResp)
    val dcreq  = Wire(new CBusReq)
    val dcresp = Wire(new CBusResp)
    val oreq   = Wire(new CBusReq)
    val oresp  = Wire(new CBusResp)

    val i2c = Module(new IBus2CBus)
    i2c.io.ireq   := ireq
    iresp         := i2c.io.iresp
    icreq         := i2c.io.icreq
    i2c.io.icresp := icresp
    val d2c = Module(new DBus2CBus)
    d2c.io.dreq   := dreq
    dresp         := d2c.io.dresp
    dcreq         := d2c.io.dcreq
    d2c.io.dcresp := dcresp

    val arb = Module(new CBusArbiter)
    arb.io.ireq := icreq
    arb.io.dreq := dcreq
    icresp := arb.io.iresp
    dcresp := arb.io.dresp
    oreq := arb.io.oreq
    arb.io.oresp := oresp

    val ram = Module(new RAMHelper2)
    ram.io.clk := clock
    ram.io.reset := reset
    ram.io.oreq := oreq.asUInt
    oresp := ram.io.oresp.asTypeOf(new CBusResp)

    lazy val conf = CoreConfig()
    val core = Module(new Core()(conf))
    ireq := core.io.ireq
    core.io.iresp := iresp
    dreq := core.io.dreq
    core.io.dresp := dresp
    core.io.trint := ram.io.trint
    core.io.swint := ram.io.swint
    core.io.exint := ram.io.exint
}
