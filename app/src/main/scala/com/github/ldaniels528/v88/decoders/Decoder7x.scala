package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.flow.*
import com.github.ldaniels528.v88.{MemoryCursor, VirtualMemory, VirtualPC}

/**
 * Decodes instruction codes between 70h and 7Fh
 * <pre>
 * ---------------------------------------------------------------------------
 * type	size		  description 			    comments
 * ---------------------------------------------------------------------------
 * i		4-bit 		instruction type  		J/Nx=0111
 * f		3-bit		  flag index			      O=000, C=001, Z=010, S=100, P=101
 * g    1=bit     inverse (not)         normal=0, inverted=1
 *
 * Instruction code layout
 * -----------------------
 * 7654 3210 (8 bits)
 * iiii fffq
 *
 * ---------------------------------------------------------------------------
 * instruction	code 	iiii fff g nn
 * ---------------------------------------------------------------------------
 * jo  nn				70		0111 000 0 nn
 * jno nn				71		0111 000 1 nn
 * jc  nn				72		0111 001 0 nn
 * jnc nn				73		0111 001 1 nn
 * jz  nn				74		0111 010 0 nn
 * jnz nn				75		0111 010 1 nn
 * jbe nn				76		0111 011 0 nn
 * ja  nn				77		0111 011 1 nn
 * js  nn				78		0111 100 0 nn
 * jns nn				79		0111 100 1 nn
 * jpe nn				7A		0111 101 0 nn
 * jpo nn				7B		0111 101 1 nn
 * jl  nn				7C		0111 110 0 nn
 * jge nn				7D		0111 110 1 nn
 * jle nn				7E		0111 111 0 nn
 * jg  nn				7F		0111 111 1 nn
 * </pre>
 */
class Decoder7x extends Decoder:
  private val channels = Array[Int => Instruction](
    JO.apply, JNO.apply, JC.apply, JNC.apply, JZ.apply, JNZ.apply, JBE.apply, JA.apply,
    JS.apply, JNS.apply, JPE.apply, JPO.apply, JL.apply, JGE.apply, JLE.apply, JG.apply)

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val memory = vpc.memory
    val opIndex = ip.nextByte(memory) & 0x0F
    val delta = ip.nextByte(memory)
    channels(opIndex)(delta)
