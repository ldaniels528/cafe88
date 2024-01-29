package com.github.ldaniels528.v88.encoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder3x
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.bitwise.{AAA, AAS, DAA}
import com.github.ldaniels528.v88.opcodes.data.{OverrideDS, XCHG}
import com.github.ldaniels528.v88.opcodes.stack.POP
import com.github.ldaniels528.v88.opcodes.{Instruction, NOOP}
import com.github.ldaniels528.v88.operands.{AX, DS, SS}
import org.scalatest.funspec.AnyFunSpec

class Encoder3xSpec extends AnyFunSpec with VerificationTools:

  describe(classOf[Decoder3x].getSimpleName) {

    it("should encode: ss: pop ax") {
      assert(OverrideDS(SS, POP(AX)).encode sameElements Array(0x36, 0x58).map(_.toByte))
    }

    it("should encode: aaa") {
      assert(AAA.encode sameElements i2ba(0x37))
    }

    it("should encode: ds: inc ax") {
      assert(OverrideDS(DS, POP(AX)).encode sameElements Array(0x3E, 0x58).map(_.toByte))
    }

    it("should encode: aas") {
      assert(AAS.encode sameElements i2ba(0x3F))
    }

  }


