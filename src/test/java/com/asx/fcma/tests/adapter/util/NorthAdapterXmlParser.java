package com.asx.fcma.tests.adapter.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

/**
 * Created by auto_test on 9/02/2016.
 */
public class NorthAdapterXmlParser {

    public HashMap<String,String> parseXml(String xmlPath) throws Exception{

        HashMap<String,String> expected = new HashMap<String,String>();
        // Creating a hash map instance to store all retrieved elements from XML which will act as the list of expected values

        File fXmlFile = new File(xmlPath);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        // Load the input XML document, parse it and return an instance of the Document class.

        System.out.println("Parsing Input XML to capture expected values");

        Document document = builder.parse(fXmlFile);

        expected.put("EXCHANGE_CODE",document.getElementsByTagName("ASX_MARKET_ID").item(0).getFirstChild().getNodeValue());
        expected.put("COMMODITY_CODE" ,document.getElementsByTagName("UNDERLYING_ASX_CODE").item(0).getFirstChild()!= null ? document.getElementsByTagName("UNDERLYING_ASX_CODE").item(0).getFirstChild().getNodeValue():" ");

        //Identify display code for genium and downstream
        NodeList nodeList = document.getElementsByTagName("INSTR_IDENTIFIER");
        for (int i=0; i<nodeList.getLength();i++ ){
            Element elem = (Element)nodeList.item(i);

            if (elem.getElementsByTagName("INSTR_ID_TYPE").item(0).getTextContent().equalsIgnoreCase("DOWNSTREAM"))
            {
                expected.put("DOWNSTREAM_DISPLAY_CODE",elem.getElementsByTagName("INSTR_ID_VALUE").item(0).getTextContent());
                expected.put("UNDERLYING_INSTRUMENT",expected.get("DOWNSTREAM_DISPLAY_CODE"));
            }
            // if (elem.getElementsByTagName("INSTR_ID_TYPE").item(0).getTextContent().equalsIgnoreCase("GENIUM")){
            //     expected.put("GENIUM_DISPLAY_CODE",elem.getElementsByTagName("INSTR_ID_VALUE").item(0).getTextContent());
            //     expected.put("UNDERLYING_INSTRUMENT",expected.get("GENIUM_DISPLAY_CODE"));

            // }
        }
        //Parsing the generic fields which are common for both futures and options
        expected.put("DOLLAR_VALUATION_CLASS_CODE" ,document.getElementsByTagName("VALUATION_METHOD").item(0).getFirstChild()!= null ? document.getElementsByTagName("VALUATION_METHOD").item(0).getFirstChild().getNodeValue():" ");
        expected.put("NOMINAL_VALUATION_CLASS_CODE" ,document.getElementsByTagName("VALUATION_METHOD_NOMINAL").item(0).getFirstChild()!= null ? document.getElementsByTagName("VALUATION_METHOD_NOMINAL").item(0).getFirstChild().getNodeValue():" ");
        expected.put("FIRST_TRADING_DATE" ,document.getElementsByTagName("FIRST_ACTIVE_DATE").item(0).getFirstChild()!= null ? document.getElementsByTagName("FIRST_ACTIVE_DATE").item(0).getFirstChild().getNodeValue():" ");
        expected.put("LAST_TRADING_DATE" ,document.getElementsByTagName("LAST_ACTIVE_DATE").item(0).getFirstChild()!= null ? document.getElementsByTagName("LAST_ACTIVE_DATE").item(0).getFirstChild().getNodeValue():" ");
        expected.put("EXPIRY_DATE" ,document.getElementsByTagName("EXPIRY_DATE").item(0).getFirstChild()!= null ? document.getElementsByTagName("EXPIRY_DATE").item(0).getFirstChild().getNodeValue():" ");
        expected.put("VALID_FROM_DATE" ,document.getElementsByTagName("FIRST_LISTED_DATE").item(0).getFirstChild()!= null ? document.getElementsByTagName("FIRST_LISTED_DATE").item(0).getFirstChild().getNodeValue():" ");
        expected.put("VALID_TO_DATE" ,document.getElementsByTagName("LAST_LISTED_DATE").item(0).getFirstChild()!= null ? document.getElementsByTagName("LAST_LISTED_DATE").item(0).getFirstChild().getNodeValue():" ");
        expected.put("SETTLEMENT_TYPE" ,document.getElementsByTagName("SETTLEMENT_PRODUCT_CODE").item(0).getFirstChild()!= null ? document.getElementsByTagName("SETTLEMENT_PRODUCT_CODE").item(0).getFirstChild().getNodeValue():" ");
        expected.put("SIZE" ,document.getElementsByTagName("SHARES_PER_CONTRACT").item(0).getFirstChild()!= null ? document.getElementsByTagName("SHARES_PER_CONTRACT").item(0).getFirstChild().getNodeValue():" ");
        Double paymentsPerYear = Double.parseDouble(document.getElementsByTagName("INTEREST_PAYMENTS_PER_YEAR").item(0).getFirstChild() != null ? document.getElementsByTagName("INTEREST_PAYMENTS_PER_YEAR").item(0).getFirstChild().getNodeValue() : "0");
        Double yearsToMaturity =  Double.parseDouble(document.getElementsByTagName("YEARS_TO_MATURITY").item(0).getFirstChild() != null ? document.getElementsByTagName("YEARS_TO_MATURITY").item(0).getFirstChild().getNodeValue() : "0");
        expected.put("NUMBER_OF_COUPONS",Double.toString(paymentsPerYear * yearsToMaturity));
        String displayMonth = document.getElementsByTagName("DISPLAY_MONTH").item(0).getFirstChild()!= null ? document.getElementsByTagName("DISPLAY_MONTH").item(0).getFirstChild().getNodeValue():" ";
        if (displayMonth.substring(0,1).contains("0"))
        {
            displayMonth = displayMonth.substring((1));
        }
        expected.put("DISPLAY_MONTH" ,displayMonth);
        expected.put("DISPLAY_YEAR" ,document.getElementsByTagName("DISPLAY_YEAR").item(0).getFirstChild()!= null ? document.getElementsByTagName("DISPLAY_YEAR").item(0).getFirstChild().getNodeValue():" ");
        String instrumentStatus = document.getElementsByTagName("INSTRUMENT_STATUS").item(0).getFirstChild()!= null ? document.getElementsByTagName("INSTRUMENT_STATUS").item(0).getFirstChild().getNodeValue():" ";
        if (instrumentStatus.equalsIgnoreCase("TR"))
        {
            expected.put("TRADED","Y");
        }
        else if(instrumentStatus.equalsIgnoreCase("NT"))
        {
            expected.put("TRADED","N");
        }
        expected.put("RECORD_CREATION_DATE" ,document.getElementsByTagName("CREATED_DATE_TIME").item(0).getFirstChild()!= null ? document.getElementsByTagName("CREATED_DATE_TIME").item(0).getFirstChild().getNodeValue():" ");
        expected.put("RECORD_MODIFICATION_DATE" ,document.getElementsByTagName("LAST_UPDATED_DATE_TIME").item(0).getFirstChild()!= null ? document.getElementsByTagName("LAST_UPDATED_DATE_TIME").item(0).getFirstChild().getNodeValue():" ");

        String securityTypeCode = document.getElementsByTagName("SECURITY_TYPE_CODE").item(0).getFirstChild().getNodeValue();
        //Parsing the Futures Related specific fields
        if (securityTypeCode.equalsIgnoreCase("97"))
        {
            expected.put("UNDERLYING_CLASS","Futures");
            String couponRate =(document.getElementsByTagName("INTEREST_RATE").item(0).getFirstChild()!= null ? document.getElementsByTagName("INTEREST_RATE").item(0).getFirstChild().getNodeValue():" ");
            if (!couponRate.contains("."))
            {
                expected.put("COUPON_RATE",couponRate.concat(".000"));
            }
            else
            {
                expected.put("COUPON_RATE",couponRate.substring(0,7));

            }
            expected.put("PRICE_FRACTIONAL_INDICATOR" ,document.getElementsByTagName("DECIMAL_PLACES").item(0).getFirstChild()!= null ? document.getElementsByTagName("DECIMAL_PLACES").item(0).getFirstChild().getNodeValue():" ");
            expected.put("RECORD_MODIFICATION_DATE" ,document.getElementsByTagName("LAST_UPDATED_DATE_TIME").item(0).getFirstChild()!= null ? document.getElementsByTagName("LAST_UPDATED_DATE_TIME").item(0).getFirstChild().getNodeValue():" ");

        }
        //Parsing the Options Related specific fields
        if (securityTypeCode.equalsIgnoreCase("94") || securityTypeCode.equalsIgnoreCase("96")) //Options XML
        {
            expected.put("UNDERLYING_CLASS","Options");
            expected.put("EXERCISE_CLASS",document.getElementsByTagName("EXERCISE_TYPE_CODE").item(0).getFirstChild().getNodeValue());
            expected.put("STRIKE_FRACTIONAL_INDICATOR",document.getElementsByTagName("EXERCISE_PRICE_DECIMAL_PLACES").item(0).getFirstChild().getNodeValue());
            expected.put("PREMIUM_FRACTIONAL_INDICATOR" ,document.getElementsByTagName("DECIMAL_PLACES").item(0).getFirstChild()!= null ? document.getElementsByTagName("DECIMAL_PLACES").item(0).getFirstChild().getNodeValue():" ");

            String strikePrice = (document.getElementsByTagName("EXERCISE_PRICE").item(0).getFirstChild()!= null ? document.getElementsByTagName("EXERCISE_PRICE").item(0).getFirstChild().getNodeValue():" ");
            if (strikePrice.contains(".")){
                strikePrice=strikePrice.concat("0000");
            }
            else {
                strikePrice=strikePrice.concat(".0000000");
            }
            expected.put("STRIKE_PRICE" ,strikePrice);

            String cfiCode = document.getElementsByTagName("CFI_CODE").item(0).getFirstChild().getNodeValue().substring(5,6);
            if (cfiCode.equalsIgnoreCase("F")) {
                expected.put("OPTION_SERIES_TYPE","Overnight Future Option");
            }
            else if (cfiCode.equalsIgnoreCase("S")) {
                expected.put("OPTION_SERIES_TYPE","Future Option");
            }
            else if (cfiCode.equalsIgnoreCase("N")) {
                expected.put("OPTION_SERIES_TYPE","Serial Option");
            }
        }
        //Parsing the fields for PUT/CALL option
        if (securityTypeCode.equalsIgnoreCase("96"))
        {
            expected.put("OPTION_TYPE","P");
        }
        if (securityTypeCode.equalsIgnoreCase("94"))
        {
            expected.put("OPTION_TYPE","C");
        }
        return expected;
    }

}
