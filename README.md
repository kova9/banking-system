# Banking System 

### Before you start: 
    1. There are 10 fixed Customer ID's to make the testing simplier.
    2. I'm not using long values as Keys in the DB Tables because of personal preferences but there is an AccountId Enum aswell as 
       an CustomerId Enum, so while testing you can use Id's 1-10 or the real UUIDs.
    3. The Email used for the Mailing service is an test Address created for this project, so there is no need for 
       any kind of configuration. 
    4. If you want to receive a mail, just edit the e-mail address in the customers.json file
    5. 100 000 transactions will get imported at the start of the server, aswell as 10 Customers and 20 accounts (two accounts per customer).
    

### Example of requests:

#### 1. save transaction data: /transaction/ (POST)

{<br>
    "senderAccountId":"4", <br>
    "receiverAccountId":"10",<br>
    "amount":100000,<br>
    "currencyId":"EUR",<br>
    "message":"testMessage"<br>
}<br>
<br>
 #### 2. get customer data: /customer/{id} (GET)
 /customer/5 <br>
 or <br>
 /customer/c805f63b-410d-4916-87cb-23b2df4d5f07
 <br>
 <br>
 #### 3. get transaction history for customer: /transaction/history/{customerId} {GET}
 /transaction/history/5?minAmount=10&currency=EUR <br>
 or <br>
 /transaction/history/c805f63b-410d-4916-87cb-23b2df4d5f07?maxAmount=10000&currency=USD
<br>
<br>
 #### 4. create new customer: /customer/ {POST}
 {<br>
    "name":"Pero Peric", <br>
    "address":"Ulica Pere Perica 12", <br>
    "email":"pero.peric@peric-pero.hr", <br>
    "phoneNumber":"0992699646" <br>
 }<br>
<br>
#### 5. transaction storno: /transaction/storno/{transactionId}
/transaction/storno/c99740a2-4e7f-44c1-8c35-e754d0a2113a
<br>
<br>
#### There is no need for external software.
<br>

#### All features from the specificatons are included and some more basic endpoints.
<br>

#### Have fun :)
    
