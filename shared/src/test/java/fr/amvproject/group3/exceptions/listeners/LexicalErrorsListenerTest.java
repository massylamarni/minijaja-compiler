package fr.amvproject.group3.exceptions.listeners;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LexicalErrorsListenerTest {

    private LexicalErrorsListener listener;

    @Before
    public void setUp() {
        listener = new LexicalErrorsListener();
    }

    @Test
    public void givenEmptyListener_when_HasErrors_then_ReturnsFalse() {
        assertThat(listener.hasErrors(), is(false));
    }

    @Test
    public void givenSyntaxError_when_HasErrors_then_ReturnsTrue() {
        listener.syntaxError(null, "?", 3, 7, "invalid character", null);

        assertThat(listener.hasErrors(), is(true));
    }

    @Test
    public void givenSyntaxError_when_GetErrors_then_ErrorIsStored() {
        listener.syntaxError(null, "@", 1, 4, "unexpected symbol", null);

        List<?> errors = listener.getErrors();

        assertThat(errors, hasSize(1));

        String errorString = errors.get(0).toString();
        assertThat(errorString, containsString("[1:4 ]"));
        assertThat(errorString, containsString("unexpected symbol"));
        assertThat(errorString, containsString("offending=@"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void givenGetErrors_when_ModifyingReturnedList_then_ExceptionThrown() {
        listener.syntaxError(null, "$", 2, 1, "lexical error", null);

        listener.getErrors().clear();
    }

    @Test
    public void givenMultipleErrors_when_ToMessage_then_MessageContainsAllErrors() {
        listener.syntaxError(null, "!", 1, 1, "error A", null);
        listener.syntaxError(null, "#", 2, 5, "error B", null);

        String message = listener.toMessage();

        assertThat(message, containsString("Lexing resulted in 2 errors."));
        assertThat(message, containsString("error A"));
        assertThat(message, containsString("error B"));
        assertThat(message, containsString("[1:1 ]"));
        assertThat(message, containsString("[2:5 ]"));
    }

    @Test
    public void givenErrorsAdded_when_Reset_then_ListenerIsEmpty() {
        listener.syntaxError(null, "%", 4, 9, "invalid token", null);

        listener.reset();

        assertThat(listener.hasErrors(), is(false));
        assertThat(listener.getErrors(), empty());
    }
}
