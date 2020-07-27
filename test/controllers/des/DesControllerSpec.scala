/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers.des

import model.des.{CustomerInformation, DirectDebitData, FinancialData, RepaymentDetailData}
import model.{EnrolmentKeys, Vrn}
import play.api.http.Status
import support._

class DesControllerSpec extends ItSpec {
  private val vrn = Vrn("2345678890")
  private val vrnFailed = Vrn("2345678891")

  private lazy val connector = injector.instanceOf[TestConnector]

  "Get Customer Information" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.customerDataOkWithBankDetails(vrn)
    val result = connector.getCustomerData(vrn).futureValue
    result.status shouldBe Status.OK
    result.json.as[CustomerInformation] shouldBe DesData.customerInformation
  }

  "Get Customer Information 404" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.customerNotFound(vrn)
    val result = connector.getCustomerData(vrn).failed.futureValue
    result.getMessage should include("returned 404 (Not Found)")
  }

  "Get Financial data" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.financialsOkSingle(vrn)
    val result = connector.getFinancialData(vrn).futureValue
    result.status shouldBe Status.OK
    result.json.as[FinancialData] shouldBe DesData.financialData
  }

  "Get Financial data (multiple)" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.financialsOkMultiple(vrn)
    val result = connector.getFinancialData(vrn).futureValue
    result.status shouldBe Status.OK
    result.json.as[FinancialData].financialTransactions.size shouldBe 5
  }

  "Get Financial data (financialsOkTRS)" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.financialsOkTRS(vrn)
    val result = connector.getFinancialData(vrn).futureValue
    result.status shouldBe Status.OK
    result.json.as[FinancialData].financialTransactions.size shouldBe 2
  }

  "Get Financial data (financialDataOkTRS404)" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.financialDataOkTRS404(vrn)
    val result = connector.getFinancialData(vrn).failed.futureValue
    result.getMessage should include("returned 404 (Not Found)")
  }

  "Get Financial data 404" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.financialsNotFound()
    val result = connector.getFinancialData(vrn).failed.futureValue
    result.getMessage should include("returned 404 (Not Found)")
  }

  "Get DD data" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.ddOk(vrn)
    val result = connector.getDDData(vrn).futureValue
    result.status shouldBe Status.OK
    val dd = result.json.as[DirectDebitData]
    dd shouldBe DesData.directDebitData
  }

  "Get DD data no mandate" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.ddOkNoMandate(vrn)
    val result = connector.getDDData(vrn).futureValue
    result.status shouldBe Status.OK
    val dd = result.json.as[DirectDebitData]
    dd shouldBe DirectDebitData(None)
  }

  "Get DD data 404" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.ddNotFound(vrn)
    val result = connector.getDDData(vrn).failed.futureValue
    result.getMessage should include("returned 404 (Not Found)")
  }

  "Get repayment data" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.repaymentDetailsOk(vrn)
    val result = connector.getRepaymentDetails(vrn).futureValue
    result.status shouldBe Status.OK
    val rd = result.json.as[Seq[RepaymentDetailData]]
    rd shouldBe DesData.repaymentsDetail
  }

  "Get repayment data 404" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.repaymentDetailsNotFound(vrn)
    val result = connector.getRepaymentDetails(vrn).failed.futureValue
    result.getMessage should include("returned 404 (Not Found)")
  }

  "Get repayment data, not authorised should result in 401" in {
    AuthWireMockResponses.authFailed()
    WireMockResponses.repaymentDetailsOk(vrn)
    val result = connector.getRepaymentDetails(vrn).failed.futureValue
    result.getMessage should include("Session record not found")
  }

  "Get repayment data, logged in but no access to VRN" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrnFailed, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.repaymentDetailsOk(vrn)
    val result = connector.getRepaymentDetails(vrn).failed.futureValue
    result.getMessage should include("You do not have access to this vrn: 2345678890")
  }

  "Get repayment data, logged in but no enrolments" in {
    AuthWireMockResponses.authOkNoEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString)
    WireMockResponses.repaymentDetailsOk(vrn)
    val result = connector.getRepaymentDetails(vrn).failed.futureValue
    result.getMessage should include("You do not have access to this service")
  }
}
