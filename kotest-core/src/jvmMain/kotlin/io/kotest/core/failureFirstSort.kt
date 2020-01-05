package io.kotest.core

import io.kotest.core.specs.SpecContainer
import java.nio.file.Paths

actual fun failureFirstSort(classes: List<SpecContainer>): List<SpecContainer> {
   // try to locate a folder called .kotest which should contain a file called spec_failures
   // each line in this file is a failed spec and they should run first
   // if the file doesn't exist then we just execute in Lexico order
   val path = Paths.get(".kotest").resolve("spec_failures")
   return if (path.toFile().exists()) {
      val classnames = path.toFile().readLines()
      val (failed, passed) = classes.partition { container ->
         container.qualifiedName.fold(
            { false },
            { classnames.contains(it) }
         )
      }
      LexicographicSpecExecutionOrder.sort(failed) + LexicographicSpecExecutionOrder.sort(passed)
   } else {
      LexicographicSpecExecutionOrder.sort(classes)
   }
}