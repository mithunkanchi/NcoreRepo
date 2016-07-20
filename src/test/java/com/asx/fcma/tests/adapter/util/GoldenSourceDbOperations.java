package com.asx.fcma.tests.adapter.util;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by auto_test on 25/01/2016.
 */
public class GoldenSourceDbOperations {

    public Connection getDBConnection() throws SQLException, ClassNotFoundException{

        // JDBC driver name and database URL
        String JDBC_DRIVER = "oracle.jdbc.OracleDriver";

        String db_url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=dgsdb201)(PORT=60002)))(CONNECT_DATA=(SERVICE_NAME=NCORE)))";

        String userName = "ncoregc";
        String password= "ncoregc";

        Connection conn = null;
        Statement stmt = null;
        //STEP 2: Register JDBC driver
        Class.forName(JDBC_DRIVER);

        //STEP 3: Open a connection
        System.out.println("Connecting to Ncore database to insert data into db...");
        if (!(userName.contentEquals("") && password.contentEquals(""))){
            conn = DriverManager.getConnection(db_url,userName,password);
        }
        else{
            conn = DriverManager.getConnection(db_url);
        }
        return conn;
    }

    public void truncateTables (Connection conn, String type) throws SQLException {
        String sql = null;
        Statement stmt = conn.createStatement();

        if (type.equalsIgnoreCase("Futures")) {
            sql ="Truncate table FUT_DATA_TEMP";
        }

        if (type.equalsIgnoreCase("Options")){
            sql = "Truncate table OPT_DATA_TEMP";
        }

        if (type.equalsIgnoreCase("Genium")){
            sql ="Truncate table GEN_DATA_TEMP";
        }
        stmt.executeUpdate(sql);
        stmt.close();

    }

    public void deleteInstrument(Connection conn, String displayCode) throws SQLException, InterruptedException {

        String sql = null;
        String instrumentId= null;
        displayCode = displayCode.replace("1","");

        conn.setAutoCommit(true);

        Statement stmt = conn.createStatement();

        sql = "select instr_id from ft_t_issu where pref_iss_id="+"'"+displayCode+"'";

        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()){

            instrumentId = rs.getString("INSTR_ID");
        }

        deleteQuery(conn,"DELETE FT_T_ISST where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn,"DELETE FT_T_IScl where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn,"DELETE FT_T_ISde where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn,"DELETE FT_T_eqch where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn,"DELETE FT_T_bdst where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn,"DELETE FT_T_riss where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn,"DELETE FT_T_rSCP where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn,"DELETE FT_T_OPCH where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn," DELETE FT_T_RISS where RLD_ISS_FEAT_ID IN (SELECT  RLD_ISS_FEAT_ID FROM  FT_T_RIDF where instr_id = "+"'"+instrumentId+"'"+")");
        deleteQuery(conn,"DELETE FT_T_RIDF where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn,"DELETE FT_T_ISS1 where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn,"DELETE FT_T_MKIS where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn,"DELETE FT_T_ISid where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn,"DELETE FT_T_ISpc where instr_id ="+"'"+instrumentId+"'");
        deleteQuery(conn,"DELETE ft_t_issu where instr_id ="+"'"+instrumentId+"'");

        conn.close();
        stmt.close();

    }

    public void deleteQuery(Connection conn, String deleteQuery) throws SQLException {
        conn.setAutoCommit(true);

        Statement stmt = conn.createStatement();

        stmt.setQueryTimeout(25);

        int count = stmt.executeUpdate(deleteQuery);
        if (count > 0)
        {
            System.out.println("Deleted Instrument from Golden Source DB successfully ");
        }
        else{
            System.out.println("Delete from Golden Source DB failed");
        }

        stmt.close();

    }

    public void runRestoreProcedures (Connection conn, String type) throws SQLException {
        String sql = null;
        String command = null;
        Statement stmt = conn.createStatement();

        if (type.equalsIgnoreCase("Futures")) {
           command = "{call RESTORE_VALUES_FUT()}";
        }

        if (type.equalsIgnoreCase("Options")){
            command = "{call RESTORE_VALUES_OPT()}";
        }

        if (type.equalsIgnoreCase("Genium")){
            command = "{call RESTORE_VALUES_GEN()}";
        }
        CallableStatement cstmt = conn.prepareCall (command);

        cstmt.execute();
        cstmt.close();
    }

    public void runDataProcedures (Connection conn, String type) throws SQLException {
        String sql = null;
        String command = null;
        Statement stmt = conn.createStatement();

        if (type.equalsIgnoreCase("Futures")) {
            command = "{call DATA_MIGRATION_FUT()}";
        }

        if (type.equalsIgnoreCase("Options")){
            command = "{call DATA_MIGRATION_OPT()}";
        }

        if (type.equalsIgnoreCase("Genium")){
            command = "{call DATA_MIGRATION_OPT()}";
        }

        CallableStatement cstmt = conn.prepareCall (command);

        cstmt.execute();
        cstmt.close();
    }

    public void generateTretEntry(Connection conn, String displayCode) throws SQLException {
        String sql = null;

        conn.setAutoCommit(true);
        Statement stmt = conn.createStatement();
        //Query to insert the data into the Database

        displayCode = displayCode.replace("1","");

        sql =   "insert into ft_t_tret(TRET_OID, XREF_TBL_ID, XREF_TBL_ROW_OID, trn_id, job_id, record_seq_num, chg_ind, last_chg_tms, last_chg_usr_id)" +
        " SELECT NEW_OID(), 'ISSU',  instr_id, new_oid(), NULL, NULL, 'C', sysdate, 'AUTOTEST1' from ft_t_issu"+
        " where pref_iss_id="+"'"+displayCode+"'"+" and end_tms is null" ;

        ResultSet rs = stmt.executeQuery(sql);

        stmt.close();
        conn.close();
    }

    public void insertDataToDb (Connection conn ,HashMap<String,String> expected) throws SQLException, InterruptedException {
        String sql = null;

        String type = expected.get("Type");

        truncateTables(conn, type); // Truncate the tables before running the insert

        Statement stmt = conn.createStatement();
        //Query to insert the data into the Database


        if (type.equalsIgnoreCase("Futures")) {
            sql = "INSERT INTO FUT_DATA_TEMP (Sett_Sys_Code,Sett_Sys_Und_Code,Security_Type,Display_Month,Display_Year,Sett_Type,First_Trading_Date,Last_Trading_Date,Expiration_Date," +
                    "Contract_Size,Price_Decimal,Valuation_Method,Nominal_Valuation_Method,Instrument_Status,Valid_From,Valid_To,Option_Type,Strike_Price,Strike_Price_Decimal,Exercise_Type_Code,Premium_Decimal)" +
                    "VALUES ("+
                    "'"+expected.get("Settlement System Code")+"',"+
                    "'"+expected.get("Settlement System - UnderlyingCode")+"',"+
                    "'"+expected.get("Security Type")+"',"+
                    "'"+expected.get("Display Month")+"',"+
                    "'"+expected.get("Display Year")+"',"+
                    "'"+expected.get("Settlement Type")+"',"+
                    "'"+expected.get("First Trading Date")+"',"+
                    "'"+expected.get("Last Trading Date")+"',"+
                    "'"+expected.get("Expiration Date")+"',"+
                    "'"+expected.get("Contract Size")+"',"+
                    "'"+expected.get("Price Decimal")+"',"+
                    "'"+expected.get("Valuation Method")+"',"+
                    "'"+expected.get("Nominal Valuation Method")+"',"+
                    "'"+expected.get("Instrument Status")+"',"+
                    "'"+expected.get("Valid From")+"',"+
                    "'"+expected.get("Valid To")+"',"+
                    "'"+expected.get("Option Type")+"',"+
                    "'"+expected.get("Strike Price")+"',"+
                    "'"+expected.get("Strike Price Decimal")+"',"+
                    "'"+expected.get("Exercise Type Code")+"',"+
                    "'"+expected.get("Premium Decimal")+"')";
        }
       // "and ib.DisplayCode ="+ "'"+downstreamDisplayCode+"'";

        if (type.equalsIgnoreCase("Options")){
            sql = "INSERT INTO OPT_DATA_TEMP (Sett_Sys_Code,Sett_Sys_Und_Code,Security_Type,Display_Month,Display_Year,Sett_Type,First_Trading_Date,Last_Trading_Date,Expiration_Date," +
                    "Contract_Size,Valuation_Method,Nom_Valuation_Method,Instrument_Status,Valid_From,Valid_To,Option_Type,Strike_Price,Strike_Price_Decimal,Exercise_Type_Code,Premium_Decimal)" +
                    "VALUES ("+
                    "'"+expected.get("Settlement System Code")+"',"+
                    "'"+expected.get("Settlement System - UnderlyingCode")+"',"+
                    "'"+expected.get("Security Type")+"',"+
                    "'"+expected.get("Display Month")+"',"+
                    "'"+expected.get("Display Year")+"',"+
                    "'"+expected.get("Settlement Type")+"',"+
                    "'"+expected.get("First Trading Date")+"',"+
                    "'"+expected.get("Last Trading Date")+"',"+
                    "'"+expected.get("Expiration Date")+"',"+
                    "'"+expected.get("Contract Size")+"',"+
                    "'"+expected.get("Valuation Method")+"',"+
                    "'"+expected.get("Nominal Valuation Method")+"',"+
                    "'"+expected.get("Instrument Status")+"',"+
                    "'"+expected.get("Valid From")+"',"+
                    "'"+expected.get("Valid To")+"',"+
                    "'"+expected.get("Option Type")+"',"+
                    "'"+expected.get("Strike Price")+"',"+
                    "'"+expected.get("Strike Price Decimal")+"',"+
                    "'"+expected.get("Exercise Type Code")+"',"+
                    "'"+expected.get("Premium Decimal")+"')";
        }

        if (type.equalsIgnoreCase("Genium")){
            sql = "INSERT INTO GEN_DATA_TEMP (instr_class,instr_series,underlying,exch,contract_dec,PQF,PQF_dec)"+
                    "VALUES ("+
                    "'"+expected.get("instr_class")+"',"+
                    "'"+expected.get("instr_series")+"',"+
                    "'"+expected.get("underlying")+"',"+
                    "'"+expected.get("exchange")+"',"+
                    "'"+expected.get("contract_dec")+"',"+
                    "'"+expected.get("PQF")+"',"+
                    "'"+expected.get("PQF_dec")+"')";
        }

        //STEP 4: Execute a query
        Thread.sleep(2000);
        stmt.setQueryTimeout(25);

        int count = stmt.executeUpdate(sql);
        if (count > 0)
        {
            System.out.println("Inserted data into Golden Source DB successfully ");
        }
        else{
             System.out.println("Insert to Golden Source DB failed");
        }

    }


}
