package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.NOOP
import com.github.ldaniels528.v88.opcodes.alu.{ADC, SBB}
import com.github.ldaniels528.v88.opcodes.data.OverrideDS
import com.github.ldaniels528.v88.opcodes.stack.{POP, PUSH}
import com.github.ldaniels528.v88.operands.*
import org.scalatest.funspec.AnyFunSpec

class Decoder1xSpec extends AnyFunSpec with VerificationTools:

  describe(classOf[Decoder1x].getSimpleName) {

    it("should decode: adc [bx],al") {
      assert(disassemble(Array(0x10, 0x07)) == ADC(Box_BX(is8Bit = true), AL))
    }

    it("should decode: adc [bx],ax") {
      assert(disassemble(Array(0x11, 0x07)) == ADC(Box_BX(is8Bit = false), AX))
    }

    it("should decode: adc al,[bx]") {
      assert(disassemble(Array(0x12, 0x07)) == ADC(AL, Box_BX(is8Bit = true)))
    }

    it("should decode: adc ax,[bx]") {
      assert(disassemble(Array(0x13, 0x07)) == ADC(AX, Box_BX(is8Bit = false)))
    }

    it("should decode: adc al,AA") {
      assert(disassemble(Array(0x14, 0xAA)) == ADC(AL, ByteValue(0xAA)))
    }

    it("should decode: adc ax,DEAD") {
      assert(disassemble(Array(0x15, 0xAD, 0xDE)) == ADC(AX, WordValue(0xDEAD)))
    }

    it("should decode: push ss") {
      assert(disassemble(Array(0x16)) == PUSH(SS))
    }

    it("should decode: pop ss") {
      assert(disassemble(Array(0x17)) == POP(SS))
    }

    it("should decode: sbb [bx],al") {
      assert(disassemble(Array(0x18, 0x07)) == SBB(Box_BX(is8Bit = true), AL))
    }

    it("should decode: sbb [bx],ax") {
      assert(disassemble(Array(0x19, 0x07)) == SBB(Box_BX(is8Bit = false), AX))
    }

    it("should decode: sbb al,[bx]") {
      assert(disassemble(Array(0x1A, 0x07)) == SBB(AL, Box_BX(is8Bit = true)))
    }

    it("should decode: sbb ax,[bx]") {
      assert(disassemble(Array(0x1B, 0x07)) == SBB(AX, Box_BX(is8Bit = false)))
    }

    it("should decode: sbb al,AA") {
      assert(disassemble(Array(0x1C, 0xAA)) == SBB(AL, ByteValue(0xAA)))
    }

    it("should decode: sbb ax,DEAD") {
      assert(disassemble(Array(0x1D, 0xAD, 0xDE)) == SBB(AX, WordValue(0xDEAD)))
    }

    it("should decode: push ds") {
      assert(disassemble(Array(0x1E)) == PUSH(DS))
    }

    it("should decode: pop ds") {
      assert(disassemble(Array(0x1F)) == POP(DS))
    }

  }
  
