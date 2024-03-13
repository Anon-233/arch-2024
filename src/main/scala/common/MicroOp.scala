package common

import chisel3._
import chisel3.util._

import common.Consts._

class MicroOp extends Bundle {
    val pc: UInt        = UInt(xLen.W)
    val instr: UInt     = UInt(coreInstrBits.W)
    val uopc: UInt      = UInt(UOPC_SZ.W)
}
