.DEFAULT_GOAL := no_arguments

no_arguments:
	@echo "Please specify a target to build"
	@echo "  - init: Initialize submodules"
	@echo "  - handin: Create a zip file for handin"
	@echo "  - gen: Generate Verilog code (if your project is based on Chisel)"
	@echo "  - test-lab1: Run lab1 test"

init:
	git submodule update --init --recursive

handin:
	@if [ ! -f docs/report.pdf ]; then \
		echo "Please write your report in the 'docs' folder and convert it to 'report.pdf' first"; \
		exit 1; \
	fi; \
	echo "Please enter your 'student id-name' (e.g., 12345678910-someone)"; \
	read filename; \
	echo "Please enter lab number (e.g., 1)"; \
	read lab_n; \
	zip -q -r "docs/$$filename-lab$$lab_n.zip" \
	  include docs/report.pdf src/main/scala src/test/scala

BUILD_DIR = $(abspath .)/build
BIN_DIR   = $(abspath .)/ready-to-run

export NOOP_HOME=$(abspath .)

sim-verilog:
	sbt "run $(BUILD_DIR)"

emu:
	$(MAKE) -C ./difftest emu $(DIFFTEST_OPTS)

TRACE_EN = 1
EMU_FLAGS = --diff $(abspath .)/ready-to-run/riscv64-nemu-interpreter-so
ifeq ($(TRACE_EN), 1)
	EMU_FLAGS += --dump-wave
endif

sim:
	rm -rf build
	mkdir -p build
	make EMU_TRACE=1 emu -j12

test-lab1: sim
	./build/emu $(EMU_FLAGS)  -i $(BIN_DIR)/lab1/lab1-test.bin

test-lab2: sim
	./build/emu $(EMU_FLAGS) -i $(BIN_DIR)/lab2/lab2-test.bin

test-lab3: sim
	./build/emu $(EMU_FLAGS) -i $(BIN_DIR)/lab3/lab3-test.bin || true

clean:
	rm -rf build

.PHONY: gen emu clean sim
