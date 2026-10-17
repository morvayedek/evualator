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
        map.put("asin", new FunctionSpec(1, 1, a -> Math.asin(a[0])));
        map.put("acos", new FunctionSpec(1, 1, a -> Math.acos(a[0])));
        map.put("atan", new FunctionSpec(1, 1, a -> Math.atan(a[0])));
        map.put("atan2", new FunctionSpec(2, 2, a -> Math.atan2(a[0],a[1])));
        map.put("sinh", new FunctionSpec(1, 1, a -> Math.sinh(a[0])));
        map.put("cosh", new FunctionSpec(1, 1, a -> Math.cosh(a[0])));
        map.put("tanh", new FunctionSpec(1, 1, a -> Math.tanh(a[0])));
        map.put("deg", new FunctionSpec(1, 1, a -> Math.toDegrees(a[0])));
        map.put("rad", new FunctionSpec(1, 1, a -> Math.toRadians(a[0])));
        map.put("floor", new FunctionSpec(1, 1, a -> Math.floor(a[0])));
        map.put("ceil", new FunctionSpec(1, 1, a -> Math.ceil(a[0])));
        map.put("rint", new FunctionSpec(1, 1, a -> Math.rint(a[0])));
        map.put("trunc", new FunctionSpec(1, 1, a -> a[0] < 0 ? Math.ceil(a[0]) : Math.floor(a[0])));
        map.put("min", new FunctionSpec(1, 256, a -> Arrays.stream(a).min().orElseThrow()));
        map.put("max", new FunctionSpec(1, 256, a -> Arrays.stream(a).max().orElseThrow()));
        map.put("sum", new FunctionSpec(1, 256, a -> Arrays.stream(a).sum()));
        map.put("mean", new FunctionSpec(1, 256, a -> Arrays.stream(a).average().orElseThrow()));
        map.put("median", new FunctionSpec(1, 256, a -> median(a)));
        map.put("variance", new FunctionSpec(1, 256, a -> variance(a, false)));
        map.put("sampleVariance", new FunctionSpec(2, 256, a -> variance(a, true)));
        map.put("stddev", new FunctionSpec(1, 256, a -> Math.sqrt(variance(a, false))));
        map.put("factorial", new FunctionSpec(1, 1, a -> factorial(a[0])));
        map.put("perm", new FunctionSpec(2, 2, a -> permutation(a[0],a[1])));
        map.put("choose", new FunctionSpec(2, 2, a -> choose(a[0],a[1])));
        map.put("gcd", new FunctionSpec(2, 2, a -> gcd(a[0],a[1])));
        map.put("lcm", new FunctionSpec(2, 2, a -> lcm(a[0],a[1])));
        map.put("bitAnd", new FunctionSpec(2, 2, a -> integer(a[0]) & integer(a[1])));
        map.put("bitOr", new FunctionSpec(2, 2, a -> integer(a[0]) | integer(a[1])));
        map.put("bitXor", new FunctionSpec(2, 2, a -> integer(a[0]) ^ integer(a[1])));
        map.put("bitNot", new FunctionSpec(1, 1, a -> integer(~integer(a[0]))));
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
    private static double median(double[] values) {
        double[] sorted = values.clone();
        Arrays.sort(sorted);
        int middle = sorted.length / 2;
        return sorted.length % 2 == 1 ? sorted[middle] : sorted[middle-1]/2 + sorted[middle]/2;
    }
    private static double variance(double[] values, boolean sample) {
        double mean = 0, sum = 0;
        int n = 0;
        for (double value : values) {
            double delta = value - mean;
            mean += delta / ++n;
            sum += delta * (value - mean);
        }
        return sum / (sample ? n - 1 : n);
    }
    private static double factorial(double number) {
        long n = integer(number);
        require(n >= 0 && n <= 170, "factorial needs an integer from 0 to 170");
        double result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }
    private static double permutation(double number, double count) {
        long n = integer(number), r = integer(count);
        require(n >= 0 && n <= 170 && r >= 0 && r <= n, "perm needs 0 <= r <= n <= 170");
        double result = 1;
        for (long i = 0; i < r; i++) result *= n-i;
        return result;
    }
    private static double choose(double number, double count) {
        long n = integer(number), r = integer(count);
        require(n >= 0 && n <= 100000 && r >= 0 && r <= n, "choose needs 0 <= r <= n <= 100000");
        r = Math.min(r, n-r);
        double result = 1;
        for (long i = 1; i <= r; i++) result *= (double)(n-r+i)/i;
        return result;
    }
    private static long gcd(double x, double y) {
        long a = Math.abs(integer(x)), b = Math.abs(integer(y));
        while (b != 0) { long remainder = a % b; a = b; b = remainder; }
        return a;
    }
    private static double lcm(double x, double y) {
        long a = Math.abs(integer(x)), b = Math.abs(integer(y));
        if (a == 0 || b == 0) return 0;
        long result = Math.multiplyExact(a / gcd(a,b), b);
        integer(result);
        return result;
    }
    // HELPERS
}
