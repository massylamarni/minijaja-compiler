package fr.amvproject.group3.interpreter;

/**
 * OutputManager handles output operations (write, writeln) for the JajaCode interpreter.
 */
public class OutputManager {
    private final StringBuilder output = new StringBuilder();

    /**
     * Write a value to output: [write] : <<w, v, cst,*>.m,a> ⊢ write –» <Afficher(v,m),a+1>
     */
    public void write(Object value) {
        if (value != null) {
            output.append(value);
        }
    }

    /**
     * Write a value with newline: [writeln] : <<w, v, cst,*>.m,a> ⊢ writeln –» <AfficherLn(v,m),a+1>
     */
    public void writeln(Object value) {
        if (value != null) {
            output.append(value);
        }
        output.append("\n");
    }

    /**
     * Get all output as string
     */
    public String getOutput() {
        return output.toString();
    }

    /**
     * Clear output buffer
     */
    public void clear() {
        output.setLength(0);
    }

    @Override
    public String toString() {
        return output.toString();
    }
}
