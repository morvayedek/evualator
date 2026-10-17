package dev.santo.evaluator;
/** One ordered batch result: successful values or a positioned evaluation error. */
public record EvaluationResult(String expression, Double value, String error, Integer position) {
    public boolean successful() { return error == null; }
}
