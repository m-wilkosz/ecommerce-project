# eCommerce Project
[![](https://skills.thijs.gg/icons?i=java,spring,ts,angular,mysql,hibernate,html,css,maven,postman)](https://skills.thijs.gg)

### Description
This is a fullstack ecommerce project utilizing Angular and Spring Boot frameworks.

### Features
- users can create their user account
- users can add products to cart
- purchase process is implemented using Stripe
- pages are mobile responsive
- searching products by name
- products are divided by category

### Technologies
The project was created with:
- Java 17
- Spring Boot 2.7.2
- TypeScript 4.7
- Angular 14
- MySQL 8.0
- Hibernate 6.1
- HTML 5
- CSS 3
- Maven 3.8.6
- Okta 2.0.1
- Stripe 21.5.0

### Setup

#### Database setup
First, unpack db-scripts.tar.gz archive:
```
tar -xvzf db-scripts.tar.gz
```
Then use this SQL scripts to create database and fill application.properties file with your db credentials.

#### Frontend setup
First, you have to install npm and AngularCLI if you don't already have them:
```
sudo apt install npm
npm install -g @angular/cli
```
Then go to the right folder:
```
cd ecommerce-project/frontend/ecommerce-app
```
To install and run app use following commands:
```
npm install
ng serve --open
```

#### Backend setup
First, you have to install Maven if you don't already have it:
```
sudo apt install maven
```
To install and run app use following command:
```
mvn install
