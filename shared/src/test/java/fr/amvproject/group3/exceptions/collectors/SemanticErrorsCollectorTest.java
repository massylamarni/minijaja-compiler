package fr.amvproject.group3.exceptions.collectors;

import fr.amvproject.group3.exceptions.SemanticException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SemanticErrorsCollectorTest {

    private SemanticErrorsCollector collector;
    private ByteArrayOutputStream outCaptor;
    private ByteArrayOutputStream errCaptor;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @Before
    public void setUp() {
        collector = new SemanticErrorsCollector();

        originalOut = System.out;
        originalErr = System.err;

        outCaptor = new ByteArrayOutputStream();
        errCaptor = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outCaptor));
        System.setErr(new PrintStream(errCaptor));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void givenEmptyCollector_when_HasErrors_then_ReturnsFalse() {
        boolean hasErrors = collector.hasErrors();
        assertThat(hasErrors, is(false));
    }

    @Test
    public void givenErrorAdded_when_HasErrors_then_ReturnsTrue() {
        collector.add("type mismatch", 4, 8);

        boolean hasErrors = collector.hasErrors();

        assertThat(hasErrors, is(true));
    }

    @Test
    public void givenErrorAdded_when_GetErrors_then_ListContainsSemanticException() {
        collector.add("undeclared variable", 12, 3);

        List<SemanticException> errors = collector.getErrors();

        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).getMessage(), is("undeclared variable"));
        assertThat(errors.get(0).getLine(), is(12));
        assertThat(errors.get(0).getColumn(), is(3));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void givenGetErrors_when_ModifyingReturnedList_then_ExceptionThrown() {
        collector.add("semantic error", 1, 1);
        List<SemanticException> errors = collector.getErrors();

        errors.add(new SemanticException("other", 2, 2));
    }

    @Test
    public void givenNoErrors_when_PrintAll_then_PrintsNoSemanticErrorsMessage() {
        collector.printAll();

        assertThat(outCaptor.toString().trim(), is("No semantic errors."));
        assertThat(errCaptor.toString(), is(emptyString()));
    }

    @Test
    public void givenMultipleErrors_when_PrintAll_then_PrintsAllErrorsOnErr() {
        collector.add("error A", 2, 5);
        collector.add("error B", 6, 9);

        collector.printAll();

        String errOutput = errCaptor.toString();
        assertThat(errOutput, containsString("=== Semantic errors (2) ==="));
        assertThat(errOutput, containsString("[2:5]error A"));
        assertThat(errOutput, containsString("[6:9]error B"));
    }
}
