package myutil

import chisel3._
import chisel3.util._
import scala.language.implicitConversions

import common._
import common.Consts._

object ImplicitCast {
    implicit def uintToBitPat(x: UInt): BitPat = BitPat(x)
}
