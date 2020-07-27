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

import java.time.LocalDate

import model.Vrn
import model.des.{DirectDebitDetails, _}
import play.api.libs.json.{JsValue, Json}

object DesData {
  private val approvedInformation =
    ApprovedInformation(
      Some(CustomerDetails(Some(true), Some(false))),
      Some(BankDetails(Some("Account holder"), Some("11112222"), Some("667788"))),
      Some(PPOB(Some(Address(Some("VAT PPOB Line1"), Some("VAT PPOB Line2"), Some("VAT PPOB Line3"), Some("VAT PPOB Line4"), Some("TF3 4ER"), Some("GB"))))))

  val approvedCustomerInformation: CustomerInformation = CustomerInformation(Some(approvedInformation), None)

  val customerInformation: CustomerInformation =
    CustomerInformation(Some(approvedInformation), Some(InFlightInformation(Some(ChangeIndicators(Some(true), Some(false))))))

  val directDebitData: DirectDebitData = DirectDebitData(Some(List(DirectDebitDetails("Tester Surname", "404784", "70872490"))))

  val financialData: FinancialData =
    FinancialData("VRN", "2345678890", "VATC", "2019-08-20T10:44:05Z", Seq(Transaction("VAT Return Credit Charge", Option("18AC"))))

  val repaymentsDetail: Seq[RepaymentDetailData] = Seq(RepaymentDetailData(
    LocalDate.parse("2001-01-01"),
    Option(LocalDate.parse("2001-01-01")),
    Option(LocalDate.parse("2001-01-01")),
    "18AC",
    "INITIAL",
    BigDecimal(1000),
    Option(1),
    100.02
  ))

  //language=JSON
  val repaymentDetailJson: JsValue = Json.parse(
    s"""[{
        "returnCreationDate": "2001-01-01",
        "sentForRiskingDate": "2001-01-01",
        "lastUpdateReceivedDate": "2001-01-01",
        "periodKey": "18AC",
        "riskingStatus": "INITIAL",
        "vatToPay_BOX5": 1000,
        "supplementDelayDays": 1,
        "originalPostingAmount": 100.02
    }]
""".stripMargin
  )

  //language=JSON
  val repaymentDetailsNotFound: JsValue = Json.parse(
    s"""{
   "code": "NOT_FOUND",
   "reason": "The remote endpoint has indicated that no data can be found"}
""".stripMargin
  )

  //language=JSON
  val directDebitDataJson: JsValue = Json.parse(
    s"""{
        "directDebitDetails": [
                     {
                          "accountHolderName": "Tester Surname",
                         "sortCode": "404784",
                         "accountNumber": "70872490"
                      }
                  ]
                  }
       """.stripMargin)

  //language=JSON
  val approvedInformationJson: JsValue = Json.parse(
    s"""
       {
          "approvedInformation":{
             "customerDetails": {
                "welshIndicator": true,
                "isPartialMigration": false
             },
             "bankDetails":{
                "accountHolderName":"Account holder",
                "bankAccountNumber":"11112222",
                "sortCode":"667788"
             },
             "PPOB":{
                "address":{
                   "line1":"VAT PPOB Line1",
                   "line2":"VAT PPOB Line2",
                   "line3":"VAT PPOB Line3",
                   "line4":"VAT PPOB Line4",
                   "postCode":"TF3 4ER",
                   "countryCode":"GB"
                }
             }
          }
       }
     """.stripMargin)

  //language=JSON
  val financialDataJson: JsValue = Json.parse(
    s"""
       {
          "idType":"VRN",
          "idNumber":"2345678890",
          "regimeType":"VATC",
          "processingDate":"2019-08-20T10:44:05Z",
          "financialTransactions":[
             {
                "chargeType" : "VAT Return Credit Charge",
                "periodKey":"18AC"
             }
          ]
       }
     """.stripMargin
  )

  //language=JSON
  val customerDataNotFound: JsValue = Json.parse(
    s"""
                                                   {
                                                       "code": "NOT_FOUND",
                                                       "reason": "The back end has indicated that No subscription can be found."
                                                   }
       """.stripMargin)

  // language=JSON
  val customerDataOk: JsValue = Json.parse(
    s"""
     {
         "approvedInformation": {
             "customerDetails": {
                 "nameIsReadOnly": true,
                 "organisationName": "TAXPAYER NAME_1",
                 "dataOrigin": "0001",
                 "mandationStatus": "1",
                 "registrationReason": "0001",
                 "effectiveRegistrationDate": "2017-01-02",
                 "businessStartDate": "2017-01-01",
                 "welshIndicator": true,
                 "partyType": "50",
                 "optionToTax": true,
                 "isPartialMigration": false,
                 "isInsolvent": false,
                 "overseasIndicator": true
             },
             "PPOB": {
                 "address": {
                     "line1": "VAT PPOB Line1",
                     "line2": "VAT PPOB Line2",
                     "line3": "VAT PPOB Line3",
                     "line4": "VAT PPOB Line4",
                     "postCode": "TF3 4ER",
                     "countryCode": "GB"
                 },
                 "contactDetails": {
                     "primaryPhoneNumber": "012345678901",
                     "mobileNumber": "012345678902",
                     "faxNumber": "012345678903",
                     "emailAddress": "lewis.hay@digital.hmrc.gov.uk",
                     "emailVerified": true
                 },
                 "websiteAddress": "www.tumbleweed.com"
             },
             "bankDetails": {
                 "accountHolderName": "Account holder",
                 "bankAccountNumber": "11112222",
                 "sortCode": "667788"
             },
             "businessActivities": {
                 "primaryMainCode": "10410",
                 "mainCode2": "10611",
                 "mainCode3": "10710",
                 "mainCode4": "10720"
             },
             "flatRateScheme": {
                 "FRSCategory": "003",
                 "FRSPercentage": 59.99,
                 "startDate": "0001-01-01",
                 "endDate": "9999-12-31",
                 "limitedCostTrader": true
             },
             "returnPeriod": {
                 "stdReturnPeriod": "MM"
             }
         },
         "inFlightInformation": {
             "changeIndicators": {
                 "organisationDetails": false,
                 "PPOBDetails": false,
                 "correspondenceContactDetails": false,
                 "bankDetails": true,
                 "returnPeriod": false,
                 "flatRateScheme": false,
                 "businessActivities": false,
                 "deregister": false,
                 "effectiveDateOfRegistration": false,
                 "mandationStatus": true
             },
             "inFlightChanges": {
                 "bankDetails": {
                     "formInformation": {
                         "formBundle": "092000001020",
                         "dateReceived": "2019-03-04"
                     },
                     "accountHolderName": "Account holder",
                     "bankAccountNumber": "11112222",
                     "sortCode": "667788"
                 },
                 "mandationStatus": {
                     "formInformation": {
                         "formBundle": "092000002124",
                         "dateReceived": "2019-08-15"
                     },
                     "mandationStatus": "3"
                 }
             }
         }
     }
       """.stripMargin)

  //language=JSON
  val customerDataOkWithoutBankDetails: JsValue = Json.parse(
    s"""
     {
         "approvedInformation": {
             "customerDetails": {
                 "nameIsReadOnly": true,
                 "organisationName": "TAXPAYER NAME_1",
                 "dataOrigin": "0001",
                 "mandationStatus": "1",
                 "registrationReason": "0001",
                 "effectiveRegistrationDate": "2017-01-02",
                 "businessStartDate": "2017-01-01",
                 "welshIndicator": true,
                 "partyType": "50",
                 "optionToTax": true,
                 "isPartialMigration": false,
                 "isInsolvent": false,
                 "overseasIndicator": true
             },
             "PPOB": {
                 "address": {
                     "line1": "VAT PPOB Line1",
                     "line2": "VAT PPOB Line2",
                     "line3": "VAT PPOB Line3",
                     "line4": "VAT PPOB Line4",
                     "postCode": "TF3 4ER",
                     "countryCode": "GB"
                 },
                 "contactDetails": {
                     "primaryPhoneNumber": "012345678901",
                     "mobileNumber": "012345678902",
                     "faxNumber": "012345678903",
                     "emailAddress": "lewis.hay@digital.hmrc.gov.uk",
                     "emailVerified": true
                 },
                 "websiteAddress": "www.tumbleweed.com"
             },
             "businessActivities": {
                 "primaryMainCode": "10410",
                 "mainCode2": "10611",
                 "mainCode3": "10710",
                 "mainCode4": "10720"
             },
             "flatRateScheme": {
                 "FRSCategory": "003",
                 "FRSPercentage": 59.99,
                 "startDate": "0001-01-01",
                 "endDate": "9999-12-31",
                 "limitedCostTrader": true
             },
             "returnPeriod": {
                 "stdReturnPeriod": "MM"
             }
         },
         "inFlightInformation": {
             "changeIndicators": {
                 "organisationDetails": false,
                 "PPOBDetails": false,
                 "correspondenceContactDetails": false,
                 "bankDetails": true,
                 "returnPeriod": false,
                 "flatRateScheme": false,
                 "businessActivities": false,
                 "deregister": false,
                 "effectiveDateOfRegistration": false,
                 "mandationStatus": true
             },
             "inFlightChanges": {
                 "bankDetails": {
                     "formInformation": {
                         "formBundle": "092000001020",
                         "dateReceived": "2019-03-04"
                     },
                     "accountHolderName": "Account holder",
                     "bankAccountNumber": "70872490",
                     "sortCode": "667788"
                 },
                 "mandationStatus": {
                     "formInformation": {
                         "formBundle": "092000002124",
                         "dateReceived": "2019-08-15"
                     },
                     "mandationStatus": "3"
                 }
             }
         }
     }
       """.stripMargin)

  //language=JSON
  val financialDataNotFound: JsValue = Json.parse(
    s"""
                                                     {
                                                        "code": "NOT_FOUND",
                                                        "reason": "The remote endpoint has indicated that no data can be found."
                                                    }
       """.stripMargin)

  //language=JSON
  def financialDataOk(vrn: Vrn): JsValue = Json.parse(
    s"""
                                                 {
                                                   "idType": "VRN",
                                                   "idNumber": "${vrn.value}",
                                                   "regimeType": "VATC",
                                                   "processingDate": "2019-08-20T10:44:05Z",
                                                   "financialTransactions": [
                                                       {
                                                           "chargeType": "VAT Return Credit Charge",
                                                           "mainType": "VAT Protective Assessment",
                                                           "periodKey": "18AC",
                                                           "periodKeyDescription": "March 2018",
                                                           "taxPeriodFrom": "2018-03-01",
                                                           "taxPeriodTo": "2018-03-31",
                                                           "businessPartner": "0100113120",
                                                           "contractAccountCategory": "33",
                                                           "contractAccount": "091700000405",
                                                           "contractObjectType": "ZVAT",
                                                           "contractObject": "00000180000000000165",
                                                           "sapDocumentNumber": "002720000571",
                                                           "sapDocumentNumberItem": "0001",
                                                           "chargeReference": "XB002616013425",
                                                           "mainTransaction": "4733",
                                                           "subTransaction": "1174",
                                                           "originalAmount": 796.0,
                                                           "outstandingAmount": 796.0,
                                                           "accruedInterest": 24.23,
                                                           "items": [
                                                               {
                                                                   "subItem": "000",
                                                                   "dueDate": "2018-09-12",
                                                                   "amount": 796.0
                                                               }
                                                           ]
                                                       },
                                                       {
                                                           "chargeType": "VAT Return Credit Charge",
                                                           "mainType": "VAT PA Default Interest",
                                                           "periodKey": "18AC",
                                                           "periodKeyDescription": "March 2018",
                                                           "taxPeriodFrom": "2018-03-01",
                                                           "taxPeriodTo": "2018-03-31",
                                                           "businessPartner": "0100113120",
                                                           "contractAccountCategory": "33",
                                                           "contractAccount": "091700000405",
                                                           "contractObjectType": "ZVAT",
                                                           "contractObject": "00000180000000000165",
                                                           "sapDocumentNumber": "002720000571",
                                                           "sapDocumentNumberItem": "0002",
                                                           "chargeReference": "XB002616013425",
                                                           "mainTransaction": "4708",
                                                           "subTransaction": "1175",
                                                           "originalAmount": 3.59,
                                                           "outstandingAmount": 3.59,
                                                           "items": [
                                                               {
                                                                   "subItem": "000",
                                                                   "dueDate": "2018-08-13",
                                                                   "amount": 3.59
                                                               }
                                                           ]
                                                       },
                                                       {
                                                           "chargeType": "VAT Return Credit Charge",
                                                           "mainType": "VAT Return Charge",
                                                           "periodKey": "18AC",
                                                           "periodKeyDescription": "March 2018",
                                                           "taxPeriodFrom": "2018-03-01",
                                                           "taxPeriodTo": "2018-03-31",
                                                           "businessPartner": "0100113120",
                                                           "contractAccountCategory": "33",
                                                           "contractAccount": "091700000405",
                                                           "contractObjectType": "ZVAT",
                                                           "contractObject": "00000180000000000165",
                                                           "sapDocumentNumber": "003030001189",
                                                           "sapDocumentNumberItem": "0001",
                                                           "chargeReference": "XJ002610110056",
                                                           "mainTransaction": "4700",
                                                           "subTransaction": "1174",
                                                           "originalAmount": 10169.45,
                                                           "outstandingAmount": 10169.45,
                                                           "items": [
                                                               {
                                                                   "subItem": "001",
                                                                   "dueDate": "2018-05-07",
                                                                   "amount": 135.0
                                                               },
                                                               {
                                                                   "subItem": "000",
                                                                   "dueDate": "2018-05-07",
                                                                   "amount": 10034.45
                                                               }
                                                           ]
                                                       },
                                                       {
                                                           "chargeType": "VAT Return Credit Charge",
                                                           "mainType": "VAT Protective Assessment",
                                                           "periodKey": "18AC",
                                                           "periodKeyDescription": "March 2018",
                                                           "taxPeriodFrom": "2018-03-01",
                                                           "taxPeriodTo": "2018-03-31",
                                                           "businessPartner": "0100113120",
                                                           "contractAccountCategory": "33",
                                                           "contractAccount": "091700000405",
                                                           "contractObjectType": "ZVAT",
                                                           "contractObject": "00000180000000000165",
                                                           "sapDocumentNumber": "003360001206",
                                                           "sapDocumentNumberItem": "0001",
                                                           "chargeReference": "XV002616013469",
                                                           "mainTransaction": "4733",
                                                           "subTransaction": "1174",
                                                           "originalAmount": 796.0,
                                                           "outstandingAmount": 796.0,
                                                           "accruedInterest": 23.45,
                                                           "items": [
                                                               {
                                                                   "subItem": "000",
                                                                   "dueDate": "2018-09-23",
                                                                   "amount": 796.0
                                                               }
                                                           ]
                                                       },
                                                       {
                                                           "chargeType": "VAT Return Credit Charge",
                                                           "mainType": "VAT PA Default Interest",
                                                           "periodKey": "18AC",
                                                           "periodKeyDescription": "March 2018",
                                                           "taxPeriodFrom": "2018-03-01",
                                                           "taxPeriodTo": "2018-03-31",
                                                           "businessPartner": "0100113120",
                                                           "contractAccountCategory": "33",
                                                           "contractAccount": "091700000405",
                                                           "contractObjectType": "ZVAT",
                                                           "contractObject": "00000180000000000165",
                                                           "sapDocumentNumber": "003360001206",
                                                           "sapDocumentNumberItem": "0002",
                                                           "chargeReference": "XV002616013469",
                                                           "mainTransaction": "4708",
                                                           "subTransaction": "1175",
                                                           "originalAmount": 5.56,
                                                           "outstandingAmount": 5.56,
                                                           "items": [
                                                               {
                                                                   "subItem": "000",
                                                                   "dueDate": "2018-08-24",
                                                                   "amount": 5.56
                                                               }
                                                           ]
                                                       }
                                                   ]
                                               }
       """.stripMargin)

  //language=JSON
  def financialDataSingleOk(vrn: Vrn): JsValue = Json.parse(
    s"""
                                                 {
                                                   "idType": "VRN",
                                                   "idNumber": "${vrn.value}",
                                                   "regimeType": "VATC",
                                                   "processingDate": "2019-08-20T10:44:05Z",
                                                   "financialTransactions": [
                                                       {
                                                           "chargeType": "VAT Return Credit Charge",
                                                           "mainType": "VAT PA Default Interest",
                                                           "periodKey": "18AC",
                                                           "periodKeyDescription": "March 2018",
                                                           "taxPeriodFrom": "2018-03-01",
                                                           "taxPeriodTo": "2018-03-31",
                                                           "businessPartner": "0100113120",
                                                           "contractAccountCategory": "33",
                                                           "contractAccount": "091700000405",
                                                           "contractObjectType": "ZVAT",
                                                           "contractObject": "00000180000000000165",
                                                           "sapDocumentNumber": "003360001206",
                                                           "sapDocumentNumberItem": "0002",
                                                           "chargeReference": "XV002616013469",
                                                           "mainTransaction": "4708",
                                                           "subTransaction": "1175",
                                                           "originalAmount": 5.56,
                                                           "outstandingAmount": 5.56,
                                                           "items": [
                                                               {
                                                                   "subItem": "000",
                                                                   "dueDate": "2018-08-24",
                                                                   "amount": 5.56,
                                                                   "clearingDate": "2018-03-31"
                                                               }
                                                           ]
                                                       }
                                                   ]
                                               }
       """.stripMargin)

  //language=JSON
  def ddOk: JsValue = Json.parse(
    s"""
       {
           "directDebitMandateFound": true,
           "directDebitDetails": [
               {
                   "directDebitInstructionNumber": "091700000409",
                   "directDebitPlanType": "VPP",
                   "dateCreated": "2019-09-20",
                   "accountHolderName": "Tester Surname",
                   "sortCode": "404784",
                   "accountNumber": "70872490"
               }
           ]
       }
       """.stripMargin)

  //language=JSON
  val ddNotFound: JsValue = Json.parse(
    s"""
                                                     {
                                                        "code": "NOT_FOUND",
                                                        "reason": "The back end has indicated that there is no match found for the given identifier"
                                                    }
       """.stripMargin)

  //language=JSON
  val ddOkNoMandate: JsValue = Json.parse(
    s"""
       {
     "directDebitMandateFound": false
      }
           """.stripMargin)

  //language=JSON
  def financialDataOkTRS(vrn: Vrn): JsValue = Json.parse(
    s"""{
   "idType":"VRN",
   "idNumber":"${vrn.value}",
   "regimeType":"VATC",
   "processingDate":"2019-11-05T10:35:24Z",
   "financialTransactions":[
      {
         "chargeType":"Payment on account",
         "mainType":"On Account",
         "businessPartner":"0100141367",
         "contractAccountCategory":"43",
         "contractAccount":"027000000864",
         "contractObjectType":"NI",
         "contractObject":"00000240000000000314",
         "sapDocumentNumber":"002850000401",
         "sapDocumentNumberItem":"0001",
         "mainTransaction":"0060",
         "subTransaction":"0100",
         "originalAmount":-4500.0,
         "clearedAmount":-4500.0,
         "items":[
            {
               "subItem":"000",
               "dueDate":"2019-05-10",
               "amount":-4500.0,
               "clearingDate":"2019-05-10",
               "clearingReason":"Allocated to Charge",
               "paymentLock":"Re-Check Payment method - Unsu",
               "paymentReference":"XG002610131772",
               "paymentAmount":5000.0,
               "paymentMethod":"CREDIT FOR INTERNET RECEIPTS",
               "paymentLot":"PCP05190514",
               "paymentLotItem":"000001",
               "clearingSAPDocument":"003330001826"
            }
         ]
      },
      {
         "chargeType":"Payment on account",
         "mainType":"On Account",
         "businessPartner":"0100141367",
         "contractAccountCategory":"43",
         "contractAccount":"027000000864",
         "contractObjectType":"NI",
         "contractObject":"00000240000000000314",
         "sapDocumentNumber":"002900001960",
         "sapDocumentNumberItem":"0001",
         "mainTransaction":"0060",
         "subTransaction":"0100",
         "originalAmount":-1670.0,
         "clearedAmount":-1670.0,
         "items":[
            {
               "subItem":"000",
               "dueDate":"2019-05-14",
               "amount":-1670.0,
               "clearingDate":"2019-05-24",
               "clearingReason":"Outgoing payment - Paid",
               "outgoingPaymentMethod":"BACS Payment out",
               "paymentReference":"0100141367",
               "paymentAmount":1670.0,
               "paymentMethod":"PAYMENTS MADE BY CHEQUE",
               "paymentLot":"PS101190514",
               "paymentLotItem":"000001",
               "clearingSAPDocument":"002150001681"
            }
         ]
      },
      {
         "chargeType":"Payment on account",
         "mainType":"On Account",
         "periodKey":"19A1",
         "periodKeyDescription":"February 2019 to April 2019",
         "businessPartner":"0100141367",
         "contractAccountCategory":"43",
         "contractAccount":"027000000864",
         "contractObjectType":"NI",
         "contractObject":"00000240000000000314",
         "sapDocumentNumber":"003160001928",
         "sapDocumentNumberItem":"0001",
         "mainTransaction":"0060",
         "subTransaction":"0100",
         "originalAmount":-600.0,
         "clearedAmount":-600.0,
         "items":[
            {
               "subItem":"000",
               "dueDate":"2019-05-10",
               "amount":-600.0,
               "clearingDate":"2019-05-10",
               "clearingReason":"Allocated to Charge",
               "paymentReference":"XF002610131861",
               "paymentAmount":600.0,
               "paymentMethod":"CREDIT FOR INTERNET RECEIPTS",
               "paymentLot":"PCP01190520",
               "paymentLotItem":"0",
               "clearingSAPDocument":"003140001771"
            }
         ]
      },
      {
         "chargeType":"Payment on account",
         "mainType":"On Account",
         "periodKey":"19A2",
         "periodKeyDescription":"May 2019 to July 2019",
         "businessPartner":"0100141367",
         "contractAccountCategory":"43",
         "contractAccount":"027000000864",
         "contractObjectType":"NI",
         "contractObject":"00000240000000000314",
         "sapDocumentNumber":"003170001871",
         "sapDocumentNumberItem":"0001",
         "mainTransaction":"0060",
         "subTransaction":"0100",
         "originalAmount":-500.0,
         "clearedAmount":-500.0,
         "items":[
            {
               "subItem":"000",
               "dueDate":"2019-05-10",
               "amount":-500.0,
               "clearingDate":"2019-05-10",
               "clearingReason":"Allocated to Charge",
               "paymentReference":"XG002610131772",
               "paymentAmount":5000.0,
               "paymentMethod":"CREDIT FOR INTERNET RECEIPTS",
               "paymentLot":"PCP05190514",
               "paymentLotItem":"000001",
               "clearingSAPDocument":"003340001799"
            }
         ]
      },
      {
         "chargeType":"VAT Return Debit Charge",
         "mainType":"VAT Return Charge",
         "periodKey":"19AA",
         "periodKeyDescription":"January 2019",
         "taxPeriodFrom":"2019-01-01",
         "taxPeriodTo":"2019-01-31",
         "businessPartner":"0100141367",
         "contractAccountCategory":"33",
         "contractAccount":"091700003858",
         "contractObjectType":"ZVAT",
         "contractObject":"00000180000000002456",
         "sapDocumentNumber":"003190002179",
         "sapDocumentNumberItem":"0001",
         "chargeReference":"XJ002610135792",
         "mainTransaction":"4700",
         "subTransaction":"1174",
         "originalAmount":30000.0,
         "outstandingAmount":30000.0,
         "items":[
            {
               "subItem":"000",
               "dueDate":"2019-03-07",
               "amount":30000.0
            }
         ]
      },
      {
         "chargeType":"VAT Return Debit Charge",
         "mainType":"VAT Return Charge",
         "taxPeriodFrom":"2019-02-01",
         "taxPeriodTo":"2019-02-28",
         "businessPartner":"0100141367",
         "contractAccountCategory":"33",
         "contractAccount":"091700003858",
         "contractObjectType":"ZVAT",
         "contractObject":"00000180000000002456",
         "sapDocumentNumber":"003460002272",
         "sapDocumentNumberItem":"0001",
         "chargeReference":"0000000012345678",
         "mainTransaction":"4700",
         "subTransaction":"1174",
         "originalAmount":2000.0,
         "outstandingAmount":2000.0,
         "items":[
            {
               "subItem":"000",
               "dueDate":"2019-03-03",
               "amount":2000.0
            }
         ]
      }
   ]
}""".stripMargin
  )

  //language=JSON
  def financialDataOkTRS404(vrn: Vrn): JsValue = Json.parse(
    s"""{
   "idType":"VRN",
   "idNumber":"${vrn.value}",
   "regimeType":"VATC",
   "processingDate":"2019-11-05T10:35:24Z",
   "financialTransactions":[
      {
         "chargeType":"Payment on account",
         "mainType":"On Account",
         "businessPartner":"0100141367",
         "contractAccountCategory":"43",
         "contractAccount":"027000000864",
         "contractObjectType":"NI",
         "contractObject":"00000240000000000314",
         "sapDocumentNumber":"002850000401",
         "sapDocumentNumberItem":"0001",
         "mainTransaction":"0060",
         "subTransaction":"0100",
         "originalAmount":-4500.0,
         "clearedAmount":-4500.0,
         "items":[
            {
               "subItem":"000",
               "dueDate":"2019-05-10",
               "amount":-4500.0,
               "clearingDate":"2019-05-10",
               "clearingReason":"Allocated to Charge",
               "paymentLock":"Re-Check Payment method - Unsu",
               "paymentReference":"XG002610131772",
               "paymentAmount":5000.0,
               "paymentMethod":"CREDIT FOR INTERNET RECEIPTS",
               "paymentLot":"PCP05190514",
               "paymentLotItem":"000001",
               "clearingSAPDocument":"003330001826"
            }
         ]
      },
      {
         "chargeType":"Payment on account",
         "mainType":"On Account",
         "businessPartner":"0100141367",
         "contractAccountCategory":"43",
         "contractAccount":"027000000864",
         "contractObjectType":"NI",
         "contractObject":"00000240000000000314",
         "sapDocumentNumber":"002900001960",
         "sapDocumentNumberItem":"0001",
         "mainTransaction":"0060",
         "subTransaction":"0100",
         "originalAmount":-1670.0,
         "clearedAmount":-1670.0,
         "items":[
            {
               "subItem":"000",
               "dueDate":"2019-05-14",
               "amount":-1670.0,
               "clearingDate":"2019-05-24",
               "clearingReason":"Outgoing payment - Paid",
               "outgoingPaymentMethod":"BACS Payment out",
               "paymentReference":"0100141367",
               "paymentAmount":1670.0,
               "paymentMethod":"PAYMENTS MADE BY CHEQUE",
               "paymentLot":"PS101190514",
               "paymentLotItem":"000001",
               "clearingSAPDocument":"002150001681"
            }
         ]
      },
      {
         "chargeType":"Payment on account",
         "mainType":"On Account",
         "periodKey":"19A1",
         "periodKeyDescription":"February 2019 to April 2019",
         "businessPartner":"0100141367",
         "contractAccountCategory":"43",
         "contractAccount":"027000000864",
         "contractObjectType":"NI",
         "contractObject":"00000240000000000314",
         "sapDocumentNumber":"003160001928",
         "sapDocumentNumberItem":"0001",
         "mainTransaction":"0060",
         "subTransaction":"0100",
         "originalAmount":-600.0,
         "clearedAmount":-600.0,
         "items":[
            {
               "subItem":"000",
               "dueDate":"2019-05-10",
               "amount":-600.0,
               "clearingDate":"2019-05-10",
               "clearingReason":"Allocated to Charge",
               "paymentReference":"XF002610131861",
               "paymentAmount":600.0,
               "paymentMethod":"CREDIT FOR INTERNET RECEIPTS",
               "paymentLot":"PCP01190520",
               "paymentLotItem":"0",
               "clearingSAPDocument":"003140001771"
            }
         ]
      },
      {
         "chargeType":"Payment on account",
         "mainType":"On Account",
         "periodKey":"19A2",
         "periodKeyDescription":"May 2019 to July 2019",
         "businessPartner":"0100141367",
         "contractAccountCategory":"43",
         "contractAccount":"027000000864",
         "contractObjectType":"NI",
         "contractObject":"00000240000000000314",
         "sapDocumentNumber":"003170001871",
         "sapDocumentNumberItem":"0001",
         "mainTransaction":"0060",
         "subTransaction":"0100",
         "originalAmount":-500.0,
         "clearedAmount":-500.0,
         "items":[
            {
               "subItem":"000",
               "dueDate":"2019-05-10",
               "amount":-500.0,
               "clearingDate":"2019-05-10",
               "clearingReason":"Allocated to Charge",
               "paymentReference":"XG002610131772",
               "paymentAmount":5000.0,
               "paymentMethod":"CREDIT FOR INTERNET RECEIPTS",
               "paymentLot":"PCP05190514",
               "paymentLotItem":"000001",
               "clearingSAPDocument":"003340001799"
            }
         ]
      }
   ]
}""".stripMargin
  )

}

