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

import play.api.libs.json.Json
import support.{DesData, UnitSpec}

class CustomerInformationSpec extends UnitSpec {

  "to json" in {
    Json.toJson(DesData.approvedCustomerInformation) shouldBe DesData.approvedInformationJson
  }

  "from json" in {
    DesData.approvedInformationJson.as[CustomerInformation] shouldBe DesData.approvedCustomerInformation
  }

  "bank details change Indicator exist" in {
    DesData.customerInformation.bankDetailsChangeIndicatorExists shouldBe Some(true)
  }

  "PPOB change Indicator not exist" in {
    DesData.customerInformation.PPOBDetailsChangeIndicatorExists shouldBe Some(false)
  }

  "bank details change Indicator not exist as None" in {
    DesData.approvedCustomerInformation.bankDetailsChangeIndicatorExists shouldBe None
  }

  "PPOB change Indicator not exist as None" in {
    DesData.approvedCustomerInformation.PPOBDetailsChangeIndicatorExists shouldBe None
  }

  "Deregistration data included" - {
    "to json" in {
      Json.toJson(DesData.approvedCustomerInformationDeregistered) shouldBe DesData.approvedInformationDeregisteredJson

    }
    "from json" in {
      DesData.approvedInformationDeregisteredJson.as[CustomerInformation] shouldBe DesData.approvedCustomerInformationDeregistered
    }
  }

}
