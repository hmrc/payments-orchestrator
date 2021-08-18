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

package connectors.des

import java.time.LocalDate
import javax.inject.{Inject, Singleton}
import model.Vrn
import model.des.{CustomerInformation, DirectDebitData, FinancialData, RepaymentDetailData}
import play.api.{Configuration, Logger}
import uk.gov.hmrc.http.{Authorization, HeaderCarrier}
import uk.gov.hmrc.http.HttpReads.Implicits.{readFromJson, readOptionOfNotFound}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DesConnector @Inject() (servicesConfig: ServicesConfig, httpClient: HttpClient, configuration: Configuration)(implicit ec: ExecutionContext) {

  private lazy val logger = Logger(this.getClass)

  private val serviceURL = servicesConfig.baseUrl("des")
  private val authorizationToken = configuration.get[String]("microservice.services.des.authorizationToken")
  private val serviceEnvironment = configuration.get[String]("microservice.services.des.environment")
  private val financialsUrl = configuration.get[String]("microservice.services.des.financials-url")
  private val customerUrl = configuration.get[String]("microservice.services.des.customer-url")
  private val ddUrl = configuration.get[String]("microservice.services.des.dd-url")
  private val repaymentDetailsUrl = configuration.get[String]("microservice.services.des.repaymentdetails-url")

  private val desHeaderCarrier = HeaderCarrier(authorization = Some(Authorization(s"Bearer $authorizationToken")))
    .withExtraHeaders("Environment" -> serviceEnvironment)

  private val desHeaderCarrier1533 = HeaderCarrier(authorization = Some(Authorization(s"Bearer $authorizationToken")))
    .withExtraHeaders("Environment" -> serviceEnvironment, "OriginatorID" -> "MDTP")

  def getFinancialData(vrn: Vrn): Future[Option[FinancialData]] = {
    logger.debug(s"Calling des api 1166 for vrn $vrn")
    val now = LocalDate.now()
    val aYearAgo = LocalDate.now().minusYears(1)
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val getFinancialURL: String = s"$serviceURL$financialsUrl/${vrn.value}/VATC?dateFrom=$aYearAgo&dateTo=$now"
    logger.debug(s"""Calling des api 1166 with url $getFinancialURL""")
    httpClient.GET[Option[FinancialData]](getFinancialURL)
  }

  def getCustomerData(vrn: Vrn): Future[Option[CustomerInformation]] = {
    logger.debug(s"Calling des api 1363 for vrn $vrn")
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val getCustomerURL: String = s"$serviceURL$customerUrl/${vrn.value}/information"
    logger.debug(s"""Calling des api 1363 with url $getCustomerURL""")
    httpClient.GET[Option[CustomerInformation]](getCustomerURL)
  }

  def getDDData(vrn: Vrn): Future[Option[DirectDebitData]] = {
    logger.debug(s"Calling des api 1396 for vrn $vrn")
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val getDDUrl: String = s"$serviceURL$ddUrl/${vrn.value}"
    logger.debug(s"""Calling des api 1396 with url $getDDUrl""")
    httpClient.GET[Option[DirectDebitData]](getDDUrl)
  }

  def getRepaymentDetails(vrn: Vrn): Future[Option[Seq[RepaymentDetailData]]] = {
    logger.debug(s"Calling des api 1533 for vrn $vrn")
    implicit val hc: HeaderCarrier = desHeaderCarrier1533
    val getRDUrl: String = s"$serviceURL$repaymentDetailsUrl/${vrn.value}"
    logger.debug(s"""Calling des api 1533 with url $getRDUrl""")
    httpClient.GET[Option[Seq[RepaymentDetailData]]](getRDUrl)
  }
}
