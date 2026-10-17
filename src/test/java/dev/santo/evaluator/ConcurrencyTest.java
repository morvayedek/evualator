package dev.santo.evaluator;
import java.util.*;
import java.util.concurrent.*;
public final class ConcurrencyTest {
    public static void main(String[] args) throws Exception {
        var pool = Executors.newFixedThreadPool(8);
        try {
            var evaluator = new Evaluator();
            var tasks = new ArrayList<Callable<Double>>();
            for (int i = 0; i < 256; i++) {
                final double value = i;
                tasks.add(() -> evaluator.evaluate("x*x+sqrt(16)", Map.of("x",value)));
            }
            var results = pool.invokeAll(tasks);
            for (int i = 0; i < results.size(); i++)
                if (results.get(i).get() != i*i+4.0) throw new AssertionError("Shared state leaked at " + i);
            System.out.println("PASS: 256 concurrent binding checks");
        } finally { pool.shutdownNow(); }
    }
}
