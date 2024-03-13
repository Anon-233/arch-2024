package ram

import chisel3._
import chisel3.util._

import bus._

class RAMHelper2 extends BlackBox {
    val io = IO(new Bundle {
        val clk = Input(Clock())
        val reset = Input(Bool())
        val oreq  = Input(UInt((new CBusReq).getWidth.W))
        val oresp = Output(UInt((new CBusResp).getWidth.W))
        val trint = Output(Bool())
        val swint = Output(Bool())
        val exint = Output(Bool())
    })
}
