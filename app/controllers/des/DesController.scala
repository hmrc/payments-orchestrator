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

import connectors.des.DesConnector
import controllers.action.Actions
import javax.inject.{Inject, Singleton}
import model.{ChargeType, Vrn}
import model.des.Transaction
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.concurrent.ExecutionContext

@Singleton()
class DesController @Inject() (
    cc:           ControllerComponents,
    desConnector: DesConnector,
    actions:      Actions)(
    implicit
    ec: ExecutionContext)

  extends BackendController(cc) {

  def getFinancialData(vrn: Vrn): Action[AnyContent] = actions.securedAction(vrn).async { implicit request =>
    for {
      fd <- desConnector.getFinancialData(vrn)
    } yield {

      val filtered: Seq[Transaction] = fd.financialTransactions.filter(f => isCreditOrDebitChargeType(f))
      if (filtered.size == 0) NotFound
      else {
        val newFd = fd.copy(financialTransactions = filtered)
        Ok(Json.toJson(newFd))
      }
    }

  }

  private def isCreditOrDebitChargeType(transaction: Transaction): Boolean = transaction.chargeType == ChargeType.vatReturnCreditCharge || transaction.chargeType == ChargeType.vatReturnDebitCharge

  def getCustomerData(vrn: Vrn): Action[AnyContent] = actions.securedAction(vrn).async { implicit request =>
    Logger.debug("getCustomerData called")
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))
    for {
      cd <- desConnector.getCustomerData(vrn)
    } yield (Ok(Json.toJson(cd)))
  }

  def getDDData(vrn: Vrn): Action[AnyContent] = actions.securedAction(vrn).async { implicit request =>
    for {
      dd <- desConnector.getDDData(vrn)
    } yield (Ok(Json.toJson(dd)))
  }

  def getRepaymentDetails(vrn: Vrn): Action[AnyContent] = actions.securedAction(vrn).async { implicit request =>
    for {
      rd <- desConnector.getRepaymentDetails(vrn)
    } yield (Ok(Json.toJson(rd)))
  }

}
