package backend

import chisel3._
import chisel3.util._

import common._
import common.Consts._

import difftest._

class RegisterFileReadPortIO (
    val addrBits: Int, val dataBits: Int
) extends Bundle {
    val addr = Input(UInt(addrBits.W))
    val data = Output(UInt(dataBits.W))
}

class RegisterFileWritePortIO (
    val addrBits: Int, val dataBits: Int
) extends Bundle {
    val wen  = Input(Bool())
    val addr = Input(UInt(addrBits.W))
    val data = Input(UInt(dataBits.W))
}

class RegisterFile()(implicit val p: CoreConfig) extends Module {
    val io = IO(new Bundle {
        val rports = Vec(2, new RegisterFileReadPortIO(5, xLen))
        val wports = Vec(1, new RegisterFileWritePortIO(5, xLen))
    })

    val regs = RegInit(VecInit(Seq.fill(lregSz)(0.U(xLen.W))))

    when (io.wports(0).addr =/= 0.U && io.wports(0).wen) {
        regs(io.wports(0).addr) := io.wports(0).data
    }

    io.rports(0).data := regs(io.rports(0).addr)
    io.rports(1).data := regs(io.rports(1).addr)

    if (!p.FPGAPlatform) {
        // --------------- ArchIntRegState ---------------
        val diff_regs = Module(new DifftestArchIntRegState)
        diff_regs.io.clock  := clock
        diff_regs.io.coreid := 0.U
        for (i <- 0 until 32) {
            diff_regs.io.gpr(i) := regs(i)
        }
    }
}
