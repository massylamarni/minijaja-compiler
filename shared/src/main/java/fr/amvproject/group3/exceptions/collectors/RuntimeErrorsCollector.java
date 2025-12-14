package fr.amvproject.group3.exceptions.collectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.amvproject.group3.exceptions.RuntimeException;

public class RuntimeErrorsCollector {
    private final List<RuntimeException> runtimeExceptions = new ArrayList<>();

    public List<RuntimeException> getRuntimeExceptions() {
      return runtimeExceptions;
    }

    public synchronized void add(String message, int line, int column) {
        runtimeExceptions.add(new RuntimeException(message, line, column));
    }

    public synchronized boolean hasErrors() {
        return !runtimeExceptions.isEmpty();
    }

    public synchronized List<RuntimeException> getErrors() {
        return Collections.unmodifiableList(runtimeExceptions);
    }

    public synchronized void printAll() {
        if (runtimeExceptions.isEmpty()) {
            System.out.println("No runtime exceptions.");
            return;
        }
        System.err.println("Runtime exceptions (" + runtimeExceptions.size() + ")");
        for (RuntimeException e : runtimeExceptions) System.err.println("["+e.getLine()+":"+e.getColumn()+"]"+e.getMessage() );
    }

    public void reset() {
        runtimeExceptions.clear();
    }
}


