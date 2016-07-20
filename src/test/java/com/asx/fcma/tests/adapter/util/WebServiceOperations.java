package com.asx.fcma.tests.adapter.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.auth.BasicScheme;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.net.util.Base64;
import org.apache.http.HttpHeaders;


/**
 * Created by kanchi_m on 29/01/2016.
 */
public class WebServiceOperations {

    public int postSoap() throws IOException {
        // Target URL of web service
        String strURL = "http://qnnco201:8880/standardgc/webservice/Events";
        // Soap Action header parameter
       // String strSoapAction = "http://www.thegoldensource.com/EventRaiserService.wsdl";
        // Body of Request in xml format
        String strXMLFilename = "src\\\\test\\\\resources\\\\data\\\\Sample1.xml";
        //Response result
        int result = 0;

        File input = new File(strXMLFilename);
        // Prepare HTTP post
        PostMethod post = new PostMethod(strURL);
        // Request content will be retrieved directly
        // from the input stream
        RequestEntity entity = new FileRequestEntity(input, "text/xml; charset=UTF-8");
        post.setRequestEntity(entity);

        // consult documentation for your web service
       // post.setRequestHeader("SOAPAction", strSoapAction);

        String auth = "gsource" + ":" + "gsource@123";
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
        String authHeader = "Basic " + new String(encodedAuth);

        post.setRequestHeader(HttpHeaders.AUTHORIZATION, authHeader);


        // Get HTTP client
        HttpClient httpclient = new HttpClient();

        // Execute request
        try {
            result = httpclient.executeMethod(post);
            // Display status code
            System.out.println("Response status code: " + result);
            // Display response
            System.out.println("Response body: ");
            System.out.println(post.getResponseBodyAsString());
        } finally {
            // Release current connection to the connection pool once you are done
            post.releaseConnection();
        }
        return result;
    }


}