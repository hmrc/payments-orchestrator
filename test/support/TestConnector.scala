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

package support

import javax.inject.{Inject, Singleton}
import model.Vrn
import uk.gov.hmrc.http.HttpReads.Implicits.readRaw
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TestConnector @Inject() (httpClient: HttpClient)(implicit executionContext: ExecutionContext) {

  val port = 19001

  def getCustomerData(vrn: Vrn)(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(
    s"http://localhost:$port/payments-orchestrator/des/customer-data/vrn/${vrn.value}")

  def getObligations(vrn: Vrn)(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(
    s"http://localhost:$port/payments-orchestrator/des/obligations-data/vrn/${vrn.value}")

  def getFinancialData(vrn: Vrn)(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(
    s"http://localhost:$port/payments-orchestrator/des/financial-data/vrn/${vrn.value}")

  def getDDData(vrn: Vrn)(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(
    s"http://localhost:$port/payments-orchestrator/des/dd-data/vrn/${vrn.value}")

  def getRepaymentDetails(vrn: Vrn)(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(
    s"http://localhost:$port/payments-orchestrator/des/repayment-details/vrn/${vrn.value}")

}
