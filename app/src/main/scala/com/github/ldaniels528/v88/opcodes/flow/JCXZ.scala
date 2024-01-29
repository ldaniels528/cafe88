package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.*

case class JCXZ(delta: Int) extends ShortJumpComputation: 

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    if (state.cx.value == 0) state.ip.address += jumpDelta

