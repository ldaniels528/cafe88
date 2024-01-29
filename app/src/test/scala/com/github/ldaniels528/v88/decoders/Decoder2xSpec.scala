package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.NOOP
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.bitwise.{DAA, DAS}
import com.github.ldaniels528.v88.opcodes.data.OverrideDS
import com.github.ldaniels528.v88.operands.*
import org.scalatest.funspec.AnyFunSpec

class Decoder2xSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[Decoder2x].getSimpleName) {

    it("should decode: and [bx],al") {
      assert(disassemble(Array(0x20, 0x07)) == AND(Box_BX(is8Bit = true), AL))
    }

    it("should decode: and [bx],ax") {
      assert(disassemble(Array(0x21, 0x07)) == AND(Box_BX(is8Bit = false), AX))
    }

    it("should decode: and al,[bx]") {
      assert(disassemble(Array(0x22, 0x07)) == AND(AL, Box_BX(is8Bit = true)))
    }

    it("should decode: and ax,[bx]") {
      assert(disassemble(Array(0x23, 0x07)) == AND(AX, Box_BX(is8Bit = false)))
    }

    it("should decode: and al,AA") {
      assert(disassemble(Array(0x24, 0xAA)) == AND(AL, ByteValue(0xAA)))
    }

    it("should decode: and ax,DEAD") {
      assert(disassemble(Array(0x25, 0xAD, 0xDE)) == AND(AX, WordValue(0xDEAD)))
    }

    it("should decode: es: nop") {
      assert(disassemble(Array(0x26, 0x90)) == OverrideDS(ES, NOOP))
    }

    it("should decode: daa") {
      assert(disassemble(Array(0x27)) == DAA)
    }

    it("should decode: sub [bx],al") {
      assert(disassemble(Array(0x28, 0x07)) == SUB(Box_BX(is8Bit = true), AL))
    }

    it("should decode: sub [bx],ax") {
      assert(disassemble(Array(0x29, 0x07)) == SUB(Box_BX(is8Bit = false), AX))
    }

    it("should decode: sub al,[bx]") {
      assert(disassemble(Array(0x2A, 0x07)) == SUB(AL, Box_BX(is8Bit = true)))
    }

    it("should decode: sub ax,[bx]") {
      assert(disassemble(Array(0x2B, 0x07)) == SUB(AX, Box_BX(is8Bit = false)))
    }

    it("should decode: sub al,AA") {
      assert(disassemble(Array(0x2C, 0xAA)) == SUB(AL, ByteValue(0xAA)))
    }

    it("should decode: sub ax,DEAD") {
      assert(disassemble(Array(0x2D, 0xAD, 0xDE)) == SUB(AX, WordValue(0xDEAD)))
    }

    it("should decode: cs: nop") {
      assert(disassemble(Array(0x2E, 0x90)) == OverrideDS(CS, NOOP))
    }

    it("should decode: das") {
      assert(disassemble(Array(0x2F)) == DAS)
    }

  }

}
