# Embedding the evaluator

Build the JAR, then compile a consuming application with it on the classpath.
No reflection, Java source compilation or script engine is used during evaluation.

```java
import dev.santo.evaluator.*;
import java.util.Map;
import java.util.List;

Evaluator evaluator = new Evaluator();
double answer = evaluator.evaluate("sqrt(x*x + y*y)", Map.of("x",3.0,"y",4.0));
List<EvaluationResult> batch = evaluator.evaluateAll(List.of("2+2","1/0"), Map.of());
```

## Custom functions

```java
Evaluator custom = new Evaluator(Map.of("twice", values -> {
    if (values.length != 1) throw new IllegalArgumentException("one argument required");
    return values[0] * 2;
}));
double result = custom.evaluate("twice(21)");
```

Callbacks receive their own argument array. Custom names override built-ins.
Use callbacks only for trusted application logic: they execute Java supplied by
the embedding application and can have side effects. The evaluator cannot
impose a timeout on a custom callback. A shared instance is safe with the
default functions; custom callbacks must supply their own thread safety.

## Errors and data ownership

`EvaluationException` identifies expression failures with a zero-based UTF-16
position. Null inputs, invalid variable maps and invalid custom-function
definitions are API argument errors. Binding maps are copied before evaluation.
`evaluateAll` preserves order and records expression failures independently;
its returned list is immutable. The library does not print or terminate the JVM.
