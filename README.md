## BDD Cucumber With Serenity

### Feature File
![img_2.png](img_2.png)


### Test Containers:
![img_1.png](img_1.png)


### Report Location: target/reports/cucumber.html
![img.png](img.png)


### Serenity Report  target/site/serenity/index.html
![img_5.png](img_5.png)
![img_3.png](img_3.png)


### Step to run cucumberTest
~~~
1. Run mvn clean install -DskipTests
2. Goto test directory
3. Run TestRunner file.
    i. It will run a mongo docker container
    ii. After that it will pick your jar from target folder and will create a docker image.
    iii. Once test run done it will create a test report and place that inside target/reports/cucumber.html
~~~


### Step to run SerenityTest
~~~
1. Run mvn clean install -DskipTests
2. Run mvn verify
3. Automatically Test will get start
    i. It will run a mongo docker container
    ii. After that it will pick your jar from target folder and will create a docker image.
    iii. Once test run done it will create a test report for both cucumber and serenity and place that inside target/site/serenity/index.html
~~~



### CURL Request for API
~~~
curl --location 'http://localhost:8080/users' \
--header 'Content-Type: application/json' \
--data '{
    "id": "1",
    "name": "DJ",
    "address": "BLR"
}'


curl --location 'http://localhost:8080/users'
~~~