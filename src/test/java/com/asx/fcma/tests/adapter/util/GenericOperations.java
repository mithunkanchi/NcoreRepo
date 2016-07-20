package com.asx.fcma.tests.adapter.util;

import com.asx.fcma.tests.adapter.config.ConfigManager;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by kanchi_m on 11/23/2015.
 */
public class GenericOperations {

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
       randomAccessFile.close();
       return errorFound;

    }

    public File convertStringToFile(File file, String content) throws IOException {
        FileUtils.writeStringToFile(file,content);
        return file;
    }

    public String changeDateFormat (String sourceFormat,String targetFomat,String dateToConvert) throws ParseException {
        DateFormat originalFormat = new SimpleDateFormat(sourceFormat, Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat(targetFomat);
        Date date = originalFormat.parse(dateToConvert);
        return targetFormat.format(date);
    }

    public void readCSVContents (){
        String csvFile = "src\\\\test\\\\resources\\\\data\\\\Test.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(csvSplitBy);

                System.out.println("Country [code= " + data[4]
                        + " , name=" + data[5] + "]");

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Done");
    }


}
