package org.tunableautogen

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import org.tunableautogen.annotations.GenerateTunableAuto
import java.io.File
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

        val function = listedFunctions.iterator().next();

        val containingFile = File(function.containingFile!!.filePath).bufferedReader().readLines()

        val startIndex = containingFile.indexOfFirst { it.contains("new TuningAutoBuilder()") }
        val endIndex = containingFile.indexOfFirst { it.contains(".writeJsonToFile();") } + 1

        val builderCode = containingFile.subList(startIndex, endIndex).joinToString("\n")

        val tuningAutoBuilderString = createTuningAutoBuilderString(builderCode)

        val tempFile = Files.createTempFile("", ".java").toFile()
        tempFile.writeText(tuningAutoBuilderString)

        ProcessBuilder().command("java", tempFile.absolutePath).start().waitFor()

        val out = File("C:\\Users\\wanna\\Documents\\GitHub\\RR9527-2023\\s.txt")
        out.createNewFile()

        ProcessBuilder().command("C:\\Users\\wanna\\AppData\\Roaming\\npm\\ts-node.cmd".r, "C:\\Users\\wanna\\Documents\\GitHub\\RR9527-2023\\scripts\\auto_generator\\make_pathing_with_hot_reloading.ts".r)
            .redirectOutput(out)
            .redirectError(out)
            .start()
            .waitFor()

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
