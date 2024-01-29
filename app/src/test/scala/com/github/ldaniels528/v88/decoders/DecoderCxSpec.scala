package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.VerificationTools
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.ADD
import com.github.ldaniels528.v88.opcodes.data.DB
import com.github.ldaniels528.v88.opcodes.flow.*
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.operands.{CX, WordValue}
import org.scalatest.funspec.AnyFunSpec

class DecoderCxSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[Decoder8x].getSimpleName) {

    it("should decode: db C0") {
      assert(disassemble(Array(0xC0)) == DB(0xC0))
    }

    it("should decode: db C1") {
      assert(disassemble(Array(0xC1)) == DB(0xC1))
    }

    it("should decode: ret AB") {
      assert(disassemble(Array(0xC2, 0xAB)) == RETn(0xAB))
    }

    it("should decode: ret") {
      assert(disassemble(Array(0xC3)) == RET)
    }

    it("should decode: db C8") {
      assert(disassemble(Array(0xC8)) == DB(0xC8))
    }

    it("should decode: db C9") {
      assert(disassemble(Array(0xC9)) == DB(0xC9))
    }

    it("should decode: retf ABCD") {
      assert(disassemble(Array(0xCA, 0xCD, 0xAB)) == RETFn(0xABCD))
    }

    it("should decode: retf") {
      assert(disassemble(Array(0xCB)) == RETF)
    }

    it("should decode: int 3") {
      assert(disassemble(Array(0xCC)) == INT3)
    }

    it("should decode: int 21") {
      assert(disassemble(Array(0xCD, 0x21)) == INT(0x21))
    }

    it("should decode: int0") {
      assert(disassemble(Array(0xCE)) == INT0)
    }

    it("should decode: iret") {
      assert(disassemble(Array(0xCF)) == IRET)
    }

  }

}
