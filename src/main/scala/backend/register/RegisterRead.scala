package backend

import chisel3._
import chisel3.util._

import common._
import common.Consts._

class RegisterReadUnit extends Module {
    val io = IO(new Bundle {
        val dec_uop = Flipped(Valid(new MicroOp))
        val rports  = Flipped(Vec(2, new RegisterFileReadPortIO(log2Ceil(lregSz), xLen)))
        val exe_req = Valid(new ExeUnitReq)
    })

    io.rports(0).addr := io.dec_uop.bits.lrs1
    io.rports(1).addr := io.dec_uop.bits.lrs2
    val rdata1 = io.rports(0).data
    val rdata2 = io.rports(1).data

    val rrdDecodeUnit = Module(new FuncUnitDecode)
    rrdDecodeUnit.io.in_uop := io.dec_uop.bits
    val rrd_uop = rrdDecodeUnit.io.out_uop

    io.exe_req.valid         := io.dec_uop.valid
    io.exe_req.bits.uop      := rrd_uop
    io.exe_req.bits.rs1_data := rdata1
    io.exe_req.bits.rs2_data := rdata2
}
