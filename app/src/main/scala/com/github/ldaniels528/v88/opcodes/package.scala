package com.github.ldaniels528.v88

import com.github.ldaniels528.v88.opcodes.data.XCHG
import com.github.ldaniels528.v88.operands.AX

package object opcodes {

  val NOOP: Instruction = XCHG(AX, AX)

}
