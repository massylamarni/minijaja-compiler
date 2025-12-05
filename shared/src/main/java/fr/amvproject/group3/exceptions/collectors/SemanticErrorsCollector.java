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

    public synchronized void printAll() {
        if (semanticExceptions.isEmpty()) {
            System.out.println("No semantic exceptions.");
            return;
        }
        System.err.println("Semantic exceptions (" + semanticExceptions.size() + ")");
        for (SemanticException e : semanticExceptions) System.err.println("["+e.getLine()+":"+e.getColumn()+"]"+e.getMessage() );
    }

    public void reset() {
        semanticExceptions.clear();
    }
}


