package org.averma.filesystem

import java.util.Scanner

import org.averma.commands.Command
import org.averma.files.Directory

object Filesystem extends App {

  val root = Directory.ROOT
  var state = State(root, root)
  val scanner = new Scanner(System.in)

  while(true) {
    state.show()
    state = Command.from(scanner.nextLine()).apply(state)
  }

}
