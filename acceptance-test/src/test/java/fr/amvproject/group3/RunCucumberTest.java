package fr.amvproject.group3;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/fr/amvproject/group3/feature",
        glue = "fr.amvproject.group3",
        plugin = {"pretty", "html:target/cucumber-report.html"}
)
public class RunCucumberTest {
}
