package tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import io.cucumber.junit.CucumberOptions;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@Slf4j
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@CucumberOptions(tags = "@development")
public class RunCucumberTests {
}
