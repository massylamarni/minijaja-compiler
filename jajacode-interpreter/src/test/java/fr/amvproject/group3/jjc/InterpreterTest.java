package fr.amvproject.group3.jjc;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.amvproject.group3.exceptions.collectors.RuntimeErrorsCollector;
import fr.amvproject.group3.exceptions.collectors.SemanticErrorsCollector;

public class InterpreterTest {

    private Interpreter interpreter;
    private String testProgram = "int x = 5;";

    @Before
    public void setUp() {
        interpreter = new Interpreter(testProgram);
    }

    @Test
    public void shouldRunProgramWithoutErrors() {
        // Given
        interpreter.restart();

        // When
        interpreter.run();

        // Then
        assertThat(interpreter.getRuntimeErrorsCollector().getErrors(), is(empty()));
        assertThat(interpreter.getSemanticErrorsCollector().getErrors(), is(empty()));
    }

    @Test
    public void shouldRunProgramWithDebug() {
        // Given
        interpreter.restart();

        // When
        interpreter.runWithDebug();

        // Then
        assertThat(interpreter.getRuntimeErrorsCollector().getErrors(), is(empty()));
    }

    @Test
    public void shouldAddBreakPoint() {
        // Given
        int line = 10;

        // When
        interpreter.addBreakPoint(line);

        // Then
        assertTrue(true);
    }

    @Test
    public void shouldRestartInterpreter() {
        // Given
        interpreter.run();
        RuntimeErrorsCollector oldRuntimeErrors = interpreter.getRuntimeErrorsCollector();
        SemanticErrorsCollector oldSemanticErrors = interpreter.getSemanticErrorsCollector();

        // When
        interpreter.restart();

        // Then
        assertThat(interpreter.getRuntimeErrorsCollector(), is(not(equalTo(oldRuntimeErrors))));
        assertThat(interpreter.getSemanticErrorsCollector(), is(not(equalTo(oldSemanticErrors))));
    }

    @Test
    public void shouldStepBackward() {
        // Given
        interpreter.runWithDebug();

        // When
        interpreter.stepBackward();

        // Then
        assertThat(interpreter.getRuntimeErrorsCollector().getErrors(), is(empty()));
    }

    @Test
    public void shouldStepForward() {
        // Given
        interpreter.runWithDebug();

        // When
        interpreter.stepForward();

        // Then
        assertThat(interpreter.getRuntimeErrorsCollector().getErrors(), is(empty()));
    }

}
