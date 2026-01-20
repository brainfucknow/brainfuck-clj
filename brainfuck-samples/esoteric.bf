Esoteric Operators Demo
========================

This demonstrates the unconventional operators in Clojure-inspired Brainfuck.

Example 1: Tape Echo (@) - Duplicate values across cells
+++++@>@>@
Result: [5, 5, 5, 5]

Example 2: Swap ($) - Shuffle cell values
++>+++<$
Result: [3, 2]

Example 3: Mirror (&) and Invert (~) - Create opposites
>+++++<&>~
Result: [5, -5]

Example 4: Quantum (#) - XOR magic
+++>++<#
Result: [1, 2]  (3 XOR 2 = 1)

Example 5: Pipe (|) - Accumulate values
++>+++<|
Result: [5, 0]  (2 + 3 = 5, next becomes 0)

Example 6: Complex combination - Functions with esoteric ops
(double @)(add |)
++>(double)<(add)>@
Result: [2, 4, 4]
