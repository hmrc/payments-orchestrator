/*
 * Copyright 2023 HM Revenue & Customs
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

import org.apache.pekko.util.Timeout
import model.EnrolmentKeys._
import model.Vrn
import model.des.{CustomerInformation, DirectDebitData, FinancialData, RepaymentDetailData}
import play.api.libs.json.Json
import play.api.test.Helpers.{contentAsJson, contentAsString, status}
import support.AuthStub.{authFailed, authOkNoEnrolments, authOkWithEnrolments, authOkWithSeveralEnrolments}
import support.DesData.vrn
import support.DesStub.{customerDataOkWithBankDetails, customerNotFound, financialsOkMultiple, financialsOkSingle}
import support._
import uk.gov.hmrc.play.bootstrap.backend.http.ErrorResponse

import java.util.concurrent.TimeUnit

class DesControllerSpec extends ItSpec {
  private val vrnFailed = Vrn("2345678891")
  private val mtdVrn: Vrn = Vrn("2345678890")
  private val vatDecVrn: Vrn = Vrn("1345678890")
  private val vatVarVrn: Vrn = Vrn("3345678890")
  private val vrnList: List[Vrn] = List(vatVarVrn, mtdVrn, vatDecVrn)
  private val keyList: List[String] = List(vatVarEnrolmentKey, mtdVatEnrolmentKey, vatDecEnrolmentKey)
  implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)

  val controller: DesController = injector.instanceOf[DesController]

  "Get Customer Information" in {
    authOkWithSeveralEnrolments(List(vatVarVrn -> vatVarEnrolmentKey, mtdVrn -> mtdVatEnrolmentKey, vatDecVrn -> vatDecEnrolmentKey))
    customerDataOkWithBankDetails(mtdVrn)
    val response = controller.getCustomerData(mtdVrn)(fakeRequest("POST", ""))

    status(response) shouldBe 200
    contentAsJson(response).as[CustomerInformation] shouldBe DesData.customerInformation
  }

  "Get Customer Information 404" in {
    authOkWithEnrolments()
    customerNotFound(vrn)
    val response = controller.getCustomerData(vrn)(fakeRequest("GET", "/payments-orchestrator/des/customer-data/vrn/2345678890"))
    status(response) shouldBe 404
    contentAsJson(response) shouldBe errorResponse("/payments-orchestrator/des/customer-data/vrn/2345678890")
  }

  private def errorResponse(url: String) = {
    Json.toJson(ErrorResponse(404, "URI not found", requested = Some(url)))
  }

  "Get Financial data" in {
    authOkWithEnrolments()
    financialsOkSingle(vrn)
    val result = controller.getFinancialData(vrn)(fakeRequest())
    status(result) shouldBe 200
    contentAsJson(result).as[FinancialData] shouldBe DesData.financialData
  }

  "Get Financial data  (multiple)" in {
    authOkWithEnrolments()
    financialsOkMultiple(vrn)
    val result = controller.getFinancialData(vrn)(fakeRequest())
    status(result) shouldBe 200
    contentAsJson(result).as[FinancialData].financialTransactions.size shouldBe 5
  }

  "Get Financial data (financialsOkTRS)" in {
    authOkWithEnrolments()
    DesStub.financialsOkTRS(vrn)
    val result = controller.getFinancialData(vrn)(fakeRequest())
    status(result) shouldBe 200
    contentAsJson(result).as[FinancialData].financialTransactions.size shouldBe 2
  }

  "Get Financial data (financialDataOkTRS404)" in {
    authOkWithEnrolments()
    DesStub.financialDataOkTRS404(vrn)
    val result = controller.getFinancialData(vrn)(fakeRequest("GET", "/payments-orchestrator/des/financial-data/vrn/2345678890"))
    status(result) shouldBe 404
    contentAsJson(result) shouldBe errorResponse("/payments-orchestrator/des/financial-data/vrn/2345678890")
  }

  "Get Financial data 404" in {
    authOkWithEnrolments()
    DesStub.financialsNotFound()
    val result = controller.getFinancialData(vrn)(fakeRequest("GET", "/payments-orchestrator/des/financial-data/vrn/2345678890"))
    status(result) shouldBe 404
    contentAsJson(result) shouldBe errorResponse("/payments-orchestrator/des/financial-data/vrn/2345678890")
  }

  "Get DD data" in {
    authOkWithEnrolments()
    DesStub.ddOk(vrn)
    val result = controller.getDDData(vrn)(fakeRequest())
    status(result) shouldBe 200
    contentAsJson(result).as[DirectDebitData] shouldBe DesData.directDebitData
  }

  "Get DD data no mandate" in {
    authOkWithEnrolments()
    DesStub.ddOkNoMandate(vrn)
    val result = controller.getDDData(vrn)(fakeRequest())
    status(result) shouldBe 200
    contentAsJson(result).as[DirectDebitData] shouldBe DirectDebitData(None)
  }

  "Get DD data 404" in {
    authOkWithEnrolments()
    DesStub.ddNotFound(vrn)
    val result = controller.getDDData(vrn)(fakeRequest("GET", "/payments-orchestrator/des/dd-data/vrn/2345678890"))
    status(result) shouldBe 404
    contentAsJson(result) shouldBe errorResponse("/payments-orchestrator/des/dd-data/vrn/2345678890")
  }

  "Get repayment data" in {
    authOkWithEnrolments()
    DesStub.repaymentDetailsOk(vrn)
    val result = controller.getRepaymentDetails(vrn)(fakeRequest())
    status(result) shouldBe 200
    contentAsJson(result).as[Seq[RepaymentDetailData]] shouldBe DesData.repaymentsDetail
  }

  "Get repayment data 404" in {
    authOkWithEnrolments()
    DesStub.repaymentDetailsNotFound(vrn)
    val result = controller.getRepaymentDetails(vrn)(fakeRequest("GET", "/payments-orchestrator/des/repayment-details/vrn/2345678890"))
    status(result) shouldBe 404
    contentAsJson(result) shouldBe errorResponse("/payments-orchestrator/des/repayment-details/vrn/2345678890")
  }

  "Get repayment data, not authorised should result in a failed future result" in {
    authFailed()
    val result = controller.getCustomerData(vrn)(fakeRequest())
    whenReady(result.failed){ ex =>
      ex.getMessage should include("Session record not found")
    }
  }

  "Get repayment data, logged in but no access to VRN" in {
    authOkWithEnrolments(vrn = vrnFailed)
    val result = controller.getCustomerData(vrn)(fakeRequest())
    status(result) shouldBe 401
    contentAsString(result) should include("You do not have access to this vrn: 2345678890")
  }

  "Get repayment data, logged in but no enrolments" in {
    authOkNoEnrolments()
    val result = controller.getCustomerData(vrn)(fakeRequest())
    status(result) shouldBe 401
    contentAsString(result) should include("You do not have access to this service")
  }
}
