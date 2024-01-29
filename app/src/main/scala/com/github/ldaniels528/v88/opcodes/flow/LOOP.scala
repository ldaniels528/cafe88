package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.*

case class LOOP(delta: Int) extends ShortJumpComputation:

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    if (state.cx.value > 0) 
      // decrement cx
      state.cx.value -= 1
      state.ip.address += jumpDelta
    