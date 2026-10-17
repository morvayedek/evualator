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
        Path file = Files.createTempFile("evaluator-cli", ".txt");
        try {
            Files.writeString(file, "2+2\n\n1/0\n3*3\n", StandardCharsets.UTF_8);
            check(new String[]{"--file",file.toString()}, "", 1, "4.0\n9.0\n");
            check(new String[]{"--file",file.toString(),"2+2"}, "", 2, "");
        } finally { Files.deleteIfExists(file); }
        check(new String[]{"--repl"}, "2+3\n1/0\n4+4\n:quit\n9+9\n", 1, "5.0\n8.0\n");
        check(new String[]{"--repl"}, "2+3\n1/0\n_*2\n:quit\n", 1, "5.0\n10.0\n");
        check(new String[]{"--json","2+2"}, "", 0, "{\"expression\":\"2+2\",\"result\":4.0}\n");
        check(new String[]{"--unknown"}, "", 2, "");
        check(new String[]{"--","--2"}, "", 0, "2.0\n");
        var helpOut = new ByteArrayOutputStream();
        if (Main.run(new String[]{"--help"}, InputStream.nullInputStream(), new PrintStream(helpOut), System.err) != 0
                || !helpOut.toString().contains("Usage:")) throw new AssertionError("Missing help");
        checks++;
        check(new String[]{"--stdin"}, "2+2\n\n3*3\n", 0, "4.0\n9.0\n");
        check(new String[]{"--stdin","--repl"}, "", 2, "");
        // CLI_CASES
        System.out.println("PASS: " + checks + " CLI checks");
    }
}
