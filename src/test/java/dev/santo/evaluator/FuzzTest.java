package dev.santo.evaluator;
import java.util.Random;
public final class FuzzTest {
    public static void main(String[] args) {
        var random = new Random(48);
        var evaluator = new Evaluator();
        String alphabet = "0123456789.+-*/%^(), abc_e\t";
        for (int i = 0; i < 5000; i++) {
            StringBuilder expression = new StringBuilder();
            for (int n = random.nextInt(64); n > 0; n--) expression.append(alphabet.charAt(random.nextInt(alphabet.length())));
            try {
                if (!Double.isFinite(evaluator.evaluate(expression.toString()))) throw new AssertionError("Nonfinite output");
            } catch (EvaluationException expected) {
                if (expected.position() < 0 || expected.position() > expression.length()) throw new AssertionError("Bad error offset");
            }
        }
        try {
            evaluator.evaluate("2^".repeat(200)+"1");
            throw new AssertionError("Unbounded exponent nesting");
        } catch (EvaluationException expected) { }
        System.out.println("PASS: 5000 deterministic parser fuzz checks and deep-power regression");
    }
}
