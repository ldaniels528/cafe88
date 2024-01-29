package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.alu.{ADD, OR}
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.{POP, PUSH}
import com.github.ldaniels528.v88.operands.*
import org.scalatest.funspec.AnyFunSpec

class Decoder0xSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[Decoder0x].getSimpleName) {

    it("should decode: add [bx],al") {
      assert(disassemble(Array(0x00, 0x07)) == ADD(Box_BX(is8Bit = true), AL))
    }

    it("should decode: add [bx],ax") {
      assert(disassemble(Array(0x01, 0x07)) == ADD(Box_BX(is8Bit = false), AX))
    }

    it("should decode: add al,[bx]") {
      assert(disassemble(Array(0x02, 0x07)) == ADD(AL, Box_BX(is8Bit = true)))
    }

    it("should decode: add ax,[bx]") {
      assert(disassemble(Array(0x03, 0x07)) == ADD(AX, Box_BX(is8Bit = false)))
    }

    it("should decode: add al,AA") {
      assert(disassemble(Array(0x04, 0xAA)) == ADD(AL, ByteValue(0xAA)))
    }

    it("should decode: add ax,ABCD") {
      assert(disassemble(Array(0x05, 0xCD, 0xAB)) == ADD(AX, WordValue(0xABCD)))
    }

    it("should decode: push es") {
      assert(disassemble(Array(0x06)) == PUSH(ES))
    }

    it("should decode: pop es") {
      assert(disassemble(Array(0x07)) == POP(ES))
    }

    it("should decode: or [bx],al") {
      assert(disassemble(Array(0x08, 0x07)) == OR(Box_BX(is8Bit = true), AL))
    }

    it("should decode: or [bx],ax") {
      assert(disassemble(Array(0x09, 0x07)) == OR(Box_BX(is8Bit = false), AX))
    }

    it("should decode: or al,[bx]") {
      assert(disassemble(Array(0x0A, 0x07)) == OR(AL, Box_BX(is8Bit = true)))
    }

    it("should decode: or ax,[bx]") {
      assert(disassemble(Array(0x0B, 0x07)) == OR(AX, Box_BX(is8Bit = false)))
    }

    it("should decode: or al,AA") {
      assert(disassemble(Array(0x0C, 0xAA)) == OR(AL, ByteValue(0xAA)))
    }

    it("should decode: or ax,ABCD") {
      assert(disassemble(Array(0x0D, 0xCD, 0xAB)) == OR(AX, WordValue(0xABCD)))
    }

    it("should decode: push cs") {
      assert(disassemble(Array(0x0E)) == PUSH(CS))
    }

    it("should decode: pop cs") {
      assert(disassemble(Array(0x0F)) == POP(CS))
    }

  }

}
