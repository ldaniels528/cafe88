package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.decoders.Decoder.{decodeNext, decodeTwoOperands}
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.bitwise.TEST
import com.github.ldaniels528.v88.opcodes.data.DB
import com.github.ldaniels528.v88.opcodes.flow.*
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.*
import com.github.ldaniels528.v88.opcodes.streaming.{REPNZ, REPZ}
import com.github.ldaniels528.v88.opcodes.{Instruction, NOOP}
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}
import com.github.ldaniels528.v88.{MemoryCursor, VirtualMemory, VirtualPC}

/**
 * Decodes instruction codes F0h thru FFh.
 * <pre>
 * ---------------------------------------------------------------------------
 * instruction			code  tt jjj mmm	iiii jjj k
 * ---------------------------------------------------------------------------
 * lock						    F0				      1111 000 0
 * (undefined)				F1				      1111 000 1
 * repnz					    F2				      1111 001 0
 * repz						    F3				      1111 001 1
 * hlt						    F4				      1111 010 0
 * cmc						    F5				      1111 010 1
 * (see below)				F6				      1111 011 0
 * (see below)				F7				      1111 011 1
 * clc						    F8				      1111 100 0
 * stc						    F9				      1111 100 1
 * cli						    FA				      1111 101 0
 * sti						    FB				      1111 101 1
 * cld						    FC				      1111 110 0
 * std						    FD				      1111 110 1
 * (see below)				FE				      1111 111 0
 * (see below)				FF				      1111 111 1
 *
 * ---------------------------------------------------------------------------
 * instruction			  code 	iiiii s v w tt rrr mmm	
 * ---------------------------------------------------------------------------
 * test bl,nn				  F6C3	11110 1 1 0 11 000 011 nn
 * test bx,nnnn			  F7C3	11110 1 1 1 11 000 011 nnnn
 * (undefined)			  F708	11110 1 1 1 00 001 000	
 * not ax					    F7D0	11110 1 1 1 11 010 000	
 * neg ax					    F7D8	11110 1 1 1 11 011 000	
 * mul ax				  	  F7E0	11110 1 1 1 11 100 000	
 * imul ax				  	F7E8	11110 1 1 1 11 101 000	
 * div ax				  	  F7F0	11110 1 1 1 11 110 000	
 * idiv ax			  		F7F8	11110 1 1 1 11 111 000	
 *
 * inc byte ptr [bx]	FE07	11111 1 1 0 00 000 111 	
 * inc word ptr [bx]	FF07	11111 1 1 1 00 000 111 	
 * dec byte ptr [bx]	FE0F	11111 1 1 0 00 001 111	
 * dec word ptr [bx]	FF0F	11111 1 1 1 00 001 111	
 * call [bx]				  FF17	11111 1 1 1 00 010 111	
 * call far [bx]			FF1F	11111 1 1 1 00 011 111	
 * jmp [bx]					  FF27	11111 1 1 1 00 100 111	
 * jmp far [bx]				FF2F	11111 1 1 1 00 101 111	
 * push [bx]				  FF37	11111 1 1 1 00 110 111	
 * (undefined)				FF3F	11111 1 1 1 00 111 111	
 * </pre>
 */
class DecoderFx extends Decoder:
  private val channels: Array[(Int, VirtualMemory, MemoryCursor, VirtualPC) => Instruction] = Array(
    (b, m, c, v) => LOCK(host = decodeNext(c)(v)), // | F0
    (b, m, c, v) => DB(b), //                         | F1
    (b, m, c, v) => REPNZ(host = decodeNext(c)(v)), //| F2
    (b, m, c, v) => REPZ(host = decodeNext(c)(v)), // | F3
    (b, m, c, v) => HLT, //                           | F4
    (b, m, c, v) => CMC, //                           | F5
    (b, m, c, v) => decodeOps(b, m, c, v), //         | F6
    (b, m, c, v) => decodeOps(b, m, c, v), //         | F7
    (b, m, c, v) => CLC, //                           | F8
    (b, m, c, v) => STC, //                           | F9
    (b, m, c, v) => CLI, //                           | FA
    (b, m, c, v) => STI, //                           | FB
    (b, m, c, v) => CLD, //                           | FC
    (b, m, c, v) => STD, //                           | FD
    (b, m, c, v) => decodeOps(b, m, c, v), //         | FE
    (b, m, c, v) => decodeOps(b, m, c, v)) //         | FF
  private val slotsA: Array[(MutableOperand, Operand) => Instruction] = Array(
    (a, b) => TEST(a, b),
    (a, b) => ???,
    (a, b) => NOT(a),
    (a, b) => NEG(a),
    (a, b) => MUL(a),
    (a, b) => IMUL(a),
    (a, b) => DIV(a),
    (a, b) => IDIV(a))
  private val slotsB: Array[(MutableOperand, Operand) => Instruction] = Array(
    (a, b) => INC(a),
    (a, b) => INC(a),
    (a, b) => DEC(a),
    (a, b) => DEC(a),
    (a, b) => CALL.NearRx(a),
    (a, b) => CALL.FarRx(a),
    (a, b) => JMP.NearRx(a),
    (a, b) => JMP.FarRx(a))
  private val slots: Array[Array[(MutableOperand, Operand) => Instruction]] = Array(slotsA, slotsB)
  private val ops: Seq[(MutableOperand, Operand) => Instruction] = Seq(
    (a, b) => TEST(a, b),
    (a, b) => NOT(a),
    (a, b) => NEG(a),
    (a, b) => MUL(a),
    (a, b) => IMUL(a),
    (a, b) => DIV(a),
    (a, b) => IDIV(a),
    (a, b) => INC(a),
    (a, b) => DEC(a),
    (a, b) => JMP.NearRx(a),
    (a, b) => PUSH(a))

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val memory = vpc.memory
    val codeI8 = ip.nextByte(memory)
    channels(codeI8 & 0x0F)(codeI8, memory, ip, vpc)

  private def decodeOps(codeI8: Int, memory: VirtualMemory, ip: MemoryCursor, vpc: VirtualPC) =
    decodeTwoOperands(ip, codeI8 = codeI8, codeR8 = ip.nextByte(memory))((i, r, m) => ops(i & 0x01))(vpc)
  