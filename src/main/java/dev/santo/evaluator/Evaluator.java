package dev.santo.evaluator;

import java.util.Objects;
import java.util.Map;

/** Stateless, thread-safe evaluator of finite double-precision arithmetic. */
public final class Evaluator {
    public double evaluate(String expression) {
        return evaluate(expression, Map.of());
    }

    public double evaluate(String expression, Map<String, Double> variables) {
        Objects.requireNonNull(expression, "expression");
        Map<String, Double> bindings = Map.copyOf(variables);
        for (var entry : bindings.entrySet()) {
            if (!entry.getKey().matches("[A-Za-z_][A-Za-z0-9_]*") || !Double.isFinite(entry.getValue())) {
                throw new IllegalArgumentException("Variables need identifier names and finite values");
            }
        }
        if (expression.length() > 10_000) {
            throw new EvaluationException("Expression exceeds 10000 characters", 10_000);
        }
        return new Parser(expression, bindings).parse();
    }

    private static final class Parser {
        private final String input;
        private final Map<String, Double> variables;
        private int cursor;
        private int depth;

        private Parser(String input, Map<String, Double> variables) {
            this.input = input;
            this.variables = variables;
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
                } else if (take('%')) {
                    double divisor = unary();
                    if (divisor == 0.0) throw error("Remainder by zero");
                    value = finite(value % divisor);
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
            skipSpace();
            if (cursor < input.length() && (Character.isLetter(input.charAt(cursor)) || input.charAt(cursor) == '_')) {
                int start = cursor++;
                while (cursor < input.length() && (Character.isLetterOrDigit(input.charAt(cursor)) || input.charAt(cursor) == '_')) cursor++;
                return resolveName(input.substring(start, cursor));
            }
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

        private double resolveName(String name) {
            Double value = variables.get(name);
            if (value != null) return value;
            throw error("Unknown variable '" + name + "'");
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
