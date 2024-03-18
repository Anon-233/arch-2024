package common

import chisel3._
import chisel3.util._

import bus._

import common.Consts._

class IBusIO extends Bundle {
    val req  = Output(new IBusReq)
    val resp = Input(new IBusResp)
}

class DBusIO extends Bundle {
    val req  = Output(new DBusReq)
    val resp = Input(new DBusResp)
}

class Redirect extends Bundle {
    val target = UInt(addrLen.W)
}

class ExeUnitReq extends Bundle {
    val uop      = new MicroOp
    val rs1_data = UInt(xLen.W)
    val rs2_data = UInt(xLen.W)
}

class ExeUnitResp extends Bundle {
    val uop = new MicroOp
    val data = UInt(xLen.W)
}
