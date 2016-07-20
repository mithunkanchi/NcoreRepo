package com.asx.fcma.tests.adapter.definitions;

import com.asx.fcma.tests.adapter.config.ConfigManager;
import com.asx.fcma.tests.adapter.util.TestConstants;
import com.asx.fcma.tests.adapter.util.*;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;

/**
 * Performs the step definitions for the system level like environment setup
 * @author kanchi_m
 * 
 */
public class SystemStepDefs {
	
	@SuppressWarnings("unused") // Just Initialized
	private ConfigManager configManager = null;
	private String envKey = TestConstants.EXECUTION_ENVIRONMENT;
	
	
	/**
	 * The Execution Environment variable can be passed as System variable using -DExecutionEnvironment="test"
	 * Or can be passed as a part of BDD
	 * @param envName
	 * @throws Throwable
	 */
	@Given("^that the tests are to be run on \"(.*?)\" environment$")
	public void that_the_tests_are_to_be_run_on_environment(String envName) throws Throwable {
		// Check if the property is passed from command line or bamboo then use the same, 
		// else set the argument coming from the feature file.
		String executionEnv = System.getProperty(envKey);
		if ((executionEnv == null) || (executionEnv.length() == 0)) {
			System.setProperty(envKey, envName);
		}
		System.out.println("***** Executing on " + System.getProperty(envKey) + "  environment *******");
		configManager = ConfigManager.getInstance();
	}

	/*@After("@negativeTests")
    public void afterScenario() throws IOException, JMSException, NamingException {
        SendXmlToESB sendXmlToESB = new SendXmlToESB();
        String message= sendXmlToESB.xmlToString("src\\\\test\\\\resources\\\\data\\\\NegativeTests\\\\EDZ2016F_DELETE.xml");
        sendXmlToESB.writeMessage(message);
    }*/


	@Given("^that I have sample \"([^\"]*)\" for generation with the \"([^\"]*)\",\"([^\"]*)\" and the \"([^\"]*)\"$")
	public void that_I_have_sample_for_generation_with_the_and_the(String xmlPath, int startExercisePrice, int lastExercisePrice, int incrementSize) throws Throwable {
		OptionSeriesGenerator optionSeriesGenerator = new OptionSeriesGenerator();
		optionSeriesGenerator.generateOptionSeries(xmlPath,startExercisePrice,lastExercisePrice,incrementSize);

	}

	@Then("^I can generate the option series$")
	public void I_can_generate_the_option_series() throws Throwable {
		// Express the Regexp above with the code you wish you had

	}

}
