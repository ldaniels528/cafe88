package com.github.ldaniels528.v88

import org.scalatest.funspec.AnyFunSpec
import org.slf4j.LoggerFactory

class FlagRegister16Spec extends AnyFunSpec with VerificationTools {
  private val logger = LoggerFactory.getLogger(getClass)

  describe(classOf[FlagRegister16].getSimpleName) {

    it("should read/write the Auxiliary flag") {
      val flags = new FlagRegister16(name = "flags")
      assert(!flags.AF)
      flags.AF = true
      assert(flags.AF)
    }

    it("should read/write the Carry flag") {
      val flags = new FlagRegister16(name = "flags")
      assert(!flags.CF)
      flags.CF = true
      assert(flags.CF)
    }

    it("should read/write the Direction flag") {
      val flags = new FlagRegister16(name = "flags")
      assert(!flags.DF)
      flags.DF = true
      assert(flags.DF)
    }

    it("should read/write the Interrupt flag") {
      val flags = new FlagRegister16(name = "flags")
      assert(flags.IF)
      flags.IF = false
      assert(!flags.IF)
    }

    it("should read/write the Overflow flag") {
      val flags = new FlagRegister16(name = "flags")
      assert(!flags.OF)
      flags.OF = true
      assert(flags.OF)
    }

    it("should read/write the Parity flag") {
      val flags = new FlagRegister16(name = "flags")
      assert(!flags.PF)
      flags.PF = true
      assert(flags.PF)
    }

    it("should read/write the Sign flag") {
      val flags = new FlagRegister16(name = "flags")
      assert(!flags.SF)
      flags.SF = true
      assert(flags.SF)
    }

    it("should read/write the Trap flag") {
      val flags = new FlagRegister16(name = "flags")
      assert(!flags.TF)
      flags.TF = true
      assert(flags.TF)
    }

    it("should read/write the Zero flag") {
      val flags = new FlagRegister16(name = "flags")
      assert(!flags.ZF)
      flags.ZF = true
      assert(flags.ZF)
    }

  }

}
