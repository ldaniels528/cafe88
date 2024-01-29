package com.github.ldaniels528.v88

import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.flow.HLT
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.*
import com.github.ldaniels528.v88.operands.{AX, CS, CX, ES}
import org.scalatest.funspec.AnyFunSpec
import org.slf4j.LoggerFactory

class VirtualCPUSpec extends AnyFunSpec {
  private val logger = LoggerFactory.getLogger(getClass)
  private val term = List[Byte](13)

  describe(classOf[VirtualCPU].getSimpleName) {

    it("should print 'Hello World'") {
      // build the image to execute
      val image: Array[Byte] = (List(
        0xBA, 0x08, 0x00, // |0000 mov dx, 0008h
        0xB4, 0x09, //       |0003 mov ah, 9h
        0xCD, 0x21, //       |0005 int 21h
        0xCD, 0x20 //        |0007 int 20h
      ).map(_.toByte) ::: "Hello World".getBytes.toList ::: term).toArray

      // setup and start the virtual CPU
      val vpc: VirtualPC = VirtualPC(memory = new VirtualMemory(image))
      val state = vpc.run(CPUState())
      assert(state.ip.address == 9)
      assert(state.dx.value == 8)
      assert(state.ah.value == 9)
    }

    it("should perform basic flow") {
      // build the image to execute
      val image: Array[Byte] = (List(
        0xB1, 0x00, //       |0000 mov cl, 00h
        0x74, 0x07, //       |0002 jz 07h (000Ah)
        0xBA, 0x0C, 0x00, // |0004 mov dx, 000Ch
        0xB4, 0x09, //       |0007 mov ah, 9h
        0xCD, 0x21, //       |0009 int 21h
        0xCD, 0x20 //        |000A int 20h
      ).map(_.toByte) ::: "ZR is clear.".getBytes.toList ::: term).toArray

      // setup and start the virtual CPU
      val vpc: VirtualPC = VirtualPC(memory = new VirtualMemory(image))
      val state = vpc.run(CPUState())
      assert(state.ip.address == 0x0D)
      assert(state.dx.value == 0x0C)
      assert(state.ah.value == 9)
    }

    it("should perform PUSH/POP") {
      // build the image to execute
      val image: Array[Byte] = Array(
        0xB9, 0xAD, 0xDE, // |0000 mov cx, DEADh
        0xB8, 0xEF, 0xBE, // |0003 mov ax, BEEFh
        0x50, //             |0006 push ax
        0x51, //             |0007 push cx
        0x58, //             |0008 pop ax
        0x59, //             |0009 pop cx
        0xCD, 0x20, //       |000A int 20h
      ).map(_.toByte)

      // create a 64K RAM module for the test PC
      val ram = new Array[Byte](65536)
      System.arraycopy(image, 0, ram, 0, image.length)

      // setup and start the virtual CPU
      val vpc: VirtualPC = VirtualPC(memory = new VirtualMemory(ram))
      val state = vpc.run(CPUState())
      logger.info(f"ax: ${state.ax.value}%04x, cx: ${state.cx.value}%04x")
      assert(state.ax.value == 0xDEAD)
      assert(state.cx.value == 0xBEEF)
    }

    it("should: INT 18h") {
      // build the image to execute
      val image: Array[Byte] = Array[Instruction](
        PUSH(CS),
        POP(ES),
        INC(AX),
        DEC(CX),
        INT(0x18),
        HLT
      ).flatMap(_.encode)
      logger.info(s"image = ${image.map(b => f"$b%02x").mkString(".")}")

      // setup and start the virtual CPU
      val vpc: VirtualPC = VirtualPC()
      vpc.init()
      val state0 = vpc.loadImage(image, segment = 0x13EE, offset = 0x0100)
      var state: CPUState = state0
      while (state.isRunning) state = vpc.update(state)
      logger.info(s"state: $state")
    }
    
  }

}
