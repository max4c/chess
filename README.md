# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Sequence Diagram
[version to be edited](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSgM536HHCkARYGGABBECE5cAJsOAAjYBxQxp8zJnRgoATw64iaAOYwADADoAHJiNQIAV2wwAxGmBUdMAEoojSDtrCSBBoLgDuABZIYCiYiKikALQAfCzUlABcMADaAAoA8mQAKgC6MAD09spQADpoAN5VlG4AtigANDC4HBxh0NKdKC3ASAgAvpgU6bApaWySHFlQvv4xUAAUTVCtHV1Kvf2Dw6MAlJOs7Bwws0Ii4guLMEYoYACq1ZvVO2e3YhJS11SaiyZAAogAZUFwIowLY7GAAMzsLVh1Uwv3uAMSsym804WTQ9gQCHO1EugJgGP+PSyIGWwhQ70on2awDanW6Byg0h+ckxPQpwJgAEkAHJgrwwuFs3acvrco4jBAi0VFfKo2i48k3PnUx50lAM0T2MARFnbGW8u56wXyLJiiVSr4yzrAE0RIoQADW6BVapgbtN6N1D2uOIuCyygY93vQnWlbVJeKu2NSuMyAfdnp9aHaCdi6Zms3iWiyACZTKY6vVo9m4-mJuhpBpbA4nM5oPxnjBwRA-KFnJForES0k06wM3lCqUKsoesE0NX8xz9vLpBNC4CtZGYAg+0g0Oadu05f0ztvOBSqQ8ss83h981a-qHZkKwZDoRqLW1Eciv8HrRfcMyR3JkoF+JNtVSa8pFpekYmNU0j0tADnyxIE7RVR0v2PTNTTrUIxX9aNUP5FMUgvR5lzwmMc0ghYw2SQssmo2tYzQJNKFTUcwHLSsl2ddkaIIxs0GbGw7EcFxbBQX1eyMBxmEHKIYjiZAtApZicgECFQSKUEynKOcOAXOo2LozcKIjfFdz7RT1nM9Bz2slNkhgmknheRCzUctAnzIwE310z9fN-CAURI9zyPHECbOjCDKKvENYNUFAEBeFBvIcrN2P8m1X0wnTIX04T2LCiL3VI-KrNixZ6MvVNmM4qBuPUjA+NMTQxJbST22WaQe1WGAAHEZSuZThzUhJmGAmgoCybJhr0wyjBlMycosicZiYlysmQAJRraDhsvw3L6tcqLby890TtopyqpfZIgo-GFQqRcKaIerFZsuKN3QSlyUku3dVkOzh1jyh4UmeqFXuJJ4xvKhHEyixjKNvMbzrRraMlWo7mta6aOpgOoYHqPHODqbJyZlYUBHaUmhxiMDj1J+QEFAL0WZdUmKdFNbQjGEpRPE1spOcbB7CgbB0vgeCVDB8IVJHNqZpiuaFoKYoVoF3zqz5mUNy2rddpgA0GTB26CM6A22mc2rAWBu8st8yH0JhkKNt9d6KqDVHUzmX6aIBh2dUAlLzZiS3bZQN2BQK+1xVBSVkZQfmfyI9UKa++Ofp3Cm6axgOtILgQCZSHiOv12mBBFnq2xcHQ0r3MIYAAKQgA8RplFx2c5qaNNmydcleGdygp9bTpzaseLgCA9ygG2a6N6YTdqrIACtO7QS3fJPDmm6gOeF-aUv7eTR3ko852btdnPXI916vdCH3Pv9vObLAkPkyBq-HjvaONc46uVUJhd8sNU7pxUK-bO791ZBzBt-S4v9w4eUcLIKOMp1gx2AbaLIrxcgCFECVGOMAu6wL-tjdeRcaoa3LskSuMAKydSbPXcWthgCeEQIaWAwBsAy0IEEEIStJo8U0jjbSul9KGTUM1NeyZaTpTwKIfhEMi5uT-oonhaj-ZPUKlI0EAZ4ZbCuK-LY99Ar6OKoY4A8MKamL-BQ1BD8rF6RsfDXyDiPqRUoY1XaNCdrTAyPQxhzCurNiAA)
[version to be viewed](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSgM536HHCkARYGGABBECE5cAJsOAAjYBxQxp8zJnRgoATw64iaAOYwADADoAHJiNQIAV2wwAxGmBUdMAEoojSDtrCSBBoLgDuABZIYCiYiKikALQAfCzUlABcMADaAAoA8mQAKgC6MAD09spQADpoAN5VlG4AtigANDC4HBxh0NKdKC3ASAgAvpgU6bApaWySHFlQvv4xUAAUTVCtHV1Kvf2Dw6MAlJOs7Bwws0Ii4guLMEYoYACq1ZvVO2e3YhJS11SaiyZAAogAZUFwIowLY7GAAMzsLVh1Uwv3uAMSsym804WTQ9gQCHO1EugJgGP+PSyIGWwhQ70on2awDanW6Byg0h+ckxPQpwJgAEkAHJgrwwuFs3acvrco4jBAi0VFfKo2i48k3PnUx50lAM0T2MARFnbGW8u56wXyLJiiVSr4yzrAE0RIoQADW6BVapgbtN6N1D2uOIuCyygY93vQnWlbVJeKu2NSuMyAfdnp9aHaCdi6Zms3iWiyACZTKY6vVo9m4-mJuhpBpbA4nM5oPxnjBwRA-KFnJForES0k06wM3lCqUKsoesE0NX8xz9vLpBNC4CtZGYAg+0g0Oadu05f0ztvOBSqQ8ss83h981a-qHZkKwZDoRqLW1Eciv8HrRfcMyR3JkoF+JNtVSa8pFpekYmNU0j0tADnyxIE7RVR0v2PTNTTrUIxX9aNUP5FMUgvR5lzwmMc0ghYw2SQssmo2tYzQJNKFTUcwHLSsl2ddkaIIxs0GbGw7EcFxbBQX1eyMBxmEHKIYjiZAtApZicgECFQSKUEynKOcOAXOo2LozcKIjfFdz7RT1nM9Bz2slNkhgmknheRCzUctAnzIwE310z9fN-CAURI9zyPHECbOjCDKKvENYNUFAEBeFBvIcrN2P8m1X0wnTIX04T2LCiL3VI-KrNixZ6MvVNmM4qBuPUjA+NMTQxJbST22WaQe1WGAAHEZSuZThzUhJmGAmgoCybJhr0wyjBlMycosicZiYlysmQAJRraDhsvw3L6tcqLby890TtopyqpfZIgo-GFQqRcKaIerFZsuKN3QSlyUku3dVkOzh1jyh4UmeqFXuJJ4xvKhHEyixjKNvMbzrRraMlWo7mta6aOpgOoYHqPHODqbJyZlYUBHaUmhxiMDj1J+QEFAL0WZdUmKdFNbQjGEpRPE1spOcbB7CgbB0vgeCVDB8IVJHNqZpiuaFoKYoVoF3zqz5mUNy2rddpgA0GTB26CM6A22mc2rAWBu8st8yH0JhkKNt9d6KqDVHUzmX6aIBh2dUAlLzZiS3bZQN2BQK+1xVBSVkZQfmfyI9UKa++Ofp3Cm6axgOtILgQCZSHiOv12mBBFnq2xcHQ0r3MIYAAKQgA8RplFx2c5qaNNmydcleGdygp9bTpzaseLgCA9ygG2a6N6YTdqrIACtO7QS3fJPDmm6gOeF-aUv7eTR3ko852btdnPXI916vdCH3Pv9vObLAkPkyBq-HjvaONc46uVUJhd8sNU7pxUK-bO791ZBzBt-S4v9w4eUcLIKOMp1gx2AbaLIrxcgCFECVGOMAu6wL-tjdeRcaoa3LskSuMAKydSbPXcWthgCeEQIaWAwBsAy0IEEEIStJo8U0jjbSul9KGTUM1NeyZaTpTwKIfhEMi5uT-oonhaj-ZPUKlI0EAZ4ZbCuK-LY99Ar6OKoY4A8MKamL-BQ1BD8rF6RsfDXyDiPqRUoY1XaNCdrTAyPQxhzCurNiAA)


## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
