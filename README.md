# Credit Card Management System  _(CreditCard-Service-Api)_


**OVERVIEW**

Credit Card Management System is a service which provides credit cards to users and maintains all the data and transaction details of a credit card. By using this system a customer can view his credit card(s) details and can verify all the transaction details done by his/her card(s). 

**GOALS**

    1. Provide credit cards to new customers
    2. Maintain all transaction records of a credit card
    3. Add reward points to credit card on each transaction/purchase
    4. Add/View/Redeem reward points
    5. View/Search Credit Card Transactions

**SPECIFICATIONS**

This service will offer the customers to apply for a credit card. Customers can view his/her credit card details by login into the application and can view all the transactions made by the credit card. Customers can also view the reward points gained on his/her transaction and can also redeem the reward points. Customers can view his/her credit card bills that are generated every month and can pay the bills from this application.

**User Cases**

**1.Apply for Credit Card**

> A new customer should be able to apply for a credit card. And he/she can track the request status. Once the admin team approves the request he/she should be able to see their Credit Card.

**2.User should able to see credit cards**

> Once the Credit Card is generated for the request, the user should be able to see his credit card and details. Here the user can see his list of credit cards that he/she owns and can also see individual credit card details.

**3.User should able to see statement of a credit cards**

> Users should be able to search for the transactions that are done on a credit card between a time period. And can also filter by debit and credit transactions.

**4. User should able to see Rewards Points**

> Add reward points on each transaction which is greater than Rs. 100. User should able to see total reward points on a credit card

**5. Show expected cashback on the redeem reward points**

> Each reward point value is Rs. 0.35. Users are able to check the cashback amount on redeem reward points when a credit card has minimum 4000 reward points.

**6. Redeem reward points**

> Users can redeem reward points when a credit card has a minimum 4000 reward points. Each reward point value is Rs. 0.35 and this cashback amount is converted in bill settlement.

    
# Tech

`Java 11`
`Spring Boot 2.6.0`
`Restful Webservices`
`MS SQL SERVER`
`JUnit 5`
`Mockito Framework`
`Maven`

