package fr.amvproject.group3.exceptions.collectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.amvproject.group3.exceptions.RuntimeException;

public class RuntimeErrorsCollector {
    private final List<RuntimeException> errors = new ArrayList<>();

    public synchronized void add(String message, int line, int column) {
        errors.add(new RuntimeException(message, line, column));
    }

    public synchronized boolean hasErrors() {
        return !errors.isEmpty();
    }

    public synchronized List<RuntimeException> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public synchronized void printAll() {
        if (errors.isEmpty()) {
            System.out.println("No runtime errors.");
            return;
        }
        System.err.println("=== runtime errors (" + errors.size() + ") ===");
        for (RuntimeException e : errors) System.err.println("["+e.getLine()+":"+e.getColumn()+"]"+e.getMessage() );
    }
}


