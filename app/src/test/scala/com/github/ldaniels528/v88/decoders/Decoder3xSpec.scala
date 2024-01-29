package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.bitwise.{AAA, AAS}
import com.github.ldaniels528.v88.opcodes.data.{OverrideDS, XCHG}
import com.github.ldaniels528.v88.opcodes.{Instruction, NOOP}
import com.github.ldaniels528.v88.operands.*
import org.scalatest.funspec.AnyFunSpec

class Decoder3xSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[Decoder3x].getSimpleName) {

    it("should decode: XOR [BX],AL") {
      assert(disassemble(Array(0x30, 0x07)) == XOR(Box_BX(is8Bit = true), AL))
    }

    it("should decode: XOR [BX],AX") {
      assert(disassemble(Array(0x31, 0x07)) == XOR(Box_BX(is8Bit = false), AX))
    }

    it("should decode: XOR AL,[BX]") {
      assert(disassemble(Array(0x32, 0x07)) == XOR(AL, Box_BX(is8Bit = true)))
    }

    it("should decode: XOR AX,[BX]") {
      assert(disassemble(Array(0x33, 0x07)) == XOR(AX, Box_BX(is8Bit = false)))
    }

    it("should decode: XOR AL,12") {
      assert(disassemble(Array(0x34, 0x12)) == XOR(AL, ByteValue(0x12)))
    }

    it("should decode: XOR AX,1234") {
      assert(disassemble(Array(0x35, 0x34, 0x12)) == XOR(AX, WordValue(0x1234)))
    }

    it("should decode: SS: NOOP") {
      assert(disassemble(Array(0x36, 0x90)) == OverrideDS(SS, NOOP))
    }

    it("should decode: AAA") {
      assert(disassemble(Array(0x37)) == AAA)
    }

    it("should decode: CMP [BX],AL") {
      assert(disassemble(Array(0x38, 0x07)) == CMP(Box_BX(is8Bit = true), AL))
    }

    it("should decode: CMP [BX],AX") {
      assert(disassemble(Array(0x39, 0x07)) == CMP(Box_BX(is8Bit = false), AX))
    }

    it("should decode: CMP AL,[BX]") {
      assert(disassemble(Array(0x3A, 0x07)) == CMP(AL, Box_BX(is8Bit = true)))
    }

    it("should decode: CMP AX,[BX]") {
      assert(disassemble(Array(0x3B, 0x07)) == CMP(AX, Box_BX(is8Bit = false)))
    }

    it("should decode: CMP AL,12") {
      assert(disassemble(Array(0x3C, 0x12)) == CMP(AL, ByteValue(0x12)))
    }

    it("should decode: CMP AX,1234") {
      assert(disassemble(Array(0x3D, 0x34, 0x12)) == CMP(AX, WordValue(0x1234)))
    }

    it("should decode: DS: NOOP") {
      assert(disassemble(Array(0x3E, 0x90)) == OverrideDS(DS, NOOP))
    }

    it("should decode: AAS") {
      assert(disassemble(Array(0x3F)) == AAS)
    }

  }

}
