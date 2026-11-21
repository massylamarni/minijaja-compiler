# JajaCode instruction set

JajaCode is the stack-based bytecode that MiniJaja programs compile to. It runs on a small virtual machine made of a code memory, a stack and a heap. This page describes the machine and every instruction. For how MiniJaja constructs map onto these instructions, see [minijaja.md](minijaja.md).

## Machine model

- **Code**: a numbered list of instructions, starting at address 1.
- **Stack**: holds the program's variables, constants, method entries, temporary values and return addresses. Each entry is a quadruple `⟨name, value, kind, type⟩`. Values created by `push` are anonymous.
- **Heap**: storage for arrays. A `tab` entry on the stack holds a reference into the heap.
- **Execution state**: `⟨stack, address⟩`. Most instructions move to `address + 1`. `goto`, `if`, `invoke` and `return` change the address, and `jcstop` ends execution.

Entry kinds: `var` (variable), `cst` (constant), `tab` (array), `meth` (method). A fifth kind, `vcst`, is a constant declared without a value, which becomes `cst` on its first assignment.

In the tables below, the **top of the stack is on the left**: `v2 · v1 · rest` means `v2` is on top.

## Instructions

### Machine control

| Instruction | Effect                     |
| ----------- | -------------------------- |
| `init`      | Resets the stack to empty. |
| `nop`       | Does nothing.              |
| `jcstop`    | Stops execution.           |

### Stack manipulation

| Instruction | Effect                                                                    |
| ----------- | ------------------------------------------------------------------------- |
| `push(v)`   | Pushes the anonymous value `v` (integer, `true`/`false`, or string).      |
| `pop`       | Removes the top entry.                                                    |
| `swap`      | Exchanges the two top entries: `q1 · q2 · rest` becomes `q2 · q1 · rest`. |

### Declarations

| Instruction          | Effect                                                                                                                                                                                                                           |
| -------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `new(i, t, var, s)`  | Turns the entry `s` positions below the top into variable `i` of type `t`. `s = 0` names the top value (local variable initialised by an expression). `s ≥ 1` is used for parameters, whose values lie below the return address. |
| `new(i, t, cst, 0)`  | Pops a value and declares it as constant `i`.                                                                                                                                                                                    |
| `new(i, t, meth, 0)` | Pops a code address and declares method `i` with that entry point.                                                                                                                                                               |
| `newarray(i, t)`     | Pops a size, allocates the array in the heap and declares `i`.                                                                                                                                                                   |

### Reading and writing variables

| Instruction | Effect                                                       |
| ----------- | ------------------------------------------------------------ |
| `load(i)`   | Pushes the value of `i`.                                     |
| `store(i)`  | Pops a value and assigns it to `i`.                          |
| `aload(i)`  | Pops an index and pushes `i[index]`.                         |
| `astore(i)` | Pops a value, then an index, and assigns `i[index] = value`. |
| `inc(i)`    | Pops `v` and does `i = i + v`.                               |
| `ainc(i)`   | Pops `v`, then an index, and does `i[index] = i[index] + v`. |
| `length(i)` | Pushes the length of array `i`.                              |

### Control flow

| Instruction | Effect                                                                                  |
| ----------- | --------------------------------------------------------------------------------------- |
| `goto(a)`   | Jumps to address `a`.                                                                   |
| `if(a)`     | Pops a boolean. If `true`, jumps to `a`; if `false`, continues at the next instruction. |

### Operators

Binary operators pop `v2` (top) then `v1`, and push `v1 op v2`. Unary operators pop one value and push the result.

| Instruction                | Result                                      |
| -------------------------- | ------------------------------------------- |
| `add`, `sub`, `mul`, `div` | Arithmetic on integers.                     |
| `cmp`                      | `true` if `v1` equals `v2` (used for `==`). |
| `sup`                      | `v1 > v2`                                   |
| `and`, `or`                | Boolean operators.                          |
| `neg`                      | Arithmetic negation.                        |
| `not`                      | Boolean negation.                           |

### Calls and I/O

| Instruction | Effect                                                                                       |
| ----------- | -------------------------------------------------------------------------------------------- |
| `invoke(i)` | Pushes the return address (the next instruction) and jumps to the entry point of method `i`. |
| `return`    | Pops the return address and jumps to it.                                                     |
| `write`     | Pops a value and prints it.                                                                  |
| `writeln`   | Same as `write`, followed by a newline.                                                      |

## Calling convention

1. The caller evaluates each argument, leaving the values on the stack.
2. `invoke(f)` pushes the return address on top of them and jumps to `f`.
3. At the start of the method, `new(p, t, var, k)` turns each argument value into a named parameter, reaching below the return address.
4. The method body runs. Local declarations are pushed on top. At the end of the body the **result is on top of the stack**.
5. The method removes its local declarations (`swap`, `pop` for each), then does `swap` so the return address is on top, then `return`.
6. Back in the caller, the result is on top and the parameters are still on the stack beneath it. The caller removes each parameter with `swap`, `pop`.

Void methods and `main` push a dummy `0` so that the stack layout is the same as for methods that return a value.

## Annotated example

The program is the factorial example from the [README](../README.md).

```text
 1 init                              // empty stack
 2 push(5)                           // entry point of factorial (address 5)
 3 new(factorial, entier, meth, 0)   // declare the method
 4 goto(22)                          // skip the body, jump to main
 5 new(n, entier, var, 1)            // name the argument below the return address
 6 load(n)
 7 push(0)
 8 cmp                               // n == 0
 9 if(19)                            // true: jump to the then-branch
10 load(n)                           // else: left operand of *
11 load(n)
12 push(1)
13 sub                               // n - 1
14 invoke(factorial)                 // recursive call
15 swap                              // result <-> parameter
16 pop                               // remove the parameter
17 mul                               // n * factorial(n - 1)
18 goto(20)                          // skip the then-branch
19 push(1)                           // then: result is 1
20 swap                              // return address <-> result
21 return
22 push(5)                           // main: argument
23 invoke(factorial)
24 swap                              // remove the parameter
25 pop
26 new(result, entier, var, 0)       // the result becomes variable "result"
27 load(result)
28 write
29 push(0)                           // dummy value returned by main
30 swap                              // remove "result"
31 pop
32 swap                              // remove "factorial"
33 pop
34 pop                               // remove main's dummy value
35 jcstop
```
