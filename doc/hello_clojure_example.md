# Clojure-inspired Brainfuck: Move Value Demo

This program demonstrates using do-while loops to move values between cells.

## Code Explanation

The program in `brainfuck-samples/hello_clojure.bf` contains:

```brainfuck
+++++{>+<-}
```

### Step by Step:

1. **Set cell 0 to 5**: `+++++`
   - Increments cell 0 five times

2. **Move value from cell 0 to cell 1 using do-while**: `{>+<-}`
   - `>` - Move to cell 1
   - `+` - Increment cell 1
   - `<` - Move back to cell 0
   - `-` - Decrement cell 0
   - This repeats while cell 0 is non-zero (5 times total)

### Expected Result:
- Cell 0 = 0
- Cell 1 = 5

The value has been successfully moved from cell 0 to cell 1.
