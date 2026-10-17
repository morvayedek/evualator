package dev.santo.evaluator;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        System.exit(run(args, System.in, System.out, System.err));
    }
    /** Runs without closing caller-owned streams; useful for embedding and tests. */
    public static int run(String[] args, InputStream input, PrintStream out, PrintStream err) {
        var variables = new HashMap<String, Double>();
        var expressions = new ArrayList<String>();
        String mode = "", file = "";
        boolean json = false;
        try {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--var": {
                        if (++i == args.length) throw new IllegalArgumentException("--var requires name=value");
                        String[] binding = args[i].split("=", 2);
                        if (binding.length != 2 || !binding[0].matches("[A-Za-z_][A-Za-z0-9_]*"))
                            throw new IllegalArgumentException("--var requires an identifier and numeric value");
                        double value = Double.parseDouble(binding[1]);
                        if (!Double.isFinite(value)) throw new IllegalArgumentException("Variable value must be finite");
                        variables.put(binding[0], value);
                        break;
                    }
                    case "--file":
                        if (!mode.isEmpty() || ++i == args.length) throw new IllegalArgumentException("Use one input mode and a file path");
                        mode = "file"; file = args[i]; break;
                    case "--repl":
                        if (!mode.isEmpty()) throw new IllegalArgumentException("Use one input mode");
                        mode = "repl"; break;
                    default:
                        if (args[i].startsWith("--") && args[i].length() > 2 && Character.isLetter(args[i].charAt(2)))
                            throw new IllegalArgumentException("Unknown option: " + args[i]);
                        expressions.add(args[i]);
                }
            }
            Evaluator evaluator = new Evaluator();
            if (!mode.isEmpty()) {
                if (!expressions.isEmpty()) throw new IllegalArgumentException("Expression arguments cannot be combined with an input mode");
                BufferedReader reader = mode.equals("file")
                        ? Files.newBufferedReader(Path.of(file), StandardCharsets.UTF_8)
                        : new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
                int status = 0;
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (mode.equals("repl") && line.strip().equals(":quit")) break;
                        if (!line.isBlank()) status |= evaluateOne(evaluator, line, variables, out, err, json, mode.equals("repl"));
                    }
                } finally {
                    if (mode.equals("file")) reader.close();
                }
                return status;
            }
            if (expressions.isEmpty()) throw new IllegalArgumentException("Provide an expression or an input mode");
            return evaluateOne(evaluator, String.join(" ", expressions), variables, out, err, json, false);
        } catch (IOException | IllegalArgumentException exception) {
            err.println("Usage error: " + exception.getMessage());
            return 2;
        }
    }
    private static int evaluateOne(Evaluator evaluator, String expression, Map<String, Double> variables,
            PrintStream out, PrintStream err, boolean json, boolean remember) {
        try {
            double value = evaluator.evaluate(expression, variables);
            out.println(value);
            if (remember) variables.put("_", value);
            return 0;
        } catch (EvaluationException exception) {
            err.println("Error: " + exception.getMessage());
            return 1;
        }
    }
}
