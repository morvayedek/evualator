package dev.santo.evaluator;

import java.util.Objects;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.ToDoubleFunction;

/** Stateless, thread-safe evaluator of finite double-precision arithmetic. */
public final class Evaluator {
    private final Map<String, FunctionSpec> functions;

    public Evaluator() {
        functions = Functions.builtins();
    }

    /** Custom functions accept a fresh argument array and can override built-ins. */
    public Evaluator(Map<String, ToDoubleFunction<double[]>> custom) {
        var registry = new HashMap<>(Functions.builtins());
        custom.forEach((name, function) -> {
            if (!name.matches("[A-Za-z_][A-Za-z0-9_]*")) throw new IllegalArgumentException("Invalid function name");
            registry.put(name, new FunctionSpec(0, 256, Objects.requireNonNull(function)));
        });
        functions = Map.copyOf(registry);
    }
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
        return new Parser(expression, bindings, functions).parse();
    }

    private static final class Parser {
        private final String input;
        private final Map<String, Double> variables;
        private final Map<String, FunctionSpec> functions;
        private int cursor;
        private int depth;

        private Parser(String input, Map<String, Double> variables, Map<String, FunctionSpec> functions) {
            this.input = input;
            this.variables = variables;
            this.functions = functions;
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
                String name = input.substring(start, cursor);
                return take('(') ? function(name, start) : resolveName(name);
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

        private double function(String name, int start) {
            FunctionSpec specification = functions.get(name);
            if (specification == null) throw new EvaluationException("Unknown function '" + name + "'", start);
            if (++depth > 128) throw error("Function nesting exceeds 128");
            var arguments = new ArrayList<Double>();
            if (!take(')')) {
                do {
                    if (arguments.size() == 256) throw error("Function has more than 256 arguments");
                    arguments.add(sum());
                } while (take(','));
                if (!take(')')) throw error("Expected ')' after arguments");
            }
            depth--;
            double[] values = arguments.stream().mapToDouble(Double::doubleValue).toArray();
            try {
                return finite(specification.apply(values));
            } catch (IllegalArgumentException | ArithmeticException exception) {
                throw new EvaluationException(name + ": " + exception.getMessage(), start);
            }
        }

        private double resolveName(String name) {
            Double value = variables.get(name);
            if (value != null) return value;
            switch (name) {
                case "pi": return Math.PI;
                case "e": return Math.E;
                case "tau": return 2 * Math.PI;
                default: break;
            }
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
