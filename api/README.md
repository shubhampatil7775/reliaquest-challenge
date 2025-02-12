# Implement this API

#### In this assessment you will be tasked with filling out the functionality of different methods that will be listed further down.

These methods will require some level of API interactions with Mock Employee API at http://localhost:8112/api/v1/employee.

Please keep the following in mind when doing this assessment:
* clean coding practices
* test driven development
* logging
* scalability

### Endpoints to implement

_See `com.reliaquest.api.controller.IEmployeeController` for details._
 
### Curl for implemented Apis
    1. getAllEmployees -> curl --location 'http://localhost:8111/employee'
    2. getEmployeesByNameSearch -> curl --location 'http://localhost:8111/employee/search/shubham'
    3. getEmployeeById -> curl --location 'http://localhost:8111/employee/2a5dd95f-97a8-4001-8966-a4e7d4d32d95'
    4. getHighestSalaryOfEmployees -> curl --location 'http://localhost:8111/employee/highest-salary'
    5. getTop10HighestEarningEmployeeNames -> curl --location 'http://localhost:8111/employee/top-ten-highest-earners'
    6. createEmployee -> curl --location 'http://localhost:8111/employee' \ --header 'Content-Type: application/json' \ --data-raw '{ "name": "yash patil", "title": "Software Engineer", "salary": 100000, "age": 30, "email": "shubh@example.com" }'
    7. deleteEmployeeById -> curl --location --request DELETE 'http://localhost:8111/employee/6d9af443-b999-4a09-b78d-47fb37df7505'

getAllEmployees()

    output - list of employees
    description - this should return all employees

getEmployeesByNameSearch(...)

    path input - name fragment
    output - list of employees
    description - this should return all employees whose name contains or matches the string input provided

getEmployeeById(...)

    path input - employee ID
    output - employee
    description - this should return a single employee

getHighestSalaryOfEmployees()

    output - integer of the highest salary
    description - this should return a single integer indicating the highest salary of amongst all employees

getTop10HighestEarningEmployeeNames()

    output - list of employees
    description - this should return a list of the top 10 employees based off of their salaries

createEmployee(...)

    body input - attributes necessary to create an employee
    output - employee
    description - this should return a single employee, if created, otherwise error

deleteEmployeeById(...)

    path input - employee ID
    output - name of the employee
    description - this should delete the employee with specified id given, otherwise error

### Testing
Please include proper integration and/or unit tests.
