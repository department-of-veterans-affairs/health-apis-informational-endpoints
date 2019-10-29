# health-apis-informational-endpoints
Provides reusable endpoints meant to publish usability information about an API such as metadata and well-known.

Most of our applications engage in the SMART on FHIR pattern for authorization and authentication due to handling sensitive data. As part of this specification, it is required to publish how to interact securely with the FHIR server that our applications are acting as. This requires implementing both a conformance statement and a JSON file of well-known URIs. http://www.hl7.org/fhir/smart-app-launch/conformance/index.html

## Conformance
This has several names across the FHIR specs, the most recent being CapabilityStatement for R4. SMART on FHIR requires this document to publish how to securely interact with the server, but CapabilityStatements can also contain information about the operations and resources supported by a FHIR server among other things. This information is made available under the `/metadata` endpoint.

## Well-Known
This is a pared down version of the CapabilityStatement for the most part. SMART on FHIR _requires_ that this endpoint be supported by appending `/.well-known/smart-configuration` to the base URL of the server.
