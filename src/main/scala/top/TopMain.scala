package top

//> using scala "2.13.12"
//> using dep "org.chipsalliance::chisel::6.2.0"
//> using plugin "org.chipsalliance:::chisel-plugin::6.2.0"
//> using options "-unchecked", "-deprecation", "-language:reflectiveCalls", "-feature", "-Xcheckinit", "-Xfatal-warnings", "-Ywarn-dead-code", "-Ywarn-unused", "-Ymacro-annotations"

import chisel3._
// _root_ disambiguates from package chisel3.util.circt if user imports chisel3.util._
import _root_.circt.stage.ChiselStage

object Main extends App {
    println(chisel3.BuildInfo.toString)

    val targetDirectory = args.head

    val firtoolOpts = Array(
        "-O=release",
        "--disable-annotation-unknown",
        "--lowering-options=explicitBitcast,disallowLocalVariables,disallowPortDeclSharing",
        // "--repl-seq-mem",
        // "--repl-seq-mem-file=Foo.sv.conf"
    )

    ChiselStage.emitSystemVerilogFile(
        gen = new SimTop,
        args = Array("--target-dir", targetDirectory),
        firtoolOpts = firtoolOpts
    )
}