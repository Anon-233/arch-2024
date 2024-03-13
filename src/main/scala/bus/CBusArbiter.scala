package bus

import chisel3._
import chisel3.util._

import common.Consts._

class CBusArbiter extends Module {
    val io = IO(new Bundle {
        val ireq  = Input(new CBusReq)
        val dreq  = Input(new CBusReq)
        val iresp = Output(new CBusResp)
        val dresp = Output(new CBusResp)
        val oreq  = Output(new CBusReq)
        val oresp = Input(new CBusResp)
    })

    val busy  = RegInit(false.B)
    val index = RegInit(0.U(1.W))
    val selected_idx = Mux(io.dreq.valid, 1.U, 0.U)
    val selected_req = Mux(selected_idx === 0.U, io.ireq, io.dreq)

    when (busy) {
        when (io.oresp.last) {
            busy := false.B
        }
    } .otherwise {
        busy  := selected_req.valid
        index := selected_idx
    }

    io.oreq := 0.U.asTypeOf(new CBusReq)
    when (busy) { io.oreq := Mux(index === 0.U, io.ireq, io.dreq) }

    io.iresp := Mux(busy && index === 0.U, io.oresp, 0.U.asTypeOf(new CBusResp))
    io.dresp := Mux(busy && index === 1.U, io.oresp, 0.U.asTypeOf(new CBusResp))
}
