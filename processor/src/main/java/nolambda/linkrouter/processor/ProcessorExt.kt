package nolambda.linkrouter.processor

import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class Logger(private val env: ProcessingEnvironment) {
    fun error(message: String) {
        print(Diagnostic.Kind.ERROR, message)
    }

    fun note(message: String) {
        print(Diagnostic.Kind.NOTE, message)
    }

    private fun print(kind: Diagnostic.Kind, message: String) {
        env.messager.printMessage(kind, message)
    }
}