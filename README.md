# retail-discount-engine
API to calculate discount

# Java Version 17
# Spring Boot 3.1.4
# Maven

# Retail Discount Engine
Run with 
`mvn clean package` then `java -jar target/*.jar`. POST /api/bills/calculate

# Deploy to AWS Lambda
1. Open the AWS Lambda Console → Create Function → Author from scratch
2. Runtime: Java 17
3. Upload your JAR file: target/your-app-name.jar
4. Set the handler: com.discount.retail.config.LambdaHandler::handleRequest
5. Configure memory (e.g., 1024 MB) and timeout (up to 15 minutes)
6. Save the function

# Configure API Gateway
1. Open the API Gateway Console → Create HTTP API or REST API
2. Add a resource/method → Integration type: Lambda function
3. Select your Lambda function
4. Deploy the API → Copy the endpoint URL


# API Usage
**API Endpoint** `/api/bills/calculate` <br>
**API Method** `POST` <br>
**API URL** `http://localhost:8081/api/bills/calculate`

**Sample Request**
```json
{
  "customerId": 3,
  "items": [
    {
      "itemId": 1,
      "quantity": 2
    },
    {
      "itemId": 3,
      "quantity": 7
    }
  ]
}
```
**Sample Response**
```json
{
  "grossTotal": 8500.00,
  "percentageDiscount": 420.0000,
  "flatDiscount": 400.0000,
  "netPayable": 7680.0000,
  "appliedPercentage": "LOYAL_CUSTOMER (5%)"
}
```