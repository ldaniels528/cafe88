package com.github.ldaniels528.v88.opcodes

/**
 * Illegal Instruction Exception
 * @param host the host [[Instruction]]
 */
case class IllegalInstructionException(host: Instruction)
  extends RuntimeException(s"Illegal instruction ${host.toCode}")
