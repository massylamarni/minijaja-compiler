package fr.amvproject.group3.exceptions.listeners;


import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;
import java.util.ArrayList;
import java.util.List;

public class LexicalErrorsListener extends BaseErrorListener {
    private static class LexicalError {
        public final int line;
        public final int charPositionInLine;
        public final String msg;
        public final String offending;

        public LexicalError(int line, int charPositionInLine, String msg, String offending) {
            this.line = line;
            this.charPositionInLine = charPositionInLine;
            this.msg = msg;
            this.offending = offending;
        }

        @Override public String toString() {
            String token = offending == null ? "" : (" offending=" + offending);
            return "[" + line + ":" + charPositionInLine + " ] " + msg + token;
        }
    }

    private final List<LexicalError> errors = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) {
        String off = (offendingSymbol == null) ? null : offendingSymbol.toString();
        errors.add(new LexicalError(line, charPositionInLine, msg, off));
    }

    public boolean hasErrors() { return !errors.isEmpty(); }
    public List<LexicalError> getErrors() { return List.copyOf(errors); }

    public String toMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Lexing resulted in ").append(errors.size()).append(" errors.\n");
        for (LexicalError se : errors) {
            sb.append(se.toString()).append("\n");
        }
        return sb.toString();
    }

    public void reset() {
        errors.clear();
    }
}
