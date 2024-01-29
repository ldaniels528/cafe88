package com.github.ldaniels528.v88.opcodes.alu

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.getRegisterID
import com.github.ldaniels528.v88.opcodes.{Instruction, OneMutableOperand}
import com.github.ldaniels528.v88.operands.{MutableOperand, Register}

case class DEC(dest: MutableOperand) extends Instruction with OneMutableOperand with Lockable:
  override def encode: Array[Byte] = dest match
    case r: Register => i2ba(0x48 | getRegisterID(r))
    case x => throw IllegalOperandException(x)
    
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = dest.add(-1)
