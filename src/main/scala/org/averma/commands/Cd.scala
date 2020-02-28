package org.averma.commands
import jdk.nashorn.internal.runtime.CodeStore.DirectoryCodeStore
import org.averma.files.{DirEntry, Directory}
import org.averma.filesystem.State

class Cd(directory: String) extends Command {
  override def apply(state: State): State = {

    // Find root
    val root = state.root
    val wd = state.wd

    // find absolute path of directory I want to cd
    val absPath =
      if(directory.startsWith(Directory.SEPARATOR)) directory
      else if(wd.isRoot) wd.path + directory
      else wd.path + Directory.SEPARATOR + directory

    // find the directory I want to cd to
    val destination = doFindEntry(root, absPath)


    // change the state given the new directory
    if(destination == null || !destination.isDirectory)
      state.setMessage(directory + ": No Such directory! ")
    else {
      State(root, destination.asDirectory)
    }

  }

  def doFindEntry(root: Directory, path: String): DirEntry = {
    @scala.annotation.tailrec
    def traverse(current: Directory, children : List[String]): DirEntry = {
      if(children.isEmpty || children.head.isEmpty) current
      else if (current.hasEntry(children.head) && current.findEntry(children.head).isDirectory)
        traverse(current.findEntry(children.head).asDirectory, children.tail)
      else null
    }

    /*
    /a/b/../c

    /b../c a
      ../c /a/b


     */

    @scala.annotation.tailrec
    def collapseRelatives(path : List[String], result: List[String] = List()): List[String] = {
      if(path.isEmpty || path.head.isEmpty) result
      else if(path.head.equals("..")) collapseRelatives(path.tail, result.init)
      else collapseRelatives(path.tail, result :+ path.head)
    }

    val dirList = path.substring(1).split(Directory.SEPARATOR).toList.filterNot(_.isEmpty)
    val absList = collapseRelatives(dirList.filter(!_.equals(".")))
    traverse(root, absList)
  }
}
