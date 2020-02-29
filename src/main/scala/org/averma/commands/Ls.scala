package org.averma.commands
import org.averma.files.Directory
import org.averma.filesystem.State

class Ls(options: String = "") extends Command {
  val SORT = "s"
  val RECURSIVE = "R"
  val RECURSIVE_DOWN = "r"

  override def apply(state: State): State = {
    options match {
      case RECURSIVE_DOWN =>
        val output = recursiveLs(state.wd, state.wd.contents.filter(e => e.isDirectory).map(_.name))
        val listDir = output.split("\n").reverse.mkString("\n")
        state.setMessage(listDir)
      case RECURSIVE =>
        val output = recursiveLs(state.wd, state.wd.contents.filter(e => e.isDirectory).map(_.name))
        state.setMessage(output)
      case SORT =>
        val output = state.wd.contents.sortWith((a,b) => a.name.compareTo(b.name) < 0).map(e => e.name + ": " + e.getType + "\n").mkString
        state.setMessage(output)
      case _ =>
        val output = state.wd.contents.map(e => e.name + ": " + e.getType + "\n").mkString
        state.setMessage(output)
    }
  }

  def recursiveLs(directory: Directory, dirs: List[String]): String = {
    if(dirs.isEmpty)
      directory.contents.map(e => e.path + ": " + e.getType + "\n").mkString
     else {
      val dir = directory.findEntry(dirs.head).asDirectory
      val subDirs = dir.contents.filter(e => e.isDirectory).map(_.name)
      recursiveLs(directory, dirs.tail) + recursiveLs(dir, subDirs)
    }
  }
}
