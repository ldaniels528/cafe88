package com.github.ldaniels528.v88.opcodes.data

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

case class DB(byteValue: Int) extends Instruction:

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ()

  override def toCode: String = f"db $byteValue%02Xh"

