package fr.amvproject.group3;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@RunWith(Cucumber.class)
@CucumberOptions(
                features = "src/test/resources/fr/amvproject/group3/feature",
                glue = "fr.amvproject.group3",
                plugin = {"pretty", "html:target/cucumber-report.html"}
)
public class RunCucumberTest {

        private static PrintStream originalErr;
        private static ByteArrayOutputStream errBaos;

        @BeforeClass
        public static void silenceErr() {
                try {
                        originalErr = System.err;
                        errBaos = new ByteArrayOutputStream();
                        System.setErr(new PrintStream(errBaos));
                } catch (Exception ignore) {
                }
        }

        @AfterClass
        public static void restoreErr() {
                try {
                        if (originalErr != null) {
                                System.setErr(originalErr);
                        }
                } catch (Exception ignore) {
                }
        }
}
