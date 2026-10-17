package dev.santo.evaluator;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
final class Functions {
    private Functions() { }
    static Map<String, FunctionSpec> builtins() {
        var map = new HashMap<String, FunctionSpec>();
        map.put("abs", new FunctionSpec(1, 1, a -> Math.abs(a[0])));
        map.put("sign", new FunctionSpec(1, 1, a -> Math.signum(a[0])));
        map.put("sqrt", new FunctionSpec(1, 1, a -> Math.sqrt(a[0])));
        map.put("cbrt", new FunctionSpec(1, 1, a -> Math.cbrt(a[0])));
        map.put("ln", new FunctionSpec(1, 1, a -> Math.log(a[0])));
        map.put("log10", new FunctionSpec(1, 1, a -> Math.log10(a[0])));
        map.put("log2", new FunctionSpec(1, 1, a -> Math.log(a[0])/Math.log(2)));
        map.put("exp", new FunctionSpec(1, 1, a -> Math.exp(a[0])));
        map.put("pow", new FunctionSpec(2, 2, a -> Math.pow(a[0],a[1])));
        map.put("sin", new FunctionSpec(1, 1, a -> Math.sin(a[0])));
        map.put("cos", new FunctionSpec(1, 1, a -> Math.cos(a[0])));
        map.put("tan", new FunctionSpec(1, 1, a -> Math.tan(a[0])));
        // REGISTRATIONS
        return Map.copyOf(map);
    }
    static void require(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }
    static long integer(double value) {
        require(value == Math.rint(value) && Math.abs(value) <= 9007199254740991.0,
                "Expected an exact integer in [-2^53+1, 2^53-1]");
        return (long) value;
    }
    // HELPERS
}
