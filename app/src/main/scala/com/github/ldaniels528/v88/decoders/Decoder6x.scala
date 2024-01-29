package com.github.ldaniels528.v88
package decoders

import com.github.ldaniels528.v88.decoders.Decoder.decodeTwoOperands
import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.opcodes.data.DB
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.{POPA, PUSHA}
import com.github.ldaniels528.v88.opcodes.streaming.{INSB, INSW, OUTSB, OUTSW}
import com.github.ldaniels528.v88.{VirtualMemory, VirtualPC}

/**
 * Decodes instruction codes between 60h and 6Fh
 * <pre>
 * ---------------------------------------------------------------------------
 * type	bits		description 			comments
 * ---------------------------------------------------------------------------
 * t		2			element code
 * r		3			register code
 * m		3			reference code
 * i		7	 		instruction type
 * j		1			instruction sub type
 *
 * Instruction code layout
 * -----------------------------
 * fedc ba98 7654 3210 (8/16 bits)
 * ttrr rmmm iiii iiij
 *
 * ---------------------------------------------------------------------------
 * instruction			code 	iiiiiii w tt rrr mmm
 * ---------------------------------------------------------------------------
 * pusha/pushad			60		0110000 0
 * popa/popad				61		0110000 1
 *
 * arpl ax,cx				63C1	0110001 1 11 000 001
 * arpl cx,ax				63C8	0110001 1 11 001 000
 *
 * insb						  6C		0110110 0
 * insw | insd			6D		0110110 1
 * outsb					  6E		0110111 0
 * outsw | outsd		6F		0110111 1
 * </pre>
 * @see [[http://pdos.csail.mit.edu/6.828/2006/readings/i386/OUTS.htm">OUT instruction]]
 */
class Decoder6x extends Decoder:
  private val channels: Seq[(Int, VirtualMemory, MemoryCursor, VirtualPC) => Instruction] = Seq(
    (b, m, c, v) => PUSHA, //| 60
    (b, m, c, v) => POPA, // | 61
    (b, m, c, v) => DB(b), //| 62
    (b, m, c, v) => DB(b), //| 63
    (b, m, c, v) => DB(b), //| 64
    (b, m, c, v) => DB(b), //| 65
    (b, m, c, v) => DB(b), //| 66
    (b, m, c, v) => DB(b), //| 67
    (b, m, c, v) => DB(b), //| 68
    (b, m, c, v) => DB(b), //| 69
    (b, m, c, v) => DB(b), //| 6A
    (b, m, c, v) => DB(b), //| 6B
    (b, m, c, v) => INSB, // | 6C
    (b, m, c, v) => INSW, // | 6D
    (b, m, c, v) => OUTSB, //| 6E
    (b, m, c, v) => OUTSW) //| 6F

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val memory = vpc.memory
    val code8 = ip.nextByte(memory)
    channels(code8 & 0x0F)(code8, memory, ip, vpc)
