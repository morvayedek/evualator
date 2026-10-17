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

## add variadic minimum and maximum

- `min`: accepts 1–256 arguments.
- `max`: accepts 1–256 arguments.

Examples: `min(8,-2,4)` → `-2.0`; `max(8,-2,4)` → `8.0`.

## add compensated sum and mean functions

- `sum`: accepts 1–256 arguments.
- `mean`: accepts 1–256 arguments.

Examples: `sum(1,2,3)` → `6.0`; `mean(2,4,6)` → `4.0`.

## add median without mutating caller arguments

- `median`: accepts 1–256 arguments.

Examples: `median(9,1,4)` → `4.0`; `median(9,1,4,2)` → `3.0`.

## add population and sample statistics

- `variance`: accepts 1–256 arguments.
- `sampleVariance`: accepts 2–256 arguments.
- `stddev`: accepts 1–256 arguments.

Examples: `variance(2,4,6)` → `2.6666666666666665`; `sampleVariance(2,4,6)` → `4.0`; `stddev(2,2)` → `0.0`.

## add bounded factorial and permutation functions

- `factorial`: accepts 1 arguments.
- `perm`: accepts 2 arguments.

Examples: `factorial(5)` → `120.0`; `perm(5,2)` → `20.0`.

## add binomial coefficients

- `choose`: accepts 2 arguments.

Examples: `choose(5,2)` → `10.0`; `choose(5,0)` → `1.0`; `choose(5,5)` → `1.0`.

## add integer gcd and overflow-checked lcm

- `gcd`: accepts 2 arguments.
- `lcm`: accepts 2 arguments.

Examples: `gcd(-48,18)` → `6.0`; `gcd(0,0)` → `0.0`; `lcm(6,8)` → `24.0`; `lcm(0,0)` → `0.0`.

## add exact-range bitwise integer functions

- `bitAnd`: accepts 2 arguments.
- `bitOr`: accepts 2 arguments.
- `bitXor`: accepts 2 arguments.
- `bitNot`: accepts 1 arguments.

Examples: `bitAnd(6,3)` → `2.0`; `bitOr(6,3)` → `7.0`; `bitXor(6,3)` → `5.0`; `bitNot(0)` → `-1.0`.

