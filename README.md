# Java Evaluator

A dependency-free Java 17+ mathematical expression evaluator. The repository
name retains its original spelling, `evualator`.

## Build and test

Install a JDK 17 or newer (including `javac` and `jar`). From this directory:

```powershell
# Windows PowerShell
.\build.ps1
```

```sh
# Linux / macOS
sh build.sh
```

```sh
java -jar build/evaluator.jar "2 * (3 + 4)"
# 14.0
```

The build runs dependency-free acceptance tests before packaging the JAR.
The PowerShell build is verified on Windows with JDK 21, targeting Java 17.

## Expressions

Supports decimal and scientific numbers, `+`, `-`, `*`, `/`, `%`, `^`,
parentheses, variables, constants and validated function calls. See the
[grammar and numerical contract](docs/grammar.md), [function catalog](docs/functions.md),
[extended numerical functions](docs/extended-functions.md),
[CLI reference](docs/cli.md) and [Java API cookbook](docs/java-api.md).
Evaluation uses IEEE 754 `double`: `0.1 + 0.2` is approximately `0.3`.

Malformed expressions, division by zero, non-finite results, inputs longer than
10,000 characters and nesting deeper than 128 produce an error. The CLI exits
with 0 on success, 1 for an invalid expression, and 2 for incorrect invocation.
No Java source or shell command is executed from an expression.

## Java API

```java
import dev.santo.evaluator.Evaluator;

double result = new Evaluator().evaluate("(12 - 2) / 5"); // 2.0
```

The [development roadmap](ROADMAP.md) records the 48 implementation and
verification increments. Each increment is covered by the build-time test suite
or documentation review. Remote PR and CI status must be checked on GitHub.
