package dev.santo.evaluator;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java -jar build/evaluator.jar \"2 * (3 + 4)\"");
            System.exit(2);
        }
        try {
            System.out.println(new Evaluator().evaluate(String.join(" ", args)));
        } catch (EvaluationException exception) {
            System.err.println("Error: " + exception.getMessage());
            System.exit(1);
        }
    }
}
