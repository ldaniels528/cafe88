package com.github.ldaniels528.v88

case class IllegalValueException(value: Int, name: String = "value") 
  extends RuntimeException(f"Illegal $name '$value' ($value%04Xh)")
