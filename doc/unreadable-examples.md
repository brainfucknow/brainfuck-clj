# Cryptic Unreadable Code Examples

These examples demonstrate maximally confusing code patterns using cryptic operators.

## Cryptic Operators

- **`%`** (Even/Odd): Increments if cell is even, decrements if odd
- **`?`** (Conditional Move): Moves pointer right if >0, left if <0, stays if 0
- **`!`** (Invisible Mutation): Silently increments cell at current-2
- **`*`** (Square): Multiplies current cell by itself
- **`:`** (Rotate): Shifts all cell values right with wrap
- **`^`** (Elevate): Moves pointer to first cell with matching value
- **`;`** (Ghost Write): Increments current cell AND pseudo-random distant cell

## Example Programs

### Example 1: What does this do?
```brainfuck
++%?!*:^;
```
**Mystery:** No one knows without executing it - context-dependent chaos

### Example 2: Context-dependent chaos
```brainfuck
+++>++<?%!*
```
**Mystery:** Behavior depends on runtime cell values

### Example 3: Invisible side effects
```brainfuck
>>>>!!!!<<<<++
```
**Mystery:** Something happened at pos 0, but you can't see it in the code

### Example 4: Ghost writes everywhere
```brainfuck
++>;++>;++>;
```
**Mystery:** Values appearing in unexpected places due to ghost writes

### Example 5: Pointer teleportation
```brainfuck
++>+++>++<<^?!
```
**Mystery:** Where is the pointer now? Good luck figuring it out

### Example 6: Combined confusion with functions
```brainfuck
(_1 !%?)(_2 :^;)(_1)(_2)++@$~#|*
```
**Mystery:** Each symbol does something different, side effects everywhere

### Example 7: The ultimate obfuscation
```brainfuck
{++?!}[%*:^];@$&#|~
```
**Mystery:** Every possible confusing operator in one line

## Why These Are Unreadable

These patterns are intentionally unreadable. The more operators you combine:
- The harder it is to predict what will happen
- Side effects occur in cells you're not looking at
- Pointer movements are conditional and non-obvious
- Values appear and disappear mysteriously

**This is Brainfuck made deliberately worse.**

## Running the Examples

The file `brainfuck-samples/unreadable.bf` contains executable code only. To run individual examples, copy the code snippets above.
