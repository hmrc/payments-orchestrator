/*
 * Copyright 2022 HM Revenue & Customs
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
import model.des.Transaction
import model.{ChargeType, Vrn}
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.json.Json.toJson
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import uk.gov.hmrc.http.HeaderCarrier
import play.api.mvc.Request
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.play.bootstrap.backend.http.ErrorResponse
import uk.gov.hmrc.play.http.HeaderCarrierConverter

import scala.concurrent.ExecutionContext

@Singleton()
class DesController @Inject() (
    cc:           ControllerComponents,
    desConnector: DesConnector,
    actions:      Actions)(
    implicit
    ec: ExecutionContext)

  extends BackendController(cc) {

  private lazy val logger = Logger(this.getClass)

  def getFinancialData(vrn: Vrn): Action[AnyContent] = actions.securedAction(vrn).async { implicit request =>
    logger.debug("getFinancialData called")
    desConnector.getFinancialData(vrn).map { maybeFinancialDetails =>
      maybeFinancialDetails.fold(notFound: Result) { fd =>
        val filtered = fd.financialTransactions.filter(f => isCreditOrDebitChargeType(f))

        if (filtered.isEmpty) notFound
        else Ok(toJson(fd.copy(financialTransactions = filtered)))
      }
    }
  }

  private def isCreditOrDebitChargeType(transaction: Transaction): Boolean = transaction.chargeType == ChargeType.vatReturnCreditCharge || transaction.chargeType == ChargeType.vatReturnDebitCharge

  def getCustomerData(vrn: Vrn): Action[AnyContent] = actions.securedAction(vrn).async { implicit request =>
    logger.debug("getCustomerData called")
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)
    desConnector.getCustomerData(vrn).map{
      case Some(cd) => Ok(toJson(cd))
      case None     => notFound
    }
  }

  def getDDData(vrn: Vrn): Action[AnyContent] = actions.securedAction(vrn).async { implicit request =>
    logger.debug("getDDData called")
    desConnector.getDDData(vrn).map{
      case Some(dd) => Ok(toJson(dd))
      case None     => notFound
    }
  }

  def getRepaymentDetails(vrn: Vrn): Action[AnyContent] = actions.securedAction(vrn).async { implicit request =>
    logger.debug("getRepaymentDetails called")
    desConnector.getRepaymentDetails(vrn).map{
      case Some(rd) => Ok(toJson(rd))
      case None     => notFound
    }
  }

  private def notFound(implicit request: Request[_]) = {
    NotFound(Json.toJson(ErrorResponse(NOT_FOUND, "URI not found", requested = Some(request.path))))
  }

}
