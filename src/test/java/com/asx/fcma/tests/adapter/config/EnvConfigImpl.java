package com.asx.fcma.tests.adapter.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import asx.config.ConfigReaderImpl;
import asx.config.IConfigReader;

/**
 * Environment Configuration
 * The Environment properties file location can be System variable using -DEnvPropFileLoc="C:\temp\file"
 * Stream Base host name can be passed through -DSBVMName=tmmm201
 * Stream Base Host User can be passed through -DSBHostUser=tibco
 * SSH Key can be passed through -DSSHKeyLoc=c:\key\test.private
 * The ESB Host URL can be passed through -DESBURL=tcp://qesb201:7224 
 * Other variable which can be set are -Dusername and -Dpassword
 * @author kamboj_a
 *
 */
public class EnvConfigImpl implements IEnvConfig {

	private boolean isDebug = false;
	// All Key Names
	private String KEY_DBURL = "dbUrl";
	private String KEY_DBUSERNAME = "dbUsername";
	private String KEY_DBPASSWORD = "dbPassword";
	private String KEY_ESBSERVERURL = "esbServerUrl";
	private String KEY_ESBUSERNAME = "esbUsername";
	private String KEY_ESBPASSWORD = "esbPassword";
	private String KEY_ESBQNAME = "esbQName";
	private String KEY_POISONQNAME = "poisonQName";
	private String KEY_LOGFILELOCATION = "logFileLocation";


	// All values to set
	private String dbusername;
	private String dbpassword;
	private String envName = "";
	private String dburl = "";
	private String esbserverurl = "";
	private String esbusername = "";
	private String esbpassword = "";
	private String esbqname = "";
	private String poisonqname = "";
	private String logFileLocation = "";



	private String envFileName = "";
	private IConfigReader configReader = null;
	private Properties properties = new Properties();
	private Logger logger = LogManager.getLogger(getClass().getName());

	/**
	 * The Environment properties file location can be System variable using -DEnvPropFileLoc="C:\temp\file"
	 * The main LogB Adapter VM Host Name can be passed through -DLogBVMName=qtest204
	 * Stream Base host name can be passed through -DSBVMName=tmmm201
	 * Stream Base Host User can be passed through -DSBHostUser=tibco
	 * The ESB Host URL can be passed through -DESBURL=tcp://qesb201:7224 
	 * Other variable which can be set are -Dusername and -Dpassword
	 */
	public EnvConfigImpl(String executionEnvName) {

		if (executionEnvName.equalsIgnoreCase("dev")) {
			envFileName = ".\\src\\test\\resources\\envprop\\DevEnv.properties";
		} else if (executionEnvName.equalsIgnoreCase("test")){
			envFileName = ".\\src\\test\\resources\\envprop\\TestEnv.properties";
		}else if (executionEnvName.equalsIgnoreCase("uat")){
			envFileName = ".\\src\\test\\resources\\envprop\\UATEnv.properties";
		}

		envName = executionEnvName;
		try {
			resolveConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void resolveConfig() throws Exception{
		readProperties();
		dbusername = System.getProperty("DBUSERNAME", getValue(KEY_DBUSERNAME));
		dbpassword = System.getProperty("DBPASSWORD", getValue(KEY_DBPASSWORD));
		dburl = System.getProperty("DBURL", getValue(KEY_DBURL));
		esbserverurl = System.getProperty("ESBURL", getValue(KEY_ESBSERVERURL));
		esbusername = System.getProperty("ESBUSERNAME", getValue(KEY_ESBUSERNAME));
		esbpassword = System.getProperty("ESBPASSWORD", getValue(KEY_ESBPASSWORD));
		esbqname = System.getProperty("ESBQNAME", getValue(KEY_ESBQNAME));
		poisonqname = System.getProperty("POISONQNAME", getValue(KEY_POISONQNAME));
		logFileLocation = System.getProperty("LOGFILELOCATION", getValue(KEY_LOGFILELOCATION));


	}
	public boolean isDebug() {
		return isDebug;
	}

	public String getDBUserName() {
		return dbusername;
	}

	public String getDbpassword() {
		return dbpassword;
	}

	public String getEnvironmentName() {
		return envName;
	}

	public String getDBurl() {
		return dburl;
	}

	public String getEsbServerUrl() {
		return esbserverurl;
	}

	public String getEsbUsername() {
		return esbusername;
	}

	public String getEsbPassword() {
		return esbpassword;
	}

	public String getEsbQname() {
		return esbqname;
	}

	public String getPoisonQname() {
		return poisonqname;
	}

	public String getLogFileLocation() {
		return logFileLocation;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public void setProperty(String key, String value) {
		properties.put(key, value);
	}

	private void readProperties() throws Exception{
		FileInputStream fis = null;
		String fileLoc = System.getProperty("EnvPropFileLoc", envFileName);
		File file = new File(fileLoc);
		logger.debug("--- Properties File Path -----" + file.getAbsolutePath());
		try {
			fis = new FileInputStream(file);
			configReader = new ConfigReaderImpl(fis);
		}finally{
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getValue(String key) throws Exception{
		return configReader.getValue(key);
	}
}
