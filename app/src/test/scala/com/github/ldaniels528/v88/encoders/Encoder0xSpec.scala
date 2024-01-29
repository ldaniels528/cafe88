package com.github.ldaniels528.v88.encoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder0x
import com.github.ldaniels528.v88.opcodes.stack.*
import com.github.ldaniels528.v88.operands.{CS, ES}
import org.scalatest.funspec.AnyFunSpec

class Encoder0xSpec extends AnyFunSpec with VerificationTools:

  describe(classOf[Decoder0x].getSimpleName) {

    it("should encode: push es") {
      assert(PUSH(ES).encode sameElements i2ba(0x06))
    }

    it("should encode: pop es") {
      assert(POP(ES).encode sameElements i2ba(0x07))
    }

    it("should encode: push cs") {
      assert(PUSH(CS).encode sameElements i2ba(0x0E))
    }

    it("should encode: pop cs") {
      assert(POP(CS).encode sameElements i2ba(0x0F))
    }
    
  }
