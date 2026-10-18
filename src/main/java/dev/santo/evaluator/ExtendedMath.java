package dev.santo.evaluator;

import java.util.Map;

/** Additional real-valued numerical functions. */
final class ExtendedMath {
    private ExtendedMath() { }
    static void register(Map<String, FunctionSpec> map) {
        map.put("asinh", new FunctionSpec(1, 1, ExtendedMath::asinh));
    }

    /** Stable inverse hyperbolic sine, including tiny and very large inputs. */
    private static double asinh(double[] a) {
        
        double x = Math.abs(a[0]);
        double value = x > 1e154 ? Math.log(x) + Math.log(2) : Math.log1p(x + x*x/(1 + Math.sqrt(1+x*x)));
        return Math.copySign(value, a[0]);
    }
}
