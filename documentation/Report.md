# Test report
  
### Short description
  
Automated testing of the tour selling application.
The work was carried out according to [plan](Plan.md). Scripts were written for testing the UI, for the correctness of checking the entry into the database, for testing the application API.
[List of found bugs](issues)  
    
    
### Number of test cases
  
UI testing - 19  
API testing - 23  
  
  
### % of successful / unsuccessful 
  
UI testing - 42%/58%  
API testing - 13%/87%  
  
  
### General recommendations
  
Fix bugs according to the above list, in particular:  
* Incorrect operation of the application with the database 
* Incorrect messages when submitting a form with empty fields
* Incorrect error handling when submitting a form directly with a POST request without using a form
* Make buttons "Buy" and "Buy on credit" the same color