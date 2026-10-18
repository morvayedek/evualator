# Extended numerical functions

All functions use finite IEEE 754 doubles and reject non-finite results.
Names are case-sensitive. Errors include an expression offset.
Domains and limits below are part of the API contract. Underflow may produce zero.
These functions complement the [original catalog](functions.md).

## `asinh`

Stable inverse hyperbolic sine, including tiny and very large inputs.

Arguments: 1.

```text
asinh(0) = 0
asinh(1) = 0.881373587019543
asinh(-1) = -0.881373587019543
asinh(1e300) = 691.4686750787737
asinh(1e-20) = 1e-20
```

## `acosh`

Inverse hyperbolic cosine for x >= 1 without squaring huge inputs.

Arguments: 1.

```text
acosh(1) = 0
acosh(2) = 1.3169578969248166
acosh(1e300) = 691.4686750787737
```

Rejected examples: `acosh(.9)`.

## `atanh`

Inverse hyperbolic tangent on the open interval (-1,1).

Arguments: 1.

```text
atanh(0) = 0
atanh(.5) = 0.5493061443340548
atanh(-.5) = -0.5493061443340548
atanh(1e-20) = 1e-20
```

Rejected examples: `atanh(1)`, `atanh(-1)`.

## `reciprocal`

Reciprocal with explicit rejection of signed zero.

Arguments: 1.

```text
reciprocal(4) = 0.25
reciprocal(-2) = -0.5
reciprocal(1e300) = 1e-300
```

Rejected examples: `reciprocal(0)`, `reciprocal(-0)`, `reciprocal(5e-324)`.

## `sinc`

Unnormalised sinc, sin(x)/x, with its continuous value at zero.

Arguments: 1.

```text
sinc(0) = 1
sinc(pi) = 0
sinc(-pi) = 0
sinc(1e-20) = 1
```

## `logBase`

Logarithm of a positive value in any positive base other than one.

Arguments: 2.

```text
logBase(8,2) = 3
logBase(100,10) = 2
logBase(.25,.5) = 2
logBase(1,3) = 0
```

Rejected examples: `logBase(0,2)`, `logBase(2,1)`, `logBase(2,-1)`.

## `root`

Real nth roots; negative radicands require an odd positive integer degree.

Arguments: 2.

```text
root(81,4) = 3
root(-27,3) = -3
root(0,2) = 0
root(16,1) = 16
```

Rejected examples: `root(-1,2)`, `root(4,0)`, `root(4,1.5)`.

