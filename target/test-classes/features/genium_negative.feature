Feature: Test FCMA Genium Futures and Options with invalid data.
  As a Genium User
  I want to make sure that my futures and options are properly updated in Genium whenever there is a change from golden source
  So that I have the right information

  Background:
    Given that the tests are to be run on "test" environment
    And I clear the genium Poison Queue

  # Delete Tests for Negative Scenarios
  Scenario Outline: Scenario sending a delete  xml to ESB and checking in Genium webservice
    Given that I prepare the "<InputXml>" for submission to Genium
    When I submit the input xml to ESB
    Then there should be no data in Genium
    Examples:
      |InputXml|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000_DELETE.xml|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F_DELETE.xml|


    # Negative Tests
  @negativeTests
  Scenario Outline: Scenario sending an invalid input xml to ESB and checking in Genium webservice
    Given that I prepare the "<InputXml>" for submission with "<TagName>" and "<Value>"
    And I submit the input xml to ESB
    And I check that the error has been logged in the log file
    And the inputXml is in the poison Queue
    Then there should be no data in Genium

  #Input XMLS having atleast one field with an invalid entry.
    Examples:
      |InputXml|                                               TagName|     Value|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|EXERCISE_PRICE|abcd|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|PRICE_QUOTATION_FACTOR|abcd|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|ASX_MARKET_ID|123|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|ASX_MARKET_ID|aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|CREATED_DATE_TIME|abcd|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|CREATED_DATE_TIME|2015-11-12T27:72:40|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|DECIMAL_PLACES|12345678901234567890123456789012|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|EXPIRY_DATE|ABC|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|EXPIRY_DATE|122312|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|FIRST_ACTIVE_DATE|ABC|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|FIRST_ACTIVE_DATE|122312|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|FIRST_LISTED_DATE|ABC|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|FIRST_LISTED_DATE|122312|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|INSTRUMENT_STATUS|ABC|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|INSTRUMENT_STATUS|122312|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|INTEREST_PAYMENTS_PER_YEAR|abcd|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|INTEREST_RATE|abcd|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|INTEREST_RATE|123456789012345678901234567890123456789|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|LAST_ACTIVE_DATE|ABC|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|LAST_ACTIVE_DATE|122312|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|LAST_LISTED_DATE|ABC|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|LAST_LISTED_DATE|122312|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|SECURITY_TYPE_CODE|12332|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|SECURITY_TYPE_CODE|abcdabcda|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|SHARES_PER_CONTRACT|abcd|
      |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|SHARES_PER_CONTRACT|12345678901234567890123456789012|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|EXERCISE_PRICE|abcd|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|PRICE_QUOTATION_FACTOR|abcd|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|ASX_MARKET_ID|123|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|ASX_MARKET_ID|aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|CREATED_DATE_TIME|abcd|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|CREATED_DATE_TIME|2015-11-12T27:72:40|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|DECIMAL_PLACES|12345678901234567890123456789012|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|EXPIRY_DATE|ABC|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|EXPIRY_DATE|122312|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|FIRST_ACTIVE_DATE|ABC|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|FIRST_ACTIVE_DATE|122312|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|FIRST_LISTED_DATE|ABC|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|FIRST_LISTED_DATE|122312|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|INSTRUMENT_STATUS|ABC|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|INSTRUMENT_STATUS|122312|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|INTEREST_PAYMENTS_PER_YEAR|abcd|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|INTEREST_RATE|abcd|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|INTEREST_RATE|123456789012345678901234567890123456789|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|LAST_ACTIVE_DATE|ABC|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|LAST_ACTIVE_DATE|122312|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|LAST_LISTED_DATE|ABC|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|LAST_LISTED_DATE|122312|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|SECURITY_TYPE_CODE|12332|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|SECURITY_TYPE_CODE|abcdabcda|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|SHARES_PER_CONTRACT|abcd|
      |src\\test\\resources\\data\\NegativeTests\\EAF2022C0002000.xml|SHARES_PER_CONTRACT|12345678901234567890123456789012|
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|UNDERLYING_ASX_CODE|12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|SETTLEMENT_PRODUCT_CODE|12345678| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|SETTLEMENT_PRODUCT_CODE|abcdabcdab| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|VALUATION_METHOD|abcd| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|VALUATION_METHOD|1234| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|VALUATION_METHOD_NOMINAL|abcd| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|VALUATION_METHOD_NOMINAL|1234| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|SHARES_PER_CONTRACT|1.123456789012| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|LAST_UPDATED_DATE_TIME|abcd| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|LAST_UPDATED_DATE_TIME|1233| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|INTEREST_PAYMENTS_PER_YEAR|17.89| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|INTEREST_PAYMENTS_PER_YEAR|12345678901| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|DISPLAY_MONTH|111|**
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|DISPLAY_MONTH|1.7|**
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|DISPLAY_MONTH|ABC|**
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|DISPLAY_YEAR|22222|**
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|DISPLAY_YEAR|20.17|**
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|DISPLAY_YEAR|ABC|**
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|CREATED_DATE_TIME|1233| **
    #  |src\\test\\resources\\data\\NegativeTests\\EAH2022F.xml|DECIMAL_PLACES|100.12|  **





