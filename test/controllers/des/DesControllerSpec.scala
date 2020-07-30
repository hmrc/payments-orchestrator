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

import model.Vrn
import model.des.{CustomerInformation, DirectDebitData, FinancialData, RepaymentDetailData}
import play.api.libs.json.JsNull
import support.AuthStub.{authFailed, authOkNoEnrolments, authOkWithEnrolments}
import support.DesData.vrn
import support.DesStub.{customerDataOkWithBankDetails, customerNotFound, financialsOkMultiple, financialsOkSingle}
import support._

class DesControllerSpec extends ItSpec {
  private val vrnFailed = Vrn("2345678891")

  private lazy val connector = injector.instanceOf[TestConnector]

  "Get Customer Information" in {
    authOkWithEnrolments()
    customerDataOkWithBankDetails(vrn)
    val result = connector.getCustomerData(vrn).futureValue
    result.status shouldBe 200
    result.json.as[CustomerInformation] shouldBe DesData.customerInformation
  }

  "Get Customer Information 404" in {
    authOkWithEnrolments()
    customerNotFound(vrn)
    val result = connector.getCustomerData(vrn).futureValue
    result.status shouldBe 200
    result.json shouldBe JsNull
  }

  "Get Financial data" in {
    authOkWithEnrolments()
    financialsOkSingle(vrn)
    val result = connector.getFinancialData(vrn).futureValue
    result.status shouldBe 200
    result.json.as[FinancialData] shouldBe DesData.financialData
  }

  "Get Financial data (multiple)" in {
    authOkWithEnrolments()
    financialsOkMultiple(vrn)
    val result = connector.getFinancialData(vrn).futureValue
    result.status shouldBe 200
    result.json.as[FinancialData].financialTransactions.size shouldBe 5
  }

  "Get Financial data (financialsOkTRS)" in {
    authOkWithEnrolments()
    DesStub.financialsOkTRS(vrn)
    val result = connector.getFinancialData(vrn).futureValue
    result.status shouldBe 200
    result.json.as[FinancialData].financialTransactions.size shouldBe 2
  }

  "Get Financial data (financialDataOkTRS404)" in {
    authOkWithEnrolments()
    DesStub.financialDataOkTRS404(vrn)
    val result = connector.getCustomerData(vrn).futureValue
    result.status shouldBe 200
    result.json shouldBe JsNull
  }

  "Get Financial data 404" in {
    authOkWithEnrolments()
    DesStub.financialsNotFound()
    val result = connector.getCustomerData(vrn).futureValue
    result.status shouldBe 200
    result.json shouldBe JsNull
  }

  "Get DD data" in {
    authOkWithEnrolments()
    DesStub.ddOk(vrn)
    val result = connector.getDDData(vrn).futureValue
    result.status shouldBe 200
    val dd = result.json.as[DirectDebitData]
    dd shouldBe DesData.directDebitData
  }

  "Get DD data no mandate" in {
    authOkWithEnrolments()
    DesStub.ddOkNoMandate(vrn)
    val result = connector.getDDData(vrn).futureValue
    result.status shouldBe 200
    val dd = result.json.as[DirectDebitData]
    dd shouldBe DirectDebitData(None)
  }

  "Get DD data 404" in {
    authOkWithEnrolments()
    DesStub.ddNotFound(vrn)
    val result = connector.getDDData(vrn).futureValue
    result.status shouldBe 200
    result.json shouldBe JsNull
  }

  "Get repayment data" in {
    authOkWithEnrolments()
    DesStub.repaymentDetailsOk(vrn)
    val result = connector.getRepaymentDetails(vrn).futureValue
    result.status shouldBe 200
    val rd = result.json.as[Seq[RepaymentDetailData]]
    rd shouldBe DesData.repaymentsDetail
  }

  "Get repayment data 404" in {
    authOkWithEnrolments()
    DesStub.repaymentDetailsNotFound(vrn)
    val result = connector.getCustomerData(vrn).futureValue
    result.status shouldBe 200
    result.json shouldBe JsNull
  }

  "Get repayment data, not authorised should result in 401" in {
    authFailed()
    val result = connector.getCustomerData(vrn).futureValue
    result.status shouldBe 401
    result.body should include("Session record not found")
  }

  "Get repayment data, logged in but no access to VRN" in {
    authOkWithEnrolments(vrn = vrnFailed)
    val result = connector.getCustomerData(vrn).futureValue
    result.status shouldBe 401
    result.body should include("You do not have access to this vrn: 2345678890")
  }

  "Get repayment data, logged in but no enrolments" in {
    authOkNoEnrolments()
    val result = connector.getCustomerData(vrn).futureValue
    result.status shouldBe 401
    result.body should include("You do not have access to this service")
  }
}
