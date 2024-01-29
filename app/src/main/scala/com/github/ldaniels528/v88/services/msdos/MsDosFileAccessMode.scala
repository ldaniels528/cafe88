package com.github.ldaniels528.v88.services.msdos

/**
 * Represents an MS-DOS File Access Mode
 * <pre>
 * Access modes (bits):
 * 7 6 5 4 3 2 1 0
 * | | | | | | | |
 * | | | | | +-------- read/write/update access mode
 * | | | | +--------- reserved, always 0
 * | +-------------- sharing mode (see below) (DOS 3.1+)
 * +--------------- 1 = private, 0 = inheritable (DOS 3.1+)
 *
 * Sharing mode bits (DOS 3.1+): Access mode bits:
 * 000 compatibility mode (exclusive)
 * 001 deny others read/write access
 * 010 deny others write access
 * 011 deny others read access
 * 100 full access permitted to all
 * </pre>
 */
case class MsDosFileAccessMode(readWrite: Int, sharingMode: Boolean, isInheritable: Boolean)

object MsDosFileAccessMode:
  // sharing mode constants
  val SHARING_COMPATIBLITY_MODE = 0x00 //      |000
  val SHARING_DENY_OTHERS_READ_WRITE = 0x01 // |001
  val SHARING_DENY_OTHERS_WRITE = 0x02 //      |010
  val SHARING_DENY_OTHERS_READ = 0x03 //       |011
  val SHARING_FULL_ACCESS_TO_ALL = 0x04 //     |100

  def apply(code: Int): MsDosFileAccessMode =
    new MsDosFileAccessMode( //             | is.. .rwu
      readWrite = code & 0x07, //           | .... .111 - read/write/update access mode
      sharingMode = (code & 0x40) != 0, //  | .1.. .... - sharing mode
      isInheritable = (code & 0x80) == 0) //| 1... .... - 1 = private, 0 = inheritable
