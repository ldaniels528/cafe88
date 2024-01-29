package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.bitwise.TEST
import com.github.ldaniels528.v88.opcodes.data.MOV
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.streaming.*
import com.github.ldaniels528.v88.operands.*
import org.scalatest.funspec.AnyFunSpec

class DecoderAxSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[DecoderAx].getSimpleName) {

    it("should decode: MOV AL,[8822]") {
      assert(disassemble(Array(0xA0, 0x22)) == MOV(AL, BoxNear(0x22, is8Bit = true)))
    }

    it("should decode: MOV AX,[2288]") {
      assert(disassemble(Array(0xA1, 0x88, 0x22)) == MOV(AX, BoxNear(0x2288, is8Bit = false)))
    }

    it("should decode: MOV [8822],AL") {
      assert(disassemble(Array(0xA2, 0x88)) == MOV(BoxNear(0x88, is8Bit = true), AL))
    }

    it("should decode: MOV [2288],AX") {
      assert(disassemble(Array(0xA3, 0x88, 0x22)) == MOV(BoxNear(0x2288, is8Bit = false), AX))
    }

    it("should decode: MOVSB") {
      assert(disassemble(Array(0xA4)) == MOVSB)
    }

    it("should decode: MOVSW") {
      assert(disassemble(Array(0xA5)) == MOVSW)
    }

    it("should decode: CMPSB") {
      assert(disassemble(Array(0xA6)) == CMPSB)
    }

    it("should decode: CMPSW") {
      assert(disassemble(Array(0xA7)) == CMPSW)
    }

    it("should decode: TEST AL,8E") {
      assert(disassemble(Array(0xA8, 0x8E)) == TEST(AL, ByteValue(0x8E)))
    }

    it("should decode: TEST AX,8EF5") {
      assert(disassemble(Array(0xA9, 0xF5, 0x8E)) == TEST(AX, WordValue(0x8EF5)))
    }

    it("should decode: STOSB") {
      assert(disassemble(Array(0xAA)) == STOSB)
    }

    it("should decode: STOSW") {
      assert(disassemble(Array(0xAB)) == STOSW)
    }

    it("should decode: LODSB") {
      assert(disassemble(Array(0xAC)) == LODSB)
    }

    it("should decode: LODSW") {
      assert(disassemble(Array(0xAD)) == LODSW)
    }

    it("should decode: SCASB") {
      assert(disassemble(Array(0xAE)) == SCASB)
    }

    it("should decode: SCASW") {
      assert(disassemble(Array(0xAF)) == SCASW)
    }

  }

}
