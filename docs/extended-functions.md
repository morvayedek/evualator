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

## `wrap`

Wrap values to [low,high), requiring a finite positive interval width.

Arguments: 3.

```text
wrap(370,0,360) = 10
wrap(-10,0,360) = 350
wrap(180,-180,180) = -180
wrap(10,10,20) = 10
```

Rejected examples: `wrap(1,2,2)`, `wrap(1,-1e308,1e308)`.

## `smoothstep`

Cubic interpolation between ordered edges with saturation outside the interval.

Arguments: 3.

```text
smoothstep(0,1,.5) = 0.5
smoothstep(0,1,-1) = 0
smoothstep(0,1,2) = 1
smoothstep(0,1,.25) = 0.15625
```

Rejected examples: `smoothstep(1,1,1)`.

## `smootherstep`

Quintic interpolation with zero first and second derivatives at both edges.

Arguments: 3.

```text
smootherstep(0,1,.5) = 0.5
smootherstep(0,1,-1) = 0
smootherstep(0,1,2) = 1
smootherstep(0,1,.25) = 0.103515625
```

Rejected examples: `smootherstep(2,1,1)`.

## `logistic`

Overflow-safe logistic activation for arbitrarily large finite inputs.

Arguments: 1.

```text
logistic(0) = 0.5
logistic(1000) = 1
logistic(-1000) = 0
logistic(1) = 0.7310585786300049
```

## `logit`

Inverse logistic transform for probabilities strictly between zero and one.

Arguments: 1.

```text
logit(.5) = 0
logit(.75) = 1.0986122886681098
logistic(logit(.2)) = 0.2
```

Rejected examples: `logit(0)`, `logit(1)`.

## `softplus`

Stable log(1+exp(x)) activation, retaining small negative tails.

Arguments: 1.

```text
softplus(0) = 0.6931471805599453
softplus(1000) = 1000
softplus(-1000) = 0
softplus(-10) = 4.539889921686465e-05
```

## `softsign`

Bounded softsign activation x/(1+abs(x)).

Arguments: 1.

```text
softsign(0) = 0
softsign(2) = 0.6666666666666666
softsign(-2) = -0.6666666666666666
softsign(1e300) = 1
```

## `relu`

Rectified linear activation with a nonnegative zero result.

Arguments: 1.

```text
relu(-2) = 0
relu(3) = 3
relu(0) = 0
relu(-0) = 0
```

## `leakyRelu`

Leaky rectifier with a caller-selected negative slope in [0,1].

Arguments: 2.

```text
leakyRelu(-2,.1) = -0.2
leakyRelu(2,.1) = 2
leakyRelu(-3,0) = 0
leakyRelu(-3,1) = -3
```

Rejected examples: `leakyRelu(1,-1)`, `leakyRelu(1,2)`.

## `elu`

Exponential linear activation with a positive alpha parameter.

Arguments: 2.

```text
elu(2,1) = 2
elu(0,2) = 0
elu(-1,1) = -0.6321205588285577
elu(-1000,2) = -2
```

Rejected examples: `elu(-1,0)`.

## `square`

Square a value; the evaluator rejects results outside the finite-double range.

Arguments: 1.

```text
square(-3) = 9
square(.5) = 0.25
square(0) = 0
```

Rejected examples: `square(1e308)`.

## `cube`

Cube a value, preserving its sign.

Arguments: 1.

```text
cube(-3) = -27
cube(.5) = 0.125
cube(0) = 0
```

Rejected examples: `cube(1e200)`.

## `fma`

Fused multiply-add with one final rounding and no intermediate product overflow.

Arguments: 3.

```text
fma(2,3,4) = 10
fma(1e308,2,-1e308) = 1e+308
fma(-2,3,6) = 0
```

## `copySign`

Copy the sign of the second value to the magnitude of the first, including signed zero.

Arguments: 2.

```text
copySign(3,-1) = -3
copySign(-3,1) = 3
copySign(3,-0) = -3
```

## `scalb`

Scale by an integral power of two with a bounded exponent parameter.

Arguments: 2.

```text
scalb(3,2) = 12
scalb(8,-3) = 1
scalb(1,-1074) = 5e-324
```

Rejected examples: `scalb(1,2099)`, `scalb(1,.5)`, `scalb(1,1024)`.

## `exponent`

Return the unbiased binary exponent; zero and subnormals return -1023.

Arguments: 1.

```text
exponent(8) = 3
exponent(.5) = -1
exponent(0) = -1023
exponent(5e-324) = -1023
```

## `ieeeRemainder`

IEEE remainder using the nearest ties-to-even quotient, distinct from percent remainder.

Arguments: 2.

```text
ieeeRemainder(7,4) = -1
ieeeRemainder(6,4) = -2
ieeeRemainder(-7,4) = 1
```

Rejected examples: `ieeeRemainder(1,0)`.

## `roundToMultiple`

Round to a positive decimal step with ties to even using decimal arithmetic.

Arguments: 2.

```text
roundToMultiple(1.25,.5) = 1
roundToMultiple(1.75,.5) = 2
roundToMultiple(-1.25,.5) = -1
roundToMultiple(1e300,1e-300) = 1e+300
```

Rejected examples: `roundToMultiple(1,0)`, `roundToMultiple(1,-1)`.

## `quantile`

Type-7 linearly interpolated quantile: probability first, then one or more observations.

Arguments: 2..256.

```text
quantile(.5,9,1,4,2) = 3
quantile(.25,0,10,20,30) = 7.5
quantile(0,3,1,2) = 1
quantile(1,3,1,2) = 3
quantile(.7,5) = 5
```

Rejected examples: `quantile(-.1,1,2)`, `quantile(1.1,1,2)`.

## `rangeSpread`

Maximum observation minus minimum observation.

Arguments: 1..256.

```text
rangeSpread(1,8,-2) = 10
rangeSpread(4) = 0
rangeSpread(2,2) = 0
```

Rejected examples: `rangeSpread(-1e308,1e308)`.

## `rms`

Root-mean-square with scaling to avoid overflow from squaring large observations.

Arguments: 1..256.

```text
rms(3,4) = 3.5355339059327378
rms(-2,2) = 2
rms(0,0) = 0
rms(1e308,1e308) = 1e+308
```

## `geometricMean`

Geometric mean of strictly positive observations, calculated in log space.

Arguments: 1..256.

```text
geometricMean(1,4) = 2
geometricMean(2,8) = 4
geometricMean(1e200,1e200) = 1e+200
geometricMean(1) = 1
```

Rejected examples: `geometricMean(0,1)`, `geometricMean(-1,2)`.

## `harmonicMean`

Harmonic mean of positive observations with reciprocal scaling.

Arguments: 1..256.

```text
harmonicMean(1,2) = 1.3333333333333333
harmonicMean(2,2) = 2
harmonicMean(1e-300,1e-300) = 1e-300
```

Rejected examples: `harmonicMean(0,1)`, `harmonicMean(-1,2)`.

## `meanAbsoluteDeviation`

Mean absolute deviation around the arithmetic mean.

Arguments: 1..256.

```text
meanAbsoluteDeviation(1,2,3) = 0.6666666666666666
meanAbsoluteDeviation(2,2) = 0
meanAbsoluteDeviation(-1e308,1e308) = 1e+308
```

## `medianAbsoluteDeviation`

Median absolute deviation around the median, without normal-distribution scaling.

Arguments: 1..256.

```text
medianAbsoluteDeviation(1,2,3) = 1
medianAbsoluteDeviation(1,1,2,2) = 0.5
medianAbsoluteDeviation(9) = 0
```

## `sampleStddev`

Sample standard deviation with scaled observations and n-1 denominator.

Arguments: 2..256.

```text
sampleStddev(2,4,6) = 2
sampleStddev(1e200,1e200) = 0
sampleStddev(-1e200,1e200) = 1.414213562373095e+200
sampleStddev(0,0) = 0
```

## `coefficientVariation`

Population coefficient of variation for strictly positive observations.

Arguments: 1..256.

```text
coefficientVariation(1,3) = 0.5
coefficientVariation(2,2) = 0
coefficientVariation(1e300,1e300) = 0
```

Rejected examples: `coefficientVariation(0,1)`, `coefficientVariation(-1,1)`.

## `sumSquares`

Sum of squares calculated with a stable Euclidean norm.

Arguments: 1..256.

```text
sumSquares(3,4) = 25
sumSquares(-2,2) = 8
sumSquares(0) = 0
```

Rejected examples: `sumSquares(1e200)`.

## `product`

Product accumulated in decimal arithmetic to avoid intermediate double overflow or underflow.

Arguments: 1..256.

```text
product(2,3,4) = 24
product(-2,3) = -6
product(1e300,1e300,1e-300) = 1e+300
product(1e-300,1e-300,1e300) = 1e-300
product(1e300,1e300,0) = 0
```

Rejected examples: `product(1e300,1e300)`.

## `weightedMean`

Weighted mean from value,weight pairs; weights are nonnegative with at least one positive weight.

Arguments: 2..256.

```text
weightedMean(10,1,20,3) = 17.5
weightedMean(5,0,10,1) = 10
weightedMean(1e308,1e308,1e308,1e308) = 1e+308
weightedMean(0,1) = 0
```

Rejected examples: `weightedMean(1,0)`, `weightedMean(1,-1)`, `weightedMean(1,1,2)`.

## `dot`

Dot product from interleaved x,y pairs, using decimal products and accumulation.

Arguments: 2..256.

```text
dot(1,3,2,4) = 11
dot(1e300,1e300,-1e300,1e300) = 0
dot(2,-3) = -6
```

Rejected examples: `dot(1,2,3)`.

## `distance2d`

Euclidean distance between two planar points (x1,y1,x2,y2).

Arguments: 4.

```text
distance2d(0,0,3,4) = 5
distance2d(-1,-1,-1,-1) = 0
distance2d(0,0,1e300,0) = 1e+300
```

## `distance3d`

Euclidean distance between two spatial points (x1,y1,z1,x2,y2,z2).

Arguments: 6.

```text
distance3d(0,0,0,2,3,6) = 7
distance3d(1,2,3,1,2,3) = 0
distance3d(0,0,0,1e300,0,0) = 1e+300
```

## `circleArea`

Area of a circle with a nonnegative radius.

Arguments: 1.

```text
circleArea(1) = 3.141592653589793
circleArea(0) = 0
circleArea(2) = 12.566370614359172
```

Rejected examples: `circleArea(-1)`.

## `circleCircumference`

Circumference of a circle with a nonnegative radius.

Arguments: 1.

```text
circleCircumference(1) = 6.283185307179586
circleCircumference(0) = 0
circleCircumference(2) = 12.566370614359172
```

Rejected examples: `circleCircumference(-1)`.

