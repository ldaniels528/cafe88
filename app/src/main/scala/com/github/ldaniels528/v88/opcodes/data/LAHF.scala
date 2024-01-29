package com.github.ldaniels528.v88.opcodes.data

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

/**
 * LAHF — Load AH from Flags
 */
case object LAHF extends Instruction:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???

