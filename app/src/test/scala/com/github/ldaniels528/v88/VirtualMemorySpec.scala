package com.github.ldaniels528.v88

import com.github.ldaniels528.v88.VirtualMemory.toAddress
import org.scalatest.funspec.AnyFunSpec

class VirtualMemorySpec extends AnyFunSpec {

  describe(classOf[VirtualMemory].getSimpleName) {

    it("should write bytes then retrieve a slice of memory") {
      implicit val memory: VirtualMemory = new VirtualMemory(new Array[Byte](60))
      memory.setByte(50, 0xde)
      memory.setByte(51, 0xad)
      memory.setByte(52, 0xbe)
      memory.setByte(53, 0xef)

      val bytes = memory.slice(50, 54)
      assert(bytes sameElements Array(0xde, 0xad, 0xbe, 0xef).map(_.toByte))
    }

    it("should read/write word values") {
      implicit val memory: VirtualMemory = new VirtualMemory(new Array[Byte](10))
      memory.setWord(0, 0xf89f)
      val value = memory.getWord(from = 0)
      assert(value == 0xf89f)
    }

    it("should convert a segment and offset into a memory address") {
      assert(toAddress(0xA000, 0xBCDE) == 0xABCDE)
    }

  }

}
