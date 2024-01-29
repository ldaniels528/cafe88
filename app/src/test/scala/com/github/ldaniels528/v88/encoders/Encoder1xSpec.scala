package com.github.ldaniels528.v88.encoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder1x
import com.github.ldaniels528.v88.opcodes.stack.*
import com.github.ldaniels528.v88.operands.{DS, SS}
import org.scalatest.funspec.AnyFunSpec

class Encoder1xSpec extends AnyFunSpec with VerificationTools:

  describe(classOf[Decoder1x].getSimpleName) {

    it("should encode: push ss") {
      assert(PUSH(SS).encode sameElements i2ba(0x16))
    }

    it("should encode: pop ss") {
      assert(POP(SS).encode sameElements i2ba(0x17))
    }

    it("should encode: push ds") {
      assert(PUSH(DS).encode sameElements i2ba(0x1E))
    }

    it("should encode: pop ds") {
      assert(POP(DS).encode sameElements i2ba(0x1F))
    }

  }

