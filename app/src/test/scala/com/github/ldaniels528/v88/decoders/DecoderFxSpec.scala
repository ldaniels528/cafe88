package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.VerificationTools
import com.github.ldaniels528.v88.opcodes.NOOP
import com.github.ldaniels528.v88.opcodes.alu.LOCK
import com.github.ldaniels528.v88.opcodes.data.DB
import com.github.ldaniels528.v88.opcodes.flow.*
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.*
import com.github.ldaniels528.v88.opcodes.streaming.{REPNZ, REPZ}
import org.scalatest.funspec.AnyFunSpec

class DecoderFxSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[DecoderFx].getSimpleName) {

    it("should decode: lock") {
      assert(disassemble(Array(0xF0, 0x90)) == LOCK(NOOP))
    }

    it("should decode: db 0xF1") {
      assert(disassemble(Array(0xF1)) == DB(0xF1))
    }

    it("should decode: repnz") {
      assert(disassemble(Array(0xF2, 0x90)) == REPNZ(NOOP))
    }

    it("should decode: repz") {
      assert(disassemble(Array(0xF3, 0x90)) == REPZ(NOOP))
    }

    it("should decode: hlt") {
      assert(disassemble(Array(0xF4)) == HLT)
    }

    it("should decode: cmc") {
      assert(disassemble(Array(0xF5)) == CMC)
    }

    it("should decode: clc") {
      assert(disassemble(Array(0xF8)) == CLC)
    }

    it("should decode: stc") {
      assert(disassemble(Array(0xF9)) == STC)
    }

    it("should decode: cli") {
      assert(disassemble(Array(0xFA)) == CLI)
    }

    it("should decode: sti") {
      assert(disassemble(Array(0xFB)) == STI)
    }

    it("should decode: cld") {
      assert(disassemble(Array(0xFC)) == CLD)
    }

    it("should decode: std") {
      assert(disassemble(Array(0xFD)) == STD)
    }
    
  }

}
