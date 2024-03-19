package backend

import chisel3._
import chisel3.util._

import bus._

import common._
import common.Consts._

class MemUnit extends AbsExeUnit(
    is_alu = false,
    is_br  = false,
    is_mem = true,
) {
    io.fu_code := FU_MEM

    val uop = io.req.bits.uop
    val rs1 = io.req.bits.rs1_data
    val rs2 = io.req.bits.rs2_data

    io.dbus.req.valid  := io.req.valid
    io.dbus.req.addr   := rs1 + uop.imm
    io.dbus.req.size   := MSIZE8
    io.dbus.req.strobe := Mux(uop.uopc === uopLD, 0x00.U, 0xff.U)
    io.dbus.req.data   := rs2

    val stall_en = RegInit(false.B)
    when (io.dbus.resp.data_ok && io.dbus.resp.addr_ok) {
        stall_en := false.B
    } .elsewhen (io.dbus.req.valid) {
        stall_en := true.B
    }
    io.stall := stall_en

    io.resp.valid     := (
        io.req.valid &&
        io.dbus.resp.addr_ok && io.dbus.resp.data_ok
    )
    io.resp.bits.uop  := uop
    io.resp.bits.data := io.dbus.resp.data
}
