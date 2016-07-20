package com.asx.fcma.tests.adapter.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by auto_test on 4/01/2016.
 */
public class GeniumInputXmlParser {

    public HashMap<String,String> parseXml(String xmlPath) throws Exception {

        String upperLevelSeries = null;
        HashMap<String,String> expected = new HashMap<String,String>();
        // Creating a hash map instance to store all retrieved elements from XML which will act as the list of expected values

        File fXmlFile = new File(xmlPath);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        // Load the input XML document, parse it and return an instance of the Document class.

        Document document = builder.parse(fXmlFile);

        //Parsing the xml and populating the hash map
        String securityTypeCode = document.getElementsByTagName("SECURITY_TYPE_CODE").item(0).getFirstChild().getNodeValue();

        //Identify display code for genium and downstream
        NodeList nodeList = document.getElementsByTagName("INSTR_IDENTIFIER");
        for (int i=0; i<nodeList.getLength();i++ ){
            Element elem = (Element)nodeList.item(i);

            if (elem.getElementsByTagName("INSTR_ID_TYPE").item(0).getTextContent().equalsIgnoreCase("GENIUM")){
                expected.put("GENIUM_DISPLAY_CODE",elem.getElementsByTagName("INSTR_ID_VALUE").item(0).getTextContent());
                expected.put("INSTRUMENT_SERIES",expected.get("GENIUM_DISPLAY_CODE"));
            }
            if (elem.getElementsByTagName("INSTR_ID_TYPE").item(0).getTextContent().equalsIgnoreCase("InstrumentClass")){
                expected.put("INSTRUMENT_CLASS",elem.getElementsByTagName("INSTR_ID_VALUE").item(0).getTextContent());
            }
            if ((securityTypeCode.equalsIgnoreCase("94") || securityTypeCode.equalsIgnoreCase("96")) && (elem.getElementsByTagName("INSTR_ID_TYPE").item(0).getTextContent().equalsIgnoreCase("GENIUM_UNDERLYING"))){
                expected.put("UPPER_LEVEL_SERIES",elem.getElementsByTagName("INSTR_ID_VALUE").item(0).getTextContent());
            }
        }
        expected.put("INSTRUMENT_STATUS" ,document.getElementsByTagName("INSTRUMENT_STATUS").item(0).getFirstChild()!= null ? document.getElementsByTagName("INSTRUMENT_STATUS").item(0).getFirstChild().getNodeValue():" ");
        expected.put("EXCHANGE_CODE", document.getElementsByTagName("ASX_MARKET_ID").item(0).getFirstChild().getNodeValue());

        //Parsing the Futures specific fields
        if (securityTypeCode.equalsIgnoreCase("97"))
        {
            expected.put("DERIVATIVE_LEVEL","1");
          //expected.put("CONTRACT_SIZE" ,document.getElementsByTagName("PRICE_QUOTATION_FACTOR_FORMATTED").item(0).getFirstChild()!= null ? document.getElementsByTagName("PRICE_QUOTATION_FACTOR_FORMATTED").item(0).getFirstChild().getNodeValue().concat("00"):" ");
            expected.put("CONTRACT_SIZE" ,document.getElementsByTagName("PRICE_QUOTATION_FACTOR_FORMATTED").item(0).getFirstChild()!= null ? document.getElementsByTagName("PRICE_QUOTATION_FACTOR_FORMATTED").item(0).getFirstChild().getNodeValue().replace(".",""):" ");
        }
        //Parsing the Options specific fields
        if (securityTypeCode.equalsIgnoreCase("94") || securityTypeCode.equalsIgnoreCase("96")) //Options XML
        {
            expected.put("DERIVATIVE_LEVEL","2");
            expected.put("STRIKE_PRICE" ,document.getElementsByTagName("EXERCISE_PRICE_FORMATTED").item(0).getFirstChild()!= null ? document.getElementsByTagName("EXERCISE_PRICE_FORMATTED").item(0).getFirstChild().getNodeValue().replace(".",""):" ");
        }
        //Parsing the generic fields which are common for both futures and options
        expected.put("FIRST_TRADING_DATE" ,document.getElementsByTagName("FIRST_ACTIVE_DATE").item(0).getFirstChild()!= null ? document.getElementsByTagName("FIRST_ACTIVE_DATE").item(0).getFirstChild().getNodeValue():" ");
        expected.put("LAST_TRADING_DATE" ,document.getElementsByTagName("LAST_ACTIVE_DATE").item(0).getFirstChild()!= null ? document.getElementsByTagName("LAST_ACTIVE_DATE").item(0).getFirstChild().getNodeValue():" ");
        String expiryDate =document.getElementsByTagName("EXPIRY_DATE").item(0).getFirstChild()!= null ? document.getElementsByTagName("EXPIRY_DATE").item(0).getFirstChild().getNodeValue():" ";
        String expiryList[] = expiryDate.split("-");
        if (xmlPath.contains("NEW")){
            expected.put("EXPIRY_DATE",expiryDate);
        }
        if (xmlPath.contains("AMEND")){
            expected.put("EFFECTIVE_EXPIRY_DATE",expiryDate);
        }
        expected.put("EXPIRY_YEAR",expiryList[0]);
        expected.put("EXPIRY_MONTH",expiryList[1]);
        //expected.put("PRICE_QUOTATION_FACTOR" ,document.getElementsByTagName("PRICE_QUOTATION_FACTOR_FORMATTED").item(0).getFirstChild()!= null ? document.getElementsByTagName("PRICE_QUOTATION_FACTOR_FORMATTED").item(0).getFirstChild().getNodeValue().concat("00"):" ");
        expected.put("PRICE_QUOTATION_FACTOR" ,document.getElementsByTagName("PRICE_QUOTATION_FACTOR_FORMATTED").item(0).getFirstChild()!= null ? document.getElementsByTagName("PRICE_QUOTATION_FACTOR_FORMATTED").item(0).getFirstChild().getNodeValue().replace(".",""):" ");

        //Some of the fields are not mapped in XML and never change in Genium, These fields are below and are used for verification purpose only
        expected.put("STATUS", "ACTIVE");
        expected.put("CALCULATE_DATES","False");
        expected.put("AT_END_OF_TRADING","True");
        expected.put("AT_START_OF_TRADING","True");
        expected.put("TRADED_IN_GENIUM","False");
        return expected;
    }


    public String buildXmlForErrorCases (String xmlPath, String xmlTagName, String value) throws Exception {

        File fXmlFile = new File(xmlPath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        // Load the input XML document, parse it and return an instance of the Document class.
        Document document = builder.parse(fXmlFile);

        //Editing the input xml to add the current date to Last Updated time tag in xml for future usage
        if (document.getElementsByTagName(xmlTagName).item(0).getFirstChild() != null) {
            document.getElementsByTagName(xmlTagName).item(0).getChildNodes().item(0).setTextContent(value);
        }
        //Saving the edited xml
        Transformer xformer = TransformerFactory.newInstance().newTransformer();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(new Date());
        String newXmlPath = xmlPath.replace(".xml","-"+currentDate+".xml");
        xformer.transform(new DOMSource(document), new StreamResult(new File(newXmlPath)));
        return newXmlPath;
    }

    public String returnGeniumDisplayCode (String xmlPath) throws ParserConfigurationException, IOException, SAXException {

        String geniumDisplayCode = null;
        File fXmlFile = new File(xmlPath);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        // Load the input XML document, parse it and return an instance of the Document class.

        System.out.println("Parsing Input XML to capture expected values");
        Document document = builder.parse(fXmlFile);

        NodeList nodeList = document.getElementsByTagName("INSTR_IDENTIFIER");
        for (int i=0; i<nodeList.getLength();i++ ){
            Element elem = (Element)nodeList.item(i);

            if (elem.getElementsByTagName("INSTR_ID_TYPE").item(0).getTextContent().equalsIgnoreCase("GENIUM"))
            {
                geniumDisplayCode =elem.getElementsByTagName("INSTR_ID_VALUE").item(0).getTextContent();
                break;
            }

        }
        return geniumDisplayCode;
    }

}


