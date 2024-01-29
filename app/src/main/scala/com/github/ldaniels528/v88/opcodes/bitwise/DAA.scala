package com.github.ldaniels528.v88.opcodes.bitwise

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.*

/**
 * <h4>DAA</h4>
 * These instructions are used in conjunction with the add and subtract instructions to perform
 * binary-coded decimal arithmetic in packed (one BCD digit per nibble) form. For the unpacked
 * equivalents, see Section B.4.
 * <p/>
 * DAA should be used after a one-byte ADD instruction whose destination was the AL register:
 * by means of examining the value in the AL and also the auxiliary carry flag AF, it determines
 * whether either digit of the addition has overflowed, and adjusts it (and sets the carry and
 * auxiliary-carry flags) if so. You can add long BCD strings together by doing ADD/DAA on the
 * low two digits, then doing ADC/DAA on each subsequent pair of digits.
 * <p/>
 * DAS works similarly to DAA, but is for use after SUB instructions rather than ADD.
 * <p/>
 * The daa instruction is used to adjust the content of the AL register after that register is
 * used to perform the addition of two packed BCDs. The CPU uses the following logic:
 * <pre>
 * CF_old = CF
 * IF (al AND 0Fh > 9) or (the Auxilliary Flag is set)
 * al = al+6
 * CF = CF or CF_old
 * AF set
 * ENDIF
 * IF (al > 99h) or (Carry Flag is set)
 * al = al + 60h
 * CF set
 * ENDIF
 * </pre>
 * @author lawrence.daniels@gmail.com
 * @see DAS
 */
case object DAA extends Instruction:
  override def encode: Array[Byte] = i2ba(0x27)

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = {
    // save the state of CF
    val oldCF = state.flags.CF

    // http://www.ray.masmcode.com/BCDdaa.html// http://www.ray.masmcode.com/BCDdaa.html
    if (state.flags.AF || (state.al.value & 0x0F) > 9) {
      state.al.value += 6
      state.flags.CF = state.flags.CF || oldCF
      state.flags.AF = true
    }

    if (state.flags.CF || state.al.value > 0x99) {
      state.al.value += 0x60
      state.flags.CF = true
    }
  }
