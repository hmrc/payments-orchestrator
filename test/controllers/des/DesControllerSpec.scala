/*
 * Copyright 2021 HM Revenue & Customs
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

import akka.util.Timeout
import model.Vrn
import model.des.{CustomerInformation, DirectDebitData, FinancialData, RepaymentDetailData}
import play.api.libs.json.{Json}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsJson, contentAsString, status}
import support.AuthStub.{authFailed, authOkNoEnrolments, authOkWithEnrolments}
import support.DesData.vrn
import support.DesStub.{customerDataOkWithBankDetails, customerNotFound, financialsOkMultiple, financialsOkSingle}
import support._
import uk.gov.hmrc.play.bootstrap.backend.http.ErrorResponse

import java.util.concurrent.TimeUnit

class DesControllerSpec extends ItSpec {
  private val vrnFailed = Vrn("2345678891")
  implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)

  val controller = injector.instanceOf[DesController]

  "Get Customer Information" in {
    val request = FakeRequest()
    authOkWithEnrolments()
    customerDataOkWithBankDetails(vrn)
    val response = controller.getCustomerData(vrn)(request)
    status(response) shouldBe 200
    contentAsJson(response).as[CustomerInformation] shouldBe DesData.customerInformation
  }

  "Get Customer Information 404" in {
    val request = FakeRequest("GET", "/payments-orchestrator/des/customer-data/vrn/2345678890")
    authOkWithEnrolments()
    customerNotFound(vrn)
    val response = controller.getCustomerData(vrn)(request)
    status(response) shouldBe 404
    contentAsJson(response) shouldBe errorResponse("/payments-orchestrator/des/customer-data/vrn/2345678890")
  }

  private def errorResponse(url: String) = {
    Json.toJson(ErrorResponse(404, "URI not found", requested = Some(url)))
  }

  "Get Financial data" in {
    val request = FakeRequest()
    authOkWithEnrolments()
    financialsOkSingle(vrn)
    val result = controller.getFinancialData(vrn)(request)
    status(result) shouldBe 200
    contentAsJson(result).as[FinancialData] shouldBe DesData.financialData
  }

  "Get Financial data  (multiple)" in {
    val request = FakeRequest()
    authOkWithEnrolments()
    financialsOkMultiple(vrn)
    val result = controller.getFinancialData(vrn)(request)
    status(result) shouldBe 200
    contentAsJson(result).as[FinancialData].financialTransactions.size shouldBe 5
  }

  "Get Financial data (financialsOkTRS)" in {
    val request = FakeRequest()
    authOkWithEnrolments()
    DesStub.financialsOkTRS(vrn)
    val result = controller.getFinancialData(vrn)(request)
    status(result) shouldBe 200
    contentAsJson(result).as[FinancialData].financialTransactions.size shouldBe 2
  }

  "Get Financial data (financialDataOkTRS404)" in {
    val request = FakeRequest("GET", "/payments-orchestrator/des/financial-data/vrn/2345678890")
    authOkWithEnrolments()
    DesStub.financialDataOkTRS404(vrn)
    val result = controller.getFinancialData(vrn)(request)
    status(result) shouldBe 404
    contentAsJson(result) shouldBe errorResponse("/payments-orchestrator/des/financial-data/vrn/2345678890")
  }

  "Get Financial data 404" in {
    val request = FakeRequest("GET", "/payments-orchestrator/des/financial-data/vrn/2345678890")
    authOkWithEnrolments()
    DesStub.financialsNotFound()
    val result = controller.getFinancialData(vrn)(request)
    status(result) shouldBe 404
    contentAsJson(result) shouldBe errorResponse("/payments-orchestrator/des/financial-data/vrn/2345678890")
  }

  "Get DD data" in {
    val request = FakeRequest()
    authOkWithEnrolments()
    DesStub.ddOk(vrn)
    val result = controller.getDDData(vrn)(request)
    status(result) shouldBe 200
    contentAsJson(result).as[DirectDebitData] shouldBe DesData.directDebitData
  }

  "Get DD data no mandate" in {
    val request = FakeRequest()
    authOkWithEnrolments()
    DesStub.ddOkNoMandate(vrn)
    val result = controller.getDDData(vrn)(request)
    status(result) shouldBe 200
    contentAsJson(result).as[DirectDebitData] shouldBe DirectDebitData(None)
  }

  "Get DD data 404" in {
    val request = FakeRequest("GET", "/payments-orchestrator/des/dd-data/vrn/2345678890")
    authOkWithEnrolments()
    DesStub.ddNotFound(vrn)
    val result = controller.getDDData(vrn)(request)
    status(result) shouldBe 404
    contentAsJson(result) shouldBe errorResponse("/payments-orchestrator/des/dd-data/vrn/2345678890")
  }

  "Get repayment data" in {
    val request = FakeRequest()
    authOkWithEnrolments()
    DesStub.repaymentDetailsOk(vrn)
    val result = controller.getRepaymentDetails(vrn)(request)
    status(result) shouldBe 200
    contentAsJson(result).as[Seq[RepaymentDetailData]] shouldBe DesData.repaymentsDetail
  }

  "Get repayment data 404" in {
    val request = FakeRequest("GET", "/payments-orchestrator/des/repayment-details/vrn/2345678890")
    authOkWithEnrolments()
    DesStub.repaymentDetailsNotFound(vrn)
    val result = controller.getRepaymentDetails(vrn)(request)
    status(result) shouldBe 404
    contentAsJson(result) shouldBe errorResponse("/payments-orchestrator/des/repayment-details/vrn/2345678890")
  }

  "Get repayment data, not authorised should result in a failed future result" in {
    val request = FakeRequest()
    authFailed()
    val result = controller.getCustomerData(vrn)(request)
    whenReady(result.failed){ ex =>
      ex.getMessage should include("Session record not found")
    }
  }

  "Get repayment data, logged in but no access to VRN" in {
    val request = FakeRequest()
    authOkWithEnrolments(vrn = vrnFailed)
    val result = controller.getCustomerData(vrn)(request)
    status(result) shouldBe 401
    contentAsString(result) should include("You do not have access to this vrn: 2345678890")
  }

  "Get repayment data, logged in but no enrolments" in {
    val request = FakeRequest()
    authOkNoEnrolments()
    val result = controller.getCustomerData(vrn)(request)
    status(result) shouldBe 401
    contentAsString(result) should include("You do not have access to this service")
  }
}
