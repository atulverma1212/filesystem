package org.averma.commands

import org.averma.files.Directory
import org.averma.filesystem.State

class Rm(name : String) extends Command {

  override def apply(state: State): State = {

    // get wd
    val wd = state.wd

    // find absolute path
    val absPath =
      if(name.startsWith(Directory.SEPARATOR)) name
      else if(wd.isRoot) wd.path + name
      else wd.path + Directory.SEPARATOR + name

    // do some checks
    if (Directory.ROOT_PATH.equals(absPath))
      state.setMessage("Can't delete the root")
    else
      execute(state, absPath)


  }

  private def execute(state: State, absPath: String): State = {
    // find Entry & update structure


    def rmHelper(current: Directory, path: List[String]): Directory = {
      if (path.isEmpty) current
      else if (path.tail.isEmpty) current.removeEntry(path.head)
      else {
        val nextDir = current.findEntry(path.head)
        if (!nextDir.isDirectory) current
        else{
          val newNextDir = rmHelper(nextDir.asDirectory, path.tail)
          if(newNextDir == nextDir) current
          else  current.replaceEntry(path.head, newNextDir)
        }
      }
    }

    val tokens = absPath.substring(1).split("/").toList.filter(!_.isEmpty)

    val newRoot : Directory = rmHelper(state.root, tokens)

    if (newRoot == state.root)
      state.setMessage(absPath + ": no such file or directory")
    else
      State(newRoot, newRoot.findDescendant(state.wd.path.substring(1)))

  }
}
