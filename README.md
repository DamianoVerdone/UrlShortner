# Url Shortner 
The project is using [http4s](http://http4s.org/) and [circe](https://github.com/circe/circe).

The microservice allows two operation, adding a new long Url and redirecting the short url to the long one.

## End points
The end points are:

Method | Url           | Description
------ |---------------| -----------
GET    | /{externalId} | Redirect to the corrisponding long url if exist, 404 when no long url is present with this externalId.
POST   | /shorten      | Generate a short url, give as body JSON with the long url (destination field), returns a 201 with the created short url.

Here are some examples curl:

Create a short url:

``` curl --location --request POST 'http://localhost:8080/shorten' --header 'Content-Type: application/json' --data-raw '{"destination": "https:www.google.com"}'```


Get a short url (assuming the id of the short url is aaaaab):
```curl http://localhost:8080/aaaaab```






## Running
You can run the application with `sbt run`. By default it listens to port number 8080.

## Test
 
 * Execute unit test: 
``` stb test```

 * Execute Integration test:
```sbt it:test```
