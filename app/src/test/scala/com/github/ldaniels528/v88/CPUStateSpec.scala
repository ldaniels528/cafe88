package com.github.ldaniels528.v88

import org.scalatest.funspec.AnyFunSpec
import org.slf4j.LoggerFactory

class CPUStateSpec extends AnyFunSpec with VerificationTools {
  private val logger = LoggerFactory.getLogger(getClass)

  describe(classOf[CPUState].getSimpleName) {

    it("should push/pop values to/from the stack") {
      implicit val memory: VirtualMemory = new VirtualMemory(new Array[Byte](100))
      implicit val state: CPUState = CPUState()
      val cpu: VirtualCPU = VirtualCPU()
      
      // set the stack pointer
      state.sp.value = memory.length - 1

      // push items onto the stack
      memory.push(0x1234)
      memory.push(0x5678)
      memory.push(0x2222)
      memory.push(0xdead)

      // pop items from the stack
      val a = memory.pop()
      val b = memory.pop()
      val c = memory.pop()
      val d = memory.pop()

      // verify the values
      assert(a == 0xdead)
      assert(b == 0x2222)
      assert(c == 0x5678)
      assert(d == 0x1234)
    }

  }


}
