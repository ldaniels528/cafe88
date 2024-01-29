package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.flow.*
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.operands.{AL, AX, ByteValue, DX}
import org.scalatest.funspec.AnyFunSpec

class DecoderExSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[DecoderEx].getSimpleName) {

    it("should decode: loopnz 11") {
      assert(disassemble(Array(0xE0, 0x11)) == LOOPNZ(0x11))
    }

    it("should decode: loopz 22") {
      assert(disassemble(Array(0xE1, 0x22)) == LOOPZ(0x22))
    }

    it("should decode: loop 33") {
      assert(disassemble(Array(0xE2, 0x33)) == LOOP(0x33))
    }

    it("should decode: jcxz 44") {
      assert(disassemble(Array(0xE3, 0x44)) == JCXZ(0x44))
    }

    it("should decode: in al,55h") {
      assert(disassemble(Array(0xE4, 0x55)) == IN(AL, ByteValue(0x55)))
    }

    it("should decode: in ax,66h") {
      assert(disassemble(Array(0xE5, 0x66)) == IN(AX, ByteValue(0x66)))
    }

    it("should decode: out 88h,al") {
      assert(disassemble(Array(0xE6, 0x88)) == OUT(ByteValue(0x88), AL))
    }

    it("should decode: out 88h,ax") {
      assert(disassemble(Array(0xE7, 0x88)) == OUT(ByteValue(0x88), AX))
    }

    it("should decode: call AA99") {
      assert(disassemble(Array(0xE8, 0x99, 0xAA)) == CALL.Near(0xAA99))
    }

    it("should decode: jmp near AA99") {
      assert(disassemble(Array(0xE9, 0x99, 0xAA)) == JMP.Near(0xAA99))
    }

    it("should decode: jmp 1234:5678") {
      assert(disassemble(Array(0xEA, 0x34, 0x12, 0x78, 0x56)) == JMP.Far(0x1234, 0x5678))
    }

    it("should decode: jmp short AA") {
      assert(disassemble(Array(0xEB, 0xAA)) == JMP.Small(0xAA))
    }

    it("should decode: in al,dx") {
      assert(disassemble(Array(0xEC)) == IN(AL, DX))
    }

    it("should decode: in ax,dx") {
      assert(disassemble(Array(0xED)) == IN(AX, DX))
    }

    it("should decode: out dx,al") {
      assert(disassemble(Array(0xEE)) == OUT(DX, AL))
    }

    it("should decode: out dx,ax") {
      assert(disassemble(Array(0xEF)) == OUT(DX, AX))
    }

  }

}
