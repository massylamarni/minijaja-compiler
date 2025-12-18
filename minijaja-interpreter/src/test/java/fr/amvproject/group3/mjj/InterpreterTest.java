package fr.amvproject.group3.mjj;

import fr.amvproject.group3.exceptions.collectors.RuntimeErrorsCollector;
import fr.amvproject.group3.exceptions.collectors.SemanticErrorsCollector;
import fr.amvproject.group3.exceptions.listeners.LexicalErrorsListener;
import fr.amvproject.group3.exceptions.listeners.SyntaxicErrorsListener;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class InterpreterTest {

    private Interpreter interpreter;
    private String validProgram;

    @Before
    public void setUp() {
        // Given
        validProgram = "class Test { main {}}";
        interpreter = new Interpreter(validProgram);
    }

    @Test
    public void Given_ValidProgram_When_InterpreterIsCreated_Then_LexicalListenerIsNotNull() {
        // When
        LexicalErrorsListener lexicalListener = interpreter.getLexicalErrorsListener();

        // Then
        assertThat(lexicalListener, is(notNullValue()));
    }

    @Test
    public void Given_ValidProgram_When_InterpreterIsCreated_Then_SyntaxicListenerIsNotNull() {
        // When
        SyntaxicErrorsListener syntaxicListener = interpreter.getSyntaxicErrorsListener();

        // Then
        assertThat(syntaxicListener, is(notNullValue()));
    }

    @Test
    public void Given_ValidProgram_When_RunIsCalled_Then_NoSemanticErrorsAreReported() {
        // When
        interpreter.run();
        SemanticErrorsCollector semanticErrors = interpreter.getSemanticErrorsCollector();

        // Then
        assertThat(semanticErrors, is(notNullValue()));
        assertThat(semanticErrors.getErrors().isEmpty(), is(true));
    }

    @Test
    public void Given_ValidProgram_When_RunIsCalled_Then_NoRuntimeErrorsAreReported() {
        // When
        interpreter.run();
        RuntimeErrorsCollector runtimeErrors = interpreter.getRuntimeErrorsCollector();

        // Then
        assertThat(runtimeErrors, is(notNullValue()));
        assertThat(runtimeErrors.getErrors().isEmpty(), is(true));
    }

    @Test
    public void Given_InterpreterExecuted_When_RestartIsCalled_Then_CollectorsAreReset() {
        // Given
        interpreter.run();

        // When
        interpreter.reset();

        // Then
        assertThat(interpreter.getRuntimeErrorsCollector().getErrors().isEmpty(), is(true));
        assertThat(interpreter.getSemanticErrorsCollector().getErrors().isEmpty(), is(true));
    }

    @Test
    public void Given_ValidProgram_When_RunWithDebugIsCalled_Then_NoExceptionIsThrown() {
        // When / Then
        try {
            interpreter.runWithDebug();
        } catch (Exception e) {
            assertThat("No exception should be thrown", false);
        }
    }

    @Test
    public void Given_DebugModeEnabled_When_ResumeIsCalled_Then_NoExceptionIsThrown() {
        // Given
        interpreter.runWithDebug();

        // When / Then
        try {
            interpreter.resume();
        } catch (Exception e) {
            assertThat("No exception should be thrown", false);
        }
    }

    @Test
    public void Given_DebugModeEnabled_When_StepForwardIsCalled_Then_NoExceptionIsThrown() {
        // Given
        interpreter.runWithDebug();

        // When / Then
        try {
            interpreter.stepForward();
        } catch (Exception e) {
            assertThat("No exception should be thrown", false);
        }
    }

    @Test
    public void Given_DebugModeEnabled_When_StepBackwardIsCalled_Then_NoExceptionIsThrown() {
        // Given
        interpreter.runWithDebug();

        // When / Then
        try {
            interpreter.stepBackward();
        } catch (Exception e) {
            assertThat("No exception should be thrown", false);
        }
    }

    @Test
    public void Given_Interpreter_When_AddAndRemoveBreakpointAreCalled_Then_NoExceptionIsThrown() {
        // When / Then
        try {
            interpreter.addBreakPoint(1);
            interpreter.removeBreakpoint(1);
        } catch (Exception e) {
            assertThat("No exception should be thrown", false);
        }
    }

    @Test
    public void Given_Interpreter_When_GetStackIsCalled_Then_StackIsNotNull() {
        // When
        Stack stack = interpreter.getStack();

        // Then
        assertThat(stack, is(notNullValue()));
    }
}
