package com.asx.fcma.tests.adapter.definitions;


import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

/**
 * Class to hold all the setup and tear down activities and the properties for the single scenario.
 * @author kamboj_a
 *
 */

public class LogBTestScenario {

	private static Scenario scenario = null;


	@Before
	public void before(Scenario s){
		System.out.println("\n\n********  STARTING SCENARIO --  " + s.getName() + "   ********\n");
		LogBTestScenario.setScenario(s);
	}

	@After
	public void after(Scenario s){
		System.out.println("\n\n********   FINISHING SCENARIO --  " + s.getName() + "   With Status = " + s.getStatus() + "   ********\n");
	}

	public static Scenario getScenario() {
		return scenario;
	}

	public static void setScenario(Scenario scenario) {
		LogBTestScenario.scenario = scenario;
	}
}
