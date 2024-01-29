package com.github.ldaniels528.v88.encoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder5x
import com.github.ldaniels528.v88.opcodes.bitwise.AAA
import com.github.ldaniels528.v88.opcodes.stack.{POP, PUSH}
import com.github.ldaniels528.v88.operands.*
import org.scalatest.funspec.AnyFunSpec

class Encoder5xSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[Decoder5x].getSimpleName) {

    it("should encode: push ax") {
      assert(PUSH(AX).encode sameElements i2ba(0x50))
    }

    it("should encode: push cx") {
      assert(PUSH(CX).encode sameElements i2ba(0x51))
    }

    it("should encode: push dx") {
      assert(PUSH(DX).encode sameElements i2ba(0x52))
    }

    it("should encode: push bx") {
      assert(PUSH(BX).encode sameElements i2ba(0x53))
    }

    it("should encode: push sp") {
      assert(PUSH(SP).encode sameElements i2ba(0x54))
    }

    it("should encode: push bp") {
      assert(PUSH(BP).encode sameElements i2ba(0x55))
    }

    it("should encode: push si") {
      assert(PUSH(SI).encode sameElements i2ba(0x56))
    }

    it("should encode: push di") {
      assert(PUSH(DI).encode sameElements i2ba(0x57))
    }

    it("should encode: pop ax") {
      assert(POP(AX).encode sameElements i2ba(0x58))
    }

    it("should encode: pop cx") {
      assert(POP(CX).encode sameElements i2ba(0x59))
    }

    it("should encode: pop dx") {
      assert(POP(DX).encode sameElements i2ba(0x5A))
    }

    it("should encode: pop bx") {
      assert(POP(BX).encode sameElements i2ba(0x5B))
    }

    it("should encode: pop sp") {
      assert(POP(SP).encode sameElements i2ba(0x5C))
    }

    it("should encode: pop bp") {
      assert(POP(BP).encode sameElements i2ba(0x5D))
    }

    it("should encode: pop si") {
      assert(POP(SI).encode sameElements i2ba(0x5E))
    }

    it("should encode: pop di") {
      assert(POP(DI).encode sameElements i2ba(0x5F))
    }

  }

}
