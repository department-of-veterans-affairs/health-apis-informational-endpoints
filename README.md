# health-apis-informational-endpoints
Provides reusable endpoints meant to publish usability information about an API like Conformance and OpenAPI specs.

## Background
Most of our applications engage in the SMART on FHIR pattern for authorization and authentication due to handling sensitive data. As part of this specification, it is required to publish how to interact securely with the FHIR server that our applications are acting as. This requires implementing both a conformance statement and a JSON file of well-known URIs. http://www.hl7.org/fhir/smart-app-launch/conformance/index.html

### Conformance
This has several names across the FHIR specs, the most recent being CapabilityStatement for R4. SMART on FHIR requires this document to publish how to securely interact with the server, but CapabilityStatements can also contain information about the operations and resources supported by a FHIR server among other things. This information is made available under the `/metadata` endpoint.

### Well-Known
This is a pared down version of the CapabilityStatement for the most part. SMART on FHIR _requires_ that this endpoint be supported by appending `/.well-known/smart-configuration` to the base URL of the server.

### OpenAPI
This is another way to specify what a server is capable of, this is not FHIR specific like Conformance and Well-Known though. OpenAPI and Swagger are used fairly interchangeably on this project, further reading about them can be found here https://swagger.io/docs/specification/about/

## Usage
This repo currently is R4 focused, but the concepts should be the same as other specs are added.

### Conformance
These endpoints rely on three beans to instantiate, `CapabilityStatementProperties`, `WellKnownProperties`, and `CapabilityResourcesProperties`. The first two are configured through an application.properties file and following naming conventions. The test folder contains an example properties file for a guide on what information and property name scheme to follow.

The Capability statement can list the resources the FHIR server is able to serve and how. Due to this being more complex than most of the other properties, this configuration is done through a special class instead of a properties file. For R4, a bean of type `CapabilityResourcesProperties` needs to be provided. This bean contains a List of `CapabilityResources`. As long as this bean is of the appropriate type Spring will be able to autowire it into the MetadataController. Refer to the `MetadataTestConfig` for an example of building this bean within your application.

Importing the controllers and configured beans can be done by importing the InformationalEndpointsBaseR4Config to the base Application controller of your project, or anywhere you like. The only necessary class not provided when importing this is `CapabilityResourcesProperties`, a bean for this _needs_ to be defined within the application using this library.  The classes included in this configuration will automatically attempt to pick up configuration from your Spring Context and add Metadata and Well-Known controllers. The classes that the BaseR4Config uses can also be imported piecemeal if needed.

MetadataController will listen on the following endpoint by default:
```
/metadata
```

WellKnownController will listen on the following endpoint by default:
```
/.well-known/smart-configuration
```

#### OpenAPI
Using this is as simple as importing the OpenApiController. This looks for an `openapi.yaml` file on the project's classpath by default to provide all the openAPI documentation with (you can specify a different YAML if necessary). Most projects have this file created using the `swagger-maven-plugin`, but any way it gets made is good enough. This implementation will assume a YAML file and attempt to convert it to a JSON file when requested. 

OpenApiController will listen on the following endpoints by default:
```
/openapi.json 
/api/openapi.json
/openapi.yaml 
/api/openapi.yaml
```
