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

    public String toMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Runtime Exception: ");
        for (RuntimeException se : runtimeExceptions) {
            sb.append(se.toString()).append("\n");
        }
        return sb.toString();
    }

    public void reset() {
        runtimeExceptions.clear();
    }
}


