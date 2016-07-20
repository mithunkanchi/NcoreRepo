package com.asx.fcma.tests.adapter.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kanchi_m on 12/18/2015.
 */
public class OptionSeriesGenerator {


    //public  static void main(String args[])throws Exception{
      public void generateOptionSeries(String xmlPath, int startExercisePrice, int lastExercisePrice, int sizeOfIncrements) throws Exception {

        File fXmlFile = new File(xmlPath);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        // Load the input XML document, parse it and return an instance of the Document class.



        for (int x=startExercisePrice ; x <= lastExercisePrice ; x=x+sizeOfIncrements ){
            Document document = builder.parse(fXmlFile);

            NodeList nodeList = document.getElementsByTagName("INSTR_IDENTIFIER");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element elem = (Element) nodeList.item(i);

                if (elem.getElementsByTagName("INSTR_ID_TYPE").item(0).getTextContent().equalsIgnoreCase("DOWNSTREAM")) {
                     elem.getElementsByTagName("INSTR_ID_VALUE").item(0).setTextContent(elem.getElementsByTagName("INSTR_ID_VALUE").item(0).getTextContent().replace(String.valueOf(startExercisePrice), String.valueOf(x)));
                }
                if (elem.getElementsByTagName("INSTR_ID_TYPE").item(0).getTextContent().equalsIgnoreCase("GENIUM")) {
                    elem.getElementsByTagName("INSTR_ID_VALUE").item(0).setTextContent(elem.getElementsByTagName("INSTR_ID_VALUE").item(0).getTextContent().replace(String.valueOf(startExercisePrice), String.valueOf(x)));
                }
            }

            document.getElementsByTagName("EXERCISE_PRICE").item(0).getChildNodes().item(0).setTextContent(String.valueOf(x));

            String asxCode = document.getElementsByTagName("ASX_CODE").item(1).getTextContent().replace(String.valueOf(startExercisePrice), String.valueOf(x));
            document.getElementsByTagName("ASX_CODE").item(1).setTextContent(asxCode);

            //Saving the edited xml
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(new DOMSource(document), new StreamResult(new File(xmlPath.replace(String.valueOf(startExercisePrice), String.valueOf(x)))));
       }

    }
}

