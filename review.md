# Review Remarks

Please note that I was mainly defensive on the implementation not touching the parts which were not causing me problems while starting or running the applications.
I'm summarizing my thoughts on the tests created in this repository and also to some extent about the framework design.

---

## Configuration Management

Hardcoded configuration values should be extracted. The very least configuration management should be implemented to support different environments.

I would do this even for a Proof of Concept (POC). It's easy to forget about hardcoded values, and they can accidentally make their way into production. In my opinion, implementing proper configuration management is not a difficult task and prevents future issues.

---

## Use of Soft Assertions

I would introduce `SoftAssertions`. Not sure if this was not used on purpose as I see this is a dependency in the pom.xml but since it was not used I have no way to tell without any instructions if I can use it, or it's use should be avoided. 

---

## RestAssured

I see it's part of the pom.xml ad a dependency but since it's not used explicitly I didn't go ahead and use it.

---

## Implementing api interactions

In general, I think there should be two implementations for each supported https method call against an endpoint in the test fw:

1. One which returns a generic response entity, which is useful for further checks when an error is returned by the call
2. Another which performs some basic verification on the response and then returns the pojo representing the response returned by a successful call.

---

## `allContacts()` test

In the `allContacts()` test, I would avoid checking for the exact count of objects returned. Instead, the test should focus on:
* The HTTP response code.
* The response body's schema.
* Verifying that the returned list is not empty.

When testing a deployed application, especially one shared by many users, the data is volatile and can change during a test run. Asserting on the exact count of items makes the test inherently **flaky**. Checking for a specific value is fine only when the test setup has full control over the data.

---

## Missing Test Coverage

Tests are currently missing for the following endpoints:
* `/contact/lastname/{lastname}`
* `/contact/createOrUpdateContact/{id}`

I realize there is a `/contact/hello` endpoint which is useful in CI to act as a health check to see if the service responds. I see no reason to create api tests for that. 

---

## Introduce CRUD test for covering possible workflows with Contacts

I would refactor the existing tests into a single test focusing on contact entity. Rather than testing endpoints in isolation. This test could cover creating, retrieving (using both `GET` endpoints), updating, and deleting a contact. This approach would conveniently provide sufficient coverage for the main functionality of all related endpoints. Including the missing ones mentioned in the previous point.

---

## Duplicated `Contact` Model

The `Contact` model appears to be a duplicate of the one used in the application's source code. This model should be imported directly into the tests instead.

As long as the API tests and the service implementation are in the same repository, or the model is publicly available as a public library (which I would expect for easier integration). I see no reason to duplicate it within the test project.

---

## Test Strategy

If this was a service to be deployed in production I would argue that all of these tests could be implemented as **unit tests**. Since the implementation of these services are simple and self-contained and require no interaction with other layers of the SUT. I simply prefer to implement more complex tests on api level which are hard to set up otherwise or require the interaction between multiple layers of a live like system.

## Next steps

- I would start by refactoring the configuration handling and extract all hardcoded values which are related to interacting with the endpoints (url, port, timeouts).
  - I also expect that we would need to integrate with tools used for managing secrets 
- I would add a visual report. Junit based reporters are fine to some extent but a visual report like allure provides a better overview. The actual took used would depend on the needs of the team, product owner and the constraints from the environment.
- CI integration