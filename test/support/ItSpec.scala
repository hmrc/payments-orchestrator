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

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{ Millis, Seconds, Span }
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import scala.concurrent.ExecutionContext

/**
 * This is common spec for every test case which brings all of useful routines we want to use in our scenarios.
 */

trait ItSpec
  extends AnyFreeSpecLike
  with GuiceOneServerPerSuite
  with WireMockSupport
  with Matchers
  with ScalaFutures:

  given ExecutionContext = ExecutionContext.Implicits.global

  def fakeRequest(method: String = "", url: String = ""): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(method, url)
      .withHeaders(uk.gov.hmrc.http.HeaderNames.authorisation -> "Bearer 123")

  given PatienceConfig = PatienceConfig(
    timeout = scaled(Span(3, Seconds)),
    interval = scaled(Span(300, Millis)))

  lazy val injector: Injector = app.injector

  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(Map[String, Any](
        "microservice.services.auth.port" -> WireMockSupport.port,
        "microservice.services.des.port" -> WireMockSupport.port,
        "microservice.services.auth.port" -> WireMockSupport.port,
        "microservice.services.auth.host" -> WireMockSupport.wireMockHost)).build()
