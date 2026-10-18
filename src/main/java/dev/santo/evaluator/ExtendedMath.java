package dev.santo.evaluator;

import java.util.Map;

/** Additional real-valued numerical functions. */
final class ExtendedMath {
    private ExtendedMath() { }
    static void register(Map<String, FunctionSpec> map) {
        map.put("asinh", new FunctionSpec(1, 1, ExtendedMath::asinh));
        map.put("acosh", new FunctionSpec(1, 1, ExtendedMath::acosh));
        map.put("atanh", new FunctionSpec(1, 1, ExtendedMath::atanh));
        map.put("reciprocal", new FunctionSpec(1, 1, ExtendedMath::reciprocal));
    }

    /** Stable inverse hyperbolic sine, including tiny and very large inputs. */
    private static double asinh(double[] a) {
        
        double x = Math.abs(a[0]);
        double value = x > 1e154 ? Math.log(x) + Math.log(2) : Math.log1p(x + x*x/(1 + Math.sqrt(1+x*x)));
        return Math.copySign(value, a[0]);
    }

    /** Inverse hyperbolic cosine for x >= 1 without squaring huge inputs. */
    private static double acosh(double[] a) {
        
        Functions.require(a[0] >= 1, "acosh requires x >= 1");
        double x=a[0];
        return x > 1e154 ? Math.log(x)+Math.log(2) : Math.log1p((x-1)+Math.sqrt(x-1)*Math.sqrt(x+1));
    }

    /** Inverse hyperbolic tangent on the open interval (-1,1). */
    private static double atanh(double[] a) {
        
        double x=Math.abs(a[0]);
        Functions.require(x < 1, "atanh requires -1 < x < 1");
        return Math.copySign(.5*Math.log1p(2*x/(1-x)),a[0]);
    }

    /** Reciprocal with explicit rejection of signed zero. */
    private static double reciprocal(double[] a) {
        
        Functions.require(a[0] != 0, "reciprocal requires a nonzero value");
        return 1/a[0];
    }
}
