package fr.amvproject.group3;

import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BaseStepsUtility {

    public static BaseStepsUtility instance = new BaseStepsUtility();

    public String currentFileContent;
    public ProgramNode currentCompileCode;
    public String currentFilename;

    public ByteArrayOutputStream outputCaptor;
    public PrintStream originalOut;
    public String executionResultJajacode;
    public String executionResultMiniJaja;

    public static BaseStepsUtility getInstance() {
        return instance;
    }

    public String loadResource(String filename) throws IOException {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("acceptance/" + filename);
        assertThat(is, is(notNullValue()));

        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    public String loadExpected(String baseName) throws IOException {
        String expectedFile = baseName.replace(".mjj", ".out");
        return loadResource(expectedFile).trim();
    }


}
