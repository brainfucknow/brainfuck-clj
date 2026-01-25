# Esoteric Operators Demo

This demonstrates the unconventional operators in Clojure-inspired Brainfuck.

## Esoteric Operators

- **`@`** (Tape Echo): Duplicates current cell value to next cell
- **`~`** (Invert): Negates current cell value (two's complement)
- **`$`** (Swap): Swaps current cell with next cell
- **`&`** (Mirror): Copies next cell value to current cell
- **`#`** (Quantum): XORs current cell with next cell
- **`|`** (Pipe): Adds next cell to current, zeros next cell

## Example Programs

### Example 1: Tape Echo (@) - Duplicate values across cells
```brainfuck
+++++@>@>@
```
**Result:** `[5, 5, 5, 5]` - Value 5 is duplicated across cells

### Example 2: Swap ($) - Shuffle cell values
```brainfuck
++>+++<$
```
**Result:** `[3, 2]` - Cells 0 and 1 swap their values

### Example 3: Mirror (&) and Invert (~) - Create opposites
```brainfuck
>+++++<&>~
```
**Result:** `[5, -5]` - Cell 0 mirrors cell 1, then cell 1 is inverted

### Example 4: Quantum (#) - XOR magic
```brainfuck
+++>++<#
```
**Result:** `[1, 2]` - Cell 0 = 3 XOR 2 = 1

### Example 5: Pipe (|) - Accumulate values
```brainfuck
++>+++<|
```
**Result:** `[5, 0]` - Cell 1 (3) pipes into cell 0 (2), giving 5, cell 1 becomes 0

### Example 6: Complex combination - Functions with esoteric ops
```brainfuck
(_2 @)(_+ |)++>(_2)<(_+)>@
```
**Result:** `[2, 4, 4]` - Combines function definitions with esoteric operators

## Running the Examples

The file `brainfuck-samples/esoteric.bf` contains executable code only. To run individual examples, copy the code snippets above.
