# **API-Rate-Limiter**

It has 2 endpoints 
- (GET) "localhost:8080/users/data"
- (GET) "localhost:8080/hello"

### _Testing_
- Add header name="username" with any string value
- It will limit rate to 5 request per minute for each user
- Rate is not endpoint specific means, you can hit only 5 req per min what ever is the endpoint