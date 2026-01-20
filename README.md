# brainfuck-clj

A Brainfuck interpreter in Clojure with minimal Clojure-inspired syntax extensions.

## Features

This interpreter supports:
- **Standard Brainfuck**: All 8 Brainfuck commands (+ - < > [ ] . ,)
- **Function Definitions**: `(name body)` to define reusable code blocks
- **Do-While Loops**: `{body}` for loops that execute at least once
- **Esoteric Operators**: 6 unconventional operators for weird cell manipulations

## Usage

```
lein run my_awesome_brainfuck_program.b
```

You can find a few samples in the directory brainfuck-samples, e.g. to get all square numbers from 0 to 10000 type:

```
lein run brainfuck-samples/squares.b 
```

## Clojure-Inspired Extensions

### Function Definitions with ()

Define reusable code blocks using parentheses:

```brainfuck
(inc5 +++++)(inc5)
```

This defines a function `inc5` that increments a cell by 5, then calls it.

Functions can reference other functions:

```brainfuck
(add2 ++)(add4 ++(add2))(add4)
```

### Do-While Loops with {}

Execute code at least once, then loop while the current cell is non-zero:

```brainfuck
+++{-}
```

This increments to 3, then decrements in a loop until zero (executes 3 times total).

```brainfuck
++{>+<-}
```

This moves a value from one cell to another using a do-while pattern.

### Esoteric Operators

Six unconventional operators for weird cell manipulations:

**`@` - Tape Echo**: Duplicates current cell value to the next cell
```brainfuck
+++@
```
Result: [3, 3] - Current cell is copied to next

**`~` - Invert**: Negates the current cell value (two's complement)
```brainfuck
+++~
```
Result: [-3] - Cell value becomes negative

**`$` - Swap**: Swaps current cell with the next cell
```brainfuck
++>+++<$
```
Result: [3, 2] - Cells are swapped

**`&` - Mirror**: Copies next cell value to current cell
```brainfuck
++>+++<&
```
Result: [3, 3] - Next cell is copied to current

**`#` - Quantum**: XORs current cell with next cell, stores in current
```brainfuck
++>+++<#
```
Result: [1, 3] - 2 XOR 3 = 1

**`|` - Pipe**: Adds next cell to current, zeros next cell
```brainfuck
++>+++<|
```
Result: [5, 0] - Next cell value "pipes" into current

These operators enable non-obvious and esoteric programming patterns that would be verbose in standard Brainfuck.

## Examples

See [doc/clojure-inspired-examples.md](doc/clojure-inspired-examples.md) for comprehensive examples.

### Quick Examples

**Define and use a function:**
```brainfuck
(set10 ++++++++++)(set10)>(set10)
```
Result: Two cells set to 10

**Do-while to move values:**
```brainfuck
+++++{>+<-}
```
Result: Moves 5 from cell 0 to cell 1: [0, 5]

**Combined features:**
```brainfuck
(inc +++)(inc)(inc)>(inc)
```
Result: [6, 3]

## Why These Extensions?

These extensions bring Clojure's philosophy of code reuse and unconventional thinking to Brainfuck:

- **()** enables DRY principle with named, reusable code blocks
- **{}** provides a common control flow pattern (do-while)
- **Esoteric operators** (@~$&#|) add cryptic, unconventional cell manipulations
- All integrate seamlessly with existing Brainfuck code
- The syntax echoes Clojure's use of different symbols for different purposes
- Makes the language weirder and more esoteric while remaining minimal

## Implementation

The interpreter preprocesses the code to:
1. Parse function definitions and expand nested calls
2. Transform do-while loops into Brainfuck equivalents
3. Execute the resulting standard Brainfuck code

All extensions are syntactic sugar that compile to pure Brainfuck before execution.

