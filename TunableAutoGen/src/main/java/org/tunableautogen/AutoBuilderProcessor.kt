package org.tunableautogen

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import org.tunableautogen.annotations.GenerateTunableAuto
import org.tunableautogen.builder.createTuningAutoBuilderString
import java.io.File
import java.io.OutputStream
import java.nio.file.Files
import kotlin.reflect.KClass

class AutoBuilderProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    @Suppress("NewApi")
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val listedFunctions: Sequence<KSFunctionDeclaration> =
            resolver.findAnnotations(GenerateTunableAuto::class)

        if (!listedFunctions.iterator().hasNext()) {
            return emptyList()
        }

        val function = listedFunctions.iterator().next()
        val containingFile = function.containingFile!!

        val fullCode = File(containingFile.filePath).bufferedReader().readLines()

        val newFile: OutputStream = environment.codeGenerator
            .createNewFile(
                dependencies = Dependencies(false),
                packageName = containingFile.packageName.asString(),
                fileName = "ActualTuningAutoBuilder",
                extensionName = "java"
            )

        val startIndex = fullCode.indexOfFirst { it.contains("new TuningAutoBuilder()") }
        val endIndex = fullCode.indexOfFirst { it.contains(".finish();") } + 1

        val builderCode = fullCode.subList(startIndex, endIndex).joinToString("\n")

        val tuningAutoBuilderString = createTuningAutoBuilderString(builderCode)

        val tempFile = Files.createTempFile("", ".java").toFile()
        tempFile.writeText(tuningAutoBuilderString)

        ProcessBuilder().command("java", tempFile.absolutePath).start().waitFor()

        return listedFunctions.filterNot { it.validate() }.toList()
    }

    private val String.r get() = replace("\\", "\\\\")

    private fun Resolver.findAnnotations(kClass: KClass<*>) =
        getSymbolsWithAnnotation(kClass.qualifiedName.toString())
            .filterIsInstance<KSFunctionDeclaration>()
            .filter {
                it.parameters.isEmpty()
            }
}
