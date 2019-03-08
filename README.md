# newBot-for-ffin.kz Currency Rate Telegram Bot

This Telegram Bot can be used to get the latest Currency Rate from https://openexchangerates.org.
You can find this Bot in Telegram by id @KZTtoUSDBot.

## Deployment
This is updated Spring Boot application that response with the latest KZT/USD conversion rate.
How it works:
1.recieve updated currency rate every 10 min from api https://openexchangerates.org/api/latest.json?app_id=16b7885aeffc45998bc5ac35fbf9e0d4
2.save it on h2 database
3.bot is using records in the database of conversion rate

## How to run
You can run this application by typing into the terminal(cmd) "mvn spring-boot:run"

## Built With

* [Spring Boot](https://spring.io)
* [Telegrambot](htp://www.telegram.org/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [H2](http://www.h2database.com) - DDataBase
