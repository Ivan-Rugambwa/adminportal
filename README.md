## Seat Rapport Dokumentation


## Inledning

Denna dokumentation kommer att gå igenom projektets struktur och förklara målet för och vad varje del gör. 


## Övergripande struktur

Projektet är uppdelat i fem delar:



* Camunda
* Camunda Zeebe API
* Email
* Seat API
* Website

Camunda innehåller docker, alla diagram och former, templates för connectors samt connectors och ett Java Spring Boot API som används för att kommunicera med de andra delarna. 

Email innehåller alla e-mail som skickas ut i form av HTML-filer med intern CSS. Dessa filer kan ändras i realtid utan att behöva bygga eller starta om  projektet.

Seat API innehåller ett Java Spring Boot API som används för all hantering av data, autentisering och logik för webbsidan samt databasen.

Website innehåller ett backend i form av en NodeJS server vars enda uppgift är att servera HTML-filer för vår frontend. Frontend består endast av HTML, CSS och Javascript. All kommunikation mellan webbsidan och API:et sker i Javascript-koden.


## Konfiguration

Det finns konfigurationsfiler som behöver ändras/fyllas i för att programmet ska fungera, för mer information se avsnitt under respektive del.



* Camunda
    * /docker/connector-secrets.txt:

        Innehåller hemligheter som camunda använder.

    * /docker/docker-compose.yaml: 

        Innehåller konfiguration för Docker Compose.

* Seat API
    * application.yml:

        Denna fil ska inte behöva ändras.

    * secrets.yml:

        Denna fil innehåller all data som behöver ändras.

* Java Zeebe API
    * application.properties:

        Denna fil ska inte behöva ändras.

    * secrets.yml:

        Denna fil innehåller all data som behöver ändras.

* Webbsida:
    * /public/js/shared.js:

        Innehåller all data som behöver ändras för webbsidan.



## 


## Camunda


### .camunda

Innehåller templates för connectors som används i Camund Modeler programmet.


#### email.v2.json

Connector template som tillhör email-connector


#### get-users-by-business.json

Connector template som tillhör get-users-by-business connector


#### save.json

Connector template som tillhör save-data connector


#### seat.json

Connector template som tillhör create-seat connector


#### seat-single.json

Connector template som tillhör create-single-seat connector


### connectors

Innehåller alla connector projekt. De är skrivna i Java och baseras på Camundas connector-template [https://github.com/camunda/connector-template-outbound](https://github.com/camunda/connector-template-outbound). De används med connector templates som ligger i mappen .camunda.


#### create-seat

Connector för att skapa seat rapporter. Skapar rapporter för alla företag med datumet som skickas in, till exempel med “2020-01-01” så skapas en rapport för året och månaden 2020/01.


#### create-seat-single

Connector för att skapa en seat rapport. Skapar en rapport för datumet och företaget som skickas in, till exempel med “2020-01-01” och “APENDO” så skapas en rapport för året och månaden 2020/01 för företaget “APENDO”. Företaget måste finnas i databasen.


#### email-connector

Connector för att skicka e-mail. Den kan skicka 5 olika typer av e-mail:



* Seat report: skickar e-mail att kunden ska fylla i seat rapporten som skapats.
* Denial: skickar e-mail att rapporten som kunden fyllt i blev ej godkänd av support.
* Remainder: skickar påminnelse e-mail att kunden ska fylla i rapporten.
* Invoice: skickar e-mail till faktureringspersonal om kunden använt mer än deras baseline.
* All done: skickar ett e-mail när alla rapport för en månad har fyllts i.


#### get-users-by-business

Connector för att hämta alla kunder som tillhör ett företag. Till exempel om “APENDO” skickas in hämtas alla kunder i databasen som tillhör företaget.


#### save-data

Connector för att spara seat rapport. Används för att ändra seat rapport till “COMPLETE” när den blivit godkänd.


### docker

Innehåller alla delar som används av docker. 


#### .jar filer

Connectors som kommer från connectors-mappen.


#### connector-secrets.txt

Säkerhetsdata.

EMAIL=&lt;e-mail för admin konto>

PASSWORD=&lt;lösenord för admin konto>

FROM=&lt;e-mail för att skicka e-mail>

FROM_PASSWORD=&lt;lösenord för att skicka e-mail>

HOST=&lt;email host>

PORT=&lt;email host port>

API_URL=&lt;URL för seat API>


#### docker-compose.yml

Data för att skapa ett Docker Compose projekt. Innehåller delarna Operate, Tasklist, Zeebe, Elasticsearch och Connectors.




## Email

Innehåller alla email-HTML texter som skickas. 

I ett e-mail kan det finnas klammerparenteser, de innehåller ett variabelnamn som kommer att bytas ut när programmet använder filen. Till exempel om det står {firstName} kommer det att bytas ut till ett förnamn, till exempel “Kim”.

Alla e-mail kan ändras i realtid utan att behöva starta eller bygga om projektet. Du behöver bara göra ändringar och spara dem. När nästa e-mail skickas ut kommer det att använda den nya versionen.


### all-done.html

E-mail som skickas när alla rapporter för en viss månad är godkända.

Används i email-connector.


```
{forDate}: placeholder för rapporternas datum, ex. "2020/01".
```



### denied.html

E-mail som skickas när en rapport har blivit nekad av en supportpersonal.

Används i email-connector.


```
{firstName}: placeholder för kundens förnamn, ex. "Kim".
{lastName}: placeholder för kundens efternamn, ex. "Almroth".
{forDate}: placeholder för rapporternas datum, ex. "2020/01".
{reasonForDenial}: placeholder för anledningen av nekande av rapporten som skrivs av supportpersonal som nekar rapporten, ex. "Denna text är skriven av supportpersonal".
{reportUrl}: placeholder för rapportens URL, ex. "http://www.hemsida.se/rapport" eller "http://www.hemsida.se/rapport?uuid=<rapportens uuid>".
```



### invoice.html

E-mail som skickas till faktureringspersonal när en kund har rapporterat in mer användning än företagets baseline. 

Används i email-connector.


```
{businessName}: placeholder för företagets namn, ex. "APENDO".
{seatOverUsage}: placeholder för företagets överanvändning, ex. "5".
{forDate}: placeholder för rapporternas datum, ex. "2020/01".
{baseLine}: placeholder för företagets baseline, ex. "50".
{seatUsedAmount}: placeholder för företagets inrapporterade användning, ex. "55".
```



### password-reset.html

E-mail som skickas när en kund har begärt att återställa sitt lösenord.

Används i Seat API “passwordReset/PasswordResetService”.


```
{firstName}: placeholder för kundens förnamn, ex. "Kim".
{passwordResetUrl}: placeholder för länken till återställning av lösenord, ex. "http://www.hemsida.se/återställ?uuid=<återställnings-uuid>".
```



### register.html

E-mail som skickas till kund när en support/admin personal skapar ett konto för en kund. E-mailet innehåller en länk där kunden får skriva in ett lösenord.

Används i Seat API “preRegister/PreRegisterService”.


```
{firstName}: placeholder för kundens förnamn, ex. "Kim".
{lastName}: placeholder för kundens förnamn, ex. "Almroth".
{registerUrl}: placeholder för länken till skapande av lösenord, ex. "http://www.hemsida.se/skapa?uuid=<återställnings-uuid>".
```



### remainder.html

E-mail som skickas för att påminna kunden när den inte fyllt i rapporten inom ett visst antal dagar.

Används i email-connector.


```
{firstName}: placeholder för kundens förnamn, ex. "Kim".
{lastName}: placeholder för kundens efternamn, ex. "Almroth".
{forDate}: placeholder för rapporternas datum, ex. "2020/01".
{reportUrl}: placeholder för rapportens URL, ex. "http://www.hemsida.se/rapport" eller "http://www.hemsida.se/rapport?uuid=<rapportens uuid>".
```



### seat-report.html

E-mail som skickas när en rapport har skapats och ska fyllas i.

Används i email-connector


```
{firstName}: placeholder för kundens förnamn, ex. "Kim".
{lastName}: placeholder för kundens efternamn, ex. "Almroth".
{forDate}: placeholder för rapporternas datum, ex. "2020/01".
{reportUrl}: placeholder för rapportens URL, ex. "http://www.hemsida.se/rapport" eller "http://www.hemsida.se/rapport?uuid=<rapportens uuid>".
```





## Seat API

Java Spring Boot projekt som används för att kommunicera, och hantera data samt logik för databasen. Det är byggt så att varje del av API:et ligger i sin egen mapp, till exempel så ligger allt som har med “Account” att göra i en mapp som heter “account”. Den innehåller dto, model, controller, repository och service. 

Programmet använder JWT med refresh tokens för autentisering/auktorisering.


### Konfiguration

Ändringar kan göras utan att behöva bygga om projektet, för att ändra:



1. Gör ändringar och spara.
2. Starta om API:et.


#### application.yml

Innehåller konfiguration för Spring Boot.

Ingenting i denna fil ska behöva ändras.


```
spring:
 config:
   import: secrets.yml
 jpa:
   hibernate:
     ddl-auto: update
   show-sql: false
   properties:
     hibernate:
       format_sql: true
     dialect: org.hibernate.dialect.SQLServerDialect
 sql:
   init:
     mode: always
 main:
   banner-mode: off
logging:
 level:
   org.springframework: error
server:
 error:
   include-message: always
   include-stacktrace: never
springdoc:
 swagger-ui:
   path: /swagger-ui.html
```



#### secrets.yml

Konfigurationsfil för API:et.

Innehåller hemlig data och ska inte delas med andra.


```
spring:
 datasource:
   url: URL:en för databasen som ska kopplas
   username: användarnamn för databasen
   password: lösenord för databasen
 jpa:
  database: Vilken typ av databas, för MSSQL använd: sql_server
notion:
 adminEmail: email för första admin konto
 adminFirstName: förnamn för första admin konto
 adminLastName: efternamn för första admin konto
 secret: 128-hex string för kryptering av lösenord
 aes_key: 32-hex string för kryptering av databasfält
 smtp_auth_email: e-mailadress som används för att skicka e-mail
 smtp_auth_password: lösenord för e-mailadress som används för att skicka e-mail
 smtp_auth: smtp autentisering, true/false
 smtp_starttls_enable: smtp TLS kryptering, true/false
 smtp_host: smtp host
 smtp_port: smtp port
 mail_from: e-mailadressen som kommer stå som avsändare i e-mailen
 registerUrl: URl för att registrera, ex. http://camcaas.apendo.se/auth/register
 resetPasswordUrl: URL för att återställa lösenord, ex. http://camcaas.apendo.se/auth/reset/password
 enableSwagger: true/false. Aktivera/inaktivera Swagger. OBS: öppen för alla med tillgång till api:et.
 allowedCorsUrls:
 - Lista med URL:er som ska kunna kommunicera med API:et
 - Exempel: http://camcaas.apendo.se
 - Den enda URL:en som ska behövas är webbsidan.
server:
 port: port som API:et ska använda
```



### 


### Mappar


#### account

Konto för användare av programmet.

Innehåller inte registrering eller autentisering.


#### accountProfile

Data som tillhör konton.


#### auth

Registrering/autentisering/auktorisering av konton för åtkomst till API:et. Använder JWT samt refresh tokens för autentisering och auktorisering.


#### business

Data för företag


#### config

Konfigurationer för API:et.



* NotionConfigProperties

    Använder variablerna från secrets.yml filen.

* ColumnEncryptor

    Används för att kryptera token-fältet i databasen för Refresh Token. Se src/main/java/almroth/kim/seat_api/refreshToken/model/RefreshToken.java


    för användning.

* CorsConfig

    Använder variabeln “allowedCorsUrls” från “secrets.yml” för att sätta tillåtna URL:er. 

* SecurityConfiguration

    Använder variabeln “enableSwagger” från “secrets.yml” för att aktivera swagger. OBS: notera att alla kan komma åt sidan för swagger då den inte ligger bakom någon autentisering.

* DatabaseSeeder

    Skapar två roller, “admin” och “user”, samt med hjälp av “adminEmail”, “adminFirstName” och “adminLastName” variablerna registrerar den det första admin-kontot. Ett mail skickas ut som innehåller en länk där administratören får skapa ett lösenord för att aktivera kontot. 



#### error

Data för egen felhantering. 


#### mapper

Data för att automatiskt mappa olika klasser, exempelvis ett DTO till en model eller vice versa.


#### passwordReset

Data för att återställa lösenord.


#### preRegister

Data för att registrera användare.


#### refreshToken

Data för hantering av refresh tokens.


#### role

Data för hantering av roller.


#### seat

Data för hantering av seat-rapporter


### Endpoints

Dokumentation för endpoints är gjord med Swagger, för att aktivera swagger för API:et, ändra “enableSwagger” i “secrets.yml” till “true”. Starta om API:et och gå sen till “&lt;server-adress>/swagger-ui/index.html#/” eller “&lt;server-adress>/v3/api-docs”. 

Exempel: http://localhost:35462/swagger-ui/index.html#/

Notera att URL:en är öppen för alla (ingen autentisering). Det finns också en redan nedladdad version från “/v3/api-docs” här: **—LÄNK TILL API DOC FILEN— **

**Obs:** om programmet uppdateras bör filen också göra det genom att gå till “/v3/api-docs” och ladda ner sidan som en fil.

Innehållet i filen kan exempelvis skrivas in i [https://editor.swagger.io/](https://editor.swagger.io/) för att se endpoints utan att behöva aktivera Swagger i programmet. 


## 


## Java Zeebe API

API för att kommunicera mellan Camunda Zebee och resten av programmet. 

Använder sig av Spring Boot samt Camundas Java Zeebe klient:

[https://docs.camunda.io/docs/apis-tools/java-client/](https://docs.camunda.io/docs/apis-tools/java-client/)

[https://search.maven.org/artifact/io.camunda/zeebe-client-java](https://search.maven.org/artifact/io.camunda/zeebe-client-java)


### Konfiguration

“application.properties” används för att sätta in variabler som programmet använder

Ska inte behöva ändras.


```
zeebe.client.broker.gateway-address=127.0.0.1:26500
zeebe.client.security.plaintext=true
server.port=9001
spring.config.import=secrets.yml
```


“secrets.yml” används för att sätta in hemliga variabler som programmet använder:


```
notion:
 allowedCorsUrls:
   - Lista med URL:er som ska tillåtas genom CORS
   - Exempel: http://camcaas.apendo.se
   - Den enda adressen som ska behövas är webbsidan
```


Dessa filer kan ändras utan att bygga om programmet. 

För att ändra:



1. Gör ändringar och spara.
2. Starta om API:et.


### Endpoints

Det finns 3 endpoints:



* api/camunda/start/single

    Används för att starta en seat rapport från admin portalen


    ```
     "forDate": Datum för rapporten,
     "businessUuid": Företagets UUID
    ```



    Exempel:


    ```
    {
     "forDate": "2023-05",
     "businessUuid": "4d14eaa7-42b9-4dba-88d7-bee747dfcd53"
    }
    ```


* api/message

    Används för att meddela camundaprocessen att en kund har fyllt i deras användning från kundportalen, för att processen ska gå vidare.


    ```
     "message": namnet på "message event:et" i camunda processdiagrammet
     "email": Email från den som fyllde i rapporten
     "forYearMonth": Datum för rapporten
     "business": Företagets namn
     "seatUuid": Seat rapportens UUID
     "amountOfSeatUsed": Mängden av seat som använts
    ```



    Exempel:


    ```
    {
     "message": "report-response",
     "email": "jane@smith.com",
     "forYearMonth": "2023/05",
     "business": "MAX",
     "seatUuid": "678fec4a-1700-419c-be72-9e0b810c657b",
     "amountOfSeatUsed": 45
    }
    ```


* api/camunda/start

    Används för att starta processen för seatrapporter för alla företag för given månad.


    ```
     "processId": Camundaprocessens ID
     "variables": Lista med variabler för processen, "forDate" måste finnas med med ett datum i formatet "2020-01-01" eller "2020-01"

    ```


	

	Exempel:


```
    {
     "processId": "Process_15ghqno",
     "variables":
       { "forDate": "2023-05-25" }
    }
