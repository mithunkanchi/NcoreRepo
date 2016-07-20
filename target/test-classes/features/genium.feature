Feature: Test FCMA Genium Futures and Options.
  As a Genium User
  I want to make sure that my futures and options are properly updated in Genium whenever there is a change from golden source
  So that I have the right information

 Background:
 Given that the tests are to be run on "test" environment

  # New and Amend Scenarios
  Scenario Outline: Scenario sending an input xml to ESB and checking in Genium
    Given that I prepare the "<InputXml>" for submission to Genium
    And I submit the input xml to ESB
    When I retrieve data from Genium
    Then the genium data should match to the xml submitted

    #Input XMLS are covering both NEW and AMEND scenarios
    Examples:
      |InputXml|


  # Delete Scenarios
  Scenario Outline: Scenario sending a delete xml to ESB and checking in Genium
    Given that I prepare the "<InputXml>" for submission to Genium
    And I submit the input xml to ESB
    Then there should be no data in Genium
    Examples:
      |InputXml|
      |src\\test\\resources\\data\\New\\EAF2022C0002000-DELETE.xml|
      |src\\test\\resources\\data\\New\\EAF2022P0002000-DELETE.xml|
      |src\\test\\resources\\data\\New\\EAF2022C0002100-DELETE.xml|
      |src\\test\\resources\\data\\New\\EAF2022P0002100-DELETE.xml|
      |src\\test\\resources\\data\\New\\EAH2022F-DELETE.xml|