package com.asx.fcma.tests.adapter.definitions;

import com.asx.fcma.tests.adapter.util.*;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.unitils.reflectionassert.ReflectionAssert;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;

/**
 * Created by auto_test on 16/02/2016.
 */
public class NCoreAdapterStepDefs {

    HashMap<String,String> expected = new HashMap<String,String>();
    HashMap<String,String> geniumExpected = new HashMap<String,String>();
    HashMap<String,String> actual = new HashMap<String,String>();
    String tempFilePath = "src\\\\test\\\\resources\\\\data\\FutureSample.xml";
    GoldenSourceDbOperations dbOperations = new GoldenSourceDbOperations();

    @And("^I clear the data in the Queue$")
    public void I_clear_the_data_in_the_Queue() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Given("^that I read the message from the queue$")
    public void that_I_read_the_message_from_the_queue() throws Throwable {
        SendXmlToESB sendXmlToESB = new SendXmlToESB();
        String content = sendXmlToESB.readMessage1();
        if (content != null) {
            File tempFile = new File(tempFilePath);
            GenericOperations fileOp = new GenericOperations();
            fileOp.convertStringToFile(tempFile, content);
        }
        else{
            System.out.println("No messages in queue- cannot proceed further");
            System.exit(0);
        }
    }

    @Then("^I should have my actual data$")
    public void I_should_have_my_actual_data() throws Throwable {
        NorthAdapterXmlParser northAdapterXmlParser = new NorthAdapterXmlParser();
        actual = northAdapterXmlParser.parseXml(tempFilePath);
     }

    @And("^I publish the webservice to generate the xml$")
    public void I_publish_the_webservice_to_generate_the_xml() throws Throwable {
       Connection conn = dbOperations.getDBConnection();
       dbOperations.generateTretEntry(conn,geniumExpected.get("instr_series"));

       WebServiceOperations webServiceOperations = new WebServiceOperations();
       webServiceOperations.postSoap();
    }

    @Then("^the data should match to the csv submitted$")
    public void the_data_should_match_to_the_csv_submitted() throws Throwable {
        NorthAdapterXmlParser northAdapterXmlParser = new NorthAdapterXmlParser();
        actual = northAdapterXmlParser.parseXml(tempFilePath);

        System.out.println("Asserting values from XML generated and Ncore DB");
        ReflectionAssert.assertReflectionEquals(expected,actual);
    }

    @And("^I load the CsvFile into Golden Source db$")
    public void I_load_the_CsvFile_into_Golden_Source_db() throws Throwable {
        Connection conn = dbOperations.getDBConnection();
        dbOperations.insertDataToDb(conn,expected);

        dbOperations.insertDataToDb(conn,geniumExpected);

        dbOperations.runDataProcedures(conn,expected.get("Type"));
       // dbOperations.generateTretEntry(conn,);
    }

    @Given("^that I prepare to load the \"([^\"]*)\" and \"([^\"]*)\" into db$")
    public void thatIPrepareToLoadTheAndIntoDb(String futureCsvPath, String geniumCsvPath) throws Throwable {
        CSVParser csvParser = new CSVParser();
        expected = csvParser.parseCSV(futureCsvPath);
        geniumExpected = csvParser.parseCSV(geniumCsvPath);

    }

    @And("^I delete the Instrument generated$")
    public void iDeleteTheInstrumentGenerated() throws Throwable {
        Connection conn = dbOperations.getDBConnection();
        dbOperations.deleteInstrument(conn,geniumExpected.get("instr_series"));

    }
}
