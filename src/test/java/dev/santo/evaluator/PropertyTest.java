package dev.santo.evaluator;
import java.util.Random;
public final class PropertyTest {
    public static void main(String[] args) {
        var random = new Random(1337);
        var evaluator = new Evaluator();
        for (int i = 0; i < 2000; i++) {
            int a = random.nextInt(101)-50, b = random.nextInt(101)-50, c = random.nextInt(101)-50;
            String expression = "("+a+")+ ("+b+") * ("+c+")";
            if (evaluator.evaluate(expression) != a+b*c) throw new AssertionError(expression);
            String grouped = "(("+a+") + ("+b+")) * ("+c+")";
            if (evaluator.evaluate(grouped) != (a+b)*c) throw new AssertionError(grouped);
        }
        System.out.println("PASS: 4000 generated precedence checks");
    }
}
