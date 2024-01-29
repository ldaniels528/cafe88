package com.github.ldaniels528.v88.opcodes.alu

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.OneMutableOperand
import com.github.ldaniels528.v88.operands.MutableOperand

case class MUL(dest: MutableOperand) extends OneMutableOperand:
  
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???

