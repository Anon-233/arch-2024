package common

import chisel3._
import chisel3.util._

object Consts {
    val xLen: Int = 64
    val coreInstrBits: Int = 32
    val coreInstrBytes: Int = coreInstrBits / 8

    val addrLen: Int = xLen
    val dataLen: Int = xLen

    val MSIZE_SZ = 3
    val MSIZE1 = 0.U(MSIZE_SZ.W)
    val MSIZE2 = 1.U(MSIZE_SZ.W)
    val MSIZE4 = 2.U(MSIZE_SZ.W)
    val MSIZE8 = 3.U(MSIZE_SZ.W)

    val MLEN_SZ = 8
    val MLEN1   = 0x00.U(MLEN_SZ.W)
    val MLEN2   = 0x01.U(MLEN_SZ.W)
    val MLEN4   = 0x03.U(MLEN_SZ.W)
    val MLEN8   = 0x07.U(MLEN_SZ.W)
    val MLEN16  = 0x0f.U(MLEN_SZ.W)
    val MLEN32  = 0x1f.U(MLEN_SZ.W)
    val MLEN64  = 0x3f.U(MLEN_SZ.W)
    val MLEN128 = 0x7f.U(MLEN_SZ.W)
    val MLEN256 = 0xff.U(MLEN_SZ.W)

    val AXI_BURST_SZ = 2
    val AXI_BURST_FIXED    = 0.U(AXI_BURST_SZ.W)
    val AXI_BURST_INCR     = 1.U(AXI_BURST_SZ.W)
    val AXI_BURST_WRAP     = 2.U(AXI_BURST_SZ.W)
    val AXI_BURST_RESERVED = 3.U(AXI_BURST_SZ.W)

    val PC_START = 0x80000000L.U(addrLen.W)

    val BR_SZ: Int = 2  // change this when adding new branche types
    val BR_N    = 0.U(BR_SZ.W)
    val BR_JAL  = 1.U(BR_SZ.W)
    val BR_EQ   = 2.U(BR_SZ.W)
    val BR_JALR = 3.U(BR_SZ.W)

    val UOPC_SZ: Int = 4    // change this when adding new micro-ops
    // uopc-lab1
    val uopADDI  = 0.U
    val uopXORI  = 1.U
    val uopORI   = 2.U
    val uopANDI  = 3.U
    val uopLUI   = 4.U
    val uopJAL   = 5.U
    val uopBEQ   = 6.U
    val uopADD   = 7.U
    val uopSUB   = 8.U
    val uopAND   = 9.U
    val uopOR    = 10.U
    val uopXOR   = 11.U
    val uopAUIPC = 12.U
    val uopJALR  = 13.U
}
