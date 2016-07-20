package com.asx.fcma.tests.adapter.definitions;


/**
 * Created by auto_test on 11/02/2016.
 */

import com.asx.fcma.tests.adapter.util.*;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import org.unitils.reflectionassert.ReflectionAssert;
import java.io.File;
import java.util.HashMap;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GeniumStepDefs {

    HashMap<String,String> expected = new HashMap<String,String>();
    HashMap<String,String> actual = new HashMap<String,String>();
    String xmlPath = null;
    String tempFilePath = "src\\\\test\\\\resources\\\\data\\\\OptionSample.xml";
    SendXmlToESB sendXmlToESB = new SendXmlToESB();
    String geniumData = null;
    String updateType = null;
    String geniumDisplayCode = null;

    @Given("^that I prepare the \"([^\"]*)\" for submission to Genium$")
    public void that_I_prepare_the_for_submission_to_Genium(String xml) throws Throwable {
        xmlPath = xml;
        GeniumInputXmlParser geniumInputXmlParser = new GeniumInputXmlParser();
        expected = geniumInputXmlParser.parseXml(xml);
    }

    @And("^I submit the input xml to ESB$")
    public void I_submit_the_input_xml_to_ESB() throws Throwable {
        String message= sendXmlToESB.xmlToString(xmlPath);
        sendXmlToESB.writeMessage(message);
        Thread.sleep(1000);
    }

    @When("^I retrieve data from Genium$")
    public void I_retrieve_data_from_Genium() throws Throwable {
        // Call Genium webservice and parse the response xml to get the hash map of actual values
        GeniumWebService gws = new GeniumWebService();
        Thread.sleep(2000);
        geniumData = gws.getdata("Futures",expected.get("GENIUM_DISPLAY_CODE"));
        if (geniumData.length() > 100) {
            geniumData = geniumData.substring(40);
            File tempFile = new File(tempFilePath);
            FileOperations fileOp = new FileOperations();
            fileOp.convertStringToFile(tempFile, geniumData);

            GeniumWebServiceOutputXmlParser geniumWebServiceOutputXmlParser = new GeniumWebServiceOutputXmlParser();
            if(xmlPath.contains("NEW")){
                updateType="NEW";
            }
            else if(xmlPath.contains("AMEND")){
                updateType="AMEND";
            }
            actual = geniumWebServiceOutputXmlParser.parseXml(tempFilePath,updateType);
        }
        else{
            System.out.println("**********"+geniumData+"***************");
            //System.exit(1);
        }

    }

    @Then("^the genium data should match to the xml submitted$")
    public void the_genium_data_should_match_to_the_xml_submitted() throws Throwable {
        System.out.println("Asserting values between Input XML and response received from Genium Web service");
        ReflectionAssert.assertReflectionEquals(expected, actual);
    }

    @Then("^there should be no data in Genium$")
    public void there_should_be_no_data_in_Genium() throws Throwable {
        GeniumWebService gws = new GeniumWebService();
        if (expected.get("GENIUM_DISPLAY_CODE")!= null)
        {
            geniumDisplayCode = expected.get("GENIUM_DISPLAY_CODE");
        }
        geniumData = gws.getdata("Futures",geniumDisplayCode);
        ReflectionAssert.assertReflectionEquals(geniumData,"Instrument series "+geniumDisplayCode+" does not exists ");
    }

    @And("^I clear the genium Poison Queue$")
    public void I_clear_the_genium_Poison_Queue() throws Throwable {
        // Read all the messages in the poison queue
        sendXmlToESB.readMessage();
    }

    @Given("^that I prepare the \"([^\"]*)\" for submission with \"([^\"]*)\" and \"([^\"]*)\"$")
    public void that_I_prepare_the_for_submission_with_and(String xml, String tagName, String value) throws Throwable {
       GeniumInputXmlParser geniumInputXmlParser = new GeniumInputXmlParser();
       xmlPath = geniumInputXmlParser.buildXmlForErrorCases(xml,tagName,value);
       geniumDisplayCode  = geniumInputXmlParser.returnGeniumDisplayCode(xmlPath);
    }


    @Given("^I submit an empty input xml to ESB$")
    public void I_submit_an_empty_input_xml_to_ESB() throws Throwable {
        sendXmlToESB.writeMessage("");
        Thread.sleep(1000);
    }
}
