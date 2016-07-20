package com.asx.fcma.tests.adapter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Created by auto_test on 7/03/2016.
 */
public class CSVParser {
    public HashMap<String,String> parseCSV (String csvPath) throws FileNotFoundException
    {
        ArrayList<String> expected = new ArrayList<String>();
        int i =0;
        //Get scanner instance
        Scanner scanner = new Scanner(new File(csvPath));

        //Set the delimiter used in file
        scanner.useDelimiter(",");

        //Get all tokens and store them in some data structure
        while (scanner.hasNext())
        {
            expected.add(scanner.next());
        }
        //Close the scanner
        scanner.close();

        HashMap<String,String> expectedData = new HashMap<String,String>();

        if (expected.size() > 16) {
            for (i = 0; i <= 21; i++) {
                expectedData.put(expected.get(i), expected.get(i + 22));
            }
            if (expectedData.get("Settlement System Code").contains("\r")) {
                expectedData.put("Settlement System Code", expectedData.get("Settlement System Code").replace("\r", "").replace("\n", ""));
            }
        }
        else {
            for (i = 0; i < 8; i++) {
                expectedData.put(expected.get(i), expected.get(i + 8));
            }
            if (expectedData.get("instr_class").contains("\r")) {
                expectedData.put("instr_class", expectedData.get("instr_class").replace("\r", "").replace("\n", ""));
            }

        }
        return expectedData;

    }

    public static void main(String args[]) throws FileNotFoundException {
        CSVParser csvParser = new CSVParser();
        System.out.println(csvParser.parseCSV("src\\\\test\\\\resources\\\\data\\\\DRPS-FutureData1.csv"));
        //System.out.println(expected);
    }

}

