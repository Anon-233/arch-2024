// Generated by CIRCT firtool-1.62.0
// Standard header to adapt well known macros for register randomization.
`ifndef RANDOMIZE
  `ifdef RANDOMIZE_REG_INIT
    `define RANDOMIZE
  `endif // RANDOMIZE_REG_INIT
`endif // not def RANDOMIZE

// RANDOM may be set to an expression that produces a 32-bit random unsigned value.
`ifndef RANDOM
  `define RANDOM $random
`endif // not def RANDOM

// Users can define INIT_RANDOM as general code that gets injected into the
// initializer block for modules with registers.
`ifndef INIT_RANDOM
  `define INIT_RANDOM
`endif // not def INIT_RANDOM

// If using random initialization, you can also define RANDOMIZE_DELAY to
// customize the delay used, otherwise 0.002 is used.
`ifndef RANDOMIZE_DELAY
  `define RANDOMIZE_DELAY 0.002
`endif // not def RANDOMIZE_DELAY

// Define INIT_RANDOM_PROLOG_ for use in our modules below.
`ifndef INIT_RANDOM_PROLOG_
  `ifdef RANDOMIZE
    `ifdef VERILATOR
      `define INIT_RANDOM_PROLOG_ `INIT_RANDOM
    `else  // VERILATOR
      `define INIT_RANDOM_PROLOG_ `INIT_RANDOM #`RANDOMIZE_DELAY begin end
    `endif // VERILATOR
  `else  // RANDOMIZE
    `define INIT_RANDOM_PROLOG_
  `endif // RANDOMIZE
`endif // not def INIT_RANDOM_PROLOG_

// Include register initializers in init blocks unless synthesis is set
`ifndef SYNTHESIS
  `ifndef ENABLE_INITIAL_REG_
    `define ENABLE_INITIAL_REG_
  `endif // not def ENABLE_INITIAL_REG_
`endif // not def SYNTHESIS

// Include rmemory initializers in init blocks unless synthesis is set
`ifndef SYNTHESIS
  `ifndef ENABLE_INITIAL_MEM_
    `define ENABLE_INITIAL_MEM_
  `endif // not def ENABLE_INITIAL_MEM_
`endif // not def SYNTHESIS

module DBus2CBus(	// src/main/scala/bus/Convert.scala:8:7
  input         io_dreq_valid,	// src/main/scala/bus/Convert.scala:9:16
  input  [63:0] io_dreq_addr,	// src/main/scala/bus/Convert.scala:9:16
  input  [2:0]  io_dreq_size,	// src/main/scala/bus/Convert.scala:9:16
  output        io_dresp_addr_ok,	// src/main/scala/bus/Convert.scala:9:16
  output        io_dresp_data_ok,	// src/main/scala/bus/Convert.scala:9:16
  output [63:0] io_dresp_data,	// src/main/scala/bus/Convert.scala:9:16
  output        io_dcreq_valid,	// src/main/scala/bus/Convert.scala:9:16
  output [2:0]  io_dcreq_size,	// src/main/scala/bus/Convert.scala:9:16
  output [63:0] io_dcreq_addr,	// src/main/scala/bus/Convert.scala:9:16
  input         io_dcresp_ready,	// src/main/scala/bus/Convert.scala:9:16
  input         io_dcresp_last,	// src/main/scala/bus/Convert.scala:9:16
  input  [63:0] io_dcresp_data	// src/main/scala/bus/Convert.scala:9:16
);

  wire okay = io_dcresp_ready & io_dcresp_last;	// src/main/scala/bus/Convert.scala:25:32
  assign io_dresp_addr_ok = okay;	// src/main/scala/bus/Convert.scala:8:7, :25:32
  assign io_dresp_data_ok = okay;	// src/main/scala/bus/Convert.scala:8:7, :25:32
  assign io_dresp_data = io_dcresp_data;	// src/main/scala/bus/Convert.scala:8:7
  assign io_dcreq_valid = io_dreq_valid;	// src/main/scala/bus/Convert.scala:8:7
  assign io_dcreq_size = io_dreq_size;	// src/main/scala/bus/Convert.scala:8:7
  assign io_dcreq_addr = io_dreq_addr;	// src/main/scala/bus/Convert.scala:8:7
endmodule

module IBus2CBus(	// src/main/scala/bus/Convert.scala:32:7
  input  [63:0] io_ireq_addr,	// src/main/scala/bus/Convert.scala:33:16
  output        io_iresp_addr_ok,	// src/main/scala/bus/Convert.scala:33:16
  output        io_iresp_data_ok,	// src/main/scala/bus/Convert.scala:33:16
  output [63:0] io_iresp_data,	// src/main/scala/bus/Convert.scala:33:16
  output        io_icreq_valid,	// src/main/scala/bus/Convert.scala:33:16
  output [2:0]  io_icreq_size,	// src/main/scala/bus/Convert.scala:33:16
  output [63:0] io_icreq_addr,	// src/main/scala/bus/Convert.scala:33:16
  input         io_icresp_ready,	// src/main/scala/bus/Convert.scala:33:16
  input         io_icresp_last,	// src/main/scala/bus/Convert.scala:33:16
  input  [63:0] io_icresp_data	// src/main/scala/bus/Convert.scala:33:16
);

  wire [63:0] _d2c_io_dresp_data;	// src/main/scala/bus/Convert.scala:41:21
  DBus2CBus d2c (	// src/main/scala/bus/Convert.scala:41:21
    .io_dreq_valid    (1'h1),	// src/main/scala/bus/Convert.scala:33:16, :41:21
    .io_dreq_addr     (io_ireq_addr),
    .io_dreq_size     (3'h2),	// src/main/scala/bus/Convert.scala:46:24
    .io_dresp_addr_ok (io_iresp_addr_ok),
    .io_dresp_data_ok (io_iresp_data_ok),
    .io_dresp_data    (_d2c_io_dresp_data),
    .io_dcreq_valid   (io_icreq_valid),
    .io_dcreq_size    (io_icreq_size),
    .io_dcreq_addr    (io_icreq_addr),
    .io_dcresp_ready  (io_icresp_ready),
    .io_dcresp_last   (io_icresp_last),
    .io_dcresp_data   (io_icresp_data)
  );
  assign io_iresp_data =
    {32'h0, io_ireq_addr[2] ? _d2c_io_dresp_data[63:32] : _d2c_io_dresp_data[31:0]};	// src/main/scala/bus/Convert.scala:32:7, :41:21, :55:{22,28,41,63,90}
endmodule

module CBusArbiter(	// src/main/scala/bus/CBusArbiter.scala:8:7
  input         clock,	// src/main/scala/bus/CBusArbiter.scala:8:7
  input         reset,	// src/main/scala/bus/CBusArbiter.scala:8:7
  input         io_ireq_valid,	// src/main/scala/bus/CBusArbiter.scala:9:16
  input  [2:0]  io_ireq_size,	// src/main/scala/bus/CBusArbiter.scala:9:16
  input  [63:0] io_ireq_addr,	// src/main/scala/bus/CBusArbiter.scala:9:16
  input         io_dreq_valid,	// src/main/scala/bus/CBusArbiter.scala:9:16
  input  [2:0]  io_dreq_size,	// src/main/scala/bus/CBusArbiter.scala:9:16
  input  [63:0] io_dreq_addr,	// src/main/scala/bus/CBusArbiter.scala:9:16
  output        io_iresp_ready,	// src/main/scala/bus/CBusArbiter.scala:9:16
  output        io_iresp_last,	// src/main/scala/bus/CBusArbiter.scala:9:16
  output [63:0] io_iresp_data,	// src/main/scala/bus/CBusArbiter.scala:9:16
  output        io_dresp_ready,	// src/main/scala/bus/CBusArbiter.scala:9:16
  output        io_dresp_last,	// src/main/scala/bus/CBusArbiter.scala:9:16
  output [63:0] io_dresp_data,	// src/main/scala/bus/CBusArbiter.scala:9:16
  output        io_oreq_valid,	// src/main/scala/bus/CBusArbiter.scala:9:16
  output [2:0]  io_oreq_size,	// src/main/scala/bus/CBusArbiter.scala:9:16
  output [63:0] io_oreq_addr,	// src/main/scala/bus/CBusArbiter.scala:9:16
  input         io_oresp_ready,	// src/main/scala/bus/CBusArbiter.scala:9:16
  input         io_oresp_last,	// src/main/scala/bus/CBusArbiter.scala:9:16
  input  [63:0] io_oresp_data	// src/main/scala/bus/CBusArbiter.scala:9:16
);

  reg  busy;	// src/main/scala/bus/CBusArbiter.scala:18:24
  reg  index;	// src/main/scala/bus/CBusArbiter.scala:19:24
  wire _io_iresp_T_1 = busy & ~index;	// src/main/scala/bus/CBusArbiter.scala:18:24, :19:24, :35:{26,35}
  wire _io_dresp_T_1 = busy & index;	// src/main/scala/bus/CBusArbiter.scala:18:24, :19:24, :36:26
  always @(posedge clock) begin	// src/main/scala/bus/CBusArbiter.scala:8:7
    if (reset) begin	// src/main/scala/bus/CBusArbiter.scala:8:7
      busy <= 1'h0;	// src/main/scala/bus/CBusArbiter.scala:8:7, :18:24
      index <= 1'h0;	// src/main/scala/bus/CBusArbiter.scala:8:7, :19:24
    end
    else if (busy)	// src/main/scala/bus/CBusArbiter.scala:18:24
      busy <= ~io_oresp_last & busy;	// src/main/scala/bus/CBusArbiter.scala:18:24, :24:30, :25:18
    else begin	// src/main/scala/bus/CBusArbiter.scala:18:24
      busy <= io_dreq_valid ? io_dreq_valid : io_ireq_valid;	// src/main/scala/bus/CBusArbiter.scala:18:24, :21:27
      index <= io_dreq_valid;	// src/main/scala/bus/CBusArbiter.scala:19:24
    end
  end // always @(posedge)
  `ifdef ENABLE_INITIAL_REG_	// src/main/scala/bus/CBusArbiter.scala:8:7
    `ifdef FIRRTL_BEFORE_INITIAL	// src/main/scala/bus/CBusArbiter.scala:8:7
      `FIRRTL_BEFORE_INITIAL	// src/main/scala/bus/CBusArbiter.scala:8:7
    `endif // FIRRTL_BEFORE_INITIAL
    logic [31:0] _RANDOM[0:0];	// src/main/scala/bus/CBusArbiter.scala:8:7
    initial begin	// src/main/scala/bus/CBusArbiter.scala:8:7
      `ifdef INIT_RANDOM_PROLOG_	// src/main/scala/bus/CBusArbiter.scala:8:7
        `INIT_RANDOM_PROLOG_	// src/main/scala/bus/CBusArbiter.scala:8:7
      `endif // INIT_RANDOM_PROLOG_
      `ifdef RANDOMIZE_REG_INIT	// src/main/scala/bus/CBusArbiter.scala:8:7
        _RANDOM[/*Zero width*/ 1'b0] = `RANDOM;	// src/main/scala/bus/CBusArbiter.scala:8:7
        busy = _RANDOM[/*Zero width*/ 1'b0][0];	// src/main/scala/bus/CBusArbiter.scala:8:7, :18:24
        index = _RANDOM[/*Zero width*/ 1'b0][1];	// src/main/scala/bus/CBusArbiter.scala:8:7, :18:24, :19:24
      `endif // RANDOMIZE_REG_INIT
    end // initial
    `ifdef FIRRTL_AFTER_INITIAL	// src/main/scala/bus/CBusArbiter.scala:8:7
      `FIRRTL_AFTER_INITIAL	// src/main/scala/bus/CBusArbiter.scala:8:7
    `endif // FIRRTL_AFTER_INITIAL
  `endif // ENABLE_INITIAL_REG_
  assign io_iresp_ready = _io_iresp_T_1 & io_oresp_ready;	// src/main/scala/bus/CBusArbiter.scala:8:7, :35:{20,26}
  assign io_iresp_last = _io_iresp_T_1 & io_oresp_last;	// src/main/scala/bus/CBusArbiter.scala:8:7, :35:{20,26}
  assign io_iresp_data = _io_iresp_T_1 ? io_oresp_data : 64'h0;	// src/main/scala/bus/CBusArbiter.scala:8:7, :35:{20,26,66}
  assign io_dresp_ready = _io_dresp_T_1 & io_oresp_ready;	// src/main/scala/bus/CBusArbiter.scala:8:7, :36:{20,26}
  assign io_dresp_last = _io_dresp_T_1 & io_oresp_last;	// src/main/scala/bus/CBusArbiter.scala:8:7, :36:{20,26}
  assign io_dresp_data = _io_dresp_T_1 ? io_oresp_data : 64'h0;	// src/main/scala/bus/CBusArbiter.scala:8:7, :35:66, :36:{20,26}
  assign io_oreq_valid = index ? io_dreq_valid : io_ireq_valid;	// src/main/scala/bus/CBusArbiter.scala:8:7, :19:24, :33:33
  assign io_oreq_size = index ? io_dreq_size : io_ireq_size;	// src/main/scala/bus/CBusArbiter.scala:8:7, :19:24, :33:33
  assign io_oreq_addr = index ? io_dreq_addr : io_ireq_addr;	// src/main/scala/bus/CBusArbiter.scala:8:7, :19:24, :33:33
endmodule

// external module RAMHelper2

// external module DifftestInstrCommit

// external module DifftestArchIntRegState

// external module DifftestCSRState

// external module DifftestTrapEvent

module Core(	// src/main/scala/Core.scala:12:7
  input         clock,	// src/main/scala/Core.scala:12:7
  input         reset,	// src/main/scala/Core.scala:12:7
  output [63:0] io_ireq_addr,	// src/main/scala/Core.scala:13:16
  input         io_iresp_addr_ok,	// src/main/scala/Core.scala:13:16
  input         io_iresp_data_ok,	// src/main/scala/Core.scala:13:16
  input  [63:0] io_iresp_data	// src/main/scala/Core.scala:13:16
);

  reg [63:0] this_pc;	// src/main/scala/Core.scala:26:26
  always @(posedge clock) begin	// src/main/scala/Core.scala:12:7
    if (reset)	// src/main/scala/Core.scala:12:7
      this_pc <= 64'h80000000;	// src/main/scala/Core.scala:26:26
    else if (io_iresp_data_ok)	// src/main/scala/Core.scala:13:16
      this_pc <= 64'(this_pc + 64'h4);	// src/main/scala/Core.scala:26:26, :28:28
  end // always @(posedge)
  `ifdef ENABLE_INITIAL_REG_	// src/main/scala/Core.scala:12:7
    `ifdef FIRRTL_BEFORE_INITIAL	// src/main/scala/Core.scala:12:7
      `FIRRTL_BEFORE_INITIAL	// src/main/scala/Core.scala:12:7
    `endif // FIRRTL_BEFORE_INITIAL
    logic [31:0] _RANDOM[0:1];	// src/main/scala/Core.scala:12:7
    initial begin	// src/main/scala/Core.scala:12:7
      `ifdef INIT_RANDOM_PROLOG_	// src/main/scala/Core.scala:12:7
        `INIT_RANDOM_PROLOG_	// src/main/scala/Core.scala:12:7
      `endif // INIT_RANDOM_PROLOG_
      `ifdef RANDOMIZE_REG_INIT	// src/main/scala/Core.scala:12:7
        for (logic [1:0] i = 2'h0; i < 2'h2; i += 2'h1) begin
          _RANDOM[i[0]] = `RANDOM;	// src/main/scala/Core.scala:12:7
        end	// src/main/scala/Core.scala:12:7
        this_pc = {_RANDOM[1'h0], _RANDOM[1'h1]};	// src/main/scala/Core.scala:12:7, :26:26
      `endif // RANDOMIZE_REG_INIT
    end // initial
    `ifdef FIRRTL_AFTER_INITIAL	// src/main/scala/Core.scala:12:7
      `FIRRTL_AFTER_INITIAL	// src/main/scala/Core.scala:12:7
    `endif // FIRRTL_AFTER_INITIAL
  `endif // ENABLE_INITIAL_REG_
  DifftestInstrCommit diff_instr (	// src/main/scala/Core.scala:38:32
    .clock    (clock),
    .coreid   (8'h0),	// src/main/scala/Core.scala:24:13
    .index    (8'h0),	// src/main/scala/Core.scala:24:13
    .valid    (io_iresp_addr_ok & io_iresp_data_ok),	// src/main/scala/Core.scala:33:40
    .pc       (this_pc),	// src/main/scala/Core.scala:26:26
    .instr    (io_iresp_data[31:0]),	// src/main/scala/Core.scala:45:29
    .skip     (1'h0),	// src/main/scala/Core.scala:12:7
    .isRVC    (1'h0),	// src/main/scala/Core.scala:12:7
    .scFailed (1'h0),	// src/main/scala/Core.scala:12:7
    .wen      (1'h0),	// src/main/scala/Core.scala:12:7
    .wdata    (64'h0),	// src/main/scala/Core.scala:24:13
    .wdest    (8'h0)	// src/main/scala/Core.scala:24:13
  );
  DifftestArchIntRegState diff_regs (	// src/main/scala/Core.scala:47:31
    .clock  (clock),
    .coreid (8'h0),	// src/main/scala/Core.scala:24:13
    .gpr_0  (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_1  (64'h114514),	// src/main/scala/Core.scala:51:29
    .gpr_2  (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_3  (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_4  (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_5  (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_6  (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_7  (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_8  (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_9  (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_10 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_11 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_12 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_13 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_14 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_15 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_16 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_17 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_18 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_19 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_20 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_21 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_22 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_23 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_24 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_25 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_26 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_27 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_28 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_29 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_30 (64'h0),	// src/main/scala/Core.scala:24:13
    .gpr_31 (64'h0)	// src/main/scala/Core.scala:24:13
  );
  DifftestCSRState diff_csrs (	// src/main/scala/Core.scala:53:31
    .clock          (clock),
    .coreid         (8'h0),	// src/main/scala/Core.scala:24:13
    .priviledgeMode (2'h3),	// src/main/scala/Core.scala:57:37
    .mstatus        (64'h0),	// src/main/scala/Core.scala:24:13
    .sstatus        (64'h0),	// src/main/scala/Core.scala:24:13
    .mepc           (64'h0),	// src/main/scala/Core.scala:24:13
    .sepc           (64'h0),	// src/main/scala/Core.scala:24:13
    .mtval          (64'h0),	// src/main/scala/Core.scala:24:13
    .stval          (64'h0),	// src/main/scala/Core.scala:24:13
    .mtvec          (64'h0),	// src/main/scala/Core.scala:24:13
    .stvec          (64'h0),	// src/main/scala/Core.scala:24:13
    .mcause         (64'h0),	// src/main/scala/Core.scala:24:13
    .scause         (64'h0),	// src/main/scala/Core.scala:24:13
    .satp           (64'h0),	// src/main/scala/Core.scala:24:13
    .mip            (64'h0),	// src/main/scala/Core.scala:24:13
    .mie            (64'h0),	// src/main/scala/Core.scala:24:13
    .mscratch       (64'h0),	// src/main/scala/Core.scala:24:13
    .sscratch       (64'h0),	// src/main/scala/Core.scala:24:13
    .mideleg        (64'h0),	// src/main/scala/Core.scala:24:13
    .medeleg        (64'h0)	// src/main/scala/Core.scala:24:13
  );
  DifftestTrapEvent diff_trap (	// src/main/scala/Core.scala:59:31
    .clock    (clock),
    .coreid   (8'h0),	// src/main/scala/Core.scala:24:13
    .valid    (1'h0),	// src/main/scala/Core.scala:12:7
    .code     (3'h0),	// src/main/scala/Core.scala:24:13
    .pc       (64'h0),	// src/main/scala/Core.scala:24:13
    .cycleCnt (64'h0),	// src/main/scala/Core.scala:24:13
    .instrCnt (64'h0)	// src/main/scala/Core.scala:24:13
  );
  assign io_ireq_addr = this_pc;	// src/main/scala/Core.scala:12:7, :26:26
endmodule

module SimTop(	// src/main/scala/top/SimTop.scala:13:7
  input         clock,	// src/main/scala/top/SimTop.scala:13:7
  input         reset,	// src/main/scala/top/SimTop.scala:13:7
  input  [63:0] io_logCtrl_log_begin,	// src/main/scala/top/SimTop.scala:14:16
  input  [63:0] io_logCtrl_log_end,	// src/main/scala/top/SimTop.scala:14:16
  input  [63:0] io_logCtrl_log_level,	// src/main/scala/top/SimTop.scala:14:16
  input         io_perfInfo_clean,	// src/main/scala/top/SimTop.scala:14:16
  input         io_perfInfo_dump,	// src/main/scala/top/SimTop.scala:14:16
  output        io_uart_out_valid,	// src/main/scala/top/SimTop.scala:14:16
  output [7:0]  io_uart_out_ch,	// src/main/scala/top/SimTop.scala:14:16
  output        io_uart_in_valid,	// src/main/scala/top/SimTop.scala:14:16
  input  [7:0]  io_uart_in_ch	// src/main/scala/top/SimTop.scala:14:16
);

  wire [63:0] _core_io_ireq_addr;	// src/main/scala/top/SimTop.scala:60:22
  wire [65:0] _ram_oresp;	// src/main/scala/top/SimTop.scala:53:21
  wire        _arb_io_iresp_ready;	// src/main/scala/top/SimTop.scala:45:21
  wire        _arb_io_iresp_last;	// src/main/scala/top/SimTop.scala:45:21
  wire [63:0] _arb_io_iresp_data;	// src/main/scala/top/SimTop.scala:45:21
  wire        _arb_io_dresp_ready;	// src/main/scala/top/SimTop.scala:45:21
  wire        _arb_io_dresp_last;	// src/main/scala/top/SimTop.scala:45:21
  wire [63:0] _arb_io_dresp_data;	// src/main/scala/top/SimTop.scala:45:21
  wire        _arb_io_oreq_valid;	// src/main/scala/top/SimTop.scala:45:21
  wire [2:0]  _arb_io_oreq_size;	// src/main/scala/top/SimTop.scala:45:21
  wire [63:0] _arb_io_oreq_addr;	// src/main/scala/top/SimTop.scala:45:21
  wire        _d2c_io_dcreq_valid;	// src/main/scala/top/SimTop.scala:39:21
  wire [2:0]  _d2c_io_dcreq_size;	// src/main/scala/top/SimTop.scala:39:21
  wire [63:0] _d2c_io_dcreq_addr;	// src/main/scala/top/SimTop.scala:39:21
  wire        _i2c_io_iresp_addr_ok;	// src/main/scala/top/SimTop.scala:34:21
  wire        _i2c_io_iresp_data_ok;	// src/main/scala/top/SimTop.scala:34:21
  wire [63:0] _i2c_io_iresp_data;	// src/main/scala/top/SimTop.scala:34:21
  wire        _i2c_io_icreq_valid;	// src/main/scala/top/SimTop.scala:34:21
  wire [2:0]  _i2c_io_icreq_size;	// src/main/scala/top/SimTop.scala:34:21
  wire [63:0] _i2c_io_icreq_addr;	// src/main/scala/top/SimTop.scala:34:21
  IBus2CBus i2c (	// src/main/scala/top/SimTop.scala:34:21
    .io_ireq_addr     (_core_io_ireq_addr),	// src/main/scala/top/SimTop.scala:60:22
    .io_iresp_addr_ok (_i2c_io_iresp_addr_ok),
    .io_iresp_data_ok (_i2c_io_iresp_data_ok),
    .io_iresp_data    (_i2c_io_iresp_data),
    .io_icreq_valid   (_i2c_io_icreq_valid),
    .io_icreq_size    (_i2c_io_icreq_size),
    .io_icreq_addr    (_i2c_io_icreq_addr),
    .io_icresp_ready  (_arb_io_iresp_ready),	// src/main/scala/top/SimTop.scala:45:21
    .io_icresp_last   (_arb_io_iresp_last),	// src/main/scala/top/SimTop.scala:45:21
    .io_icresp_data   (_arb_io_iresp_data)	// src/main/scala/top/SimTop.scala:45:21
  );
  DBus2CBus d2c (	// src/main/scala/top/SimTop.scala:39:21
    .io_dreq_valid    (1'h0),	// src/main/scala/top/SimTop.scala:31:22, :34:21, :39:21, :45:21, :60:22
    .io_dreq_addr     (64'h0),	// src/main/scala/top/SimTop.scala:34:21, :39:21, :45:21, :60:22
    .io_dreq_size     (3'h0),	// src/main/scala/top/SimTop.scala:39:21, :60:22
    .io_dresp_addr_ok (/* unused */),
    .io_dresp_data_ok (/* unused */),
    .io_dresp_data    (/* unused */),
    .io_dcreq_valid   (_d2c_io_dcreq_valid),
    .io_dcreq_size    (_d2c_io_dcreq_size),
    .io_dcreq_addr    (_d2c_io_dcreq_addr),
    .io_dcresp_ready  (_arb_io_dresp_ready),	// src/main/scala/top/SimTop.scala:45:21
    .io_dcresp_last   (_arb_io_dresp_last),	// src/main/scala/top/SimTop.scala:45:21
    .io_dcresp_data   (_arb_io_dresp_data)	// src/main/scala/top/SimTop.scala:45:21
  );
  CBusArbiter arb (	// src/main/scala/top/SimTop.scala:45:21
    .clock          (clock),
    .reset          (reset),
    .io_ireq_valid  (_i2c_io_icreq_valid),	// src/main/scala/top/SimTop.scala:34:21
    .io_ireq_size   (_i2c_io_icreq_size),	// src/main/scala/top/SimTop.scala:34:21
    .io_ireq_addr   (_i2c_io_icreq_addr),	// src/main/scala/top/SimTop.scala:34:21
    .io_dreq_valid  (_d2c_io_dcreq_valid),	// src/main/scala/top/SimTop.scala:39:21
    .io_dreq_size   (_d2c_io_dcreq_size),	// src/main/scala/top/SimTop.scala:39:21
    .io_dreq_addr   (_d2c_io_dcreq_addr),	// src/main/scala/top/SimTop.scala:39:21
    .io_iresp_ready (_arb_io_iresp_ready),
    .io_iresp_last  (_arb_io_iresp_last),
    .io_iresp_data  (_arb_io_iresp_data),
    .io_dresp_ready (_arb_io_dresp_ready),
    .io_dresp_last  (_arb_io_dresp_last),
    .io_dresp_data  (_arb_io_dresp_data),
    .io_oreq_valid  (_arb_io_oreq_valid),
    .io_oreq_size   (_arb_io_oreq_size),
    .io_oreq_addr   (_arb_io_oreq_addr),
    .io_oresp_ready (_ram_oresp[65]),	// src/main/scala/top/SimTop.scala:53:21, :57:35
    .io_oresp_last  (_ram_oresp[64]),	// src/main/scala/top/SimTop.scala:53:21, :57:35
    .io_oresp_data  (_ram_oresp[63:0])	// src/main/scala/top/SimTop.scala:53:21, :57:35
  );
  RAMHelper2 ram (	// src/main/scala/top/SimTop.scala:53:21
    .clk   (clock),
    .reset (reset),
    .oreq  ({_arb_io_oreq_valid, 1'h0, _arb_io_oreq_size, _arb_io_oreq_addr, 82'h0}),	// src/main/scala/top/SimTop.scala:31:22, :34:21, :39:21, :45:21, :56:25, :60:22
    .oresp (_ram_oresp),
    .trint (/* unused */),
    .swint (/* unused */),
    .exint (/* unused */)
  );
  Core core (	// src/main/scala/top/SimTop.scala:60:22
    .clock            (clock),
    .reset            (reset),
    .io_ireq_addr     (_core_io_ireq_addr),
    .io_iresp_addr_ok (_i2c_io_iresp_addr_ok),	// src/main/scala/top/SimTop.scala:34:21
    .io_iresp_data_ok (_i2c_io_iresp_data_ok),	// src/main/scala/top/SimTop.scala:34:21
    .io_iresp_data    (_i2c_io_iresp_data)	// src/main/scala/top/SimTop.scala:34:21
  );
  assign io_uart_out_valid = 1'h0;	// src/main/scala/top/SimTop.scala:13:7, :31:22, :34:21, :39:21, :45:21, :60:22
  assign io_uart_out_ch = 8'h0;	// src/main/scala/top/SimTop.scala:13:7, :21:17
  assign io_uart_in_valid = 1'h0;	// src/main/scala/top/SimTop.scala:13:7, :31:22, :34:21, :39:21, :45:21, :60:22
endmodule

