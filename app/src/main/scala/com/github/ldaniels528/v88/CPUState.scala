package com.github.ldaniels528.v88

import com.github.ldaniels528.v88.VirtualMemory.toAddress

case class CPUState(ip: MemoryCursor,
                    flags: FlagRegister16,
                    ax: Register16C,
                    cx: Register16C,
                    bx: Register16C,
                    dx: Register16C,
                    si: Register16,
                    di: Register16,
                    bp: Register16,
                    sp: Register16,
                    cs: Register16,
                    ds: Register16,
                    es: Register16,
                    ss: Register16,
                    ah: Register8,
                    al: Register8,
                    bh: Register8,
                    bl: Register8,
                    ch: Register8,
                    cl: Register8,
                    dh: Register8,
                    dl: Register8,
                    var isRunning: Boolean)

object CPUState {

  def apply(segment: Int = 0, offset: Int = 0): CPUState =
    val ah: Register8 = Register8(name = "ah")
    val al: Register8 = Register8(name = "al")
    val bh: Register8 = Register8(name = "bh")
    val bl: Register8 = Register8(name = "bl")
    val ch: Register8 = Register8(name = "ch")
    val cl: Register8 = Register8(name = "cl")
    val dh: Register8 = Register8(name = "dh")
    val dl: Register8 = Register8(name = "dl")
    val state = new CPUState(
      ip = MemoryCursor(address = toAddress(segment, offset)),
      flags = FlagRegister16(name = "flags"),
      ax = Register16C(name = "ax", ah, al),
      cx = Register16C(name = "cx", ch, cl),
      bx = Register16C(name = "bx", bh, bl),
      dx = Register16C(name = "dx", dh, dl),
      cs = Register16A(name = "cs"),
      ds = Register16A(name = "ds"),
      es = Register16A(name = "es"),
      ss = Register16A(name = "ss"),
      si = Register16A(name = "si"),
      di = Register16A(name = "di"),
      bp = Register16A(name = "bp"),
      sp = Register16A(name = "sp"),
      ah = Register8(name = "ah"),
      al = Register8(name = "al"),
      bh = Register8(name = "bh"),
      bl = Register8(name = "bl"),
      ch = Register8(name = "ch"),
      cl = Register8(name = "cl"),
      dh = Register8(name = "dh"),
      dl = Register8(name = "dl"),
      isRunning = true)
    state.sp.value = 0xFFFE
    state

}