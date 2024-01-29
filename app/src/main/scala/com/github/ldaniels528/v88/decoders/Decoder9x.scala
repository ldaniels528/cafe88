package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.data.*
import com.github.ldaniels528.v88.opcodes.flow.CALL
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.{POPF, PUSHF}
import com.github.ldaniels528.v88.opcodes.{Instruction, NOOP}
import com.github.ldaniels528.v88.operands.*

/**
 * Processes instruction codes between 90h and 9Fh (e.g. "CBW")
 * <pre>
 * ---------------------------------------------------------------------------
 * type	bits	description 				      comments
 * ---------------------------------------------------------------------------
 * i		4	 	  instruction type
 * j		3		  instruction sub type
 * k		1	 	  instruction category code
 * n		8/16	data value					      (optional)
 *
 * ---------------------------------------------------------------------------
 * instruction			  code 			iiii jjj k nnnn nnnn
 * ---------------------------------------------------------------------------
 * xchg ax,ax | nop		90				1001 000 0
 * xchg cx,ax			    91				1001 000 1
 * xchg dx,ax			    92				1001 001 0
 * xchg bx,ax			    93				1001 001 1
 * xchg sp,ax			    94				1001 010 0
 * xchg bp,ax			    95				1001 010 1
 * xchg si,ax			    96				1001 011 0
 * xchg di,ax			    97				1001 011 1
 * cbw					      98				1001 100 0
 * cwd					      99				1001 100 1
 * call abcd:defg		  9A				1001 101 0 cd ab fg de
 * wait					      9B				1001 101 1
 * pushf				      9C				1001 110 0
 * popf					      9D				1001 110 1
 * sahf					      9E				1001 111 0
 * lahf					      9F				1001 111 1
 * </pre>
 */
class Decoder9x extends Decoder:
  private val channels = Seq[(MemoryCursor, VirtualMemory) => Instruction](
    (c, m) => XCHG(AX, AX), // NOOP
    (c, m) => XCHG(CX, AX),
    (c, m) => XCHG(DX, AX),
    (c, m) => XCHG(BX, AX),
    (c, m) => XCHG(SP, AX),
    (c, m) => XCHG(BP, AX),
    (c, m) => XCHG(SI, AX),
    (c, m) => XCHG(DI, AX),
    (c, m) => CBW,
    (c, m) => CWD,
    (c, m) => CALL.Far(segment = c.nextWord(m), offset = c.nextWord(m)),
    (c, m) => WAIT,
    (c, m) => PUSHF,
    (c, m) => POPF,
    (c, m) => SAHF,
    (c, m) => LAHF)

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val memory = vpc.memory
    channels(ip.nextByte(memory) & 0x0F)(ip, memory)
