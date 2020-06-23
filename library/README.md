# Vipps-client-web

The content of this library provides a convenient integration with the payment service known as [Vipps](https://www.vipps.no/).

## Content

### Service module

The VippsService module provides methods for generating a token request (needed for all calls towards Vipps),
payment creation, capturing payments, cancelling an order, and retrieving details for a previously placed order.

### REST API
The library extends a classic Java Spring Boot REST API interface, through the implementation of a [RestController](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestController.html).
The rest controller exposes endpoints required for Vipps callbacks.

According to the official [Vipps eCom API checklist](https://github.com/vippsas/vipps-ecom-api/blob/master/vipps-ecom-api-checklist.md),
an integration needs to fulfill the requirements of implementing callbacks for handling successful and unsuccessful payments,
providing shipping details (express checkout only), and removing user's consent (express checkout only). 

## Limitations

Current project version limits integration to regular payments (not express checkout), and does not implement the required shipping endpoint for such a service. 
Nor is the required consent endpoint implemented, but responds the required 200 on request.
Future implementations may be provided at a convenient time.

## Requirements for Vipps integration

In order to integrate with Vipps, you need to enroll in the [Vipps Developer's programme](https://github.com/vippsas/vipps-developers/blob/master/vipps-getting-started.md).
You will gain access to a set of API keys, as well as test user credentials (mobile number) for conducting payment without actually being charged.

Note that you should expect the sign-up process to take several days, as there are manual steps involved!

## Configuration Profiles

The library module has options for loading configuration profiles.
There are currently two profiles available:
- dev - Provided for development and testing.
- prod - Provided for production.

The default profile is "dev". To activate the production profile, launch the application as follows:

```$ mvn spring-boot:run -Drun-arguments=--spring.profiles.active=prod```

## Environment variables
You need to define the following environment variables (Note that there are different values for testapi.vipps.no and api.vipps.no. Test variable are therefore post fixed **_TEST**, for separation and convenience):

Name | Value
---------|------
VIPPS_CLIENT_ID | Your client id (provided by Vipps)
VIPPS_CLIENT_SECRET | Your client secret (provided by Vipps)
VIPPS_MERCHANT_SERIAL_NO | This is the serial number for the payment receiver (provided by Vipps) Note that there are separate serials for test/production!
VIPPS_OCP_APIM_SUBSCRIPTION_KEY_ECOMMERCE | This is your API subscription key (provided by Vipps)
VIPPS_BASE_URL | This specifies the base url for the Vipps client, and will make the basis for all callback endpoints
VIPPS_FALLBACK_URL | This is the url Vipps uses to fall back to upon payment completion/cancellation

# Testing
Though you should find a series of unit tests embedded in this module, we recommend completing extended testing before going live.
The previously referenced Vipps eCom API checklist contains an appropriate description of the 'Flow to go live for direct integrations for partners'.
