package com.github.ldaniels528.v88.encoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.{Decoder0x, Decoder2x}
import com.github.ldaniels528.v88.opcodes.NOOP
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.bitwise.{DAA, DAS}
import com.github.ldaniels528.v88.opcodes.data.OverrideDS
import com.github.ldaniels528.v88.operands.{AX, CS, ES}
import org.scalatest.funspec.AnyFunSpec

class Encoder2xSpec extends AnyFunSpec with VerificationTools:

  describe(classOf[Decoder2x].getSimpleName) {

    it("should encode: es: inc ax") {
      assert(OverrideDS(ES, INC(AX)).encode sameElements Array(0x26, 0x40).map(_.toByte))
    }

    it("should encode: daa") {
      assert(DAA.encode sameElements i2ba(0x27))
    }

    it("should encode: cs: inc ax") {
      assert(OverrideDS(CS, INC(AX)).encode sameElements Array(0x2E, 0x40).map(_.toByte))
    }

    it("should encode: das") {
      assert(DAS.encode sameElements i2ba(0x2F))
    }

  }

