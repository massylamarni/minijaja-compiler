package fr.amvproject.group3.exceptions.collectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.amvproject.group3.exceptions.SemanticException;

public class SemanticErrorsCollector {
    private final List<SemanticException> semanticExceptions = new ArrayList<>();

    public List<SemanticException> getSemanticExceptions() {
      return semanticExceptions;
    }

    public synchronized void add(String message, int line, int column) {
        semanticExceptions.add(new SemanticException(message, line, column));
    }

    public synchronized boolean hasErrors() {
        return !semanticExceptions.isEmpty();
    }

    public synchronized List<SemanticException> getErrors() {
        return Collections.unmodifiableList(semanticExceptions);
    }

    public String toMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Semantic Exceptions: ");
        for (RuntimeException se : semanticExceptions) {
            sb.append(se.toString()).append("\n");
        }
        return sb.toString();
    }

    public void reset() {
        semanticExceptions.clear();
    }
}


