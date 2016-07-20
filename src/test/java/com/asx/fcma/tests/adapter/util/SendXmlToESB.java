package com.asx.fcma.tests.adapter.util;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.tibco.tibjms.TibjmsQueueConnectionFactory;

import javax.jms.*;
import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import asx.esb.ESBInterface;

import com.asx.fcma.tests.adapter.config.ConfigManager;


/**
 * Created by kanchi_m on 11/20/2015.
 */
public class SendXmlToESB {

    // Getting the ESB related information from the environment file
    public String serverUrl = ConfigManager.getInstance().getEnvConfig().getEsbServerUrl();
    public String username = ConfigManager.getInstance().getEnvConfig().getEsbUsername();
    public String password = ConfigManager.getInstance().getEnvConfig().getEsbPassword();
    public String qName = ConfigManager.getInstance().getEnvConfig().getEsbQname();
    public String poisonQName = ConfigManager.getInstance().getEnvConfig().getPoisonQname();


    public String xmlToString(String xmlPath) throws IOException {
        XML xml = new XMLDocument(new File(xmlPath));
        String xmlString = xml.toString();
        return xmlString;
    }

    //write a message to a queue
    public void writeMessage(String message) throws NamingException, JMSException {
        //Sending the xml message to the bus

        try {
            QueueConnectionFactory factory = new TibjmsQueueConnectionFactory(serverUrl);
            QueueConnection connection = factory.createQueueConnection(username, password);
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(qName);
            QueueSender sender = session.createSender(queue);
            TextMessage mess = session.createTextMessage();
            mess.setJMSDeliveryMode(1);
            mess.setText(message);
            System.out.println("Sending the Input XML to ESB");
            sender.send(mess);
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    //read the message from a specific queue
    public String readMessage1() throws NamingException, JMSException, InterruptedException {
        List<Message> messagesList = new ArrayList<Message>();
        String outputXml[] = null;
        try {
            QueueConnectionFactory factory = new TibjmsQueueConnectionFactory(serverUrl);
            QueueConnection connection = factory.createQueueConnection(username, password);
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("asx.autotest.instrument.notify");
            QueueReceiver queueReceiver = session.createReceiver(queue);
            connection.start();
            Message message = null;
            boolean exit = false;
            int wait = 10;
            while (!exit) {
                while ((message = queueReceiver.receiveNoWait()) != null) {
                    // message.getText();
                    messagesList.add(message);
                    exit = true;
                }
                if (message == null && wait <= 0) {
                    exit = true;
                }
                Thread.sleep(1000);
                wait--;
            }
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
            System.exit(0);
        }
        for (Iterator<Message> iterator = messagesList.iterator(); iterator.hasNext(); ) {
            Message message = (Message) iterator.next();
            String queueMessage = message.toString();
            Thread.sleep(1000);
            String queueText[] = queueMessage.split("Text=\\{");
            outputXml = queueText[1].split("}");
        }
        if (outputXml == null){
            System.out.println("No Messages in the Queue");
            return null;
        }
        else{
            return outputXml[0];
        }
    }

    //read the message from a specific queue
    public List readMessage() throws NamingException, JMSException, InterruptedException {
        List<Message> messagesList = new ArrayList<Message>();

        QueueConnectionFactory factory = new TibjmsQueueConnectionFactory(serverUrl);
        QueueConnection connection = factory.createQueueConnection(username, password);
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(poisonQName);
        QueueReceiver queueReceiver = session.createReceiver(queue);
        connection.start();
        Message message = null;
        boolean exit = false;
        int wait = 10;
        while (!exit) {
            while ((message = queueReceiver.receiveNoWait()) != null) {
                // message.getText();
                messagesList.add(message);
                exit = true;
            }
            if (message == null && wait <= 0) {
                exit = true;
            }
            Thread.sleep(1000);
            wait--;
        }
        connection.close();
        return messagesList;
    }
}





