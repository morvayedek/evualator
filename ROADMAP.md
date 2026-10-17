# Evaluator development increments

Each increment has its own implementation or reviewable verification purpose.
Entries describe planned scope; a PR being listed does not mean it is merged.

1. feat: introduce arithmetic evaluator and runnable JAR
2. feat: parse scientific notation with explicit exponent errors
3. feat: add right-associative exponentiation
4. feat: add remainder operator with zero checks
5. feat: support isolated named variable bindings
6. feat: expose mathematical pi e and tau constants
7. feat: add validated function calls and custom function API
8. feat: add absolute value and sign functions
9. feat: add square and cube root functions
10. feat: add natural common and binary logarithms
11. feat: add exponential and power functions
12. feat: add radian trigonometric functions
13. feat: add inverse trigonometric functions
14. feat: add hyperbolic functions
15. feat: add angle conversion functions
16. feat: add rounding with explicit tie semantics
17. feat: add variadic minimum and maximum
18. feat: add compensated sum and mean functions
19. feat: add median without mutating caller arguments
20. feat: add population and sample statistics
21. feat: add bounded factorial and permutation functions
22. feat: add binomial coefficients
23. feat: add integer gcd and overflow-checked lcm
24. feat: add exact-range bitwise integer functions
25. feat: add bounded integer shifts
26. feat: add validated range clamping
27. feat: add linear interpolation functions
28. feat: add overflow-resistant Euclidean norms
29. feat: add positive-modulus normalization
30. feat: expose floating-point spacing helpers
31. feat: add percentage and relative-change calculations
32. feat: add degree-based trigonometric functions
33. feat: add numerically stable small-argument transforms
34. feat: add decimal-place rounding
35. feat: accept CLI variable bindings
36. feat: evaluate UTF-8 expression files
37. feat: add interactive expression REPL
38. feat: retain the last successful REPL result
39. feat: add machine-readable JSON output
40. feat: add CLI help and option validation
41. feat: evaluate expression streams from standard input
42. feat: add structured batch evaluation API
43. test: verify concurrent evaluator isolation
44. test: verify generated arithmetic invariants
45. test: fuzz malformed expressions with deterministic seeds
46. ci: verify JDK compatibility on Linux Windows and macOS
47. docs: add Java embedding and function extension cookbook
48. docs: specify expression grammar and compatibility contracts
