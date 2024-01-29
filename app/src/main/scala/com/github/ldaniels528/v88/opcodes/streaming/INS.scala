package com.github.ldaniels528.v88.opcodes.streaming

import com.github.ldaniels528.v88.opcodes.streaming.MassDataInstruction
import com.github.ldaniels528.v88.{CPUState, VirtualMemory, VirtualPC, i2ba}

sealed trait INS extends MassDataInstruction

case object INSB extends INS:
  override def encode: Array[Byte] = i2ba(0x6C)
  
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???

case object INSW extends INS:
  override def encode: Array[Byte] = i2ba(0x6D)
  
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    state.flags.value = ???

