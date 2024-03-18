package common

import chisel3._
import chisel3.util._

object Consts {
    val X = BitPat("b?")
    val Y = BitPat("b1")
    val N = BitPat("b0")

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
    val BR_BEQ  = 2.U(BR_SZ.W)
    val BR_JALR = 3.U(BR_SZ.W)

    val FUC_SZ: Int = 3  // change this when adding new function units
    val FU_X        = 0.U(FUC_SZ.W)
    val FU_ALU      = 1.U(FUC_SZ.W)
    val FU_BR       = 2.U(FUC_SZ.W)
    val FU_MEM      = 3.U(FUC_SZ.W)

    val OP1_X    = BitPat("b??")
    val OP1_RS1  = 0.U(2.W) // Register Source #1
    val OP1_ZERO = 1.U(2.W) // constant 0
    val OP1_PC   = 2.U(2.W) // PC

    val OP2_X    = BitPat("b???")
    val OP2_RS2  = 0.U(3.W) // Register Source #2
    val OP2_IMM  = 1.U(3.W) // immediate
    val OP2_NEXT = 2.U(3.W) // constant 4 (for PC + 4)

    val FN_SZ: Int = 4  // change this when adding new ALU functions
    val FN_X    = 0.U(FN_SZ.W)

    val FN_ADD  = 1.U(FN_SZ.W)
    val FN_SUB  = 2.U(FN_SZ.W)
    val FN_AND  = 3.U(FN_SZ.W)
    val FN_OR   = 4.U(FN_SZ.W)
    val FN_XOR  = 5.U(FN_SZ.W)

    val FN_JAL  = 0.U(FN_SZ.W)
    val FN_JALR = 1.U(FN_SZ.W)
    val FN_BEQ  = 2.U(FN_SZ.W)

    val FN_LD   = 0.U(FN_SZ.W)
    val FN_SD   = 1.U(FN_SZ.W)

    val lregSz: Int = 32

    val RT_X    = 0.U(1.W)
    val RT_FIX  = 1.U(1.W)

    val IMM_X = BitPat("b???")
    val IMM_I = 0.U(3.W)  // I-Type
    val IMM_S = 1.U(3.W)  // S-Type
    val IMM_B = 2.U(3.W)  // SB-Type
    val IMM_U = 3.U(3.W)  // U-Type
    val IMM_J = 4.U(3.W)  // UJ-Type

    val UOPC_SZ: Int = 5    // change this when adding new micro-ops
    val uopNOP   =  0.U
    val uopADDI  =  1.U
    val uopXORI  =  2.U
    val uopORI   =  3.U
    val uopANDI  =  4.U
    val uopLUI   =  5.U
    val uopJAL   =  6.U
    val uopBEQ   =  7.U
    val uopADD   =  8.U
    val uopSUB   =  9.U
    val uopAND   = 10.U
    val uopOR    = 11.U
    val uopXOR   = 12.U
    val uopAUIPC = 13.U
    val uopJALR  = 14.U
    val uopLD    = 15.U
    val uopSD    = 16.U
}
