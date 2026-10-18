package dev.santo.evaluator;

/** Examples and domain/arity boundaries for the extended function catalog. */
public final class ExtendedMathTest {
    private static int checks;
    public static void main(String[] args) {
        var e = new Evaluator();
        check(e, "asinh(0)", 0.0);
        check(e, "asinh(1)", 0.881373587019543);
        check(e, "asinh(-1)", -0.881373587019543);
        check(e, "asinh(1e300)", 691.4686750787737);
        check(e, "asinh(1e-20)", 1e-20);
        reject(e, "asinh()");
        check(e, "acosh(1)", 0.0);
        check(e, "acosh(2)", 1.3169578969248166);
        check(e, "acosh(1e300)", 691.4686750787737);
        reject(e, "acosh(.9)");
        reject(e, "acosh()");
        check(e, "atanh(0)", 0.0);
        check(e, "atanh(.5)", 0.5493061443340548);
        check(e, "atanh(-.5)", -0.5493061443340548);
        check(e, "atanh(1e-20)", 1e-20);
        reject(e, "atanh(1)");
        reject(e, "atanh(-1)");
        reject(e, "atanh()");
        check(e, "reciprocal(4)", 0.25);
        check(e, "reciprocal(-2)", -0.5);
        check(e, "reciprocal(1e300)", 1e-300);
        reject(e, "reciprocal(0)");
        reject(e, "reciprocal(-0)");
        reject(e, "reciprocal(5e-324)");
        reject(e, "reciprocal()");
        System.out.println("PASS: " + checks + " extended numerical checks");
    }
    private static void check(Evaluator e, String expression, double expected) {
        double actual=e.evaluate(expression);
        double tolerance=Math.max(Math.ulp(expected)*8,Math.abs(expected)*2e-12);
        if(!Double.isFinite(actual) || Math.abs(actual-expected)>tolerance)
            throw new AssertionError(expression+": expected "+expected+", received "+actual);
        checks++;
    }
    private static void reject(Evaluator e, String expression) {
        try { e.evaluate(expression); throw new AssertionError("Accepted "+expression); }
        catch(EvaluationException expected) {
            if(expected.position()<0 || expected.position()>expression.length())
                throw new AssertionError("Bad diagnostic offset for "+expression);
            checks++;
        }
    }
}
