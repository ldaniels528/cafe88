package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.*

case class JNC(delta: Int) extends ShortJumpComputation: 

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    if (!state.flags.CF) state.ip.address += jumpDelta