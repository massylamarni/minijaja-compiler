package fr.amvproject.group3.exceptions.collectors;

import fr.amvproject.group3.exceptions.RuntimeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RuntimeErrorsCollectorTest {

    private RuntimeErrorsCollector collector;
    private ByteArrayOutputStream outCaptor;
    private ByteArrayOutputStream errCaptor;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @Before
    public void setUp() {
        collector = new RuntimeErrorsCollector();

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
        collector.add("division by zero", 10, 5);

        boolean hasErrors = collector.hasErrors();

        assertThat(hasErrors, is(true));
    }

    @Test
    public void givenErrorAdded_when_GetErrors_then_ListContainsRuntimeException() {
        collector.add("null pointer", 3, 7);

        List<RuntimeException> errors = collector.getErrors();

        assertThat(errors, hasSize(1));
        assertThat(errors.get(0).getMessage(), is("null pointer"));
        assertThat(errors.get(0).getLine(), is(3));
        assertThat(errors.get(0).getColumn(), is(7));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void givenGetErrors_when_ModifyingReturnedList_then_ExceptionThrown() {
        collector.add("error", 1, 1);
        List<RuntimeException> errors = collector.getErrors();

        errors.add(new RuntimeException("other", 2, 2));
    }

    @Test
    public void givenNoErrors_when_PrintAll_then_PrintsNoRuntimeErrorsMessage() {
        collector.printAll();

        assertThat(outCaptor.toString().trim(), is("No runtime errors."));
        assertThat(errCaptor.toString(), is(emptyString()));
    }

    @Test
    public void givenMultipleErrors_when_PrintAll_then_PrintsAllErrorsOnErr() {
        collector.add("error A", 1, 2);
        collector.add("error B", 3, 4);

        collector.printAll();

        String errOutput = errCaptor.toString();
        assertThat(errOutput, containsString("=== runtime errors (2) ==="));
        assertThat(errOutput, containsString("[1:2]error A"));
        assertThat(errOutput, containsString("[3:4]error B"));
    }
}
