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
        check(e, "sinc(0)", 1.0);
        check(e, "sinc(pi)", 0.0);
        check(e, "sinc(-pi)", 0.0);
        check(e, "sinc(1e-20)", 1.0);
        reject(e, "sinc()");
        check(e, "logBase(8,2)", 3.0);
        check(e, "logBase(100,10)", 2.0);
        check(e, "logBase(.25,.5)", 2.0);
        check(e, "logBase(1,3)", 0.0);
        reject(e, "logBase(0,2)");
        reject(e, "logBase(2,1)");
        reject(e, "logBase(2,-1)");
        reject(e, "logBase()");
        check(e, "root(81,4)", 3.0);
        check(e, "root(-27,3)", -3.0);
        check(e, "root(0,2)", 0.0);
        check(e, "root(16,1)", 16.0);
        reject(e, "root(-1,2)");
        reject(e, "root(4,0)");
        reject(e, "root(4,1.5)");
        reject(e, "root()");
        check(e, "wrap(370,0,360)", 10.0);
        check(e, "wrap(-10,0,360)", 350.0);
        check(e, "wrap(180,-180,180)", -180.0);
        check(e, "wrap(10,10,20)", 10.0);
        reject(e, "wrap(1,2,2)");
        reject(e, "wrap(1,-1e308,1e308)");
        reject(e, "wrap()");
        check(e, "smoothstep(0,1,.5)", 0.5);
        check(e, "smoothstep(0,1,-1)", 0.0);
        check(e, "smoothstep(0,1,2)", 1.0);
        check(e, "smoothstep(0,1,.25)", 0.15625);
        reject(e, "smoothstep(1,1,1)");
        reject(e, "smoothstep()");
        check(e, "smootherstep(0,1,.5)", 0.5);
        check(e, "smootherstep(0,1,-1)", 0.0);
        check(e, "smootherstep(0,1,2)", 1.0);
        check(e, "smootherstep(0,1,.25)", 0.103515625);
        reject(e, "smootherstep(2,1,1)");
        reject(e, "smootherstep()");
        check(e, "logistic(0)", 0.5);
        check(e, "logistic(1000)", 1.0);
        check(e, "logistic(-1000)", 0.0);
        check(e, "logistic(1)", 0.7310585786300049);
        reject(e, "logistic()");
        check(e, "logit(.5)", 0.0);
        check(e, "logit(.75)", 1.0986122886681098);
        check(e, "logistic(logit(.2))", 0.2);
        reject(e, "logit(0)");
        reject(e, "logit(1)");
        reject(e, "logit()");
        check(e, "softplus(0)", 0.6931471805599453);
        check(e, "softplus(1000)", 1000.0);
        check(e, "softplus(-1000)", 0.0);
        check(e, "softplus(-10)", 4.539889921686465e-05);
        reject(e, "softplus()");
        check(e, "softsign(0)", 0.0);
        check(e, "softsign(2)", 0.6666666666666666);
        check(e, "softsign(-2)", -0.6666666666666666);
        check(e, "softsign(1e300)", 1.0);
        reject(e, "softsign()");
        check(e, "relu(-2)", 0.0);
        check(e, "relu(3)", 3.0);
        check(e, "relu(0)", 0.0);
        check(e, "relu(-0)", 0.0);
        reject(e, "relu()");
        check(e, "leakyRelu(-2,.1)", -0.2);
        check(e, "leakyRelu(2,.1)", 2.0);
        check(e, "leakyRelu(-3,0)", 0.0);
        check(e, "leakyRelu(-3,1)", -3.0);
        reject(e, "leakyRelu(1,-1)");
        reject(e, "leakyRelu(1,2)");
        reject(e, "leakyRelu()");
        check(e, "elu(2,1)", 2.0);
        check(e, "elu(0,2)", 0.0);
        check(e, "elu(-1,1)", -0.6321205588285577);
        check(e, "elu(-1000,2)", -2.0);
        reject(e, "elu(-1,0)");
        reject(e, "elu()");
        check(e, "square(-3)", 9.0);
        check(e, "square(.5)", 0.25);
        check(e, "square(0)", 0.0);
        reject(e, "square(1e308)");
        reject(e, "square()");
        check(e, "cube(-3)", -27.0);
        check(e, "cube(.5)", 0.125);
        check(e, "cube(0)", 0.0);
        reject(e, "cube(1e200)");
        reject(e, "cube()");
        System.out.println("PASS: " + checks + " extended numerical checks");
    }
    private static void check(Evaluator e, String expression, double expected) {
        double actual=e.evaluate(expression);
        double tolerance=Math.max(Math.ulp(expected)*8,Math.abs(expected)*2e-12);
        if(expected==0) tolerance=1e-14;
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
