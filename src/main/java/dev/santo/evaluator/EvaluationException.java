package dev.santo.evaluator;

/** A malformed expression or an arithmetic error, with a zero-based offset. */
public final class EvaluationException extends IllegalArgumentException {
    private final int position;

    public EvaluationException(String message, int position) {
        super(message + " at position " + position);
        this.position = position;
    }

    public int position() {
        return position;
    }
}
