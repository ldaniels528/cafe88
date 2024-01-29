package com.github.ldaniels528.v88.opcodes.bitwise

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.operands.{AH, AL}

case object AAA extends Instruction {
  override def encode: Array[Byte] = i2ba(0x37)
  
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = {
    // get the low nibble of AL (mask = 0000 1111)
    val lowNibble = AL.get & 0x0F

    /* if the low nibble (lower 4-bits) of AL > 9 or AF = 1
         then:
            AL = AL + 6
            AH = AH + 1
            AF = 1
            CF = 1
        else:
            AF = 0 and CF = 0
    */
    val FLAGS = state.flags
    if ((lowNibble > 9) || FLAGS.AF) {
      AL.add(6)
      AH.add(1)
      FLAGS.AF = true
      FLAGS.CF = true
    } else {
      FLAGS.AF = false
      FLAGS.CF = false
    }
  }
}

