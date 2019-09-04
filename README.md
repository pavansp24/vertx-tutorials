# vertx-tutorials
Examples related to Vertx
<ul>
<li><b><a href="https://github.com/pavansp24/vertx-tutorials/tree/master/api-simulator" value="Api Simulator"></a></b></li>
<li><b>vertx-spring boot integration </b></li>
</ul>


<b>Api Simulator</b> - Application is all about building a configurable https rest api that will help to mock a service.

Run the verticle with proper jvm args requested, use the simulate api to submit the response expected to be returned by the simulated api.

Step1:
curl -X POST \
  https://localhost:8443/simulate \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 25' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8443' \
  -H 'Postman-Token: 93276b41-ad28-4b64-861d-a89527d6a637,b62d59cf-44aa-47b3-8ee8-c750aa02e56a' \
  -H 'User-Agent: PostmanRuntime/7.16.3' \
  -H 'cache-control: no-cache' \
  -d '{
	"test":"successfull"
}'


Step2:
curl -X POST \
  https://localhost:8443/auth \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: ' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8443' \
  -H 'Postman-Token: 716c67cf-dcd7-43b0-b9c8-874e874c132e,54c255d7-9f43-488b-b46b-e9535645f6db' \
  -H 'User-Agent: PostmanRuntime/7.16.3' \
  -H 'cache-control: no-cache'
  
  

<b>vertx-spring boot integration </b>- CRUD service using vertx and spring boot.

- Used vertx capability for creating a rest solution
- Used spring for dependency injection and data framework wrapped on top of hibernate for orm operations

mvn clean install

navigate to target folder and run the jar

 java -jar vertx-springboot-0.0.1-SNAPSHOT.jar


CURL Commands:

Submit a  transaction :

curl -X POST \
  http://localhost:8080/payment/authorize/report \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 331' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: 042edd5d-e76f-4e26-84c8-7a2766959899,cce85aad-15cd-444a-bc14-06da6dfa5265' \
  -H 'User-Agent: PostmanRuntime/7.16.3' \
  -H 'cache-control: no-cache' \
  -d '{
	"firstname":"pavan",
	"lastname":"puranik",
	"cvv":"123",
	"cardnumber":"1111222233334444",
	"expdate":"042022",
	"totalamt":"500",
	"sender":"test@vpa",
	"receiver":"receiver@vpa",
	"ip":"127.0.0.1",
	"city":"bangalore",
	"pincode":"560020",
	"ecommerce":"fashion",
	"email":"test@gmail.com",
	"transactionid":"1567619885322"
}'

Get transaction details by transaction id:

curl -X GET \
  http://localhost:8080/payment/authorize/report/1567618618017/ \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 331' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: 6be688dd-4ef3-4a15-9645-29c6368097f0,da095bbc-cae4-4bfe-a078-7c69abdfa66c' \
  -H 'User-Agent: PostmanRuntime/7.16.3' \
  -H 'cache-control: no-cache' \
  -d '{
	"firstname":"pavan",
	"lastname":"puranik",
	"cvv":"123",
	"cardnumber":"1111222233334444",
	"expdate":"042022",
	"totalamt":"500",
	"sender":"test@vpa",
	"receiver":"receiver@vpa",
	"ip":"127.0.0.1",
	"city":"bangalore",
	"pincode":"560020",
	"ecommerce":"fashion",
	"email":"test@gmail.com",
	"transactionid":"1567619885322"
}'


Get Paginated response:
curl -X GET \
  'http://localhost:8080/payment/authorize/report?page=0' \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 331' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: b64d9feb-2035-49f1-9d64-9e062e855824,f7f30261-4869-405d-b6b0-05cc50aed211' \
  -H 'User-Agent: PostmanRuntime/7.16.3' \
  -H 'cache-control: no-cache' \
  -d '{
	"firstname":"pavan",
	"lastname":"puranik",
	"cvv":"123",
	"cardnumber":"1111222233334444",
	"expdate":"042022",
	"totalamt":"500",
	"sender":"test@vpa",
	"receiver":"receiver@vpa",
	"ip":"127.0.0.1",
	"city":"bangalore",
	"pincode":"560020",
	"ecommerce":"fashion",
	"email":"test@gmail.com",
	"transactionid":"1567619885322"
}'
