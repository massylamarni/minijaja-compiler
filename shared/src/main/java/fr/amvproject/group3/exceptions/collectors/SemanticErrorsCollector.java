package fr.amvproject.group3.exceptions.collectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.amvproject.group3.exceptions.SemanticException;

public class SemanticErrorsCollector {
    private final List<SemanticException> errors = new ArrayList<>();

    public synchronized void add(String message, int line, int column) {
        errors.add(new SemanticException(message, line, column));
    }

    public synchronized boolean hasErrors() {
        return !errors.isEmpty();
    }

    public synchronized List<SemanticException> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public synchronized void printAll() {
        if (errors.isEmpty()) {
            System.out.println("No semantic errors.");
            return;
        }
        System.err.println("=== Semantic errors (" + errors.size() + ") ===");
        for (SemanticException e : errors) System.err.println("["+e.getLine()+":"+e.getColumn()+"]"+e.getMessage() );
    }
}


