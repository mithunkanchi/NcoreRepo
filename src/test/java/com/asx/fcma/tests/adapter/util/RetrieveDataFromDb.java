package com.asx.fcma.tests.adapter.util;

import com.asx.fcma.tests.adapter.config.ConfigManager;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by kanchi_m on 11/24/2015.
 */
public class RetrieveDataFromDb {

    // JDBC driver name and database URL
    static String JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";

    // Retrieving the Database url from the config file
    String db_url = ConfigManager.getInstance().getEnvConfig().getDBurl();

    //  Retrieving the Database credentials from the environment file
    String userName = ConfigManager.getInstance().getEnvConfig().getDBUserName();
    String password= ConfigManager.getInstance().getEnvConfig().getDbpassword();

    public Statement getDBConnection() throws SQLException, ClassNotFoundException{


        Connection conn = null;
        Statement stmt = null;
        //STEP 2: Register JDBC driver
        Class.forName(JDBC_DRIVER);

        //STEP 3: Open a connection
        System.out.println("Connecting to Downstream database to retrieve data from db...");
        if (!(userName.contentEquals("") && password.contentEquals(""))){
            conn = DriverManager.getConnection(db_url,userName,password);
        }
        else{
            conn = DriverManager.getConnection(db_url);
        }
        System.out.println("Connected database successfully...");
        System.out.println("Creating statement...");
        stmt = conn.createStatement();
        return stmt;
    }

   public HashMap<String,String> getData(Statement stmt, String downstreamDisplayCode, String type) throws SQLException, InterruptedException {
        HashMap<String,String> actual = new HashMap<String,String>();
        String sql = null;
        //Query to get the Futures related information from the Database

        if (type.equalsIgnoreCase("Futures")){
            actual.put("UNDERLYING_CLASS","Futures");
            sql ="Select e.ExchangeCode,c.CommodityCode,ib.DisplayCode, "+
                    "(select GSClassCode from GS_CLASS_MAPPING where ClassTypeCode = 'DVC' and DSClassId = f.DollarValuationClassId) as DollarValuationClass, "+
                    "(select GSClassCode from GS_CLASS_MAPPING where ClassTypeCode = 'NVC' and DSClassId = f.NominalValuationClassId) as NominalValuationClass, "+
                    "ib.FirstTradingDate,ib.LastTradingDate, "+
                    "(select GSClassCode from GS_CLASS_MAPPING where ClassTypeCode = 'STT' and DSClassId = f.SettlementTypeId) as SettlementType, "+
                    "f.Size,f.ExpiryDate,f.PriceFractionalIndicator,ib.Traded,f.CouponRate,f.NoCoupons,f.Month,f.Year,f.RecordStartDate,f.RecordEndDate,f.RecordCreationDate,f.RecordModificationDate "+
                    "from INSTRUMENT_BASE ib, "+
                    "EXCHANGE e, "+
                    "FUTURE f, "+
                    "COMMODITY c "+
                    "where f.InstrumentId = ib.InstrumentId "+
                    "and ib.CommodityId = c.CommodityId "+
                    "and e.ExchangeId = c.ExchangeId "+
                    "and ib.DisplayCode ="+ "'"+downstreamDisplayCode+"'";
        }
        //Query to get the Options related information from the Database
        else if (type.equalsIgnoreCase("Options")){
            actual.put("UNDERLYING_CLASS","Options");
            sql =   "Select e.ExchangeCode,c.CommodityCode,ost.Description as OptionSeriesType,ib.DisplayCode, "+
                    "(Select DisplayCode from INSTRUMENT_BASE where InstrumentId = os.UnderlyingInstrumentId) as underlyingInstrument, "+
                    "(select GSClassCode from GS_CLASS_MAPPING where ClassTypeCode = 'DVC' and DSClassId = os.DollarValuationClassId) as DollarValuationClass, "+
                    "(select GSClassCode from GS_CLASS_MAPPING where ClassTypeCode = 'NVC' and DSClassId = os.NominalValuationClassId) as NominalValuationClass, "+
                    "ib.FirstTradingDate,"+"ib.LastTradingDate, "+
                    "(select GSClassCode from GS_CLASS_MAPPING where ClassTypeCode = 'STT' and DSClassId = os.SettlementTypeId)as SettlementType, "+
                    "os.Size, "+
                    "os.ExpiryDate, "+
                    "ib.Traded, "+
                    "os.CouponRate, "+
                    "os.NoCoupons, "+
                    "os.Month, "+
                    "os.Year, "+
                    "ib.RecordStartDate, "+
                    "ib.RecordEndDate, "+
                    "ib.RecordCreationDate, "+
                    "os.RecordModificationDate, "+
                    "o.PutCall, "+
                    "o.RecordModificationDate as OptionsModificationDate,"+
                    "o.StrikePrice, "+
                    "(select GSClassCode from GS_CLASS_MAPPING where ClassTypeCode = 'ECC' and DSClassId = os.ExerciseClassId)as ExerciseClassCode, "+
                    "os.StrikeFractionalIndicator, "+
                    "os.PremiumFractionalIndicator "+
                    "from INSTRUMENT_BASE ib, "+
                    "OPTIONS o, "+
                    "OPTION_SERIES os, "+
                    "EXCHANGE e, "+
                    "COMMODITY c, "+
                    "OPTION_SERIES_TYPE ost "+
                    "where "+
                    "o.InstrumentId = ib.InstrumentId "+
                    "and o.OptionSeriesId = os.OptionSeriesId "+
                    "and ost.OptionSeriesTypeId = os.OptionSeriesTypeId "+
                    "and ib.CommodityId = c.CommodityId "+
                    "and e.ExchangeId = c.ExchangeId "+
                    "and ib.DisplayCode ="+ "'"+downstreamDisplayCode+"'";

        }
       //STEP 4: Execute a query
        Thread.sleep(2000);
        stmt.setQueryTimeout(25);
        ResultSet rs = stmt.executeQuery(sql);

        //STEP 5: Extract data from database result set and store it in a hash map, this will act as the set of actual data
        while(rs.next()){
            //Retrieve by column name
            actual.put("DISPLAY_MONTH",rs.getString("Month"));
            actual.put("DISPLAY_YEAR",rs.getString("Year"));
            actual.put("EXPIRY_DATE",rs.getString("ExpiryDate").substring(0, 10));
            actual.put("DOLLAR_VALUATION_CLASS_CODE",rs.getString("DollarValuationClass"));
            actual.put("NOMINAL_VALUATION_CLASS_CODE",rs.getString("NominalValuationClass"));
            actual.put("SIZE",rs.getString("Size"));
            actual.put("NUMBER_OF_COUPONS",rs.getString("NoCoupons"));
            actual.put("NUMBER_OF_COUPONS",Double.toString(rs.getDouble("NoCoupons")));
            actual.put("SETTLEMENT_TYPE",rs.getString("SettlementType"));
            actual.put("VALID_FROM_DATE",rs.getString("RecordStartDate").substring(0, 10));
            actual.put("VALID_TO_DATE",rs.getString("RecordEndDate").substring(0, 10));
            actual.put("RECORD_CREATION_DATE",rs.getString("RecordCreationDate").substring(0, 10));
            actual.put("COMMODITY_CODE",rs.getString("CommodityCode"));
            actual.put("UNDERLYING_INSTRUMENT",rs.getString("DisplayCode"));
            actual.put("FIRST_TRADING_DATE",rs.getString("FirstTradingDate").substring(0, 10));
            actual.put("LAST_TRADING_DATE",rs.getString("LastTradingDate").substring(0,10));
            actual.put("TRADED",rs.getString("Traded"));
            actual.put("DOWNSTREAM_DISPLAY_CODE",rs.getString("DisplayCode"));
            if (rs.getString("ExchangeCode").contains("SFE")){
                actual.put("EXCHANGE_CODE","X"+ rs.getString("ExchangeCode"));
            }
            else if (rs.getString("ExchangeCode").contains("NZFOE")){
                actual.put("EXCHANGE_CODE",rs.getString("ExchangeCode").replace("OE","X"));
            }
            actual.put("GENIUM_DISPLAY_CODE","ignore");

            //values specific to futures
            if (type.equalsIgnoreCase("FUTURES")) {
                actual.put("PRICE_FRACTIONAL_INDICATOR",rs.getString("PriceFractionalIndicator"));
                actual.put("COUPON_RATE",rs.getString("CouponRate"));
                actual.put("RECORD_MODIFICATION_DATE",rs.getString("RecordModificationDate").replace(" ","T").replace(".0",""));
            }
            //values specific to Options
            else if (type.equalsIgnoreCase("OPTIONS")){
                actual.put("STRIKE_PRICE",rs.getString("StrikePrice"));
                actual.put("EXERCISE_CLASS",rs.getString("ExerciseClassCode"));
                actual.put("STRIKE_FRACTIONAL_INDICATOR",rs.getString("StrikeFractionalIndicator"));
                actual.put("PREMIUM_FRACTIONAL_INDICATOR",rs.getString("PremiumFractionalIndicator"));
                actual.put("OPTION_SERIES_TYPE",rs.getString("OptionSeriesType"));
                actual.put("OPTION_TYPE",rs.getString("PutCall"));
                actual.put("RECORD_MODIFICATION_DATE",rs.getString("OptionsModificationDate").replace(" ","T").replace(".0",""));
            }

        }
        rs.close();
        stmt.close();
        //return the hash map of actual data to compare with the set of expected data
        return actual;
    }


    public int getRowCountForDisplayCode(Statement stmt, String downstreamDisplayCode) throws SQLException {
        int NoOfRows = 1;
        String sql = "Select count(*) as NoOfRows from INSTRUMENT_BASE where DisplayCode ="+ "'"+downstreamDisplayCode+"'";
        //STEP 4: Execute a query
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()) {
            //Retrieve by column name
            NoOfRows = rs.getInt("NoOfRows");
        }
        return NoOfRows;
    }
}
