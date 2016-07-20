package com.asx.fcma.tests.adapter.config;

/**
 * Interface for configuration parameters 
 * @author kamboj_a
 * 
 *
 */
public interface IEnvConfig {

	public boolean isDebug();
	
	/**
	 * Get the DB username
	 * @return
	 */
	public String getDBUserName();
	
	/**
	 * Gets the DB password
	 * @return
	 */
	public String getDbpassword();
	
	/**
	 * Execution environment name
	 * @return
	 */
	public String getEnvironmentName();
	
	/**
	 * Get the DB host Url
	 * @return
	 */
	public String getDBurl();
	
	/**
	 * Get the ESB Server url
	 * @return
	 */
	public String getEsbServerUrl();
	
	/**
	 * Get ESB host name
	 * @return
	 */
	public String getEsbUsername();
	
	/**
	 * Get ESB password
	 * @return
	 */
	public String getEsbPassword();
	
	/**
	 * Get ESB Q Name
	 * @return
	 */
	public String getEsbQname();
	/**
	 * Get Log File Location
	 * @return
	 */

	public String getPoisonQname();
	/**
	 * Get Poison Q Name
	 * @return
	 */

	public String getLogFileLocation();
	
	public String getProperty(String key);
	
	public void setProperty(String key, String value);

}
