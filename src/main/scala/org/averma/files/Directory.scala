package org.averma.files

import org.averma.filesystem.FileSystemException

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry]) extends DirEntry(parentPath, name) {

  def removeEntry(entryName: String): Directory =
    if (!hasEntry(entryName)) this
    else new Directory(parentPath, name, contents.filter(!_.name.equals(entryName)))

  def isRoot: Boolean = parentPath.isEmpty


  override def asFile: File = throw new FileSystemException(s"$name is not a file")

  def replaceEntry(entryName: String, newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents.filterNot(_.name.equals(entryName)) :+ newEntry)

  def findEntry(name: String): DirEntry = {
    @scala.annotation.tailrec
    def getFromList(list: List[DirEntry], name: String): DirEntry = {
      if (list.isEmpty) null
      else if (list.head.name.equals(name)) list.head
      else getFromList(list.tail, name)
    }

    getFromList(contents, name)
    //  todo: implement using filter
//    dirList.filter(e => e.name.equals(name)).map(e => e).head
  }

  def addEntry(newDir: DirEntry): Directory = new Directory(parentPath, name, contents :+ newDir)

  def findDescendant(path: List[String]) : Directory =
    if (path.isEmpty) this
    else findEntry(path.head).asDirectory.findDescendant(path.tail)

  def findDescendant(relativePath: String) : Directory =
    if (relativePath.isEmpty) this
    else findDescendant(relativePath.split(Directory.SEPARATOR).toList)

  def getAllFoldersInPath: List[String] = path.substring(1).split(Directory.SEPARATOR).toList.filterNot(_.isEmpty)

  def hasEntry(name: String): Boolean = findEntry(name) != null

  override def asDirectory: Directory = this

  override def getType: String = "Directory"

}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: Directory = empty("","")

  def empty(parentPath: String, name: String): Directory = new Directory(parentPath, name, List())
}
