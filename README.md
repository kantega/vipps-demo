# vipps-demo
Learn how to integrate your Spring Boot application with Vipps payment.

In this demo, you will learn how to set up and use Vipps payment services in a Spring Boot application.

This project contains two sub-modules; [application](./application/README.md) and [library](./library/README.md) -
where the application module contains a running Spring Boot web application, hosting a very simple shopping cart,
and the library module contains a service API which allows your application to quickly integrate with the Vipps eCom service.

This project is based on documentation and exampled found in various [Vipps repositories](https://github.com/vippsas),
including the [Vipps eCom API, version 2](https://github.com/vippsas/vipps-ecom-api),
the [Vipps developers](https://github.com/vippsas/vipps-developers) documentation, version 2.1.1,
and the [Vipps streamlabs](https://github.com/vippsas/vipps-streamlabs) demo application.

For Heroku integration, please see our [heroku-demo](https://github.com/kantega/heroku-demo) project.
If you would like to continue using MongoDB for your demo on Heroku,
go to [https://elements.heroku.com/addons/mongolab](https://elements.heroku.com/addons/mongolab) or run
```
$ heroku addons:create mongolab
```

Fetch your connection string by running
```
$ heroku config:get MONGODB_URI
```

Because we have a multi-module component in our repository, we need to specify the location of the application for Heroku.
The project's Procfile specifies the destination where Heroku should find the it.

We will provide Heroku with the environment variable to hold the application path.

```
$ heroku config:set PATH_TO_JAR=application/target/<application jar>
```
