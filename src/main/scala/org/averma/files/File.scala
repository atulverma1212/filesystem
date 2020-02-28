package org.averma.files

import org.averma.filesystem.FileSystemException

class File(override val parentPath: String, override val name: String, val contents: String) extends DirEntry(parentPath, name) {
  override def asDirectory: Directory = throw new FileSystemException(s"$name is not a directory")

  override def asFile: File = this

  override def getType: String = "file"


}

object File {
  def empty(parentPath: String, name: String): File =
    new File(parentPath, name, "")
}