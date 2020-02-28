package org.averma.files

import scala.util.Try

abstract class DirEntry(val parentPath: String, val name: String) {

  def path : String =
    if (parentPath.equals(Directory.SEPARATOR)) parentPath + name
    else parentPath + Directory.SEPARATOR + name

  def asDirectory: Directory
  def asFile: File

  def isDirectory: Boolean = Try(this.asDirectory).isSuccess
  def isFile: Boolean = Try(this.asFile).isSuccess

  def getType: String

}
