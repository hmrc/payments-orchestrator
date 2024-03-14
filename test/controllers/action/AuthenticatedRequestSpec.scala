/*
 * Copyright 2024 HM Revenue & Customs
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

import model.EnrolmentKeys._
import model.Vrn
import org.scalatest.funsuite.AnyFunSuiteLike
import play.api.mvc.{AnyContent, AnyContentAsEmpty}
import play.api.test.FakeRequest
import support.UnitSpec
import uk.gov.hmrc.auth.core.{Enrolment, EnrolmentIdentifier, Enrolments}

class AuthenticatedRequestSpec extends UnitSpec {

  def fakeRequest(method: String = "", url: String = ""): FakeRequest[AnyContentAsEmpty.type] = FakeRequest(method, url).withHeaders(
    uk.gov.hmrc.http.HeaderNames.authorisation -> "Bearer 123"
  )

  val vatDecEnrolment = Enrolment(vatDecEnrolmentKey, Seq(EnrolmentIdentifier("VRN", "1345678890")), "Activated")
  val vatVarEnrolment = Enrolment(vatVarEnrolmentKey, Seq(EnrolmentIdentifier("VRN", "2345678890")), "Activated")
  val mtdVatEnrolment = Enrolment(mtdVatEnrolmentKey, Seq(EnrolmentIdentifier("VRN", "3345678890")), "Activated")

  val enrolments: Enrolments = Enrolments(Set(vatDecEnrolment, vatVarEnrolment, mtdVatEnrolment))
  def authenticatedRequest(): AuthenticatedRequest[AnyContent] = new AuthenticatedRequest[AnyContent](fakeRequest("POST", ""), enrolments)

  "Authenticated request should not give vat" in {

    //    authenticatedRequest().enrolmentsVrn shouldBe (())

  }

}
