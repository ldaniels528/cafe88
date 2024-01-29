package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.VerificationTools
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.{POPA, PUSHA}
import com.github.ldaniels528.v88.opcodes.streaming.{INSB, INSW, OUTSB, OUTSW}
import com.github.ldaniels528.v88.operands.{AX, CX}
import org.scalatest.funspec.AnyFunSpec

class Decoder6xSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[Decoder6x].getSimpleName) {

    it("should decode: pusha") {
      assert(disassemble(Array(0x60)) == PUSHA)
    }

    it("should decode: popa") {
      assert(disassemble(Array(0x61)) == POPA)
    }

    it("should decode: insb") {
      assert(disassemble(Array(0x6C)) == INSB)
    }

    it("should decode: insw") {
      assert(disassemble(Array(0x6D)) == INSW)
    }

    it("should decode: outsb") {
      assert(disassemble(Array(0x6E)) == OUTSB)
    }

    it("should decode: outsw") {
      assert(disassemble(Array(0x6F)) == OUTSW)
    }

  }

}
