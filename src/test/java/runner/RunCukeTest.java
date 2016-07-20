package runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Kanchi_m
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/cucumber-report/", "json:target/cucumber.json"},
glue = {"com.asx.fcma.tests.adapter.definitions"},
features = ".\\src\\test\\resources\\features")
public class RunCukeTest{

}
