# ebank
s8 hiring challenges

# Code Organization
code is organized in microservice architecture include:
1. common (handle all dependencies and common function)
2. transaction (mainly handle transaction behavior)
3. user (mainly handle user behavior)

# User APIs
## register
1. check mail availability
2. create User object
3. encode pwd
4. insert into db

## login
1. find user from db by mail
2. compare encode pwd
3. build token by user info

## get name by user id (used for feign call by transaction service)
1. return the name by given user id

# User Account APIs
## create account
1. get current user
2. check if account already exist
3. create user account object
4. insert into db

## transfer (used for feign call by transaction service)
1. check if both send & receive user own that currency account
2. minus balance from send user
    - because I use a sql to complete the process,
    - it will only update when balance >= send amount
    - since sql check balance, and then update it in atomic action, so
    - it will not have error in multi-thread
3. add balance to receive user
    - only execute when third step success

# Transaction APIs
## transfer
1. create and set transactionDO object and check if send user has a same id as receive user
   2 to 4. feign call for user transfer service
5. check feign call status and save transactionDO object into db

## page
1. check input month&year
2. find paging based on size, page, and given month in given year
3. convert TransactionDO object into VO to show user
4. send to kafka for each transaction with id as key
5. consume kafka msg
6. return page, total page, current page info


# swagger documentation
can be found in:
http://localhost:7080/swagger-ui/index.html#/
http://localhost:7081/swagger-ui/index.html#/


