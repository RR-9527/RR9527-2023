package org.tunableautogen.builder

fun createTuningAutoBuilderString(builderCode: String) =
    object {}.javaClass.classLoader.getResource("ActualTuningAutoBuilder.java")
        ?.readText()
        ?.dropLast(1) +
        """
            
            public static void main(String[] args) {
                 args[0];
                ${builderCode.replace("TuningAutoBuilder", "ActualTuningAutoBuilder")}
            }
        }
        """.trimIndent()
