package bus

import chisel3._
import chisel3.util._

import common.Consts._

class DBus2CBus extends Module {
    val io = IO(new Bundle {
        val dreq  = Input(new DBusReq)
        val dresp = Output(new DBusResp)
        val dcreq = Output(new CBusReq)
        val dcresp = Input(new CBusResp)
    })

    io.dcreq.valid    := io.dreq.valid
    io.dcreq.is_write := io.dreq.strobe.orR
    io.dcreq.size     := io.dreq.size
    io.dcreq.addr     := io.dreq.addr
    io.dcreq.strobe   := io.dreq.strobe
    io.dcreq.data     := io.dreq.data
    io.dcreq.len      := MLEN1
    io.dcreq.burst    := AXI_BURST_FIXED

    val okay = io.dcresp.ready && io.dcresp.last

    io.dresp.addr_ok := okay
    io.dresp.data_ok := okay
    io.dresp.data    := io.dcresp.data
}

class IBus2CBus extends Module {
    val io = IO(new Bundle {
        val ireq  = Input(new IBusReq)
        val iresp = Output(new IBusResp)
        val icreq = Output(new CBusReq)
        val icresp = Input(new CBusResp)
    })

    val dresp = Wire(new DBusResp)
    val d2c = Module(new DBus2CBus)
    dresp := d2c.io.dresp
    // convert i to d
    d2c.io.dreq.valid  := io.ireq.valid
    d2c.io.dreq.addr   := io.ireq.addr
    d2c.io.dreq.size   := MSIZE4
    d2c.io.dreq.strobe := 0.U
    d2c.io.dreq.data   := 0.U
    // convert d to c
    d2c.io.dcresp := io.icresp
    io.icreq      := d2c.io.dcreq
    // convert d to i
    io.iresp.addr_ok := d2c.io.dresp.addr_ok
    io.iresp.data_ok := d2c.io.dresp.data_ok
    io.iresp.data    := Mux(io.ireq.addr(2), d2c.io.dresp.data(63, 32), d2c.io.dresp.data(31, 0))
}
