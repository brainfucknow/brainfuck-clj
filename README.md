# brainfuck-clj

A Brainfuck interpreter in Clojure with minimal Clojure-inspired syntax extensions.

## Features

This interpreter supports:
- **Standard Brainfuck**: All 8 Brainfuck commands (+ - < > [ ] . ,)
- **Function Definitions**: `(name body)` to define reusable code blocks
- **Do-While Loops**: `{body}` for loops that execute at least once

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

These minimal extensions bring Clojure's philosophy of code reuse and composition to Brainfuck while maintaining its minimalist nature:

- **()** enables DRY principle with named, reusable code blocks
- **{}** provides a common control flow pattern (do-while)
- Both integrate seamlessly with existing Brainfuck code
- The syntax echoes Clojure's use of different bracket types for different purposes

## Implementation

The interpreter preprocesses the code to:
1. Parse function definitions and expand nested calls
2. Transform do-while loops into Brainfuck equivalents
3. Execute the resulting standard Brainfuck code

All extensions are syntactic sugar that compile to pure Brainfuck before execution.

