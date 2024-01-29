package com.github.ldaniels528.v88

import com.github.ldaniels528.v88.VirtualCPU.decoders8086
import com.github.ldaniels528.v88.decoders.*
import com.github.ldaniels528.v88.decoders.Decoder.decodeNext
import com.github.ldaniels528.v88.opcodes.Instruction

/**
 * Represents a Virtual 8086 CPU
 * @param decoders the [[Decoder instruction decoders]]
 */
case class VirtualCPU(decoders: Seq[Decoder] = decoders8086):

  /**
   * Executes the next opcode at CS:IP
   * @param state the [[CPUState CPU state]]
   * @param vpc   the [[VirtualPC virtual PC]]
   * @return the updated [[CPUState CPU state]]
   */
  def executeNext(state: CPUState)(implicit vpc: VirtualPC): Option[(Instruction, CPUState)] =
    if (state.isRunning && (state.ip.address < vpc.memory.length))
      val opCode = decodeNext(state.ip, decoders)
      opCode.execute()(state, vpc)
      Some((opCode, state))
    else None

  /**
   * Starts execution at CS:IP
   * @param state the [[CPUState CPU state]]
   * @param vpc   the [[VirtualPC virtual PC]]
   * @return the updated [[CPUState CPU state]]
   */
  def run(state: CPUState)(implicit vpc: VirtualPC): CPUState =
    while (state.isRunning && (state.ip.address < vpc.memory.length)) 
      decodeNext(state.ip, decoders).execute()(state, vpc)
    state

object VirtualCPU:
  // define the default 8086 decoders
  val decoders8086: Array[Decoder] = Array[Decoder](
    new Decoder0x(), new Decoder1x(), new Decoder2x(), new Decoder3x(),
    new Decoder4x(), new Decoder5x(), new Decoder6x(), new Decoder7x(),
    new Decoder8x(), new Decoder9x(), new DecoderAx(), new DecoderBx(),
    new DecoderCx(), new DecoderDx(), new DecoderEx(), new DecoderFx())