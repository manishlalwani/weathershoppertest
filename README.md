# weathershoppertest
   
UI Automation Framework is developed using Selenium ,Java, TestNG with Page Object design pattern.

Under src/main

1. Created "com.flinkpages.pages" package and which has page classes to hold element locators and page methods(actions)
on those locators to mimic the real user actions.

2. Created "com.flinkpages.properties" package which has application.properties file to hold key,value pair of browser,
application url and some browser properties.

3. Created "com.flinkpages.utilities" package which has Utility Classes like below
       ElementUtil    : Custom Methods to interact with locators using selenium provided methods
       JavaScriptUtil : Custom JavaScript methods to interact directly with dom elements in case selenium/ElementUtil
                        methods are not able to interact.
       DriverFactory  : This Class contains methods for browser initialization, reading properties file
       OptionsManager : This Class contains options for browsers.
       
src/test
 
1. Created "com.flinkTestCases.basetest" package which has basetest class to Setup the driver before starting any test
    and tear down(quit) browser after performing the test.

2. Created "com.flinkTestCases.TestCases.testcases" package which has class to hold test cases with @Test TestNG
    annotation to execute and this class extends the basetest.java class from com.flinkTestCases.basetest" package 
    so that it can use setup and tear down methods.

3. Created "com.flinkTestCases.TestCases.runner" package which has testng.xml file where details around test cases to run
   along with additiona details like below
      1. browser XML parameter values to pass so that test cases should run on those
      2. parallel and thread-counts tags to run tests in parallel
      
    
