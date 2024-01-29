package com.github.ldaniels528.v88.operands

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.OpCode
import com.github.ldaniels528.v88.opcodes.NOOP
import com.github.ldaniels528.v88.operands.{BoxNear, Box_BP, Box_BP_DI, Box_BP_SI, Box_BX, Box_BX_DI, Box_BX_SI, Box_DI, Box_SI, MemoryBox}
import org.scalatest.funspec.AnyFunSpec
import org.slf4j.LoggerFactory

class MemoryBoxSpec extends AnyFunSpec with VerificationTools:
  private implicit val vpc: VirtualPC = VirtualPC(memory = VirtualMemory())
  private val ip = MemoryCursor(segment = 0x13FF, offset = 0x000)

  describe(classOf[MemoryBox].getSimpleName) {

    it("should decode: byte ptr [bx+si]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x00) // | .... ...0 00.. .000 [bx+si]
      assert(m == Box_BX_SI(is8Bit = true))
    }

    it("should decode: word ptr [bx+si]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x00) // | .... ...1 00.. .000 [bx+si]
      assert(m == Box_BX_SI(is8Bit = false))
    }

    it("should decode: byte ptr [bx+di]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x01) // | .... ...0 00.. .001 [bx+di]
      assert(m == Box_BX_DI(is8Bit = true))
    }

    it("should decode: word ptr [bx+di]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x01) // | .... ...1 00.. .001 [bx+di]
      assert(m == Box_BX_DI(is8Bit = false))
    }

    it("should decode: byte ptr [bp+si]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x02) // | .... ...0 00.. .010 [bp+si]
      assert(m == Box_BP_SI(is8Bit = true))
    }

    it("should decode: word ptr [bp+si]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x02) // | .... ...1 00.. .010 [bp+si]
      assert(m == Box_BP_SI(is8Bit = false))
    }

    it("should decode: byte ptr [bp+di]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x03) // | .... ...0 00.. .010 [bp+di]
      assert(m == Box_BP_DI(is8Bit = true))
    }

    it("should decode: word ptr [bp+di]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x03) // | .... ...1 00.. .010 [bp+di]
      assert(m == Box_BP_DI(is8Bit = false))
    }

    it("should decode: byte ptr [si]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x04) // | .... ...0 00.. .100 [si]
      assert(m == Box_SI(is8Bit = true))
    }

    it("should decode: word ptr [si]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x04) // | .... ...1 00.. .100 [si]
      assert(m == Box_SI(is8Bit = false))
    }

    it("should decode: byte ptr [di]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x05) // | .... ...0 00.. .101 [di]
      assert(m == Box_DI(is8Bit = true))
    }

    it("should decode: word ptr [di]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x05) // | .... ...1 00.. .101 [di]
      assert(m == Box_DI(is8Bit = false))
    }

    it("should decode: byte ptr [1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x06) // | .... ...0 00.. .110 [1234]
      assert(m == BoxNear(offset = 0x1234, is8Bit = true))
    }

    it("should decode: word ptr [4321]") {
      setWord(address = ip.address, value = 0x4321)

      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x06) // | .... ...1 00.. .110 [4321]
      assert(m == BoxNear(offset = 0x4321, is8Bit = false))
    }

    it("should decode: byte ptr [bx]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x07) // | .... ...0 00.. .111 [bx]
      assert(m == Box_BX(is8Bit = true))
    }

    it("should decode: word ptr [bx]") {
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x07) // | .... ...1 00.. .111 [bx]
      assert(m == Box_BX(is8Bit = false))
    }

    it("should decode: byte ptr [bx+si+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x40) // | .... ...0 01.. .000 [bx+si+77]
      assert(m == Box_BX_SI(offset = 0x77, is8Bit = true))
    }

    it("should decode: word ptr [bx+si+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x40) // | .... ...1 01.. .000 [bx+si+77]
      assert(m == Box_BX_SI(offset = 0x77, is8Bit = false))
    }

    it("should decode: byte ptr [bx+di+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x41) // | .... ...0 01.. .001 [bx+di+77]
      assert(m == Box_BX_DI(offset = 0x77, is8Bit = true))
    }

    it("should decode: word ptr [bx+di+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x41) // | .... ...1 01.. .001 [bx+di+77]
      assert(m == Box_BX_DI(offset = 0x77, is8Bit = false))
    }

    it("should decode: byte ptr [bp+si+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x42) // | .... ...0 01.. .010 [bp+si+77]
      assert(m == Box_BP_SI(offset = 0x77, is8Bit = true))
    }

    it("should decode: word ptr [bp+si+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x42) // | .... ...1 01.. .010 [bp+si+77]
      assert(m == Box_BP_SI(offset = 0x77, is8Bit = false))
    }

    it("should decode: byte ptr [bp+di+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x43) // | .... ...0 01.. .010 [bp+di+77]
      assert(m == Box_BP_DI(offset = 0x77, is8Bit = true))
    }

    it("should decode: word ptr [bp+di+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x43) // | .... ...1 01.. .010 [bp+di+77]
      assert(m == Box_BP_DI(offset = 0x77, is8Bit = false))
    }

    it("should decode: byte ptr [si+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x44) // | .... ...0 01.. .100 [si+77]
      assert(m == Box_SI(offset = 0x77, is8Bit = true))
    }

    it("should decode: word ptr [si+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x44) // | .... ...1 01.. .100 [si+77]
      assert(m == Box_SI(offset = 0x77, is8Bit = false))
    }

    it("should decode: byte ptr [di+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x45) // | .... ...0 01.. .101 [di+77]
      assert(m == Box_DI(offset = 0x77, is8Bit = true))
    }

    it("should decode: word ptr [di+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x45) // | .... ...1 01.. .101 [di+77]
      assert(m == Box_DI(offset = 0x77, is8Bit = false))
    }

    it("should decode: byte ptr [bp+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x46) // | .... ...0 01.. .110 [bp+77]
      assert(m == Box_BP(offset = 0x77, is8Bit = true))
    }

    it("should decode: word ptr [bp+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x46) // | .... ...1 01.. .110 [bp+77]
      assert(m == Box_BP(offset = 0x77, is8Bit = false))
    }

    it("should decode: byte ptr [bx+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x47) // | .... ...0 01.. .111 [bx+77]
      assert(m == Box_BX(offset = 0x77, is8Bit = true))
    }

    it("should decode: word ptr [bx+77]") {
      setWord(address = ip.address, value = 0x77)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x47) // | .... ...1 01.. .111 [bx+77]
      assert(m == Box_BX(offset = 0x77, is8Bit = false))
    }

    it("should decode: byte ptr [bx+si+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x80) // | .... ...0 10.. .000 [bx+si+1234]
      assert(m == Box_BX_SI(offset = 0x1234, is8Bit = true))
    }

    it("should decode: word ptr [bx+si+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x80) // | .... ...1 10.. .000 [bx+si+1234]
      assert(m == Box_BX_SI(offset = 0x1234, is8Bit = false))
    }

    it("should decode: byte ptr [bx+di+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x81) // | .... ...0 10.. .001 [bx+di+1234]
      assert(m == Box_BX_DI(offset = 0x1234, is8Bit = true))
    }

    it("should decode: word ptr [bx+di+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x81) // | .... ...1 10.. .001 [bx+di+1234]
      assert(m == Box_BX_DI(offset = 0x1234, is8Bit = false))
    }

    it("should decode: byte ptr [bp+si+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x82) // | .... ...0 10.. .010 [bp+si+1234]
      assert(m == Box_BP_SI(offset = 0x1234, is8Bit = true))
    }

    it("should decode: word ptr [bp+si+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x82) // | .... ...1 10.. .010 [bp+si+1234]
      assert(m == Box_BP_SI(offset = 0x1234, is8Bit = false))
    }

    it("should decode: byte ptr [bp+di+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x83) // | .... ...0 10.. .010 [bp+di+1234]
      assert(m == Box_BP_DI(offset = 0x1234, is8Bit = true))
    }

    it("should decode: word ptr [bp+di+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x83) // | .... ...1 10.. .010 [bp+di+1234]
      assert(m == Box_BP_DI(offset = 0x1234, is8Bit = false))
    }

    it("should decode: byte ptr [si+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x84) // | .... ...0 10.. .100 [si+1234]
      assert(m == Box_SI(offset = 0x1234, is8Bit = true))
    }

    it("should decode: word ptr [si+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x84) // | .... ...1 10.. .100 [si+1234]
      assert(m == Box_SI(offset = 0x1234, is8Bit = false))
    }

    it("should decode: byte ptr [di+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x85) // | .... ...0 10.. .101 [di+1234]
      assert(m == Box_DI(offset = 0x1234, is8Bit = true))
    }

    it("should decode: word ptr [di+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x85) // | .... ...1 10.. .101 [di+1234]
      assert(m == Box_DI(offset = 0x1234, is8Bit = false))
    }

    it("should decode: byte ptr [bp+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x86) // | .... ...0 10.. .110 [bp+1234]
      assert(m == Box_BP(offset = 0x1234, is8Bit = true))
    }

    it("should decode: word ptr [bp+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x86) // | .... ...1 10.. .110 [bp+1234]
      assert(m == Box_BP(offset = 0x1234, is8Bit = false))
    }

    it("should decode: byte ptr [bx+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x00, codeTRM = 0x87) // | .... ...0 10.. .111 [bx+1234]
      assert(m == Box_BX(offset = 0x1234, is8Bit = true))
    }

    it("should decode: word ptr [bx+1234]") {
      setWord(address = ip.address, value = 0x1234)
      // decode a memory reference                    | iiii ivsw ttrr rmmm
      val m = box(codeIVSW = 0x01, codeTRM = 0x87) // | .... ...1 10.. .111 [bx+1234]
      assert(m == Box_BX(offset = 0x1234, is8Bit = false))
    }

  }

  private def box(codeIVSW: Int, codeTRM: Int): MemoryBox =
    operands.MemoryBox(ip, OpCode(ip, codeIVSW = codeIVSW, codeTRM = codeTRM))

  private def setWord(address: Int, value: Int): Unit =
    vpc.memory.setWord(address = address, value = value)
