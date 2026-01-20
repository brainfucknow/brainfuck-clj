Clojure-Inspired Brainfuck Examples
=====================================

This file contains examples of the minimal Clojure-inspired extensions to Brainfuck.

## Syntax Extensions

### 1. Function Definitions: ()

Define reusable code blocks:
```
(name body)
```

Call a function:
```
(name)
```

### 2. Do-While Loops: {}

Execute body at least once, then loop while cell is non-zero:
```
{body}
```

Translates to: `body[body]`

## Examples

### Example 1: Simple Function
```
(inc5 +++++)(inc5)
```
Defines `inc5` as `+++++`, then calls it. Result: cell 0 = 5

### Example 2: Multiple Functions
```
(inc3 +++)(inc2 ++)(inc3)(inc2)(inc3)
```
Defines two functions and calls them. Result: cell 0 = 8

### Example 3: Function Composition
```
(add2 ++)(add4 ++(add2))(add4)
```
`add4` is defined as `++(add2)`, which expands to `++++`.
Result: cell 0 = 4

### Example 4: Do-While Loop
```
+++++{-}
```
Starts at 5, then decrements until zero. The body `-` executes 5 times.

### Example 5: Do-While with Move
```
+++{>+<-}
```
Moves value from cell 0 to cell 1. Result: [0, 3]

### Example 6: Combined Features
```
(set3 +++)(set3)>(set3){>+<-}
```
Uses functions and do-while to set cell 0 to 3, move right, set cell 1 to 3,
then move cell 1's value to cell 2. Result: [3, 0, 3]

### Example 7: Hello World (traditional BF style)
```
+++++ +++++             Initialize cell 0 to 10
[                       Loop to set up cells
    > +++++ ++          Add 7 to cell 1 (70)
    > +++++ +++++       Add 10 to cell 2 (100)  
    > +++               Add 3 to cell 3 (30)
    > +                 Add 1 to cell 4 (10)
    <<<< -              Decrement cell 0
]
> ++ .                  Cell 1 = 72 ('H')
> + .                   Cell 2 = 101 ('e')
+++++ ++ .              Cell 2 = 108 ('l')
.                       Cell 2 = 108 ('l')
+++ .                   Cell 2 = 111 ('o')
```

### Example 8: Hello World with Functions
```
(H +++++ +++++[> +++++ ++ < -]>++)
(e +)
(l +++++ ++)
(l)
(o +++)
(space ---- ----)
(W ----- ---)
(o +++ +++)
(r +++)
(l +++)
(d --- ---)
(exclaim ----- ----- -----)

(H).
(e).
(l).
(l).
(o).
(space).
(W).
(o).
(r).
(l).
(d).
(exclaim).
```
This demonstrates function reuse for printing specific ASCII characters.

### Example 9: Do-While for Conditional Execution
```
+++{                    If cell is 3 (non-zero), execute at least once
  >+                    Move right and increment
  <-                    Move back and decrement
}
```
Executes the body 3 times, moving value to next cell.

### Example 10: Fibonacci-like Sequence Start
```
(init >+++<++)          Set cell 1=3, cell 0=2
(swap [>+>+<<-]>>[<<+>>-]<<)  Standard BF swap pattern

(init)
>{                      Move to cell 1, do-while to process
  .                     Output current value
  (swap)                Swap cells
  -                     Decrement for loop control
}
```

## Notes

- Functions are expanded before execution
- Nested function calls in definitions are supported
- Do-while loops execute body at least once
- All standard Brainfuck operations still work
- Comments can be added as any non-BF characters
