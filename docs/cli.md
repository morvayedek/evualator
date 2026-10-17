# Command-line interface

## accept CLI variable bindings

Pass `--var x=3` before an expression such as `x*2`. Repeat the option for multiple bindings. Names must be identifiers and values must be finite numeric literals.

## evaluate UTF-8 expression files

Use `--file expressions.txt` to evaluate one expression per UTF-8 line. Blank lines are ignored. An invalid line is reported without skipping later lines; any expression failure makes the final exit status 1.

