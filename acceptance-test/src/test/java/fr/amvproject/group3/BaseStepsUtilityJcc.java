package fr.amvproject.group3;

import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class BaseStepsUtilityJcc {

    public static BaseStepsUtilityJcc instance = new BaseStepsUtilityJcc();

    public ProgramNode currentCompileCode;
    public String currentFilename;
    public String currentProgramContent;
    public Exception lastException;

    public ByteArrayOutputStream outputCaptor;
    public PrintStream originalOut;
    public String executionResult;

    public static BaseStepsUtilityJcc getInstance() {
        return instance;
    }

    public String loadResource(String filename) throws IOException {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("acceptance/" + filename);
        assertThat("La ressource fichier 'acceptance/" + filename + "' est introuvable", is, is(notNullValue()));

        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
}