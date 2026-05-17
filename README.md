# minijaja-compiler

IDE for the MiniJaja language - a statically typed, Java-like teaching language. Features a full compilation chain to JajaCode (a custom bytecode), an interpreter, and a step-by-step debugger with execution and memory inspection. Built with Java, JavaFX and ANTLR4.

---

## Language overview

**MiniJaja** is a simplified, Java-inspired language supporting:
- Primitive types: `int`, `boolean`
- Variables, constants (`final`), and arrays
- Methods with parameters and return values
- Control flow: `if / else`, `while`
- Expressions: arithmetic, logical, comparison, unary operators
- I/O: `write`, `writeln`
- Single-class programs with a `main` block

**JajaCode** is the stack-based bytecode MiniJaja compiles to. Instructions include:
`new`, `newarray`, `load/store`, `aload/astore`, `push/pop`, `invoke`, `return`, `goto`, `if`, `inc`, `write/writeln`, and standard arithmetic/logic operators.

### Example MiniJaja program

```java
class Factorial {
    int factorial(int n) {
        if (n == 0) {
            return 1;
        } else {
            return n * factorial(n - 1);
        };
    };
    main {
        int result = factorial(5);
        write(result);
    }
}
```


### Compiled to JajaCode:

```nasm
1 init
2 push(5)
3 new(factorial, entier, meth, 0)
4 goto(22)
5 new(n, entier, var, 1)
6 load(n)
7 push(0)
8 cmp
9 if(19)
10 load(n)
11 load(n)
12 push(1)
13 sub
14 invoke(factorial)
15 swap
16 pop
17 mul
18 goto(20)
19 push(1)
20 swap
21 return
22 push(5)
23 invoke(factorial)
24 swap
25 pop
26 new(result, entier, var, 0)
27 load(result)
28 write
29 push(0)
30 swap
31 pop
32 swap
33 pop
34 pop
35 jcstop
```

---

## Architecture

The project is organized as a Maven multi-module build:

| Module | Role |
|---|---|
| `minijaja-core` | ANTLR4 lexer/parser for MiniJaja |
| `minijaja-compiler` | Compiles MiniJaja to JajaCode |
| `minijaja-interpreter` | Directly interprets MiniJaja AST |
| `jajacode-core` | ANTLR4 lexer/parser for JajaCode |
| `jajacode-interpreter` | Executes JajaCode bytecode |
| `memory-core` | Memory model (stack, heap, symbol table) |
| `gui` | JavaFX IDE interface |
| `interface` | Shared interfaces between modules |
| `shared` | Common utilities |

---

## Features

- **Code editor** - Write MiniJaja/JajaCode programs directly in the GUI
- **Compilation** - Translates MiniJaja to JajaCode bytecode
- **Interpreter** - Run MiniJaja programs without compilation or JajaCode bytecode
- **Step-by-step debugger** - Step through/back MiniJaja/JajaCode instructions one at a time
- **Memory inspector** - Inspect the stack and heap state at each step
- **Output console** - Displays `write`/`writeln` output

---

## Getting started

### Prerequisites

- Java 11+
- Maven 3.6+

### Build

```bash
mvn clean install
```

### Run

```bash
bash settings.sh
```

Or launch the main JavaFX application class from your IDE.
