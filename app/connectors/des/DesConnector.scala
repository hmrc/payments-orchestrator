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

package connectors.des

import javax.inject.{Inject, Singleton}
import model.Vrn
import model.des.{CustomerInformation, DirectDebitData, FinancialData}
import play.api.{Configuration, Logger}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DesConnector @Inject() (servicesConfig: ServicesConfig, httpClient: HttpClient, configuration: Configuration)(implicit ec: ExecutionContext) {

  private val serviceURL: String = servicesConfig.baseUrl("des")
  private val authorizationToken: String = configuration.get[String]("microservice.services.des.authorizationToken")
  private val serviceEnvironment: String = configuration.get[String]("microservice.services.des.environment")
  private val obligationsUrl: String = configuration.get[String]("microservice.services.des.obligations-url")
  private val financialsUrl: String = configuration.get[String]("microservice.services.des.financials-url")
  private val customerUrl: String = configuration.get[String]("microservice.services.des.customer-url")
  private val ddUrl: String = configuration.get[String]("microservice.services.des.dd-url")

  private val desHeaderCarrier: HeaderCarrier = HeaderCarrier(authorization = Some(Authorization(s"Bearer $authorizationToken")))
    .withExtraHeaders("Environment" -> serviceEnvironment)

  def getFinancialData(vrn: Vrn): Future[FinancialData] = {
    Logger.debug(s"Calling des api 1166 for vrn ${vrn}")
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val getFinancialURL: String = s"$serviceURL$financialsUrl/${vrn.value}/VATC?onlyOpenItems=true"
    Logger.debug(s"""Calling des api 1166 with url ${getFinancialURL}""")
    httpClient.GET[FinancialData](getFinancialURL)
  }

  def getCustomerData(vrn: Vrn): Future[CustomerInformation] = {
    Logger.debug(s"Calling des api 1363 for vrn ${vrn}")
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val getCustomerURL: String = s"$serviceURL$customerUrl/${vrn.value}/information"
    Logger.debug(s"""Calling des api 1363 with url ${getCustomerURL}""")
    httpClient.GET[CustomerInformation](getCustomerURL)
  }

  def getDDData(vrn: Vrn): Future[DirectDebitData] = {
    Logger.debug(s"Calling des api 1396 for vrn ${vrn}")
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val getDDUrl: String = s"$serviceURL$ddUrl/vatc/vrn/${vrn.value}"
    Logger.debug(s"""Calling des api 1396 with url ${getDDUrl}""")
    httpClient.GET[DirectDebitData](getDDUrl)
  }

}
