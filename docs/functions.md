# Built-in functions

All arguments and results must be finite. Names are case-sensitive.

## add absolute value and sign functions

- `abs`: accepts 1 arguments.
- `sign`: accepts 1 arguments.

Examples: `abs(-3)` → `3.0`; `sign(-4)` → `-1.0`.

## add square and cube root functions

- `sqrt`: accepts 1 arguments.
- `cbrt`: accepts 1 arguments.

Examples: `sqrt(81)` → `9.0`; `cbrt(-27)` → `-3.0`.

## add natural common and binary logarithms

- `ln`: accepts 1 arguments.
- `log10`: accepts 1 arguments.
- `log2`: accepts 1 arguments.

Examples: `ln(e)` → `1.0`; `log10(1000)` → `3.0`; `log2(8)` → `3.0`.

## add exponential and power functions

- `exp`: accepts 1 arguments.
- `pow`: accepts 2 arguments.

Examples: `exp(0)` → `1.0`; `pow(2,-3)` → `0.125`.

## add radian trigonometric functions

- `sin`: accepts 1 arguments.
- `cos`: accepts 1 arguments.
- `tan`: accepts 1 arguments.

Examples: `sin(pi/2)` → `1.0`; `cos(pi)` → `-1.0`; `tan(0)` → `0.0`.

## add inverse trigonometric functions

- `asin`: accepts 1 arguments.
- `acos`: accepts 1 arguments.
- `atan`: accepts 1 arguments.
- `atan2`: accepts 2 arguments.

Examples: `asin(1)` → `1.5707963267948966`; `acos(1)` → `0.0`; `atan2(0,1)` → `0.0`.

## add hyperbolic functions

- `sinh`: accepts 1 arguments.
- `cosh`: accepts 1 arguments.
- `tanh`: accepts 1 arguments.

Examples: `sinh(0)` → `0.0`; `cosh(0)` → `1.0`; `tanh(0)` → `0.0`.

## add angle conversion functions

- `deg`: accepts 1 arguments.
- `rad`: accepts 1 arguments.

Examples: `deg(pi)` → `180.0`; `rad(180)-pi` → `0.0`.

## add rounding with explicit tie semantics

- `floor`: accepts 1 arguments.
- `ceil`: accepts 1 arguments.
- `rint`: accepts 1 arguments.
- `trunc`: accepts 1 arguments.

Examples: `floor(-1.2)` → `-2.0`; `ceil(-1.2)` → `-1.0`; `rint(2.5)` → `2.0`; `rint(3.5)` → `4.0`; `trunc(-1.9)` → `-1.0`.

