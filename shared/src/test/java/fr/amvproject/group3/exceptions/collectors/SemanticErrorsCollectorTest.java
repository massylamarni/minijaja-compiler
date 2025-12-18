package fr.amvproject.group3.exceptions.collectors;

import fr.amvproject.group3.exceptions.SemanticException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SemanticErrorsCollectorTest {

    private SemanticErrorsCollector collector;

    @Before
    public void setUp() {
        collector = new SemanticErrorsCollector();
    }

    @Test
    public void givenEmptyCollector_when_HasErrors_then_ReturnsFalse() {
        assertThat(collector.hasErrors(), is(false));
    }

    @Test
    public void givenErrorAdded_when_HasErrors_then_ReturnsTrue() {
        collector.add("type mismatch", 4, 8);

        assertThat(collector.hasErrors(), is(true));
    }

    @Test
    public void givenErrorAdded_when_GetErrors_then_ErrorIsCorrectlyStored() {
        collector.add("undeclared variable", 12, 3);

        List<SemanticException> errors = collector.getErrors();

        assertThat(errors, hasSize(1));
        SemanticException error = errors.get(0);
        assertThat(error.getMessage(), is("undeclared variable"));
        assertThat(error.getLine(), is(12));
        assertThat(error.getColumn(), is(3));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void givenGetErrors_when_ModifyingReturnedList_then_ExceptionThrown() {
        collector.add("semantic error", 1, 1);

        collector.getErrors().add(new SemanticException("other", 2, 2));
    }

    @Test
    public void givenGetSemanticExceptions_when_ModifyingReturnedList_then_ListIsModified() {
        collector.add("error", 1, 1);

        collector.getSemanticExceptions()
                .add(new SemanticException("other", 2, 2));

        assertThat(collector.getSemanticExceptions(), hasSize(2));
    }

    @Test
    public void givenMultipleErrors_when_ToMessage_then_MessageContainsAllErrors() {
        collector.add("error A", 2, 5);
        collector.add("error B", 6, 9);

        String message = collector.toMessage();

        assertThat(message, containsString("Semantic Exceptions:"));
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
