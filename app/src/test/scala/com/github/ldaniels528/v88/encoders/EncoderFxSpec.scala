package com.github.ldaniels528.v88.encoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.DecoderFx
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.data.*
import com.github.ldaniels528.v88.opcodes.flow.*
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.*
import com.github.ldaniels528.v88.opcodes.streaming.{REPNZ, REPZ}
import com.github.ldaniels528.v88.operands.CX
import org.scalatest.funspec.AnyFunSpec

class EncoderFxSpec extends AnyFunSpec with VerificationTools:

  describe(classOf[DecoderFx].getSimpleName) {

    it("should encode: lock") {
      assert(LOCK(INC(CX)).encode sameElements Array(0xF0, 0x41).map(_.toByte))
    }

    it("should encode: hlt") {
      assert(HLT.encode sameElements i2ba(0xF4))
    }

    it("should encode: cmc") {
      assert(CMC.encode sameElements i2ba(0xF5))
    }

    it("should encode: clc") {
      assert(CLC.encode sameElements i2ba(0xF8))
    }

    it("should encode: stc") {
      assert(STC.encode sameElements i2ba(0xF9))
    }

    it("should encode: cli") {
      assert(CLI.encode sameElements i2ba(0xFA))
    }

    it("should encode: sti") {
      assert(STI.encode sameElements i2ba(0xFB))
    }

    it("should encode: cld") {
      assert(CLD.encode sameElements i2ba(0xFC))
    }

    it("should encode: std") {
      assert(STD.encode sameElements i2ba(0xFD))
    }

  }
