package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.decoders.Decoder.registerByID
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.data.MOV
import com.github.ldaniels528.v88.{MemoryCursor, VirtualMemory, VirtualPC}

/**
 * Decodes instruction codes B0 thru BF 
 * <pre>
 * ------------------------------------------------------------------
 * instruction			code 		  iiii c rrr
 * ------------------------------------------------------------------
 * mov al,nn				B0 nn			1011 0 000 nn
 * mov cl,nn				B1 nn			1011 0 001 nn
 * mov dl,nn				B2 nn			1011 0 010 nn
 * mov bl,nn				B3 nn			1011 0 011 nn
 * mov ah,nn				B4 nn			1011 0 100 nn
 * mov ch,nn				B5 nn			1011 0 101 nn
 * mov dh,nn				B6 nn			1011 0 110 nn
 * mov bh,nn				B7 nn			1011 0 111 nn
 *
 * mov ax,nnnn			B8 nnnn		1011 1 000 nnnn
 * mov cx,nnnn			B9 nnnn		1011 1 001 nnnn
 * mov dx,nnnn			BA nnnn		1011 1 010 nnnn
 * mov bx,nnnn			BB nnnn		1011 1 011 nnnn
 * mov sp,nnnn			BC nnnn		1011 1 100 nnnn
 * mov bp,nnnn			BD nnnn		1011 1 101 nnnn
 * mov si,nnnn			BE nnnn		1011 1 110 nnnn
 * mov di,nnnn			BF nnnn		1011 1 111 nnnn
 * </pre>
 */
class DecoderBx extends Decoder:
  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val registerID = ip.nextByte(vpc.memory) & 0x0F
    MOV(registerByID(registerID), nextValue(ip, registerID))

