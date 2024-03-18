package common

import chisel3._
import chisel3.util._

import common.Consts._

class MicroOp extends Bundle {
    val pc: UInt        = UInt(xLen.W)
    val instr: UInt     = UInt(coreInstrBits.W)
    val uopc: UInt      = UInt(UOPC_SZ.W)
    val fu_code: UInt   = UInt(FUC_SZ.W)

    val ldst_type: UInt = UInt(RT_X.getWidth.W)
    val lrs1_type: UInt = UInt(RT_X.getWidth.W)
    val lrs2_type: UInt = UInt(RT_X.getWidth.W)
    val ldst: UInt      = UInt(log2Ceil(lregSz).W)
    val lrs1: UInt      = UInt(log2Ceil(lregSz).W)
    val lrs2: UInt      = UInt(log2Ceil(lregSz).W)
    val imm: UInt       = UInt(xLen.W)

    val br_type: UInt   = UInt(BR_N.getWidth.W)
    val op_func: UInt   = UInt(FN_X.getWidth.W)
    val op1_sel: UInt   = UInt(OP1_X.getWidth.W)
    val op2_sel: UInt   = UInt(OP2_X.getWidth.W)
    val rf_wen: Bool    = Bool()

    def is_jal: Bool = uopc === uopJAL
    def is_jalr: Bool = uopc === uopJALR
}
