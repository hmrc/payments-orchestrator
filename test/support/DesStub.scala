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

package support

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import model.Vrn

object DesStub {
  def financialsOkMultiple(vrn: Vrn): StubMapping =
    stubFor(get(urlMatching("/enterprise/financial-data/VRN/.*"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.financialDataOk(vrn).toString().stripMargin)))

  def financialsOkTRS(vrn: Vrn): StubMapping =
    stubFor(get(urlMatching("/enterprise/financial-data/VRN/.*"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.financialDataOkTRS(vrn).toString().stripMargin)))

  def financialDataOkTRS404(vrn: Vrn): StubMapping =
    stubFor(get(urlMatching("/enterprise/financial-data/VRN/.*"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.financialDataOkTRS404(vrn).toString().stripMargin)))

  def financialsOkSingle(vrn: Vrn): StubMapping =
    stubFor(get(urlMatching("/enterprise/financial-data/VRN/.*"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.financialDataSingleOk(vrn: Vrn).toString().stripMargin)))

  def customerDataOkWithBankDetails(vrn: Vrn): StubMapping =
    stubFor(get(urlMatching(s"""/vat/customer/vrn/${vrn.value}/information"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.customerDataOk.toString().stripMargin)))

  def customerDataOkWithoutBankDetails(vrn: Vrn): StubMapping =
    stubFor(get(urlMatching(s"""/vat/customer/vrn/${vrn.value}/information"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.customerDataOkWithoutBankDetails.toString().stripMargin)))

  def financialsNotFound(): StubMapping =
    stubFor(get(urlMatching("/enterprise/financial-data/VRN/.*"))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(DesData.financialDataNotFound.toString().stripMargin)))

  def customerNotFound(vrn: Vrn): StubMapping =
    stubFor(get(urlMatching(s"""/vat/customer/vrn/${vrn.value}/information"""))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(DesData.customerDataNotFound.toString().stripMargin)))

  def ddNotFound(vrn: Vrn): StubMapping =
    stubFor(get(urlMatching(s"""/cross-regime/direct-debits/vatc/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(DesData.ddNotFound.toString().stripMargin)))

  def ddOk(vrn: Vrn): StubMapping =
    stubFor(get(urlMatching(s"""/cross-regime/direct-debits/vatc/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.ddOk.toString().stripMargin)))

  def ddOkNoMandate(vrn: Vrn): StubMapping =
    stubFor(get(urlMatching(s"""/cross-regime/direct-debits/vatc/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.ddOkNoMandate.toString().stripMargin)))

  def repaymentDetailsNotFound(vrn: Vrn): StubMapping =
    stubFor(get(urlMatching(s"""/cross-regime/repayment/VATC/VRN/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(DesData.repaymentDetailsNotFound.toString().stripMargin)))

  def repaymentDetailsOk(vrn: Vrn): StubMapping =
    stubFor(get(urlMatching(s"""/cross-regime/repayment/VATC/VRN/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.repaymentDetailJson.toString().stripMargin)))
}
