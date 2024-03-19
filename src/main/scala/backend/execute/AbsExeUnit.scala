package backend

import chisel3._
import chisel3.util._

import common._
import common.Consts._

class AbsExeUnit(
    val is_alu: Boolean = true,
    val is_br: Boolean  = false,
    val is_mem: Boolean = false,
) extends Module {
    val io = IO(new Bundle {
        val req      = Flipped(Valid(new ExeUnitReq))
        val resp     = Decoupled(new ExeUnitResp)
        val redirect = if (is_br) Valid(new Redirect) else null
        val stall    = if (is_mem) Output(Bool()) else null
        val dbus     = if (is_mem) new DBusIO else null

        val fu_code  = Output(UInt(FUC_SZ.W))
    })
}
