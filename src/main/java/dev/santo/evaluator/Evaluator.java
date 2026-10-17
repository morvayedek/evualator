package dev.santo.evaluator;

import java.util.Objects;

/** Stateless, thread-safe evaluator of finite double-precision arithmetic. */
public final class Evaluator {
    public double evaluate(String expression) {
        Objects.requireNonNull(expression, "expression");
        if (expression.length() > 10_000) {
            throw new EvaluationException("Expression exceeds 10000 characters", 10_000);
        }
        return new Parser(expression).parse();
    }

    private static final class Parser {
        private final String input;
        private int cursor;
        private int depth;

        private Parser(String input) {
            this.input = input;
        }

        private double parse() {
            double value = sum();
            skipSpace();
            if (cursor != input.length()) {
                throw error("Unexpected character '" + input.charAt(cursor) + "'");
            }
            return value;
        }

        private double sum() {
            double value = product();
            while (true) {
                if (take('+')) {
                    value = finite(value + product());
                } else if (take('-')) {
                    value = finite(value - product());
                } else {
                    return value;
                }
            }
        }

        private double product() {
            double value = unary();
            while (true) {
                if (take('*')) {
                    value = finite(value * unary());
                } else if (take('/')) {
                    double divisor = unary();
                    if (divisor == 0.0) {
                        throw error("Division by zero");
                    }
                    value = finite(value / divisor);
                } else {
                    return value;
                }
            }
        }

        private double unary() {
            // Iterative signs avoid recursion for expressions such as ----1.
            boolean negative = false;
            while (true) {
                if (take('-')) {
                    negative = !negative;
                } else if (!take('+')) {
                    break;
                }
            }
            double value = power();
            return negative ? -value : value;
        }

        private double power() {
            double value = primary();
            if (take('^')) {
                if (++depth > 128) throw error("Exponent nesting exceeds 128");
                value = finite(Math.pow(value, unary()));
                depth--;
            }
            return value;
        }

        private double primary() {
            if (take('(')) {
                if (++depth > 128) {
                    throw error("Parentheses exceed maximum nesting of 128");
                }
                double value = sum();
                if (!take(')')) {
                    throw error("Expected ')'");
                }
                depth--;
                return value;
            }
            return number();
        }

        private double number() {
            skipSpace();
            int start = cursor;
            int digits = 0;
            while (cursor < input.length() && digit(input.charAt(cursor))) {
                cursor++;
                digits++;
            }
            if (cursor < input.length() && input.charAt(cursor) == '.') {
                cursor++;
                while (cursor < input.length() && digit(input.charAt(cursor))) {
                    cursor++;
                    digits++;
                }
            }
            if (digits > 0 && cursor < input.length()
                    && (input.charAt(cursor) == 'e' || input.charAt(cursor) == 'E')) {
                cursor++;
                if (cursor < input.length() && (input.charAt(cursor) == '+' || input.charAt(cursor) == '-')) cursor++;
                int exponentStart = cursor;
                while (cursor < input.length() && digit(input.charAt(cursor))) cursor++;
                if (cursor == exponentStart) throw error("Expected exponent digits");
            }
            if (digits == 0) {
                throw error("Expected a number or '('");
            }
            try {
                return finite(Double.parseDouble(input.substring(start, cursor)));
            } catch (NumberFormatException exception) {
                throw new EvaluationException("Invalid number", start);
            }
        }

        private static boolean digit(char character) {
            return character >= '0' && character <= '9';
        }

        private double finite(double value) {
            if (!Double.isFinite(value)) {
                throw error("Number or result is not finite");
            }
            return value;
        }

        private boolean take(char expected) {
            skipSpace();
            if (cursor < input.length() && input.charAt(cursor) == expected) {
                cursor++;
                return true;
            }
            return false;
        }

        private void skipSpace() {
            while (cursor < input.length() && Character.isWhitespace(input.charAt(cursor))) {
                cursor++;
            }
        }

        private EvaluationException error(String message) {
            return new EvaluationException(message, cursor);
        }
    }
}
