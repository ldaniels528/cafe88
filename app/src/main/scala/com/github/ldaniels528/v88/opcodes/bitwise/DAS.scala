package com.github.ldaniels528.v88.opcodes.bitwise

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.operands.AL

/**
 * DAS
 * <p>These instructions are used in conjunction with the add and subtract instructions to perform
 * binary-coded decimal arithmetic in packed (one BCD digit per nibble) form. For the unpacked
 * equivalents, see Section B.4.
 * <p/><p>DAA should be used after a one-byte ADD instruction whose destination was the AL register:
 * by means of examining the value in the AL and also the auxiliary carry flag AF, it determines
 * whether either digit of the addition has overflowed, and adjusts it (and sets the carry and
 * auxiliary-carry flags) if so. You can add long BCD strings together by doing ADD/DAA on the
 * low two digits, then doing ADC/DAA on each subsequent pair of digits.
 * <p/><p>
 * DAS works similarly to DAA, but is for use after SUB instructions rather than ADD.
 * <p/><p>
 * The das instruction is used to adjust the content of the AL register after that register is used
 * to perform the subtraction of two packed BCDs. The CPU uses the following logic:</p>
 * <pre>
 * CF_old = CF
 * IF (al AND 0Fh > 9) or (the Auxiliary Flag is set)
 * al = al-6
 * CF = CF or CF_old
 * AF set
 * ENDIF
 * IF (al > 99h) or (Carry Flag is set)
 * al = al - 60h
 * CF set
 * ENDIF
 * </pre>
 * @see [[DAA]]
 */
case object DAS extends Instruction:
  override def encode: Array[Byte] = i2ba(0x2F)

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = {
    // save the state of CF
    val FLAGS = state.flags
    val oldCF = FLAGS.CF

    // http://www.ray.masmcode.com/BCDdas.html
    if (FLAGS.AF || (AL.get & 0x0F) > 9) {
      AL.add(-6)
      FLAGS.CF = FLAGS.CF || oldCF
      FLAGS.AF = true
    }

    if (FLAGS.CF || AL.get > 0x99) {
      AL.add(-0x60)
      FLAGS.CF = true
    }
  }


