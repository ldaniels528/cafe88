package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.NOOP
import com.github.ldaniels528.v88.opcodes.data.*
import com.github.ldaniels528.v88.opcodes.flow.CALL
import com.github.ldaniels528.v88.opcodes.flow.CALL.Far
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.{POPF, PUSHF}
import com.github.ldaniels528.v88.operands.*
import org.scalatest.funspec.AnyFunSpec

class Decoder9xSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[Decoder9x].getSimpleName) {

    it("should decode: xchg ax,ax | nop") {
      assert(disassemble(Array(0x90)) == XCHG(AX, AX))
    }

    it("should decode: xchg cx,ax") {
      assert(disassemble(Array(0x91)) == XCHG(CX, AX))
    }

    it("should decode: xchg dx,ax") {
      assert(disassemble(Array(0x92)) == XCHG(DX, AX))
    }

    it("should decode: xchg bx,ax") {
      assert(disassemble(Array(0x93)) == XCHG(BX, AX))
    }

    it("should decode: xchg sp,ax") {
      assert(disassemble(Array(0x94)) == XCHG(SP, AX))
    }

    it("should decode: xchg bp,ax") {
      assert(disassemble(Array(0x95)) == XCHG(BP, AX))
    }

    it("should decode: xchg si,ax") {
      assert(disassemble(Array(0x96)) == XCHG(SI, AX))
    }

    it("should decode: xchg di,ax") {
      assert(disassemble(Array(0x97)) == XCHG(DI, AX))
    }

    it("should decode: cbw") {
      assert(disassemble(Array(0x98)) == CBW)
    }

    it("should decode: cwd") {
      assert(disassemble(Array(0x99)) == CWD)
    }

    it("should decode: call DEAD:BEEF") {
      assert(disassemble(Array(0x9A, 0xAD, 0xDE, 0xEF, 0xBE)) == CALL.Far(0xDEAD, 0xBEEF))
    }

    it("should decode: wait") {
      assert(disassemble(Array(0x9B)) == WAIT)
    }

    it("should decode: pushf") {
      assert(disassemble(Array(0x9C)) == PUSHF)
    }

    it("should decode: popf") {
      assert(disassemble(Array(0x9D)) == POPF)
    }

    it("should decode: sahf") {
      assert(disassemble(Array(0x9E)) == SAHF)
    }

    it("should decode: lahf") {
      assert(disassemble(Array(0x9F)) == LAHF)
    }

  }

}
