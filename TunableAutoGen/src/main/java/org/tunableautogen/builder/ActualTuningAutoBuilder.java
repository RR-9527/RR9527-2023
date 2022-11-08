package org.tunableautogen.builder;

import org.tunableautogen.MarkerCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ActualTuningAutoBuilder {
    private final List<MethodCall> methodCalls;
    private final Map<String, Integer> methodTimesCalled;

    public ActualTuningAutoBuilder() {
        this.methodCalls = new ArrayList<>();
        this.methodTimesCalled = new HashMap<>();
    }

    // Lambdas: MarkerCallback

    // -- START --
                                                                                             
    public ActualTuningAutoBuilder startAt(double x, double y, double heading) {                                                                              
        Var param0 = new Var("x", "double", x);
        Var param1 = new Var("y", "double", y);
        Var param2 = new Var("heading", "double", heading);                                                           
        addMethodCall("startAt", null, param0, param1, param2);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder startAtT(double x, double y, double heading) {                                                                              
        Var param0 = new TunableVar("x", "double", x);
        Var param1 = new TunableVar("y", "double", y);
        Var param2 = new TunableVar("heading", "double", heading);                                                           
        addMethodCall("startAtT", null, param0, param1, param2);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder startAt(double x, double y, double heading, String tag) {                                                                              
        Var param0 = new Var("x", "double", x);
        Var param1 = new Var("y", "double", y);
        Var param2 = new Var("heading", "double", heading);
        Var param3 = new Var("tag", "String", tag);                                                           
        addMethodCall("startAt", null, param0, param1, param2, param3);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder startAtT(double x, double y, double heading, String tag) {                                                                              
        Var param0 = new TunableVar("x", "double", x);
        Var param1 = new TunableVar("y", "double", y);
        Var param2 = new TunableVar("heading", "double", heading);
        Var param3 = new TunableVar("tag", "String", tag);                                                           
        addMethodCall("startAtT", null, param0, param1, param2, param3);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder forward(double distance) {                                                                              
        Var param0 = new Var("distance", "double", distance);                                                           
        addMethodCall("forward", null, param0);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder forwardT(double distance) {                                                                              
        Var param0 = new TunableVar("distance", "double", distance);                                                           
        addMethodCall("forwardT", null, param0);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder forward(double distance, String tag) {                                                                              
        Var param0 = new Var("distance", "double", distance);
        Var param1 = new Var("tag", "String", tag);                                                           
        addMethodCall("forward", null, param0, param1);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder forwardT(double distance, String tag) {                                                                              
        Var param0 = new TunableVar("distance", "double", distance);
        Var param1 = new TunableVar("tag", "String", tag);                                                           
        addMethodCall("forwardT", null, param0, param1);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder back(double distance) {                                                                              
        Var param0 = new Var("distance", "double", distance);                                                           
        addMethodCall("back", null, param0);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder backT(double distance) {                                                                              
        Var param0 = new TunableVar("distance", "double", distance);                                                           
        addMethodCall("backT", null, param0);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder back(double distance, String tag) {                                                                              
        Var param0 = new Var("distance", "double", distance);
        Var param1 = new Var("tag", "String", tag);                                                           
        addMethodCall("back", null, param0, param1);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder backT(double distance, String tag) {                                                                              
        Var param0 = new TunableVar("distance", "double", distance);
        Var param1 = new TunableVar("tag", "String", tag);                                                           
        addMethodCall("backT", null, param0, param1);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder turn(double angle) {                                                                              
        Var param0 = new Var("angle", "double", angle);                                                           
        addMethodCall("turn", null, param0);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder turnT(double angle) {                                                                              
        Var param0 = new TunableVar("angle", "double", angle);                                                           
        addMethodCall("turnT", null, param0);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder turn(double angle, String tag) {                                                                              
        Var param0 = new Var("angle", "double", angle);
        Var param1 = new Var("tag", "String", tag);                                                           
        addMethodCall("turn", null, param0, param1);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder turnT(double angle, String tag) {                                                                              
        Var param0 = new TunableVar("angle", "double", angle);
        Var param1 = new TunableVar("tag", "String", tag);                                                           
        addMethodCall("turnT", null, param0, param1);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder splineTo(double x, double y, double heading) {                                                                              
        Var param0 = new Var("x", "double", x);
        Var param1 = new Var("y", "double", y);
        Var param2 = new Var("heading", "double", heading);                                                           
        addMethodCall("splineTo", null, param0, param1, param2);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder splineToT(double x, double y, double heading) {                                                                              
        Var param0 = new TunableVar("x", "double", x);
        Var param1 = new TunableVar("y", "double", y);
        Var param2 = new TunableVar("heading", "double", heading);                                                           
        addMethodCall("splineToT", null, param0, param1, param2);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder splineTo(double x, double y, double heading, String tag) {                                                                              
        Var param0 = new Var("x", "double", x);
        Var param1 = new Var("y", "double", y);
        Var param2 = new Var("heading", "double", heading);
        Var param3 = new Var("tag", "String", tag);                                                           
        addMethodCall("splineTo", null, param0, param1, param2, param3);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder splineToT(double x, double y, double heading, String tag) {                                                                              
        Var param0 = new TunableVar("x", "double", x);
        Var param1 = new TunableVar("y", "double", y);
        Var param2 = new TunableVar("heading", "double", heading);
        Var param3 = new TunableVar("tag", "String", tag);                                                           
        addMethodCall("splineToT", null, param0, param1, param2, param3);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder temporalMarker(MarkerCallback callback) {                                                                              
        Var param0 = new Var("callback", "MarkerCallback", callback);                                                           
        addMethodCall("temporalMarker", null, param0);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder temporalMarkerT(MarkerCallback callback) {                                                                              
        Var param0 = new TunableVar("callback", "MarkerCallback", callback);                                                           
        addMethodCall("temporalMarkerT", null, param0);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder temporalMarker(MarkerCallback callback, String tag) {                                                                              
        Var param0 = new Var("callback", "MarkerCallback", callback);
        Var param1 = new Var("tag", "String", tag);                                                           
        addMethodCall("temporalMarker", null, param0, param1);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder temporalMarkerT(MarkerCallback callback, String tag) {                                                                              
        Var param0 = new TunableVar("callback", "MarkerCallback", callback);
        Var param1 = new TunableVar("tag", "String", tag);                                                           
        addMethodCall("temporalMarkerT", null, param0, param1);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder temporalMarker(double offset, MarkerCallback callback) {                                                                              
        Var param0 = new Var("offset", "double", offset);
        Var param1 = new Var("callback", "MarkerCallback", callback);                                                           
        addMethodCall("temporalMarker", null, param0, param1);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder temporalMarkerT(double offset, MarkerCallback callback) {                                                                              
        Var param0 = new TunableVar("offset", "double", offset);
        Var param1 = new TunableVar("callback", "MarkerCallback", callback);                                                           
        addMethodCall("temporalMarkerT", null, param0, param1);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder temporalMarker(double offset, MarkerCallback callback, String tag) {                                                                              
        Var param0 = new Var("offset", "double", offset);
        Var param1 = new Var("callback", "MarkerCallback", callback);
        Var param2 = new Var("tag", "String", tag);                                                           
        addMethodCall("temporalMarker", null, param0, param1, param2);                               
        return this;                                                                         
    }                                                                                        
                                                                                             
    public ActualTuningAutoBuilder temporalMarkerT(double offset, MarkerCallback callback, String tag) {                                                                              
        Var param0 = new TunableVar("offset", "double", offset);
        Var param1 = new TunableVar("callback", "MarkerCallback", callback);
        Var param2 = new TunableVar("tag", "String", tag);                                                           
        addMethodCall("temporalMarkerT", null, param0, param1, param2);                               
        return this;                                                                         
    }                                                                                        

    // -- END --

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void finish() throws Exception {
        File file = new File("pathname");

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
        protected final String name, type;
        protected final Object data;

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

    static class Lambda extends Var {
        public Lambda(String name, String type, String lambda) {
            super(name, type, lambda);
        }

        @Override
        public String toString() {
            return
                "{" +
                    "\"name\": \"" + name + "\"," +
                    "\"type\": \"" + type + "\"," +
                    "\"data\": \"" + data + "\"," +
                    "\"is_lambda\": true" +
                    "}";
        }
    }
}