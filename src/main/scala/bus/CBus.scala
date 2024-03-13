package bus

import chisel3._
import chisel3.util._

import common.Consts._

class CBusReq extends Bundle {
    val valid    = Bool()
    val is_write = Bool()
    val size     = UInt(3.W)
    val addr     = UInt(addrLen.W)
    val strobe   = UInt(8.W)
    val data     = UInt(dataLen.W)
    val len      = UInt(8.W)
    val burst    = UInt(2.W)
}

class CBusResp extends Bundle {
    val ready = Bool()
    val last  = Bool()
    val data  = UInt(dataLen.W)
}
