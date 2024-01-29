package com.github.ldaniels528.v88

import com.github.ldaniels528.v88.decoders.Decoder
import com.github.ldaniels528.v88.decoders.Decoder.getClass
import com.github.ldaniels528.v88.opcodes.Instruction
import org.slf4j.{Logger, LoggerFactory}

trait VerificationTools {
  protected val logger: Logger = LoggerFactory.getLogger(getClass)

  def disassemble(image: Array[Int]): Instruction = {
    implicit val vpc: VirtualPC = VirtualPC(memory = new VirtualMemory(image.map(_.toByte)))
    val opcode = Decoder.decodeNext(ip = MemoryCursor(0))
    logger.info(s"disassemble: ${opcode.toCode}")
    opcode
  }

}
