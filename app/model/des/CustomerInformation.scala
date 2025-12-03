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

package model.des

import play.api.libs.json._

import java.time.LocalDate

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

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object CustomerInformation {
  implicit val format: OFormat[CustomerInformation] = Json.format[CustomerInformation]
}

@SuppressWarnings(Array("org.wartremover.warts.Any"))
final case class InFlightInformation(changeIndicators: Option[ChangeIndicators], inFlightChanges: Option[InFlightChanges])

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object InFlightInformation {
  implicit val format: OFormat[InFlightInformation] = Json.format[InFlightInformation]
}

@SuppressWarnings(Array("org.wartremover.warts.Any"))
final case class ChangeIndicators(bankDetails: Option[Boolean], PPOBDetails: Option[Boolean])

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object ChangeIndicators {
  implicit val format: OFormat[ChangeIndicators] = Json.format[ChangeIndicators]
}

final case class InFlightChanges(bankDetails: Option[BankDetails])

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object InFlightChanges {
  implicit val format: OFormat[InFlightChanges] = Json.format[InFlightChanges]
}

final case class FormInformation(dateReceived: Option[String])

object FormInformation {
  implicit val format: OFormat[FormInformation] = Json.format[FormInformation]
}

final case class ApprovedInformation(
    customerDetails: Option[CustomerDetails],
    bankDetails:     Option[BankDetails],
    PPOB:            Option[PPOB],
    deregistration:  Option[Deregistration]  = None
)

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object ApprovedInformation {
  implicit val format: OFormat[ApprovedInformation] = Json.format[ApprovedInformation]
}

final case class CustomerDetails(welshIndicator: Option[Boolean], isPartialMigration: Option[Boolean])

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object CustomerDetails {
  implicit val format: OFormat[CustomerDetails] = Json.format[CustomerDetails]
}

final case class PPOB(address: Option[Address])

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object PPOB {
  implicit val format: OFormat[PPOB] = Json.format[PPOB]
}

final case class Deregistration(
    deregistrationReason:     Option[String],
    effectDateOfCancellation: Option[LocalDate],
    lastReturnDueDate:        Option[LocalDate]
) {
}

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object Deregistration {
  implicit val format: OFormat[Deregistration] = Json.format[Deregistration]
}

final case class Address(
    line1:       Option[String],
    line2:       Option[String],
    line3:       Option[String],
    line4:       Option[String],
    postCode:    Option[String],
    countryCode: Option[String]
)

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object Address {
  implicit val format: OFormat[Address] = Json.format[Address]
}

final case class BankDetails(accountHolderName: Option[String], bankAccountNumber: Option[String], sortCode: Option[String], buildingSocietyNumber: Option[String], formInformation: Option[FormInformation])

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object BankDetails {
  implicit val format: OFormat[BankDetails] = Json.format[BankDetails]
}
