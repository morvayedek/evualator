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

## Current arithmetic

Supports decimal numbers (`.5`, `1.`, `2.5`), `+`, `-`, `*`, `/`, parentheses,
whitespace, and unary signs. Multiplication and division bind more tightly than
addition and subtraction; operations at equal precedence associate to the left.
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

See [ROADMAP.md](ROADMAP.md) for the planned increments. Roadmap entries are
planned work, not a claim that those capabilities already exist.
