package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.bitwise.TEST
import com.github.ldaniels528.v88.opcodes.data.{LEA, MOV, XCHG}
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.POP
import com.github.ldaniels528.v88.operands.*
import org.scalatest.funspec.AnyFunSpec

class Decoder8xSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[Decoder8x].getSimpleName) {

    it("should decode: add ax,ABCD") {
      assert(disassemble(Array(0x81, 0xC1, 0xCD, 0xAB)) == ADD(AX, WordValue(0xABCD)))
    }

    it("should decode: or cx,1234") {
      assert(disassemble(Array(0x81, 0xC9, 0x34, 0x12)) == OR(CX, WordValue(0x1234)))
    }

    it("should decode: adc dx,ABCD") {
      assert(disassemble(Array(0x81, 0xD1, 0xCD, 0xAB)) == ADC(DX, WordValue(0xABCD)))
    }

    it("should decode: sbb bx,1234") {
      assert(disassemble(Array(0x81, 0xD9, 0x34, 0x12)) == SBB(BX, WordValue(0x1234)))
    }

    it("should decode: test [bx], al") {
      assert(disassemble(Array(0x84, 0x07)) == TEST(Box_BX(is8Bit = true), AL))
    }

    it("should decode: test cl, al") {
      assert(disassemble(Array(0x84, 0xC1)) == TEST(CL, AL))
    }

    it("should decode: test [bx],ax") {
      assert(disassemble(Array(0x85, 0x07)) == TEST(Box_BX(is8Bit = false), AX))
    }

    it("should decode: test cx,ax") {
      assert(disassemble(Array(0x85, 0xC1)) == TEST(CX, AX))
    }

    it("should decode: xchg al,[bx]") {
      assert(disassemble(Array(0x86, 0x07)) == XCHG(AL, Box_BX(is8Bit = true)))
    }

    it("should decode: xchg al,cl") {
      assert(disassemble(Array(0x86, 0xC1)) == XCHG(AL, CL))
    }

    it("should decode: xchg ax,[bx]") {
      assert(disassemble(Array(0x87, 0x07)) == XCHG(AX, Box_BX(is8Bit = false)))
    }

    it("should decode: xchg ax,cx") {
      assert(disassemble(Array(0x87, 0xC1)) == XCHG(AX, CX))
    }

    it("should decode: mov cl,al") {
      assert(disassemble(Array(0x88, 0xC1)) == MOV(CL, AL))
    }

    it("should decode: mov ax,cx") {
      assert(disassemble(Array(0x89, 0xC8)) == MOV(AX, CX))
    }

    it("should decode: mov cl,[1235]") {
      assert(disassemble(Array(0x8A, 0x0E, 0x35, 0x12)) == MOV(CL, BoxNear(0x1235, is8Bit = true)))
    }

    it("should decode: mov ax,[bx]") {
      assert(disassemble(Array(0x8B, 0x07)) == MOV(AX, Box_BX(is8Bit = false)))
    }

    it("should decode: mov cs,cx") {
      assert(disassemble(Array(0x8C, 0xC8)) == MOV(CS, CX))
    }

    it("should decode: lea [bx],ax") {
      assert(disassemble(Array(0x8D, 0x07)) == LEA(Box_BX(is8Bit = false), AX))
    }

    it("should decode: mov cx,cs") {
      assert(disassemble(Array(0x8E, 0xC8)) == MOV(CX, CS))
    }

    it("should decode: pop [bx]") {
      assert(disassemble(Array(0x8F, 0x07)) == POP(Box_BX(is8Bit = false)))
    }

  }

}
