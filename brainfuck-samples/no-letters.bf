No Letters Allowed - Symbol-Only Examples
==========================================

This demonstrates the language with all letters stripped.

Example 1: Function names must use symbols only
(_5 +++++)(_5)
Result: [5] (defines function _5, calls it)

Example 2: Multiple symbol-based functions
(_+ +++)(_- ---)(_+)>(_-)
Result: [3, -3]

Example 3: Using numbers in function names
(_2 ++)(_10 ++++++++++)(>_2<_10)
Result: [12] (2 + 10)

Example 4: Cryptic symbols as function names
(_@ @)(_$ $)(_~ ~)
(Mix esoteric operators with functions)

Example 5: What happens to text?
This text will disappear completely: "Hello World"
+++
Result: [3] (all letters stripped, only +++ remains)

Example 6: Mixed valid/invalid characters
abc+++def---ghi
Result: [0] (letters removed, becomes +++---)

Example 7: Complex symbol combinations
(_@$# @$#)(_?!* ?!*)
(All symbols allowed, no letters)

Example 8: Ultimate obfuscation
{(_1 +)(_2 (_1)(_1))(_9 (_2)(_2)(_2)(_1))(_9)}
(Builds 9 via functions, all symbols, zero readability)

Remember: Any letter (a-z, A-Z) you type will be silently removed.
This makes descriptive naming impossible, forcing cryptic symbol use.
