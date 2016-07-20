package com.asx.fcma.tests.adapter.config;

import com.asx.fcma.tests.adapter.util.TestConstants;


/**
 * This class initializes the environment configuration.
 * @author kamboj_a
 * 
 *
 */
public class ConfigManager {

	private static IEnvConfig envConfig = null;
	private static ConfigManager configManager = null;
	private static String envName = "";

	private ConfigManager() {}

	public static ConfigManager getInstance(){
		if (configManager == null) {
			envName = System.getProperty(TestConstants.EXECUTION_ENVIRONMENT);
			envConfig = new EnvConfigImpl(envName);
			configManager = new ConfigManager();
		}
		return configManager;
	}

	public IEnvConfig getEnvConfig(){
		return envConfig;
	}
}
