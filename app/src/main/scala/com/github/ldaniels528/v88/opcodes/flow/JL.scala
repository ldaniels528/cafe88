package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.*

case class JL(delta: Int) extends ShortJumpComputation:

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    if (state.flags.SF || state.flags.OF) 
      state.ip.address += jumpDelta
