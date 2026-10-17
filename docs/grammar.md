# Grammar and numerical contract

```text
expression = sum EOF
sum        = product (("+" | "-") product)*
product    = unary (("*" | "/" | "%") unary)*
unary      = ("+" | "-")* power
power      = primary ("^" unary)?
primary    = number | identifier | identifier "(" arguments? ")" | "(" sum ")"
arguments  = sum ("," sum)*
number     = (digits ("." digits?)? | "." digits) ([eE] [+-]? digits)?
```

Whitespace is ignored between tokens. Exponentiation is right-associative:
`2^3^2 = 512`, `-2^2 = -4`, `2^-2 = 0.25`. Other binary operators associate
left. `%` is Java remainder (the dividend determines its sign); `mod(x,m)`
normalizes into `[0,m)` for a positive modulus. No implicit multiplication,
assignment, strings, shell commands, or Java expressions are accepted.

Names are case-sensitive. Public variable and custom-function names use ASCII
letters, digits and underscores and cannot begin with a digit. `pi`, `e` and
`tau` are defaults; an explicit caller binding overrides them. Standard
trigonometric functions use radians; `sinDeg`, `cosDeg`, `tanDeg` use degrees.
Function arguments are evaluated eagerly, from left to right.

Numbers and results are finite IEEE 754 doubles. Expect normal floating-point
rounding; this is not an arbitrary-precision finance engine. `rint` and
`roundPlaces` use ties-to-even. `roundPlaces` converts the double's canonical
decimal spelling with `BigDecimal.valueOf`; other operations remain binary
floating point. Combinatorial results can lose integer precision above 2^53.
Exact integer functions limit operands to `[-2^53+1,2^53-1]`.

The parser limits input to 10000 UTF-16 code units, recursive nesting to 128
and function arguments to 256. These bounds do not sandbox custom callbacks.
Errors report zero-based UTF-16 offsets, not terminal display columns.
Underflow to zero is permitted; NaN, infinity and division by signed zero are
reported as errors. `0^0` follows `Math.pow` and evaluates to 1.
