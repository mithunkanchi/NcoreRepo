Feature: Test FCMA Genium Futures and Options.
  As a Genium User
  I want to make sure that my futures and options are properly updated in Genium whenever there is a change from golden source
  So that I have the right information

  Background:
    Given that the tests are to be run on "test" environment
    And I clear the genium Poison Queue

 # Other Scenarios:
  Scenario Outline: Scenario sending an input xml to ESB and checking in Genium web service
    Given that I prepare the "<InputXml>" for submission to Genium
    And I submit the input xml to ESB
    When I retrieve data from Genium
    Then the genium data should match to the xml submitted

  #Input XMLS are covering both NEW and AMEND scenarios
    Examples:
      |InputXml|
      |src\\test\\resources\\data\\New\\EAH2022F-NEW.xml|
      |src\\test\\resources\\data\\New\\EAF2022C0002000-NEW.xml|

  #Error Scenarios:
  Scenario Outline: Scenario sending an input xml to ESB and checking in Genium web service
    Given that I prepare the "<InputXml>" for submission to Genium
    And I submit the input xml to ESB
    And I check that the error has been logged in the log file
    And the inputXml is in the poison Queue
    Examples:
      |InputXml|
      |src\\test\\resources\\data\\New\\EAH2022F-NEW.xml|
      |src\\test\\resources\\data\\New\\EAF2022C0002000-NEW.xml|

  #Delete Tests before next run
  Scenario Outline: Scenario sending a delete  xml to ESB and checking in Genium web service
    Given that I prepare the "<InputXml>" for submission to Genium
    When I submit the input xml to ESB
    Then there should be no data in Genium
    Examples:
      |InputXml|
      |src\\test\\resources\\data\\New\\EAF2022C0002000-DELETE.xml|
      |src\\test\\resources\\data\\New\\EAH2022F-DELETE.xml|

  #Error Scenarios: To be tested
  Scenario Outline: Scenario sending an input xml to ESB and checking in Genium web service
    Given that I prepare the "<InputXml>" for submission to Genium
    And I submit the input xml to ESB
    And I check that the error has been logged in the log file
    And the inputXml is in the poison Queue
    Then there should be no data in Genium
    Examples:
      |InputXml|
      |src\\test\\resources\\data\\New\\EAH2022F-AMEND.xml|
      |src\\test\\resources\\data\\New\\EAF2022C0002000-AMEND.xml|
      |src\\test\\resources\\data\\New\\EAF2022C0002000-DELETE.xml|
      |src\\test\\resources\\data\\New\\EAH2022F-DELETE.xml|

   #Sending Empty XML
  Scenario:  Scenario sending an empty input xml to ESB and checking from genium web service
    Given I submit an empty input xml to ESB
    Then the inputXml is in the poison Queue
    And I check that the error has been logged in the log file










