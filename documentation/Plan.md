### Test Automation Planning  

### 1. List of scripts to be automated

### UI and DB testing

####  Positive tests  
  1. Buying a tour by clicking the "Buy" button according to the data of a valid card (card status "APPROVED"). Checking the correctness of data writing to the payment_entity and order_entity tables in the database.  
  2. Buying a tour by clicking the "Buy on credit" button according to the data of a valid card (card status "APPROVED"). Checking the correctness of data writing to the credit_request_entity and order_entity tables of the database.  
  3. Buying a tour by clicking the "Buy" button according to the data of an invalid card (card status "DECLINED"). Checking the correctness of data writing to the payment_entity and order_entity tables in the database. 
  4. Buying a tour by clicking the "Buy on credit" button according to the data of an invalid card (card status "DECLINED"). Checking the correctness of data writing to the credit_request_entity and order_entity tables of the database.  
  
####  Negative tests  
  
  Purchase by the "Buy" button  
    
  5. Submitting a form with an empty field "Card number", the rest of the fields are filled in with valid data.  
  6. Submitting the form with an empty "Year" field, the rest of the fields are filled in with valid data.  
  7. Submitting a form with an empty "CVC / CVV" field, the rest of the fields are filled in with valid data. 
  8. Submitting a form with the "Card number" field containing 15 characters, the rest of the fields are filled in with valid data. 
  9. Submitting a form with a value greater than 12 and less than 100 in the "Month" field, the rest of the fields are filled in with valid data. 
  10. Submitting a form with a value greater than 30 and less than 100 in the "Year" field, the rest of the fields are filled in with valid data. 
  11. Submitting the form with the "Owner" field filled in in Cyrillic, the rest of the fields are filled in with valid data.
  12. Sending a form with a value containing 2 characters in the "CVC / CVV" field, the rest of the fields are filled in with valid data.
    
  Purchase by button "Buy on credit"
    
  13. Submitting a form with an empty "Month" field, the rest of the fields are filled in with valid data.  
  14. Submitting a form with an empty "Owner" field, the rest of the fields are filled in with valid data.  
  15. Submitting a form with all blank fields.
  16. Submitting a form with a "Month" field containing 1 character, the rest of the fields are filled with valid data. 
  17. Submitting a form with a value containing 1 character in the "Year" field, the rest of the fields are filled with valid data.  
  18. Submitting a form with a "Owner" field value of 257 characters, the rest of the fields are filled in with valid data. 
  19. Sending a form with the "Owner" field containing special characters, the rest of the fields are filled in with valid data.  
  
### API testing
  
#### Negative tests  
   
  Full pay purchase 
  
  20. Sending a POST-request for a purchase with an empty "Month" field, the rest of the fields are filled in with valid data. 
  21. Sending a POST request for a purchase with an empty "Owner" field, the rest of the fields are filled in with valid data.  
  22. Submitting a POST purchase request with all fields blank.
  23. Sending a POST request for a purchase with a "Month" field containing 1 character, the rest of the fields are filled with valid data.
  24. Sending a POST request for a purchase with a value containing 1 character in the "Year" field, the rest of the fields are filled with valid data.
  25. Sending a POST request for a purchase with the "Owner" field value of 257 characters, the rest of the fields are filled with valid data.  
  26. Sending a POST purchase request with the "Owner" field containing special characters, the rest of the fields are filled in with valid data.  
  27. Sending a POST purchase request with the "Card number" field containing 17 characters, the rest of the fields are filled in with valid data. 
  28. Sending a POST purchase request with the "Month" field containing 3 characters, the rest of the fields are filled with valid data.
  29. Sending a POST request for a purchase with a "Year" field containing 3 characters, the rest of the fields are filled with valid data.  
  30. Sending a POST purchase request with the "CVC / CVV" field containing 4 characters, the rest of the fields are filled with valid data.
  
  Purchase on credit
   
  31. Sending a POST request for a purchase on credit with an empty field "Card number", the rest of the fields are filled in with valid data.  
  32. Sending a POST request for a purchase on credit with an empty "Year" field, the rest of the fields are filled in with valid data. 
  33. Sending a POST request for a purchase on credit with an empty "CVC / CVV" field, the rest of the fields are filled in with valid data.  
  34. Sending a POST request for a purchase on credit with the "Card number" field containing 15 characters, the rest of the fields are filled in with valid data.  
  35. Sending a POST request for a purchase on credit with a value greater than 12 and less than 100 in the "Month" field, the rest of the fields are filled in with valid data.  
  36. Sending a POST request for a purchase on credit with a value greater than 30 and less than 100 in the "Year" field, the rest of the fields are filled in with valid data.  
  37. Sending a POST request for a purchase on credit with the "Owner" field filled in in Cyrillic, the rest of the fields are filled in with valid data. 
  38. Sending a POST request for a purchase on credit with a value containing 2 characters in the "CVC / CVV" field, the rest of the fields are filled with valid data.  
  39. Sending a POST request for a purchase on credit with the "Card number" field containing 16 characters, with values that differ from numbers, the rest of the fields are filled with valid data. 
  40. Sending a POST request for a purchase on credit with a "Month" field containing 2 characters, with values that differ from numbers, the rest of the fields are filled with valid data.  
  41. Sending a POST request for a purchase on credit with a "Year" field containing 2 characters, with values that differ from numbers, the rest of the fields are filled with valid data.  
  42. Sending a POST request for a purchase on credit with the "CVC / CVV" field containing 3 characters, with values that differ from numbers, the rest of the fields are filled with valid data.  
  
### 2. List of tools used with justification of choice
    
  1. Autotests are written in Java (tester's choice) 
  2. To test the UI, we will use selenide - a convenient tool for automated tests built on the basis of Selenium WebDriver, as it allows you to make tests more stable, allows you to handle timeouts in applications, automatic opening and closing of the browser, simpler syntax compared to the usual Selenium WebDriver.
  3. A library for junit tests with the following benefits:
     - It is a test environment.
     - Does not require user control during execution.
     - Ability to run multiple tests at the same time.
     - Messages about all errors during testing.
  4. Gradle auto build system for running tests. Benefits:
     - Flexible in settings
     - built into the IntelliJ Idea development environment
     - keeps track of what has changed in the build and subsequent builds are faster (incremental build)
  5. The allure reporting framework has many settings, produces beautifully designed reports  
  6. Rest Assured library - for API testing. Has a large set of functionality, for full use it is not necessary to deeply understand HTTP  
  7. DbUtils is a library for working with databases. Simplifies working with JDBC, provides clean and understandable code for performing database operations without the need to write code to clean up or prevent resource leaks 
  
### 3. List and description of possible risks (problems) during automation
  
  1. Configuring the correct launch of the test environment, since the container with the application is launched only after the service with the database in one container and the service of the banking services simulator in another container are started.  
  2. Complex structure of pages (complexity of writing locators) and subsequent debugging of scripts.
  3. Lack of documentation on the structure of the database and on the operation of the application with the database.
  4. Setting up test scripts on different databases (Mysql and Postgres)
  
### 4. Interval risk-based assessment (in hours)
  
1. Setting up a test environment - 8   
2. Writing and debugging autotests - 48  
3. Test run and bug description - 16  
4. Automation and testing report - 16  
Total: 88 hours  
  
### 5. Delivery plan
  
1. Setting up a test environment, writing and debugging autotests - 06.03.2020  
2. Test run and bug description - 11.03.2020  
3. Automation and testing report - 13.03.2020 
