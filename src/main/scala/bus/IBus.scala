package bus

import chisel3._
import chisel3.util._

import common.Consts._

class IBusReq extends Bundle {
    val valid = Bool()
    val addr  = UInt(addrLen.W)
}

class IBusResp extends Bundle {
    val addr_ok = Bool()
    val data_ok = Bool()
    val data    = UInt(dataLen.W)
}
