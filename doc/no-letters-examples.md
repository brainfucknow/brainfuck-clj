# No Letters Allowed - Symbol-Only Examples

This demonstrates the language with all letters stripped from input.

## Example Programs

### Example 1: Function names must use symbols only
```brainfuck
(_5 +++++)(_5)
```
**Result:** `[5]` - Defines function `_5` as five increments, then calls it

### Example 2: Multiple symbol-based functions
```brainfuck
(_+ +++)(_- ---)(_+)>(_-)
```
**Result:** `[3, -3]` - Two functions with different operations

### Example 3: Using numbers in function names
```brainfuck
(_2 ++)(_10 ++++++++++)(>_2<_10)
```
**Result:** `[12]` - Adds 2 + 10 using function calls

### Example 4: Cryptic symbols as function names
```brainfuck
(_@ @)(_$ $)(_~ ~)
```
Mixes esoteric operators with function definitions

### Example 5: What happens to text?
```brainfuck
+++
```
Input with letters: `"This text will disappear completely: 'Hello World' +++"` 
After stripping: `"+++"`
**Result:** `[3]` - All letters stripped, only `+++` remains

### Example 6: Mixed valid/invalid characters
```brainfuck
+++---
```
Input: `"abc+++def---ghi"`
After stripping: `"+++---"`
**Result:** `[0]` - Letters removed, becomes `+++---`

### Example 7: Complex symbol combinations
```brainfuck
(_@$# @$#)(_?!* ?!*)
```
All symbols allowed, no letters

### Example 8: Ultimate obfuscation
```brainfuck
{(_1 +)(_2 (_1)(_1))(_9 (_2)(_2)(_2)(_1))(_9)}
```
Builds 9 via nested function composition - all symbols, zero readability

## Key Point

**Remember:** Any letter (a-z, A-Z) you type will be silently removed.
This makes descriptive naming impossible, forcing cryptic symbol use.

## Running the Examples

The file `brainfuck-samples/no-letters.bf` contains executable code only. To run individual examples, copy the code snippets above.
