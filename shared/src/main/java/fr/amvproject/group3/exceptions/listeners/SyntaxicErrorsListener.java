package fr.amvproject.group3.exceptions.listeners;


import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;
import java.util.ArrayList;
import java.util.List;

public class SyntaxicErrorsListener extends BaseErrorListener {
    private static class SyntaxError {
        public final int line;
        public final int charPositionInLine;
        public final String msg;
        public final String offending;

        public SyntaxError(int line, int charPositionInLine, String msg, String offending) {
            this.line = line;
            this.charPositionInLine = charPositionInLine;
            this.msg = msg;
            this.offending = offending;
        }

        @Override public String toString() {
            String token = offending == null ? "" : (" offending=" + offending);
            return "line [" + line + ":" + charPositionInLine + " ] " + msg + token;
        }
    }

    private final List<SyntaxError> errors = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) {
        String off = (offendingSymbol == null) ? null : offendingSymbol.toString();
        errors.add(new SyntaxError(line, charPositionInLine, msg, off));
        // IMPORTANT : ne pas lancer d'exception ici -> on collecte juste
    }

    public boolean hasErrors() { return !errors.isEmpty(); }
    public List<SyntaxError> getErrors() { return List.copyOf(errors); }

    public void printAll() {
        for (SyntaxError se : errors) System.err.println(se);
    }

    public String toMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parsing resulted in ").append(errors.size()).append(" errors.\n");
        for (SyntaxError se : errors) {
            sb.append(se.toString()).append("\n");
        }
        return sb.toString();
    }
}
