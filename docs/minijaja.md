# Compilation scheme: MiniJaja -> JajaCode

The compiler turns a MiniJaja abstract syntax tree into a JajaCode program, preserving the meaning of the source. This page describes the scheme in plain terms. The instructions themselves are documented in [jajacode.md](jajacode.md).

## How the scheme works

Each AST node is compiled to a **list of JajaCode instructions**. The code for a node is built by concatenating the code of its children with a few instructions of its own.

Jumps use absolute addresses, so every rule also tracks **how many instructions precede it**. Each compilation step receives that count and returns the code together with its own length. This makes it possible to compute jump targets in a single pass, without backpatching.

## Program structure

A class compiles to:

```text
init
<class-level declarations>
<main>
<removal of the class-level declarations>
pop
jcstop
```

`main` compiles to its local declarations, then its instructions, then `push(0)`, then the removal of its local declarations.

**Removal.** Every declaration pushed on the stack must be removed when its scope ends. Removing one declaration costs two instructions, `swap` then `pop`, which delete the entry under the value on top of the stack. This is why programs end with a run of `swap`/`pop` pairs and a final `pop` for main's dummy `0`.

## Declarations

| MiniJaja           | JajaCode                                                                                |
| ------------------ | --------------------------------------------------------------------------------------- |
| `int x = e;`       | code of `e`, then `new(x, entier, var, 0)`                                              |
| `final int x = e;` | code of `e`, then `new(x, entier, cst, 0)`                                              |
| `int t[e];`        | code of `e` (the size), then `newarray(t, entier)`                                      |
| parameter `p`      | `new(p, type, var, k)`, where `k` is the depth of the argument below the return address |

### Methods

```text
push(<entry point>)         // address of the first instruction of the body
new(f, type, meth, 0)
goto(<after the method>)    // skip the body when the class is initialised
<parameters>                // one new(p, type, var, k) per parameter
<local declarations>
<instructions>
[push(0)]                   // dummy result, for void methods
<removal of local declarations>
swap
return
```

Because `return e` compiles to just the code of `e` (see below), the result is already on top when the epilogue runs. A `return` therefore has to be the last thing executed in the method, as in the factorial example where both branches of the `if/else` reach the epilogue.

## Instructions

| MiniJaja      | JajaCode                              |
| ------------- | ------------------------------------- |
| `x = e;`      | `e`, then `store(x)`                  |
| `x += e;`     | `e`, then `inc(x)`                    |
| `x++;`        | `push(1)`, then `inc(x)`              |
| `t[i] = e;`   | `i`, `e`, then `astore(t)`            |
| `t[i] += e;`  | `i`, `e`, then `ainc(t)`              |
| `t[i]++;`     | `i`, `push(1)`, then `ainc(t)`        |
| `write(e);`   | `e`, then `write`                     |
| `writeln(e);` | `e`, then `writeln`                   |
| `return e;`   | `e` only. The epilogue does the rest. |

### `if / else`

```text
<condition>
if(Lthen)
<else-branch>
goto(Lend)
Lthen: <then-branch>
Lend:
```

The condition is evaluated and `if` jumps to the then-branch when it is `true`. The else-branch is placed first so execution falls into it otherwise, and then skips the then-branch with `goto`.

### `while`

```text
Lstart: <condition>
not
if(Lend)
<body>
goto(Lstart)
Lend:
```

The condition is negated, so `if` leaves the loop when the original condition is false.

## Expressions

| MiniJaja          | JajaCode                                                                  |
| ----------------- | ------------------------------------------------------------------------- |
| identifier `x`    | `load(x)`                                                                 |
| `t[e]`            | code of `e`, then `aload(t)`                                              |
| integer `n`       | `push(n)`                                                                 |
| `true` / `false`  | `push(true)` / `push(false)`                                              |
| string `"s"`      | `push("s")`                                                               |
| unary `op e`      | code of `e`, then the operator                                            |
| binary `e1 op e2` | code of `e1`, code of `e2`, then the operator                             |
| `f(a1, a2)`       | code of each argument, `invoke(f)`, then `swap`, `pop` for each parameter |
| `t.length`        | `length(t)`                                                               |

**Operator mapping**

| MiniJaja        | JajaCode                |
| --------------- | ----------------------- |
| `+` `-` `*` `/` | `add` `sub` `mul` `div` |
| `==`            | `cmp`                   |
| `>`             | `sup`                   |
| `&&` `\|\|`     | `and` `or`              |
| unary `-`       | `neg`                   |
| `!`             | `not`                   |
