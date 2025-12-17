package fr.amvproject.group3.exceptions.listeners;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SyntaxicErrorsListenerTest {

    private SyntaxicErrorsListener listener;
    private ByteArrayOutputStream errCaptor;
    private PrintStream originalErr;

    @Before
    public void setUp() {
        listener = new SyntaxicErrorsListener();
        originalErr = System.err;
        errCaptor = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errCaptor));
    }

    @After
    public void tearDown() {
        System.setErr(originalErr);
    }

    @Test
    public void givenEmptyListener_when_HasErrors_then_ReturnsFalse() {
        boolean hasErrors = listener.hasErrors();
        assertThat(hasErrors, is(false));
    }

    @Test
    public void givenSyntaxError_when_HasErrors_then_ReturnsTrue() {
        listener.syntaxError(null, "}", 5, 12, "missing token", null);

        boolean hasErrors = listener.hasErrors();

        assertThat(hasErrors, is(true));
    }

    @Test
    public void givenSyntaxError_when_GetErrors_then_ListContainsError() {
        listener.syntaxError(null, ";", 2, 8, "extraneous input", null);

        List<?> errors = listener.getErrors();

        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).toString(), containsString("line [2:8 ]"));
        assertThat(errors.get(0).toString(), containsString("extraneous input"));
        assertThat(errors.get(0).toString(), containsString("offending=;"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void givenGetErrors_when_ModifyingReturnedList_then_ExceptionThrown() {
        listener.syntaxError(null, ")", 3, 6, "mismatched input", null);

        List<?> errors = listener.getErrors();
        errors.clear();
    }

    @Test
    public void givenMultipleErrors_when_PrintAll_then_ErrContainsAllErrors() {
        listener.syntaxError(null, "{", 1, 1, "error A", null);
        listener.syntaxError(null, "]", 4, 10, "error B", null);

        listener.printAll();

        String errOutput = errCaptor.toString();
        assertThat(errOutput, containsString("error A"));
        assertThat(errOutput, containsString("error B"));
        assertThat(errOutput, containsString("line [1:1 ]"));
        assertThat(errOutput, containsString("line [4:10 ]"));
    }

    @Test
    public void givenErrors_when_ToMessage_then_ReturnsFormattedMessage() {
        listener.syntaxError(null, "else", 7, 3, "unexpected token", null);

        String message = listener.toMessage();

        assertThat(message, containsString("Parsing resulted in 1 errors."));
        assertThat(message, containsString("line [7:3 ]"));
        assertThat(message, containsString("unexpected token"));
        assertThat(message, containsString("offending=else"));
    }
}
