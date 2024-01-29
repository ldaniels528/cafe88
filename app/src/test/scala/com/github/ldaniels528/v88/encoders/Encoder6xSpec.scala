package com.github.ldaniels528.v88.encoders

import com.github.ldaniels528.v88.decoders.Decoder6x
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.{POPA, PUSH, PUSHA}
import com.github.ldaniels528.v88.opcodes.streaming.{INSB, INSW, OUTSB, OUTSW}
import com.github.ldaniels528.v88.operands.{AX, CX, ES}
import com.github.ldaniels528.v88.{VerificationTools, i2ba}
import org.scalatest.funspec.AnyFunSpec

class Encoder6xSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[Decoder6x].getSimpleName) {

    it("should decode: pusha") {
      assert(PUSHA.encode sameElements i2ba(0x60))
    }

    it("should decode: popa") {
      assert(POPA.encode sameElements i2ba(0x61))
    }

    it("should decode: insb") {
      assert(INSB.encode sameElements i2ba(0x6C))
    }

    it("should decode: insw") {
      assert(INSW.encode sameElements i2ba(0x6D))
    }

    it("should decode: outsb") {
      assert(OUTSB.encode sameElements i2ba(0x6E))
    }

    it("should decode: outsw") {
      assert(OUTSW.encode sameElements i2ba(0x6F))
    }
    
  }

}
