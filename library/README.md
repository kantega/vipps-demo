# Vipps-client-web

The content of this library provides a convenient integration with the payment service known as [Vipps](https://www.vipps.no/),
and more specifically the [Vipps eCommerce API](https://vippsas.github.io/vipps-ecom-api/).

The library's intended use is with an existing Spring Boot application (such as the bundled [demo application](../application/README.md)),
but will run alongside any simple application in a SpringBootApplication context:

    @SpringBootApplication
    @ComponentScan("no.kantega")
    public class Application {
    
    	public static void main(String[] args) {
    		SpringApplication.run(Application.class, args);
    	}
    }  

## Content

## Configuration Profiles

The library module has options for loading configuration profiles.
There are currently two profiles available:
- dev - Provided for development and testing.
- prod - Provided for production.

The default profile is "dev". To activate the production profile, you may launch the application as follows
(or embed the profile in any other recommended way):

```$ mvn spring-boot:run -Drun-arguments=--spring.profiles.active=prod```

### Environment variables
You need to define the following environment variables (Note that there are different values for testapi.vipps.no and api.vipps.no. Test variable are therefore post fixed **_TEST**, for separation and convenience):

Name | Value
---------|------
VIPPS_CLIENT_ID | Your client id (provided by Vipps)
VIPPS_CLIENT_SECRET | Your client secret (provided by Vipps)
VIPPS_MERCHANT_SERIAL_NO | This is the serial number for the payment receiver (provided by Vipps) Note that there are separate serials for test/production!
VIPPS_OCP_APIM_SUBSCRIPTION_KEY_ECOMMERCE | This is your API subscription key (provided by Vipps)
VIPPS_BASE_URL | This specifies the base url for the Vipps client, and will make the basis for all callback endpoints
VIPPS_FALLBACK_URL | This is the url Vipps uses to fall back to upon payment completion/cancellation

### Service module

The 'VippsService' class module provides methods for generating a token request (needed for all calls towards Vipps),
payment creation, capturing payments, cancelling an order, and retrieving details for a previously placed order.

#### Token creation

Although creating request tokens should not be a major concern to you, as this is handled automatically,
you should make a general note of this function.

Vipps will allow for tokens to live for 1 hour in development environments, while 24 hours in production.

#### Payment initialisation

The key feature of Vipps (and subsequently your Vipps application) is payment.
The service module provides direct payment initialisation, where required parameters are:
- A mobile number - to be charged for the payment.
- An (internal) order ID - for cross-referencing and tracking.
- A transaction text - which will be displayed with the payment transaction, to the end user.

A successful payment initialisation yields a Vipps order id, which may be used for subsequent requests
(such as cancelling or capturing a payment, reimbursement, or querying order details).

### REST API
The library extends a classic Java Spring Boot REST API interface, through the implementation of a [RestController](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestController.html).
The rest controller exposes endpoints required for Vipps callbacks.

According to the official [Vipps eCom API checklist](https://github.com/vippsas/vipps-ecom-api/blob/master/vipps-ecom-api-checklist.md),
an integration needs to fulfill the requirements of implementing callbacks for handling successful and unsuccessful payments,
providing shipping details (express checkout only), and removing user's consent (express checkout only).

Vipps endpoint URI paths are all mapped at ```/vipps/v2```

    NOTE! The Vipps documentation specifies that Vipps will append e.g. '/v2/payments/**' to your base callback url,
    indicating that the natural base callback url would be '<your site>/vipps', however because we will append '/vipps'
    to your provided base url, the correct base url should be '<your site>'.

## Limitations

Current project version limits integration to regular payments (not express checkout), and does not implement the required shipping endpoint for such a service. 
Nor is the required consent endpoint implemented, but responds the required 200 on request.
Future implementations may provide a different scenario.

## Requirements for Vipps integration

In order to integrate with Vipps, you need to enroll in the [Vipps Developer's programme](https://github.com/vippsas/vipps-developers/blob/master/vipps-getting-started.md).
You will be provided with a merchant number, and gain access to a set of API keys,
as well as test user credentials (mobile number) for conducting payment without actually being charged.

Note that you should expect the sign-up process to take several days, as there are manual steps involved!

# Testing
Though you should find a series of unit tests embedded in this module, we recommend completing extended testing before going live.
The previously referenced Vipps eCom API checklist contains an appropriate description of the 'Flow to go live for direct integrations for partners'.
