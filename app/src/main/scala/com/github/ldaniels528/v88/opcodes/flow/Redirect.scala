package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.opcodes.Instruction

/**
 * Represents an instruction that restores a previous save-point created via a [[CALL]] or [[CALL.Far]]
 */
trait Redirect extends Instruction
