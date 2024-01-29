package com.github.ldaniels528.v88.encoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder4x
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.operands.{AX, BX, CX, DX}
import org.scalatest.funspec.AnyFunSpec

class Encoder4xSpec extends AnyFunSpec with VerificationTools:

  describe(classOf[Decoder4x].getSimpleName) {

    it("should encode: inc ax") {
      assert(INC(AX).encode sameElements i2ba(0x40))
    }

    it("should encode: inc cx") {
      assert(INC(CX).encode sameElements i2ba(0x41))
    }

    it("should encode: inc dx") {
      assert(INC(DX).encode sameElements i2ba(0x42))
    }

    it("should encode: inc bx") {
      assert(INC(BX).encode sameElements i2ba(0x43))
    }

  }
