package com.asx.fcma.tests.adapter.util;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by auto_test on 11/02/2016.
 */
public class GeniumWebServiceOutputXmlParser {
    public HashMap<String,String> parseXml(String xmlPath, String updateType) throws Exception {

        GenericOperations genericOperations = new GenericOperations();

        DateFormat originalFormat = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-mm-dd");

        HashMap<String,String> actual = new HashMap<String,String>();
        // Creating a hash map instance to store all retrieved elements from the XML response of the Genium Web service which will be the list of actual values

        File fXmlFile = new File(xmlPath);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        // Load the input XML document, parse it and return an instance of the Document class.

        Document document = builder.parse(fXmlFile);

        //Parsing the xml and populating the hash map
        String exchangeCode = document.getElementsByTagName("Exchange_Code").item(0).getFirstChild().getNodeValue();
        if (exchangeCode.equalsIgnoreCase("32")){
            actual.put("EXCHANGE_CODE","XSFE");}
        else if  (exchangeCode.equalsIgnoreCase("36")){
            actual.put("EXCHANGE_CODE","NZFX");}

        actual.put("PRICE_QUOTATION_FACTOR",document.getElementsByTagName("Price_Quotation_Factor").item(0).getTextContent());

        String firstTradeDate = document.getElementsByTagName("First_Trade_Date").item(0).getTextContent().substring(0,10);
        //Changing First Trading Date format from dd/mm/yyyy to yyyy-mm-dd
        actual.put("FIRST_TRADING_DATE",genericOperations.changeDateFormat("dd/mm/yyyy","yyyy-mm-dd",firstTradeDate));

        String lastTradeDate = document.getElementsByTagName("Last_Trade_Date").item(0).getTextContent().substring(0,10);
        //Changing Last Trading Date format from dd/mm/yyyy to yyyy-mm-dd
        actual.put("LAST_TRADING_DATE",genericOperations.changeDateFormat("dd/mm/yyyy","yyyy-mm-dd",lastTradeDate));

        if (updateType.toUpperCase().contentEquals("AMEND")){
           String effectiveExpiryDate = document.getElementsByTagName("Effective_Expiry_Date").item(0).getTextContent().substring(0,10);
           actual.put("EFFECTIVE_EXPIRY_DATE", genericOperations.changeDateFormat("dd/mm/yyyy", "yyyy-mm-dd", effectiveExpiryDate));
        }
        else if (updateType.toUpperCase().contentEquals("NEW")){
            String expiryDate = document.getElementsByTagName("Expiry_Date").item(0).getTextContent().substring(0,10);
            actual.put("EXPIRY_DATE", genericOperations.changeDateFormat("dd/mm/yyyy", "yyyy-mm-dd", expiryDate));
        }
        //Changing Expiry Date format from dd/mm/yyyy to yyyy-mm-dd


        String expiryMonth = document.getElementsByTagName("Expiry_Date_Month").item(0).getTextContent();
        if (expiryMonth.length() < 2){
            expiryMonth = "0"+expiryMonth;
        }
        actual.put("EXPIRY_MONTH",expiryMonth);
        actual.put("EXPIRY_YEAR",document.getElementsByTagName("Expiry_Date_Year").item(0).getTextContent());
        actual.put("STATUS",document.getElementsByTagName("Status").item(0).getTextContent());

        String derivativeLevel = document.getElementsByTagName("Derivative_Level").item(0).getTextContent();
        actual.put("DERIVATIVE_LEVEL",derivativeLevel);
        if (derivativeLevel.equalsIgnoreCase("2")){
            actual.put("STRIKE_PRICE",document.getElementsByTagName("Strike_Price").item(0).getTextContent());
            actual.put("UPPER_LEVEL_SERIES",document.getElementsByTagName("Upper_Level_Series").item(0).getTextContent());
        }
        if (derivativeLevel.equalsIgnoreCase("1")){
            actual.put("CONTRACT_SIZE",document.getElementsByTagName("Contract_Size").item(0).getTextContent());
        }
        String traded = document.getElementsByTagName("Traded").item(0).getFirstChild().getNodeValue();
        if (traded.equalsIgnoreCase("True")){
            actual.put("INSTRUMENT_STATUS","TR");}
        else if  (traded.equalsIgnoreCase("False")){
            actual.put("INSTRUMENT_STATUS","NT");}

        actual.put("INSTRUMENT_CLASS",document.getElementsByTagName("IncIdS").item(0).getTextContent());
        actual.put("GENIUM_DISPLAY_CODE",document.getElementsByTagName("InsIdS").item(0).getTextContent());
        actual.put("INSTRUMENT_SERIES",document.getElementsByTagName("InsIdS").item(0).getTextContent());
        actual.put("AT_END_OF_TRADING",document.getElementsByTagName("At_End_Of_Trading").item(0).getTextContent());
        actual.put("AT_START_OF_TRADING",document.getElementsByTagName("At_Start_Of_Trading").item(0).getTextContent());
        actual.put("TRADED_IN_GENIUM",document.getElementsByTagName("Traded_In_Click").item(0).getTextContent());
        actual.put("CALCULATE_DATES",document.getElementsByTagName("Calculate_Dates").item(0).getTextContent());
        return actual;
    }

}
