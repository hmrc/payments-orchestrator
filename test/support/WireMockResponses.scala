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

package support

import com.github.tomakehurst.wiremock.client.WireMock._
import model.Vrn

object WireMockResponses {

  def obligationsOk(vrn: Vrn) = {
    stubFor(get(urlMatching("/enterprise/obligation-data/vrn/.*"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.obligationsDataOk(vrn, "2027-11-02").toString()
            .stripMargin)))

  }

  def financialsOkMultiple(vrn: Vrn) = {
    stubFor(get(urlMatching("/enterprise/financial-data/VRN/.*"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.financialDataOk(vrn).toString()
            .stripMargin)))

  }

  def financialsOkSingle(vrn: Vrn) = {
    stubFor(get(urlMatching("/enterprise/financial-data/VRN/.*"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.financialDataSingleOk(vrn: Vrn).toString()
            .stripMargin)))

  }

  def customerDataOkWithBankDetails(vrn: Vrn) = {
    stubFor(get(urlMatching(s"""/vat/customer/vrn/${vrn.value}/information"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.customerDataOk.toString()
            .stripMargin)))

  }

  def customerDataOkWithoutBankDetails(vrn: Vrn) = {
    stubFor(get(urlMatching(s"""/vat/customer/vrn/${vrn.value}/information"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.customerDataOkWithoutBankDetails.toString()
            .stripMargin)))

  }

  def financialsNotFound = {
    stubFor(get(urlMatching("/enterprise/financial-data/VRN/.*"))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(
          DesData.financialDataNotFound.toString()
            .stripMargin)))

  }

  def obligationsNotFound = {
    stubFor(get(urlMatching("/enterprise/obligation-data/vrn/.*"))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(
          DesData.obligationsDataNotFound.toString()
            .stripMargin)))

  }

  def customerNotFound(vrn: Vrn) = {
    stubFor(get(urlMatching(s"""/vat/customer/vrn/${vrn.value}/information"""))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(
          DesData.customerDataNotFound.toString()
            .stripMargin)))

  }

}
