package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.operands.*
import org.scalatest.funspec.AnyFunSpec

class Decoder4xSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[Decoder4x].getSimpleName) {

    it("should decode: inc ax") {
      assert(disassemble(Array(0x40)) == INC(AX))
    }

    it("should decode: inc cx") {
      assert(disassemble(Array(0x41)) == INC(CX))
    }

    it("should decode: inc dx") {
      assert(disassemble(Array(0x42)) == INC(DX))
    }

    it("should decode: inc bx") {
      assert(disassemble(Array(0x43)) == INC(BX))
    }

    it("should decode: inc sp") {
      assert(disassemble(Array(0x44)) == INC(SP))
    }

    it("should decode: inc bp") {
      assert(disassemble(Array(0x45)) == INC(BP))
    }

    it("should decode: inc si") {
      assert(disassemble(Array(0x46)) == INC(SI))
    }

    it("should decode: inc di") {
      assert(disassemble(Array(0x47)) == INC(DI))
    }

    it("should decode: dec ax") {
      assert(disassemble(Array(0x48)) == DEC(AX))
    }

    it("should decode: dec cx") {
      assert(disassemble(Array(0x49)) == DEC(CX))
    }

    it("should decode: dec dx") {
      assert(disassemble(Array(0x4A)) == DEC(DX))
    }

    it("should decode: dec bx") {
      assert(disassemble(Array(0x4B)) == DEC(BX))
    }

    it("should decode: dec sp") {
      assert(disassemble(Array(0x4C)) == DEC(SP))
    }

    it("should decode: dec bp") {
      assert(disassemble(Array(0x4D)) == DEC(BP))
    }

    it("should decode: dec si") {
      assert(disassemble(Array(0x4E)) == DEC(SI))
    }

    it("should decode: dec di") {
      assert(disassemble(Array(0x4F)) == DEC(DI))
    }

  }

}
