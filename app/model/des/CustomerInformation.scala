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

package model.des

import play.api.libs.json._

final case class CustomerInformation(approvedInformation: Option[ApprovedInformation], inFlightInformation: Option[InFlightInformation]) {
  val bankDetailsChangeIndicatorExists: Option[Boolean] =
    for {
      inflight <- inFlightInformation
      changeIndicators <- inflight.changeIndicators
      bankDetails <- changeIndicators.bankDetails
    } yield bankDetails

  val PPOBDetailsChangeIndicatorExists: Option[Boolean] =
    for {
      inflight <- inFlightInformation
      changeIndicators <- inflight.changeIndicators
      pPOBDetails <- changeIndicators.PPOBDetails
    } yield pPOBDetails
}

object CustomerInformation {
  implicit val format: OFormat[CustomerInformation] = Json.format[CustomerInformation]
}

final case class InFlightInformation(changeIndicators: Option[ChangeIndicators])

object InFlightInformation {
  implicit val format: OFormat[InFlightInformation] = Json.format[InFlightInformation]
}

final case class ChangeIndicators(bankDetails: Option[Boolean], PPOBDetails: Option[Boolean])

object ChangeIndicators {
  implicit val format: OFormat[ChangeIndicators] = Json.format[ChangeIndicators]
}

final case class ApprovedInformation(customerDetails: Option[CustomerDetails], bankDetails: Option[BankDetails], PPOB: Option[PPOB])

object ApprovedInformation {
  implicit val format: OFormat[ApprovedInformation] = Json.format[ApprovedInformation]
}

final case class CustomerDetails(welshIndicator: Option[Boolean], isPartialMigration: Option[Boolean])

object CustomerDetails {
  implicit val format: OFormat[CustomerDetails] = Json.format[CustomerDetails]
}

final case class PPOB(address: Option[Address])

object PPOB {
  implicit val format: OFormat[PPOB] = Json.format[PPOB]
}

final case class Address(
    line1:       Option[String],
    line2:       Option[String],
    line3:       Option[String],
    line4:       Option[String],
    postCode:    Option[String],
    countryCode: Option[String]
)

object Address {
  implicit val format: OFormat[Address] = Json.format[Address]
}

final case class BankDetails(accountHolderName: Option[String], bankAccountNumber: Option[String], sortCode: Option[String])

object BankDetails {
  implicit val format: OFormat[BankDetails] = Json.format[BankDetails]
}
