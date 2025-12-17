package fr.amvproject.group3.exceptions.collectors;

import fr.amvproject.group3.exceptions.RuntimeException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RuntimeErrorsCollectorTest {

    private RuntimeErrorsCollector collector;

    @Before
    public void setUp() {
        collector = new RuntimeErrorsCollector();
    }

    @Test
    public void givenEmptyCollector_when_HasErrors_then_ReturnsFalse() {
        assertThat(collector.hasErrors(), is(false));
    }

    @Test
    public void givenErrorAdded_when_HasErrors_then_ReturnsTrue() {
        collector.add("division by zero", 10, 5);

        assertThat(collector.hasErrors(), is(true));
    }

    @Test
    public void givenErrorAdded_when_GetErrors_then_ErrorIsCorrectlyStored() {
        collector.add("null pointer", 3, 7);

        List<RuntimeException> errors = collector.getErrors();

        assertThat(errors, hasSize(1));
        RuntimeException error = errors.get(0);
        assertThat(error.getMessage(), is("null pointer"));
        assertThat(error.getLine(), is(3));
        assertThat(error.getColumn(), is(7));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void givenGetErrors_when_ModifyingReturnedList_then_ExceptionThrown() {
        collector.add("error", 1, 1);

        collector.getErrors().add(new RuntimeException("other", 2, 2));
    }

    @Test
    public void givenGetRuntimeExceptions_when_ModifyingReturnedList_then_ListIsModified() {
        collector.add("error", 1, 1);

        collector.getRuntimeExceptions()
                .add(new RuntimeException("other", 2, 2));

        assertThat(collector.getRuntimeExceptions(), hasSize(2));
    }

    @Test
    public void givenMultipleErrors_when_ToMessage_then_MessageContainsAllErrors() {
        collector.add("error A", 1, 2);
        collector.add("error B", 3, 4);

        String message = collector.toMessage();

        assertThat(message, containsString("Runtime Exception:"));
        assertThat(message, containsString("error A"));
        assertThat(message, containsString("error B"));
    }

    @Test
    public void givenErrorsAdded_when_Reset_then_CollectorIsEmpty() {
        collector.add("error", 1, 1);

        collector.reset();

        assertThat(collector.hasErrors(), is(false));
        assertThat(collector.getErrors(), empty());
    }
}
