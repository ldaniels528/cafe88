package com.github.ldaniels528.v88.opcodes.streaming

import com.github.ldaniels528.v88.opcodes.streaming.MassDataInstruction
import com.github.ldaniels528.v88.{CPUState, VirtualMemory, VirtualPC, i2ba}

sealed trait OUTS extends MassDataInstruction

case object OUTSB extends OUTS:
  override def encode: Array[Byte] = i2ba(0x6E)
  
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???

case object OUTSW extends OUTS:
  override def encode: Array[Byte] = i2ba(0x6F)
  
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???