
# payments-orchestrator

### What is payments orchestrator

Payment orchestrator is a microservice that is responsible for making calls out to DES. It is to be used when a frontend microservice (i.e. vat-repayment-tracker-frontend)
needs to talk to an API like DES (frontends are not supposed to message DES directly). 


### Running Locally

No explicit use case, but one profile/ instance that the orchestrator is used is in conjunction with the vat repayment tracker services.
e.g. Service manager profile: `sm --start VRT_ALL`

*make sure you have mongo running

---

### Test data
[Test data](https://confluence.tools.tax.service.gov.uk/display/OPS/VRT+Test+Data)

---

### Further information
[See further information about this service on confluence](https://confluence.tools.tax.service.gov.uk/display/OPS/payments-orchestrator)

---
### Test suites
Has unit tests within the repo.

---


### License     

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").


