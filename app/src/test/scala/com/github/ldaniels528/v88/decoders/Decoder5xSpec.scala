package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.stack.{POP, PUSH}
import com.github.ldaniels528.v88.operands.*
import org.scalatest.funspec.AnyFunSpec

class Decoder5xSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[Decoder5x].getSimpleName) {

    it("should decode: push ax") {
      assert(disassemble(Array(0x50)) == PUSH(AX))
    }

    it("should decode: push cx") {
      assert(disassemble(Array(0x51)) == PUSH(CX))
    }

    it("should decode: push dx") {
      assert(disassemble(Array(0x52)) == PUSH(DX))
    }

    it("should decode: push bx") {
      assert(disassemble(Array(0x53)) == PUSH(BX))
    }

    it("should decode: push sp") {
      assert(disassemble(Array(0x54)) == PUSH(SP))
    }

    it("should decode: push bp") {
      assert(disassemble(Array(0x55)) == PUSH(BP))
    }

    it("should decode: push si") {
      assert(disassemble(Array(0x56)) == PUSH(SI))
    }

    it("should decode: push di") {
      assert(disassemble(Array(0x57)) == PUSH(DI))
    }

    it("should decode: pop ax") {
      assert(disassemble(Array(0x58)) == POP(AX))
    }

    it("should decode: pop cx") {
      assert(disassemble(Array(0x59)) == POP(CX))
    }

    it("should decode: pop dx") {
      assert(disassemble(Array(0x5A)) == POP(DX))
    }

    it("should decode: pop bx") {
      assert(disassemble(Array(0x5B)) == POP(BX))
    }

    it("should decode: pop sp") {
      assert(disassemble(Array(0x5C)) == POP(SP))
    }

    it("should decode: pop bp") {
      assert(disassemble(Array(0x5D)) == POP(BP))
    }

    it("should decode: pop si") {
      assert(disassemble(Array(0x5E)) == POP(SI))
    }

    it("should decode: pop di") {
      assert(disassemble(Array(0x5F)) == POP(DI))
    }

  }

}
