package dev.santo.evaluator;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public final class CliTest {
    private static int checks;
    private static void check(String[] args, String input, int expectedStatus, String expectedOutput) {
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();
        int status = Main.run(args, new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(out), new PrintStream(err));
        String actual = out.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
        if (status != expectedStatus || !actual.equals(expectedOutput))
            throw new AssertionError("CLI mismatch: " + status + " " + actual + " " + err);
        checks++;
    }
    public static void main(String[] args) throws Exception {
        check(new String[]{"--var","x=3","x*2"}, "", 0, "6.0\n");
        check(new String[]{"--var","not-valid=3","1"}, "", 2, "");
        check(new String[]{"--var","x=NaN","1"}, "", 2, "");
        check(new String[]{"--var"}, "", 2, "");
        check(new String[]{"1/0"}, "", 1, "");
        check(new String[]{}, "", 2, "");
        // CLI_CASES
        System.out.println("PASS: " + checks + " CLI checks");
    }
}
