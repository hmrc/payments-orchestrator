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
import javax.inject.{Inject, Singleton}
import model.Vrn
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.concurrent.ExecutionContext

@Singleton()
class DesController @Inject() (
    cc:           ControllerComponents,
    desConnector: DesConnector)(
    implicit
    ec: ExecutionContext)

  extends BackendController(cc) {

  def getObligationData(vrn: Vrn): Action[AnyContent] = Action.async { implicit request =>
    for {
      od <- desConnector.getObligations(vrn)
    } yield (Ok(Json.toJson(od)))
  }

  def getFinancialData(vrn: Vrn): Action[AnyContent] = Action.async { implicit request =>
    for {
      fd <- desConnector.getFinancialData(vrn)
    } yield (Ok(Json.toJson(fd)))
  }

  def getCustomerData(vrn: Vrn): Action[AnyContent] = Action.async { implicit request =>
    for {
      cd <- desConnector.getCustomerData(vrn)
    } yield (Ok(Json.toJson(cd)))
  }

}
