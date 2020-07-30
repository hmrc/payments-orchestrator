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

package controllers.action

import com.google.inject.Inject
import play.api.http.Status.NOT_FOUND
import play.api.mvc._
import uk.gov.hmrc.http.UpstreamErrorResponse

import scala.concurrent.{ExecutionContext, Future}

class NotFoundResponseAction @Inject() (cc: MessagesControllerComponents) extends ActionBuilder[Request, AnyContent] with Results {
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    block(request).recover {
      case e: UpstreamErrorResponse if e.statusCode == NOT_FOUND => NotFound
      case t: Throwable => throw t
    }(executionContext)
  }

  override val parser: BodyParser[AnyContent] = cc.parsers.defaultBodyParser

  override protected val executionContext: ExecutionContext = cc.executionContext
}
