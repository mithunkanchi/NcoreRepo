Link Name - Test Automation for Future Contracts Maintenance Automation

    Following are tests being covered under this project:

    Feature: Test FCMA DownStream Futures.
    As a Downstream User
    I want to make sure that my futures are properly updated in DownStream whenever there is a change from golden source
    So that I have the right information

      Background:
       Given that the tests are to be run on "test" environment

        #Positive Tests
         Scenario Outline: Scenario sending an input xml to ESB and checking in downstream db
            Given that I prepare the "<InputXml>" for submission
            And I submit the input xml to ESB
            When I retrieve data from the Downstream db
            Then the data should match to the xml submitted

            #Input XMLS are covering both NEW and AMEND scenarios
            Examples:
            |InputXml|
            |src\\test\\resources\\data\\BNH19-invalid-couponrate.xml|
            #Options XML
            |src\\test\\resources\\data\\BNH2017C0002200_Invalid Cycle class.xml|


        #Negative Tests
            Scenario Outline: Scenario sending an invalid input xml to ESB and checking in downstream db
            Given that I prepare the "<InputXml>" for submission
            And I submit the input xml to ESB
            And I check that the error has been logged in the log file
            When I retrieve data from the Downstream db
            Then the data should not be updated in the db

        #Input XMLS having atleast one field with an invalid entry.
             Examples:
            |InputXml|
            |src\\test\\resources\\data\\BNH2017P0002200.xml|
