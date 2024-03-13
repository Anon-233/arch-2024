package bus

import chisel3._
import chisel3.util._

import common.Consts._

class DBusReq extends Bundle {
    val valid    = Bool()
    val addr     = UInt(addrLen.W)
    val size     = UInt(MSIZE_SZ.W)
    val strobe   = UInt(8.W)
    val data     = UInt(dataLen.W)
}

class DBusResp extends Bundle {
    val addr_ok = Bool()
    val data_ok = Bool()
    val data    = UInt(dataLen.W)
}
