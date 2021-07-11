# Mini Redis

Mini Redis implementation

## Implemented commands

- SET key value
- SET key value EX seconds
- GET key
- DEL key
- DBSIZE
- INCR key
- ZADD key score member
- ZCARD key
- ZRANK key member
- ZRANGE key start stop

## Usage

## As an Application
- Run `Application.java`
- Write the command down

## As a Web Application
- Run `Web.java`
- curl to `localhost:8080/?cmd=<wanted command>`

## Tests

Test sources are in `src/test` divided by domain specific and Application tests
