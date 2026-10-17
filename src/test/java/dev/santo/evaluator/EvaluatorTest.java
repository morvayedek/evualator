package dev.santo.evaluator;

/** Dependency-free acceptance tests; failures always throw (no -ea required). */
public final class EvaluatorTest {
    private static int checks;

    public static void main(String[] args) {
        Evaluator evaluator = new Evaluator();
        Object[][] cases = {
            // ADD_CASES
            {"0", 0.0}, {"42", 42.0}, {"2+3*4", 14.0},
            {"(2+3)*4", 20.0}, {"10-3-2", 5.0}, {"20/2/5", 2.0},
            {"-2*-3", 6.0}, {"1--2", 3.0}, {"-(2+3)", -5.0},
            {"---2", -2.0}, {"+ +2", 2.0}, {".5 + 1.", 1.5},
            {" 2 +\t3\n", 5.0}, {"0.1+0.2", 0.3},
            {"((8 / 2) + (3 * 5)) - 1", 18.0}
        };
        for (Object[] row : cases) {
            double actual = evaluator.evaluate((String) row[0]);
            if (Math.abs(actual - (double) row[1]) > 1e-12) {
                throw new AssertionError(row[0] + " produced " + actual);
            }
            checks++;
        }
        for (String invalid : new String[] {
                // ADD_INVALID
                "", " ", ".", "()", "1+", "(1+2", "1+2)",
                "2 3", "1..2", "2**3", "abc", "1/0", "1/-0",
                "9".repeat(400), "1".repeat(10_001),
                "(".repeat(129) + "1" + ")".repeat(129)}) {
            try {
                evaluator.evaluate(invalid);
                throw new AssertionError("Accepted invalid input: " + invalid);
            } catch (EvaluationException expected) {
                if (expected.position() < 0 || expected.position() > invalid.length()) {
                    throw new AssertionError("Invalid error offset", expected);
                }
                checks++;
            }
        }
        if (evaluator.evaluate("2+2") != 4.0) {
            throw new AssertionError("Failed evaluation poisoned the next call");
        }
        checks++;
        if (evaluator.evaluate("(".repeat(128) + "1" + ")".repeat(128)) != 1.0) {
            throw new AssertionError("Valid nesting limit rejected");
        }
        checks++;
        if (evaluator.evaluate("-".repeat(9_999) + "1") != -1.0) {
            throw new AssertionError("Long unary chain failed");
        }
        checks++;
        try {
            evaluator.evaluate(null);
            throw new AssertionError("Accepted null");
        } catch (NullPointerException expected) {
            checks++;
        }
        // EXTRA_CHECKS
        System.out.println("PASS: " + checks + " evaluator checks");
    }
}
