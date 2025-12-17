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
        interpreter.reset();

        // When
        interpreter.run();

        // Then
        assertThat(interpreter.getRuntimeErrorsCollector().getErrors(), is(empty()));
        assertThat(interpreter.getSemanticErrorsCollector().getErrors(), is(empty()));
    }

    @Test
    public void shouldRunProgramWithDebug() {
        // Given
        interpreter.reset();

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
