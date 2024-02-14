# Sorting Reviews

TBD - add Project description

## Assumptions
TBD
Parts of this exercise and its requirements are unclear, left out or ambiguous on purpose. 
Explicitly note any assumptions that you make.

### Production standards
TBD
You should also make sure that your solution meets production quality standards. 
You are free to define your own standards but please make sure that you can explain them.

## Run application

Run the server:
```shell
sbt run
```

Call the API:
```bash
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"start": 0,"end": 1708018606},"limit": 2,"min_number_reviews": 2' \
  http://localhost:8080/amazon/best-rated
[{"asin":"B000JQ0JNS","averageRating":4.5},{"asin":"B000NI7RW8","averageRating":3.6666666666666665},{"asin":"B0002F40AY","averageRating":3.3333333333333335},{"asin":"B00000AQ4N","averageRating":3.0},{"asin":"B000654P8C","averageRating":2.5}]
```

TODO - this is the expected call. Note the start and end types.
```bash
POST /amazon/best-rated HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "start": "01.01.2010",
  "end": "31.12.2020",
  "limit": 2,
  "min_number_reviews": 2
}
```

## Run tests

```shell
sbt test
```
