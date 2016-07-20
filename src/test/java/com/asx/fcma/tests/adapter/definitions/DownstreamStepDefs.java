package com.asx.fcma.tests.adapter.definitions;

/**
 * Holds the step definitions for BDD scenarios
 * @author kanchi_m
 *
 *
 */
import com.asx.fcma.tests.adapter.util.*;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


import org.junit.Assert;
import org.unitils.reflectionassert.ReflectionAssert;

import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class DownstreamStepDefs {

	HashMap<String,String> expected = new HashMap<String,String>();
	HashMap<String,String> actual = new HashMap<String,String>();
	String xmlPath = null;
	String downStreamDisplayCode = null;
	String tempFilePath = "src\\\\test\\\\resources\\\\data\\FutureSample.xml";
	SendXmlToESB sendXmlToESB = new SendXmlToESB();

	/* @And("^I submit the input xml to ESB$")
	public void I_submit_the_input_xml_to_ESB() throws Throwable {
		String message= sendXmlToESB.xmlToString(xmlPath);
		sendXmlToESB.writeMessage(message);
		Thread.sleep(1000);
	} */

	@When("^I retrieve data from the Downstream db$")
	public void i_retrieve_data_from_the_Downstream_db() throws Throwable {
		RetrieveDataFromDb retrieveDataFromDb = new RetrieveDataFromDb();
		//Getting the type of XML (Futures or Options)
		String type = expected.get("UNDERLYING_CLASS");
		Statement stmt = retrieveDataFromDb.getDBConnection();
		actual = retrieveDataFromDb.getData(stmt, expected.get("DOWNSTREAM_DISPLAY_CODE"), type);
		//Since we are validating downstream data, we have to ignore genium display code
		expected.put("GENIUM_DISPLAY_CODE","ignore");
	}

	@Then("^the data should match to the xml submitted$")
	public void the_data_should_match_to_the_xml_submitted() throws Throwable {
		//Asserting the values of hash map from xml and the hash map from database, if both the data match then the xml was processed successfully
		System.out.println("Asserting values from XML and Downstream DB");
		ReflectionAssert.assertReflectionEquals(expected,actual);
	}

	@And("^I check that the error has been logged in the log file$")
	public void I_check_that_the_error_has_been_logged_in_the_log_file() throws Throwable {
		GenericOperations fileOp = new GenericOperations();
		if (fileOp.CheckErrorInLogFile()){} //Checking that an error has been logged in the log file.
		else{
			System.out.println("No error Found, File processed successfully");
		}
	}

	@Then("^the data should not be updated in the db$")
	public void the_data_should_not_be_updated_in_the_db() throws Throwable {
		System.out.println("Checking that the record is not updated in the db whenever an error occurs");
		Assert.assertNotEquals(expected.get("RECORD_MODIFICATION_DATE"), actual.get("RECORD_MODIFICATION_DATE"));
	}

	@Then("^there should be no data in db$")
	public void there_should_be_no_data_in_db() throws Throwable {
		RetrieveDataFromDb retrieveDataFromDb = new RetrieveDataFromDb();
		Statement stmt = retrieveDataFromDb.getDBConnection();
		System.out.println("Verifying DB to see that there are no records found for the given display code");
		int NoOfRows = retrieveDataFromDb.getRowCountForDisplayCode(stmt,downStreamDisplayCode);
		Assert.assertEquals(0,NoOfRows);
	}


	@And("^the inputXml is in the poison Queue$")
	public void the_inputXml_is_in_the_poison_Queue() throws Throwable {
		List messageList = sendXmlToESB.readMessage();
		System.out.println("Checking that the error XML is stored in the poison Queue");
		Assert.assertEquals(1, messageList.size());
	}

	@And("^I clear the downstream Poison Queue$")
	public void I_clear_the_downstream_Poison_Queue() throws Throwable {
		sendXmlToESB.readMessage();
	}
}

