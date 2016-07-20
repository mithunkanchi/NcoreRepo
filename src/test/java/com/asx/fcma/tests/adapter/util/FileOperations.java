package com.asx.fcma.tests.adapter.util;

import com.asx.fcma.tests.adapter.config.ConfigManager;
import org.apache.commons.io.FileUtils;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Created by kanchi_m on 11/23/2015.
 */
public class FileOperations {

    public boolean CheckErrorInLogFile()throws Exception {
       // File file = new File("C:\\Docs\\CDM_SQL_DRPSDataRefAdapter_20151207.log");
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String currentDate = df.format(new Date());
        String errorMsg = null;
        String fileLoc = ConfigManager.getInstance().getEnvConfig().getLogFileLocation();

        fileLoc = fileLoc+"GeniumInstrument_"+currentDate+".log";
        File file = new File(fileLoc);
        Boolean errorFound = false;
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        int lines = 0;
        StringBuilder builder = new StringBuilder();
        long length = file.length();
        length--;
        randomAccessFile.seek(length);
        for(long seek = length; seek >= 0; --seek){
            randomAccessFile.seek(seek);
            char c = (char)randomAccessFile.read();
            builder.append(c);
            if(c == '\n'){
                builder = builder.reverse();
                if ((builder.toString().contains("ERROR")))
                {
                    errorMsg = builder.toString();
                    System.out.println("Error found, the xml will not be processed by the adapter, Error ->>>>");
                    System.out.println(errorMsg.trim());
                    errorFound = true;
                    break;
                }
                lines++;
                builder = null;
                builder = new StringBuilder();
                if (lines == 200){
                    break;
                }
            }
        }
       return errorFound;

    }

    public File convertStringToFile(File file, String content) throws IOException, ParserConfigurationException, SAXException, TransformerException {


                //String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"></soap:Envelope>";

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                DocumentBuilder builder;

                builder = factory.newDocumentBuilder();

                 // Use String reader
                    Document document = builder.parse( new InputSource(new StringReader( content ) ) );

                    TransformerFactory tranFactory = TransformerFactory.newInstance();
                    Transformer aTransformer = tranFactory.newTransformer();
                    Source src = new DOMSource( document );
                    Result dest = new StreamResult( file );
                    aTransformer.transform( src, dest );

                    return file;




    }

}
