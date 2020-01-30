@development
@egress_bulk_citizen
@iras
Feature: Data processing for IRAS tri-monthly citizen file

  # Possible value for each column
  # | Name  | NaturalId | Type | DOD      | Nationality           | CitizenshipRenunciationDate | AIStatus           | AIYear |
  # | Muthu | S9999999A | NRIC | 20201212 | SINGAPORE_CITIZEN     | yyyymmdd                    | PENDING_ASSESSMENT | yyyy   |
  # | AUTO  |           | FIN  | -        | PERMANENT_RESIDENT    | -                           | INCOME_ASSESSED    | -      |
  #                                       | NON_SINGAPORE_CITIZEN |                             | RECORD_NOT_FOUND   |

  Scenario: Generate IRAS egress file
    Given the following person's details, populate into the database
      | Index | Name         | NaturalId | Type | DOD      | Nationality        | CitizenshipAttainmentDate | CitizenshipRenunciationDate |
      | 1     | NRIC Alpha   | T4240137J | NRIC | -        | SINGAPORE_CITIZEN  | 20200110                  | -                           |
      | 2     | NRIC Bravo   | T7856211G | NRIC | 20181230 | SINGAPORE_CITIZEN  | 19561213                  | -                           |
      | 3     | NRIC Charlie | T4322481B | NRIC | 20181231 | SINGAPORE_CITIZEN  | 19811031                  | -                           |
      | 4     | NRIC Delta   | S4108165A | NRIC | 20190101 | SINGAPORE_CITIZEN  | 19670610                  | -                           |
      | 5     | NRIC Echo    | S5519341Z | NRIC | -        | SINGAPORE_CITIZEN  | 20200710                  | -                           |
      | 6     | NRIC Foxtrot | S6367017J | NRIC | -        | SINGAPORE_CITIZEN  | 20200630                  | -                           |
      | 7     | NRIC Golf    | T2458766A | NRIC | -        | SINGAPORE_CITIZEN  | 20200613                  | -                           |
      | 8     | NRIC Hotel   | T2895577J | NRIC | -        | SINGAPORE_CITIZEN  | 20200714                  | -                           |
      | 9     | NRIC India   | T9504393Z | NRIC | -        | SINGAPORE_CITIZEN  | 20180612                  | -                           |
      | 10    | NRIC Juliet  | T7069505C | NRIC | 20171230 | SINGAPORE_CITIZEN  | 19610112                  | -                           |
      | 11    | NRIC Kilo    | S3954418J | NRIC | 20171231 | SINGAPORE_CITIZEN  | 19620112                  | -                           |
      | 12    | NRIC Lime    | S9010578G | NRIC | 20180101 | SINGAPORE_CITIZEN  | 19630112                  | -                           |
      | 13    | FIN Alpha    | G3477370Q | FIN  | -        | PERMANENT_RESIDENT | -                         | -                           |
      | 14    | FIN Bravo    | G5233755R | FIN  | 20181230 | PERMANENT_RESIDENT | -                         | -                           |
      | 15    | FIN Charlie  | F7130443N | FIN  | 20181231 | PERMANENT_RESIDENT | -                         | -                           |
      | 16    | FIN Delta    | G1032731N | FIN  | 20190101 | PERMANENT_RESIDENT | -                         | -                           |
      | 17    | FIN Echo     | G9539647Q | FIN  | -        | PERMANENT_RESIDENT | -                         | -                           |
      | 18    | FIN Foxtrot  | G4582848X | FIN  | -        | PERMANENT_RESIDENT | -                         | -                           |
      | 19    | FIN Golf     | F6117867T | FIN  | -        | PERMANENT_RESIDENT | -                         | -                           |
      | 20    | FIN Hotel    | G6494564K | FIN  | 20161230 | PERMANENT_RESIDENT | -                         | -                           |
      | 21    | FIN India    | G4144323K | FIN  | 20161231 | PERMANENT_RESIDENT | -                         | -                           |
      | 22    | FIN Juliet   | G6626223K | FIN  | 20170101 | PERMANENT_RESIDENT | -                         | -                           |
    Given the income details for each person referenced by index, populate into the database
      | Index | AIStatus           | AIYear |
      | 1     | INCOME_ASSESSED    | 2020   |
      | 2     | PENDING_ASSESSMENT | 2019   |
      | 2     | PENDING_ASSESSMENT | 2018   |
      | 2     | PENDING_ASSESSMENT | 2017   |
      | 3     | PENDING_ASSESSMENT | 2019   |
      | 3     | PENDING_ASSESSMENT | 2018   |
      | 3     | PENDING_ASSESSMENT | 2017   |
      | 4     | PENDING_ASSESSMENT | 2020   |
      | 4     | PENDING_ASSESSMENT | 2019   |
      | 4     | INCOME_ASSESSED    | 2018   |
      | 4     | PENDING_ASSESSMENT | 2017   |
      | 6     | INCOME_ASSESSED    | 2020   |
      | 7     | INCOME_ASSESSED    | 2020   |
      | 9     | PENDING_ASSESSMENT | 2020   |
      | 9     | PENDING_ASSESSMENT | 2019   |
      | 9     | PENDING_ASSESSMENT | 2018   |
      | 9     | PENDING_ASSESSMENT | 2017   |
      | 10    | PENDING_ASSESSMENT | 2019   |
      | 10    | PENDING_ASSESSMENT | 2018   |
      | 10    | PENDING_ASSESSMENT | 2017   |
      | 11    | PENDING_ASSESSMENT | 2019   |
      | 11    | PENDING_ASSESSMENT | 2018   |
      | 11    | PENDING_ASSESSMENT | 2017   |
      | 12    | PENDING_ASSESSMENT | 2019   |
      | 12    | PENDING_ASSESSMENT | 2018   |
      | 12    | PENDING_ASSESSMENT | 2017   |
      | 13    | PENDING_ASSESSMENT | 2020   |
      | 13    | RECORD_NOT_FOUND   | 2019   |
      | 13    | RECORD_NOT_FOUND   | 2018   |
      | 13    | RECORD_NOT_FOUND   | 2017   |
      | 14    | PENDING_ASSESSMENT | 2020   |
      | 14    | PENDING_ASSESSMENT | 2019   |
      | 14    | PENDING_ASSESSMENT | 2018   |
      | 14    | PENDING_ASSESSMENT | 2017   |
      | 15    | PENDING_ASSESSMENT | 2020   |
      | 15    | PENDING_ASSESSMENT | 2019   |
      | 15    | PENDING_ASSESSMENT | 2018   |
      | 15    | PENDING_ASSESSMENT | 2017   |
      | 16    | PENDING_ASSESSMENT | 2020   |
      | 16    | INCOME_ASSESSED    | 2019   |
      | 16    | PENDING_ASSESSMENT | 2018   |
      | 16    | PENDING_ASSESSMENT | 2017   |
      | 17    | INCOME_ASSESSED    | 2020   |
      | 17    | PENDING_ASSESSMENT | 2019   |
      | 17    | INCOME_ASSESSED    | 2018   |
      | 17    | RECORD_NOT_FOUND   | 2017   |
      | 18    | INCOME_ASSESSED    | 2020   |
      | 18    | INCOME_ASSESSED    | 2019   |
      | 18    | INCOME_ASSESSED    | 2018   |
      | 18    | INCOME_ASSESSED    | 2017   |
      | 19    | PENDING_ASSESSMENT | 2020   |
      | 19    | INCOME_ASSESSED    | 2019   |
      | 19    | INCOME_ASSESSED    | 2018   |
      | 19    | INCOME_ASSESSED    | 2017   |
      | 20    | PENDING_ASSESSMENT | 2020   |
      | 20    | PENDING_ASSESSMENT | 2019   |
      | 20    | PENDING_ASSESSMENT | 2018   |
      | 20    | PENDING_ASSESSMENT | 2017   |
      | 21    | RECORD_NOT_FOUND   | 2020   |
      | 21    | RECORD_NOT_FOUND   | 2019   |
      | 21    | PENDING_ASSESSMENT | 2018   |
      | 21    | PENDING_ASSESSMENT | 2017   |
      | 22    | PENDING_ASSESSMENT | 2020   |
      | 22    | PENDING_ASSESSMENT | 2019   |
      | 22    | PENDING_ASSESSMENT | 2018   |
      | 22    | PENDING_ASSESSMENT | 2017   |
    When Datasource is triggered to generate the subsequent IRAS AI Tri Monthly file on 20200701
    Then the IRAS AI tri monthly file should be created
    And I verify the content of the tri monthly file should be as follows:
      | NaturalId | Year |
      | T7856211G | 2019 |
      | T7856211G | 2018 |
      | T4322481B | 2019 |
      | T4322481B | 2018 |
      | S4108165A | 2020 |
      | S4108165A | 2019 |
      | T9504393Z | 2020 |
      | T9504393Z | 2019 |
      | T9504393Z | 2018 |
      | T7069505C | 2018 |
      | S3954418J | 2018 |
      | S9010578G | 2019 |
      | S9010578G | 2018 |
      | G3477370Q | 2020 |
      | G5233755R | 2019 |
      | G5233755R | 2018 |
      | F7130443N | 2019 |
      | F7130443N | 2018 |
      | G1032731N | 2020 |
      | G1032731N | 2018 |
      | G9539647Q | 2019 |
      | F6117867T | 2020 |
      | G6626223K | 2018 |
    And I remove all test data from database
