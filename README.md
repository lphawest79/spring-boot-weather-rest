

### Introduction

Spring boot based weather reporting application. Data is fetched from OpenWeatherMap (OWM), stored locally and presented to 
the client using a customised DTO. 


#### Instructions
* mvn spring-boot:run


* Obtain an apiKey from [(OWM)](https://openweathermap.org/api) 


* Access following url, 
	
		http://localhost:8080/api/weather?city={city}&country={country}&apiKey={apiKey}

#### Features

##### Rate Limiting
Limit the calls to Rest API based on the ApiKey. At the moment it is configured at 5x calls per minute. If this is exceeded then a HTTP.TOO_MANY_REQUESTS
is thrown.

##### Storing Data
If the data based on the key(city, country) doesnt exist, it will fetch from OWM and then store this data as a JSON format locally. 
If it does exist, then this will be fetched instead of invoking OWM.

##### Retrieving Data
When accessing the data, only the descriptions, is returned based on the requirement. This is achieved by using a WeatherInfoDto object, which wraps an entity weatherInfo object, as a way of presenting the data. 

Note this is an array to reflect the array of weathers[].description from OWM. 


##### GET /weather
```
Summary: Returns the description of the weather report for a provided location
Headers Content-Type: 
	application/json
Parameters:
	(String) city : required
	(String) country : required
	(String) apiKey : required

Success Response: 
	Code: 200

Error Response:
	Code: 429
	description: more than 5 requests within a 1 min interval for a specific key

Error Response:	
	Code: 400
	description: Provided a missing param

Error Response:	
	Code: 401
	description: Unauthorized
	
Error Response:	
	Code: 404
	description: Record not found

URL		
http://localhost:8080/api/weather?city={city}&country={country}&apiKey={apiKey}
```


#### Key technologies

##### Bucket4J 
* Bucket4J is used to limit the the client rest API calls. 

##### Spring Boot 3 / Spring 6 
* Using transactions
* reactive RestFul api when calling external systems
* leverage spring-data to perform basic find by queries
* Using aspects (ControllerAdvice) to catch-all exceptions and return to a client a formal json response
* Integration tests that mocks external rest api calls


#### Improvements
* At the moment only descriptions is returned to the client. The json data collected from OWM contains many other data points which may serve more useful.

* The application acts as facade to OWM and the user is forced to acquire a key in order to use it. Ideally if this was a production based application, the user should not know that it is interfacing with OMW and need not provide that key. 

* Would be good to provide a nice UI to present the data in a friendly format. 

* The data returned from OWM is stored as a json message in the local H2 database. If the data grows and we decide to query the json files then it will be inevitably slow. 
A separate system (microservice ?) should be used for storing and querying the data, possibly using a more relevent technology. 




