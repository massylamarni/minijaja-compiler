package fr.amvproject.group3.interpreter;

public class OutputManager {
    private final StringBuilder output = new StringBuilder();

    // [write] : <<w, v, cst,*>.m,a> ⊢ write –» <Afficher(v,m),a+1>
    public void write(Object value) {
        if (value != null) {
            output.append(value);
        }
    }

    // [writeln] : <<w, v, cst,*>.m,a> ⊢ writeln –» <AfficherLn(v,m),a+1>
    public void writeln(Object value) {
        if (value != null) {
            output.append(value);
        }
        output.append("\n");
    }

    public String getOutput() {
        return output.toString();
    }

    public void clear() {
        output.setLength(0);
    }

    @Override
    public String toString() {
        return output.toString();
    }
}
