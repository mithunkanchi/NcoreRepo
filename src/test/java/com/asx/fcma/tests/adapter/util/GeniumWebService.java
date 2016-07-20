package com.asx.fcma.tests.adapter.util;

import com.asx.fcma.tests.adapter.util.Service.GeniumService.Service1;


/**
 * Created by auto_test on 26/02/2016.
 */
public class GeniumWebService {

    public String getdata(String instrumentType, String geniumDisplayCode ){
        Service1 geniumWebService = new Service1();
        return (geniumWebService.getBasicHttpBindingIService1().getData(instrumentType,geniumDisplayCode));
    }

    public static void main(String args[]){
        Service1 test = new Service1();
        String cl = test.getBasicHttpBindingIService1().getData("Futures","APsxdsH16");
        System.out.println(cl);
    }
}
