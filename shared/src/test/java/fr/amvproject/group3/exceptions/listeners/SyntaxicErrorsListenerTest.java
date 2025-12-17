package fr.amvproject.group3.exceptions.listeners;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SyntaxicErrorsListenerTest {

    private SyntaxicErrorsListener listener;

    @Before
    public void setUp() {
        listener = new SyntaxicErrorsListener();
    }

    @Test
    public void givenEmptyListener_when_HasErrors_then_ReturnsFalse() {
        assertThat(listener.hasErrors(), is(false));
    }

    @Test
    public void givenSyntaxError_when_HasErrors_then_ReturnsTrue() {
        listener.syntaxError(null, "}", 5, 12, "missing token", null);

        assertThat(listener.hasErrors(), is(true));
    }

    @Test
    public void givenSyntaxError_when_GetErrors_then_ListContainsError() {
        listener.syntaxError(null, ";", 2, 8, "extraneous input", null);

        List<?> errors = listener.getErrors();

        assertThat(errors, hasSize(1));

        String errorString = errors.get(0).toString();
        assertThat(errorString, containsString("[2:8 ]"));
        assertThat(errorString, containsString("extraneous input"));
        assertThat(errorString, containsString("offending=;"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void givenGetErrors_when_ModifyingReturnedList_then_ExceptionThrown() {
        listener.syntaxError(null, ")", 3, 6, "mismatched input", null);

        listener.getErrors().clear();
    }

    @Test
    public void givenMultipleErrors_when_ToMessage_then_MessageContainsAllErrors() {
        listener.syntaxError(null, "{", 1, 1, "error A", null);
        listener.syntaxError(null, "]", 4, 10, "error B", null);

        String message = listener.toMessage();

        assertThat(message, containsString("Parsing resulted in 2 errors."));
        assertThat(message, containsString("error A"));
        assertThat(message, containsString("error B"));
        assertThat(message, containsString("[1:1 ]"));
        assertThat(message, containsString("[4:10 ]"));
    }

    @Test
    public void givenErrorsAdded_when_Reset_then_ListenerIsEmpty() {
        listener.syntaxError(null, "else", 7, 3, "unexpected token", null);

        listener.reset();

        assertThat(listener.hasErrors(), is(false));
        assertThat(listener.getErrors(), empty());
    }
}
