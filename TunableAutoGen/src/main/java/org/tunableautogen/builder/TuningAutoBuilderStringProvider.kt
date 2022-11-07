package org.tunableautogen.builder

fun createTuningAutoBuilderString(builderCode: String) = """
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TuningAutoBuilder {
    public final String JSON_OUTPUT_PATH = "C:\\Users\\wanna\\Documents\\GitHub\\RR9527-2023\\tunableautoserializedoutput2.json";

    private final List<MethodCall> methodCalls;
    private final Map<String, Integer> methodTimesCalled;

    public TuningAutoBuilder() {
        this.methodCalls = new ArrayList<>();
        this.methodTimesCalled = new HashMap<>();
    }

    public TuningAutoBuilder forward(double distance) {
        Var param1 = new Var("distance", "double", distance);
        addMethodCall("forward", null, param1);
        return this;
    }

    public TuningAutoBuilder forwardT(double distance) {
        Var param1 = new TunableVar("distance", "double", distance);
        addMethodCall("forward", null, param1);
        return this;
    }

    public TuningAutoBuilder back(double distance) {
        Var param1 = new Var("distance", "double", distance);
        addMethodCall("back", null, param1);
        return this;
    }

    public TuningAutoBuilder backT(double distance) {
        Var param1 = new TunableVar("distance", "double", distance);
        addMethodCall("back", null, param1);
        return this;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void finish() throws Exception {
        File file = new File(JSON_OUTPUT_PATH);

        if (!file.exists())
            file.createNewFile();

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(toJSON().getBytes());
        }
    }

    private String toJSON() {
        return "{\"method_calls\": " + methodCalls + "}";
    }

    private void addMethodCall(String name, String tag, Var... args) {
        incrementMethodTimesCalled(name);
        String defaultTag = name + methodTimesCalled.get(name);
        methodCalls.add(new MethodCall(methodCalls.size(), name, tag == null ? defaultTag : tag, args));
    }

    private void incrementMethodTimesCalled(String name) {
        methodTimesCalled.compute(name, (k, v) -> v == null ? 0 : v + 1);
    }

    static class MethodCall {
        final int index;
        final String name;
        final String tag;
        final Var[] args;

        public MethodCall(int index, String name, String tag, Var... args) {
            this.index = index;
            this.name = name;
            this.tag = tag;
            this.args = args;
        }

        @Override
        public String toString() {
            return
                "{" +
                    "\"index\": " + index + "," +
                    "\"name\": \"" + name + "\"," +
                    "\"tag\": \"" + tag + "\"," +
                    "\"args\": " + Arrays.toString(args) +
                "}";
        }
    }

    static class Var {
        String name, type;
        Object data;

        public Var(String name, String type, Object data) {
            this.name = name;
            this.type = type;
            this.data = data;
        }

        @Override
        public String toString() {
            return
                "{" +
                    "\"name\": \"" + name + "\"," +
                    "\"type\": \"" + type + "\"," +
                    "\"data\": \"" + data + "\"" +
                "}";
        }
    }

    static class TunableVar extends Var {
        public TunableVar(String name, String type, Object data) {
            super(name, type, data);
        }

        @Override
        public String toString() {
            return
                "{" +
                    "\"name\": \"" + name + "\"," +
                    "\"type\": \"" + type + "\"," +
                    "\"data\": \"" + data + "\"," +
                    "\"is_tunable\": true" +
                "}";
        }
    }

    static class SerializableVar extends Var {
        String filePath;

        public SerializableVar(String name, String type, String filePath) {
            super(name, type, null);
            this.filePath = filePath;
        }

        @Override
        public String toString() {
            return
                "{" +
                    "\"name\": \"" + name + "\"," +
                    "\"type\": \"" + type + "\"," +
                    "\"is_serializable\": true," +
                    "\"file_path\": \"" + filePath + "\"" +
                "}";
        }
    }

    public static void main(String[] args) throws Exception {
        $builderCode
    }
}
""".trimIndent()