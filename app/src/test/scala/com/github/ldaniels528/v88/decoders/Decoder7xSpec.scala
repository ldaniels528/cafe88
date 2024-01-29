package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.opcodes.flow.*
import com.github.ldaniels528.v88.{VerificationTools, VirtualMemory}
import org.scalatest.funspec.AnyFunSpec

class Decoder7xSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[Decoder7x].getSimpleName) {

    it("should decode 70h: jo 40h") {
      assert(disassemble(Array(0x70, 0x40)) == JO(0x40))
    }

    it("should decode 71h: jno 80h") {
      assert(disassemble(Array(0x71, 0x80)) == JNO(0x80))
    }

    it("should decode 72h: jc 90h") {
      assert(disassemble(Array(0x72, 0x90)) == JC(0x90))
    }

    it("should decode 73h: jnc 80h") {
      assert(disassemble(Array(0x73, 0x80)) == JNC(0x80))
    }

    it("should decode 74h: jz 90h") {
      assert(disassemble(Array(0x74, 0x90)) == JZ(0x90))
    }

    it("should decode 75h: jnz 80h") {
      assert(disassemble(Array(0x75, 0x80)) == JNZ(0x80))
    }

    it("should decode 76h: jbe 90h") {
      assert(disassemble(Array(0x76, 0x90)) == JBE(0x90))
    }

    it("should decode 77h: ja 80h") {
      assert(disassemble(Array(0x77, 0x80)) == JA(0x80))
    }

    it("should decode 78h: js 90h") {
      assert(disassemble(Array(0x78, 0x90)) == JS(0x90))
    }

    it("should decode 79h: jns 80h") {
      assert(disassemble(Array(0x79, 0x80)) == JNS(0x80))
    }

    it("should decode 7Ah: jpe 90h") {
      assert(disassemble(Array(0x7A, 0x90)) == JPE(0x90))
    }

    it("should decode 7Bh: jpo 80h") {
      assert(disassemble(Array(0x7B, 0x80)) == JPO(0x80))
    }

    it("should decode 7Ch: jl 90h") {
      assert(disassemble(Array(0x7C, 0x90)) == JL(0x90))
    }

    it("should decode 7Dh: jge 80h") {
      assert(disassemble(Array(0x7D, 0x80)) == JGE(0x80))
    }

    it("should decode 7Eh: jle 90h") {
      assert(disassemble(Array(0x7E, 0x90)) == JLE(0x90))
    }

    it("should decode 7Fh: jg 80h") {
      assert(disassemble(Array(0x7F, 0x80)) == JG(0x80))
    }

  }

}
