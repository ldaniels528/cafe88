package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

case class LOOPNZ(delta: Int) extends ShortJumpComputation:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    if ((state.cx.value > 0) && !state.flags.ZF)
      // decrement cx
      state.cx.value -= 1
      state.ip.address += jumpDelta

