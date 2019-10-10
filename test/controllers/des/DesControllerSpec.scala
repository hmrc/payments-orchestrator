/*
 * Copyright 2019 HM Revenue & Customs
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
import play.api.Logger
import play.api.http.Status
import support.{DesData, ItSpec, WireMockResponses}

class DesControllerSpec extends ItSpec {

  val desController = injector.instanceOf[DesController]
  val vrn: Vrn = Vrn("2345678890")

  "Get Customer Information" in {
    WireMockResponses.customerDataOkWithBankDetails(vrn)
    val result = connector.getCustomerData(vrn).futureValue
    result.status shouldBe Status.OK
    result.json.as[CustomerInformation] shouldBe DesData.customerInformation
  }

  "Get Customer Information 404" in {
    WireMockResponses.customerNotFound(vrn)
    val result = connector.getCustomerData(vrn).failed.futureValue
    result.getMessage should include("returned 404 (Not Found)")
  }

  "Get Financial data" in {
    WireMockResponses.financialsOkSingle(vrn)
    val result = connector.getFinancialData(vrn).futureValue
    result.status shouldBe Status.OK
    result.json.as[FinancialData] shouldBe DesData.financialData
  }

  "Get Financial data (multiple)" in {
    WireMockResponses.financialsOkMultiple(vrn)
    val result = connector.getFinancialData(vrn).futureValue
    result.status shouldBe Status.OK
    result.json.as[FinancialData].financialTransactions.size shouldBe 5
  }

  "Get Financial data 404" in {
    WireMockResponses.financialsNotFound
    val result = connector.getFinancialData(vrn).failed.futureValue
    result.getMessage should include("returned 404 (Not Found)")
  }

  "Get DD data" in {
    WireMockResponses.ddOk(vrn)
    val result = connector.getDDData(vrn).futureValue
    result.status shouldBe Status.OK
    val dd = result.json.as[DirectDebitData]
    dd shouldBe DesData.directDebitData
  }

  "Get DD data no mandate" in {
    WireMockResponses.ddOkNoMandate(vrn)
    val result = connector.getDDData(vrn).futureValue
    result.status shouldBe Status.OK
    val dd = result.json.as[DirectDebitData]
    dd shouldBe DesData.directDebitDataNone
  }

  "Get DD data 404" in {
    WireMockResponses.ddNotFound(vrn)
    val result = connector.getDDData(vrn).failed.futureValue
    result.getMessage should include("returned 404 (Not Found)")
  }

  "Get repayment data" in {
    WireMockResponses.repaymentDetailsOk(vrn)
    val result = connector.getRepaymentDetails(vrn).futureValue
    result.status shouldBe Status.OK
    val rd = result.json.as[Seq[RepaymentDetailData]]
    rd shouldBe DesData.repaymentsDetail
  }

  "Get repayment data 404" in {
    WireMockResponses.repaymentDetailsNotFound(vrn)
    val result = connector.getRepaymentDetails(vrn).failed.futureValue
    result.getMessage should include("returned 404 (Not Found)")
  }
}
