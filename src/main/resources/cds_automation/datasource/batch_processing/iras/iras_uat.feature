@datasource
@ResetFailFast
@FailFast
Feature: IRAS UAT Test Scenarios
"""
   Note: This feature file isn't ready to be executed in gitlab pipeline due to :
         1) Hasn't implement the file retrieval portion. (i.e. exposing file path in datasource containers in order for
            automation to pick up.
         2) Date time issue, as the real world time moves on, test cases will break due to constraints. For example:
            a) File received date must be after date of run
            b) File received date cannot be one month older than current date.
  """

  @truncate
  Scenario: Bulk citizen file ingress on 20200522
  """
    Seed database
    """
    Given MHA send MHA_BULK_CITIZEN file on 20200522 with the following data:
      | NRIC      | FIN | NAME   | DOB      | DOD | GENDER | ADDR_IND | ADDR_TYPE | ADDR   | INVALID_ADDR_TAG | CTZ_ATT_DATE |
      | T4945521B | -   | <AUTO> | 19881003 | -   | M      | C        | C         | <AUTO> | -                | 19881003     |
      | T7431328G | -   | <AUTO> | 19860820 | -   | M      | C        | C         | <AUTO> | -                | 19860820     |
      | T0491551D | -   | <AUTO> | 19760107 | -   | M      | C        | C         | <AUTO> | -                | 19760107     |
      | T6383387D | -   | <AUTO> | 19570109 | -   | M      | C        | C         | <AUTO> | -                | 19570109     |
      | T2307477F | -   | <AUTO> | 19471119 | -   | M      | C        | C         | <AUTO> | -                | 19471119     |
      | S9431366Z | -   | <AUTO> | 19421103 | -   | M      | C        | C         | <AUTO> | -                | 19421103     |
      | S4035558H | -   | <AUTO> | 19470726 | -   | M      | C        | C         | <AUTO> | -                | 19470726     |
      | S5568671H | -   | <AUTO> | 19741229 | -   | M      | C        | C         | <AUTO> | -                | 19741229     |
      | S8383595H | -   | <AUTO> | 19751222 | -   | M      | C        | C         | <AUTO> | -                | 19751222     |
      | T0035110A | -   | <AUTO> | 19860927 | -   | M      | C        | C         | <AUTO> | -                | 19860927     |
      | T4666665D | -   | <AUTO> | 19540214 | -   | M      | C        | C         | <AUTO> | -                | 20200520     |
      | T9287323J | -   | <AUTO> | 19401001 | -   | M      | C        | C         | <AUTO> | -                | 20200420     |
      | S3592339Z | -   | <AUTO> | 19990831 | -   | M      | C        | C         | <AUTO> | -                | 20190320     |
      | S1115805F | -   | <AUTO> | 19881003 | -   | F      | C        | C         | <AUTO> | -                | 19881003     |
      | S8475446C | -   | <AUTO> | 19860820 | -   | F      | C        | C         | <AUTO> | -                | 19860820     |
      | S0601258B | -   | <AUTO> | 19760107 | -   | F      | C        | C         | <AUTO> | -                | 19760107     |
      | S4983542F | -   | <AUTO> | 19570109 | -   | F      | C        | C         | <AUTO> | -                | 19570109     |
      | S4435748H | -   | <AUTO> | 19471119 | -   | F      | C        | C         | <AUTO> | -                | 19471119     |
      | S0036843A | -   | <AUTO> | 19421103 | -   | F      | C        | C         | <AUTO> | -                | 19421103     |
      | T0638340D | -   | <AUTO> | 19470726 | -   | F      | C        | C         | <AUTO> | -                | 19470726     |
      | S8125763I | -   | <AUTO> | 19741229 | -   | F      | C        | C         | <AUTO> | -                | 19741229     |
      | T4461874A | -   | <AUTO> | 19751222 | -   | F      | C        | C         | <AUTO> | -                | 19751222     |
      | T2559298G | -   | <AUTO> | 19860927 | -   | F      | C        | C         | <AUTO> | -                | 19860927     |
      | T1748414H | -   | <AUTO> | 19881003 | -   | F      | C        | C         | <AUTO> | -                | 19881003     |
      | S7758879E | -   | <AUTO> | 19860820 | -   | F      | C        | C         | <AUTO> | -                | 19860820     |
      | S8125038C | -   | <AUTO> | 19760107 | -   | F      | C        | C         | <AUTO> | -                | 19760107     |
      | T5430400A | -   | <AUTO> | 19570109 | -   | F      | C        | C         | <AUTO> | -                | 19570109     |
      | S0805207G | -   | <AUTO> | 19471119 | -   | F      | C        | C         | <AUTO> | -                | 19471119     |
      | S5478995E | -   | <AUTO> | 19421103 | -   | F      | C        | C         | <AUTO> | -                | 19421103     |
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    Then the Mha Bulk Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that the following error message appeared:
      | Message                                             | Count |
      | Extraction date cannot be after File Received date. | 1     |
      | Cut-off date cannot be after File Received date.    | 1     |
    Then I update the MHA_BULK_CITIZEN file received date to 20200523
    And I re-run the batch job
    Then the Mha Bulk Citizen batch job completes running with status CLEANUP

  Scenario: Special NRIC holder insertion
  """
    Invalid natural id will not exist in database due to constraint checks. Specially for this test scenario, I will insert invalid natural id into database.
    """
    Given the following person's details, insert into database manually, valid from 20200522 till infinity
      | NRIC      | FIN | NAME   | DOB      | DOD      | GENDER | ADDR_IND | ADDR_TYPE | ADDR   | INVALID_ADDR_TAG | CTZ_ATT_DATE |
      | S0000001K | -   | <AUTO> | 19530325 | 00000000 | M      | C        | C         | <AUTO> | -                | 19530325     |
    And I verify NRIC S0000001K persisted correctly

  Scenario: FIN holder insertion
  """
    FIN holders will appear in database through martial status file, and only FIN is known.
    """
    Given the following fin person's details, insert into database manually, valid from 20180212 till infinity
      | NRIC | FIN       | NAME | DOB | DOD      | GENDER | ADDR_IND | ADDR_TYPE | ADDR | INVALID_ADDR_TAG | CTZ_ATT_DATE |
      | -    | G8688152M | -    | -   | 00000000 | F      | -        | -         | -    | -                | -            |
      | -    | G5262765L | -    | -   | 00000000 | F      | -        | -         | -    | -                | -            |
      | -    | F0000003C | -    | -   | 00000000 | F      | -        | -         | -    | -                | -            |
      | -    | G4563228N | -    | -   | 00000000 | F      | -        | -         | -    | -                | -            |
      | -    | F6307232W | -    | -   | 00000000 | F      | -        | -         | -    | -                | -            |
    And I verify FIN G8688152M persisted correctly
    And I verify FIN G5262765L persisted correctly
    And I verify FIN F0000003C persisted correctly
    And I verify FIN G4563228N persisted correctly
    And I verify FIN F6307232W persisted correctly

  Scenario: Death broadcast file ingress on 20200523
    Given MHA send MHA_DEATH_DATE file on 20200523 with the following data:
      | NATURALID | DOD      |
      | S1115805F | 20151231 |
      | S8475446C | 20160101 |
      | S0601258B | 20161231 |
      | S4983542F | 20170101 |
      | S4435748H | 20171231 |
      | S0036843A | 20180101 |
      | T0638340D | 20181231 |
      | S8125763I | 20190101 |
      | T4461874A | 20191231 |
      | T2559298G | 20200102 |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    Then the Mha Death Date batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that the following error message appeared:
      | Message                                             | Count |
      | Extraction date cannot be after File Received date. | 1     |
    Then I update the MHA_DEATH_DATE file received date to 20200524
    And I re-run the batch job
    Then the Mha Death Date batch job completes running with status CLEANUP

  Scenario: Ceased citizen file ingress on 20200523
    Given MHA send MHA_CEASED_CITIZEN file on 20200523 with the following data:
      | NRIC      | NAME   | NATIONALITY | CESSATION_DATE |
      | T1748414H | <AUTO> | OO          | 20161231       |
      | S7758879E | <AUTO> | OO          | 20171231       |
      | S8125038C | <AUTO> | OO          | 20180101       |
      | T5430400A | <AUTO> | OO          | 20181231       |
      | S0805207G | <AUTO> | OO          | 20190101       |
      | S5478995E | <AUTO> | OO          | 20191231       |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that the following error message appeared:
      | Message                                             | Count |
      | Extraction date cannot be after File Received date. | 1     |
    Then I update the MHA_DEATH_DATE file received date to 20200524
    And I re-run the batch job
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP

  Scenario: Cycle 1A, bulk assessable income egress on 25 May 2020
    Given I triggered first bulk assessable income egress job on 20200525 of year 2019
    Then I retrieve the egress file
    And I verify it has the following data:
      | 02020052501            |
      | 1S8383595H        2019 |
      | 1T4461874A        2019 |
      | 1S5568671H        2019 |
      | 1S4035558H        2019 |
      | 1S9431366Z        2019 |
      | 1S8125763I        2019 |
      | 1T0638340D        2019 |
      | 1S0036843A        2019 |
      | 1T6383387D        2019 |
      | 1T7431328G        2019 |
      | 1T4666665D        2019 |
      | 1T0491551D        2019 |
      | 1S0000001K        2019 |
      | 1G8688152M        2019 |
      | 1G5262765L        2019 |
      | 1F0000003C        2019 |
      | 1G4563228N        2019 |
      | 1F6307232W        2019 |
      | 1T0035110A        2019 |
      | 1S3592339Z        2019 |
      | 1T9287323J        2019 |
      | 1T2307477F        2019 |
      | 1T2559298G        2019 |
      | 1T4945521B        2019 |
      | 2000000024             |
    And delete the egress file

  Scenario: Cycle 1A, bulk assessable income ingress on 25 May 2020
  """
    Initiate ingress for cycle 1a
    """
    Given IRAS provide the assessable income file on 20200525 with the following data:
      | RECORD_TYPE | NATURAL_ID | ASSESSABLE_INCOME | RESULT_INDICATOR | ASSESSMENT_YEAR |
      | 1           | T4945521B  | 0000000000        | 00               | 2019            |
      | 1           | T7431328G  | 0012000000        | 01               | 2019            |
      | 1           | T0491551D  | 0000000000        | 02               | 2019            |
      | 1           | T6383387D  | 0000000000        | 04               | 2019            |
      | 1           | T2307477F  | 0000000000        | 05               | 2019            |
      | 1           | S9431366Z  | 0000000000        | 00               | 2019            |
      | 1           | S4035558H  | 0012000001        | 01               | 2019            |
      | 1           | S5568671H  | 0000000000        | 02               | 2019            |
      | 1           | S8383595H  | 0000000000        | 04               | 2019            |
      | 1           | T0035110A  | 0000000000        | 05               | 2019            |
      | 1           | S0000001K  | 0000000000        | 03               | 2019            |
      | 1           | T4666665D  | 0000000000        | 05               | 2019            |
      | 1           | T9287323J  | 0099999999        | 01               | 2019            |
      | 1           | S3592339Z  | 9999999999        | 01               | 2019            |
      | 1           | G8688152M  | 0000000001        | 02               | 2019            |
      | 1           | G5262765L  | 0000000000        | 04               | 2019            |
      | 1           | F0000003C  | 0000000000        | 03               | 2019            |
      | 1           | F6307232W  | 0000000000        | 02               | 2019            |
      | 1           | T4461874A  | 0000000000        | 05               | 2019            |
      | 1           | S0036843A  | 0000000000        | 04               | 2019            |
      | 1           | T0638340D  | 0000000020        | 05               | 2019            |
      | 1           | G4563228N  | <BLANK>           | <BLANK>          | 2019            |
      | 1           | S8125763I  | <BLANK>           | <BLANK>          | 2019            |
      | 1           | T2559298G  | <BLANK>           | <BLANK>          | 2019            |
    When IRAS sends the IRAS_BULK_AI file to Datasource sftp for processing
    Then the Iras Assessable Income batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the IRAS_BULK_AI file received date to 20200525
    And I re-run the batch job
    Then the Iras Assessable Income batch job completes running with status CLEANUP
    And I verify the following natural id's income status and value for year 2019 as of 20200525 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T4945521B  | 0                 | RECORD_NOT_FOUND   |
      | T7431328G  | 120000            | INCOME_ASSESSED    |
      | T0491551D  | 0                 | PENDING_ASSESSMENT |
      | T6383387D  | 0                 | PENDING_ASSESSMENT |
      | T2307477F  | 0                 | PENDING_ASSESSMENT |
      | S9431366Z  | 0                 | RECORD_NOT_FOUND   |
      | S5568671H  | 0                 | PENDING_ASSESSMENT |
      | S8383595H  | 0                 | PENDING_ASSESSMENT |
      | T0035110A  | 0                 | PENDING_ASSESSMENT |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | S3592339Z  | 99999999.99       | INCOME_ASSESSED    |
      | G5262765L  | 0                 | PENDING_ASSESSMENT |
      | F6307232W  | 0                 | PENDING_ASSESSMENT |
      | S0036843A  | 0                 | PENDING_ASSESSMENT |
      | T4461874A  | 0                 | PENDING_ASSESSMENT |

  Scenario: Cycle 1B, thrice monthly assessable income egress on 31 May 2020
  """
    Initiate thrice monthly egress
    """
    Given I triggered thrice monthly assessable income egress job on 20200531
    Then I retrieve the egress file
    Then I verify it has the following data:
      | 02020053101            |
      | 1T0491551D        2019 |
      | 1T6383387D        2019 |
      | 1T2307477F        2019 |
      | 1S4035558H        2019 |
      | 1S5568671H        2019 |
      | 1S8383595H        2019 |
      | 1T0035110A        2019 |
      | 1S0000001K        2019 |
      | 1T4666665D        2019 |
      | 1T9287323J        2019 |
      | 1G8688152M        2019 |
      | 1G5262765L        2019 |
      | 1F0000003C        2019 |
      | 1G4563228N        2019 |
      | 1F6307232W        2019 |
      | 1S0036843A        2019 |
      | 1T0638340D        2019 |
      | 1S8125763I        2019 |
      | 1T4461874A        2019 |
      | 1T2559298G        2019 |
      | 2000000020             |
    And delete the egress file

  Scenario: Cycle 1B, thrice monthly assessable income ingress on 31 May 2020
  """
    Initiate ingress for cycle 1b
    """
    Given IRAS provide the thrice monthly assessable income file on 20200531 with the following data:
      | RECORD_TYPE | NATURAL_ID | ASSESSABLE_INCOME | RESULT_INDICATOR | ASSESSMENT_YEAR |
      | 1           | T0491551D  | 0000000000        | 02               | 2019            |
      | 1           | T6383387D  | 0000000000        | 02               | 2019            |
      | 1           | T2307477F  | 0000000000        | 02               | 2019            |
      | 1           | S4035558H  | 0000030000        | 01               | 2019            |
      | 1           | S5568671H  | 0000130001        | 01               | 2019            |
      | 1           | S8383595H  | 0000130050        | 01               | 2019            |
      | 1           | T0035110A  | 0000000000        | 00               | 2019            |
      | 1           | T4666665D  | 0000000000        | 02               | 2019            |
      | 1           | T9287323J  | 0000000000        | 02               | 2019            |
      | 1           | G8688152M  | 0000000000        | 02               | 2019            |
      | 1           | G5262765L  | 0000000000        | 02               | 2019            |
      | 1           | G4563228N  | 9999999999        | 01               | 2019            |
      | 1           | F6307232W  | 0000000000        | 04               | 2019            |
      | 1           | S0036843A  | 0120001111        | 01               | 2019            |
      | 1           | T0638340D  | 0000000000        | 00               | 2019            |
      | 1           | S8125763I  | 0000000000        | 00               | 2019            |
      | 1           | T4461874A  | 0000000000        | 04               | 2019            |
      | 1           | T2559298G  | 0000000000        | 04               | 2019            |
    When IRAS sends the IRAS_THRICE_MONTHLY_AI file to Datasource sftp for processing
    Then the Iras Assessable Income batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the IRAS_THRICE_MONTHLY_AI file received date to 20200531
    And I re-run the batch job
    Then the Iras Assessable Income batch job completes running with status CLEANUP
    And I verify the following natural id's income status and value for year 2019 as of 20200531 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T0491551D  | 0                 | PENDING_ASSESSMENT |
      | T6383387D  | 0                 | PENDING_ASSESSMENT |
      | T2307477F  | 0                 | PENDING_ASSESSMENT |
      | S4035558H  | 300               | INCOME_ASSESSED    |
      | S5568671H  | 1300.01           | INCOME_ASSESSED    |
      | S8383595H  | 1300.5            | INCOME_ASSESSED    |
      | T0035110A  | 0                 | RECORD_NOT_FOUND   |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | T9287323J  | 0                 | PENDING_ASSESSMENT |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | G5262765L  | 0                 | PENDING_ASSESSMENT |
      | G4563228N  | 99999999.99       | INCOME_ASSESSED    |
      | F6307232W  | 0                 | PENDING_ASSESSMENT |
      | S0036843A  | 0                 | PENDING_ASSESSMENT |
      | T0638340D  | 0                 | RECORD_NOT_FOUND   |
      | S8125763I  | 0                 | RECORD_NOT_FOUND   |
      | T4461874A  | 0                 | PENDING_ASSESSMENT |
      | T2559298G  | 0                 | PENDING_ASSESSMENT |

  Scenario: Cycle 1C, bulk assessable income egress on 01 June 2020
  """
    Initiate bulk egress to IRAS on 20200601 (a.k.a Cycle 1c)
    """
    Given I triggered bulk assessable income egress job on 20200601
    Then I retrieve the egress file
    And I verify it has the following data:
      | 02020060101            |
      | 1T4945521B        2020 |
      | 1T7431328G        2020 |
      | 1T0491551D        2020 |
      | 1T6383387D        2020 |
      | 1T2307477F        2020 |
      | 1S9431366Z        2020 |
      | 1S4035558H        2020 |
      | 1S5568671H        2020 |
      | 1S8383595H        2020 |
      | 1T0035110A        2020 |
      | 1S0000001K        2020 |
      | 1T4666665D        2020 |
      | 1T9287323J        2020 |
      | 1S3592339Z        2020 |
      | 1S8125763I        2020 |
      | 1T4461874A        2020 |
      | 1T2559298G        2020 |
      | 1G8688152M        2020 |
      | 1G5262765L        2020 |
      | 1F0000003C        2020 |
      | 1G4563228N        2020 |
      | 1F6307232W        2020 |
      | 2000000022             |
    And delete the egress file

  Scenario: Cycle 1C, bulk assessable income ingress on 01 June 2020
    Given IRAS provide the assessable income file on 20200601 with the following data:
      | RECORD_TYPE | NATURAL_ID | ASSESSABLE_INCOME | RESULT_INDICATOR | ASSESSMENT_YEAR |
      | 1           | T4945521B  | 0003000049        | 01               | 2020            |
      | 1           | T7431328G  | 0000000000        | 02               | 2020            |
      | 1           | T0491551D  | 0000000010        | 02               | 2020            |
      | 1           | T6383387D  | 0000000000        | 00               | 2020            |
      | 1           | T2307477F  | 0000000000        | 04               | 2020            |
      | 1           | S9431366Z  | 0000000000        | 04               | 2020            |
      | 1           | S4035558H  | 0000000000        | 05               | 2020            |
      | 1           | S5568671H  | 0000000000        | 00               | 2020            |
      | 1           | S8383595H  | 0003000050        | 01               | 2020            |
      | 1           | T0035110A  | 0000000000        | 04               | 2020            |
      | 1           | T4666665D  | 0000000000        | 05               | 2020            |
      | 1           | T9287323J  | 0000000000        | 00               | 2020            |
      | 1           | S3592339Z  | 0003000051        | 01               | 2020            |
      | 1           | G8688152M  | 0000000000        | 05               | 2020            |
      | 1           | G5262765L  | 0000000000        | 05               | 2020            |
      | 1           | G4563228N  | 0000000000        | 00               | 2020            |
      | 1           | F6307232W  | 0000000000        | 05               | 2020            |
      | 1           | S8125763I  | 0000000000        | 04               | 2020            |
      | 1           | T4461874A  | 0000000000        | 00               | 2020            |
      | 1           | T2559298G  | 0000000000        | 01               | 2020            |
    When IRAS sends the IRAS_BULK_AI file to Datasource sftp for processing
    Then the Iras Assessable Income batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the IRAS_BULK_AI file received date to 20200601
    And I re-run the batch job
    Then the Iras Assessable Income batch job completes running with status CLEANUP
    And I verify the following natural id's income status and value for year 2020 as of 20200601 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T4945521B  | 30000.49          | INCOME_ASSESSED    |
      | T7431328G  | 0                 | PENDING_ASSESSMENT |
      | T6383387D  | 0                 | RECORD_NOT_FOUND   |
      | T2307477F  | 0                 | PENDING_ASSESSMENT |
      | S9431366Z  | 0                 | PENDING_ASSESSMENT |
      | S4035558H  | 0                 | PENDING_ASSESSMENT |
      | S5568671H  | 0                 | RECORD_NOT_FOUND   |
      | S8383595H  | 30000.5           | INCOME_ASSESSED    |
      | T0035110A  | 0                 | PENDING_ASSESSMENT |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | T9287323J  | 0                 | RECORD_NOT_FOUND   |
      | S3592339Z  | 30000.51          | INCOME_ASSESSED    |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | G5262765L  | 0                 | PENDING_ASSESSMENT |
      | G4563228N  | 0                 | RECORD_NOT_FOUND   |
      | F6307232W  | 0                 | PENDING_ASSESSMENT |
      | S8125763I  | 0                 | PENDING_ASSESSMENT |
      | T4461874A  | 0                 | RECORD_NOT_FOUND   |

  Scenario: Cycle 1C, thrice monthly assessable income egress on 02 June 2020
    Given I triggered first thrice monthly assessable income egress job for the month of June on 20200602
    Then I retrieve the egress file
    And I verify it has the following data:
      | 02020060201            |
      | 1T0491551D        2019 |
      | 1T6383387D        2019 |
      | 1T2307477F        2019 |
      | 1T4666665D        2019 |
      | 1T9287323J        2019 |
      | 1S0036843A        2019 |
      | 1T4461874A        2019 |
      | 1T2559298G        2019 |
      | 1G8688152M        2019 |
      | 1G5262765L        2019 |
      | 1F6307232W        2019 |
      | 1S0000001K        2019 |
      | 1F0000003C        2019 |
      | 2000000013             |
    And delete the egress file

  Scenario: Cycle 1C, thrice monthly assessable income ingress on 02 June 2020
    Given IRAS provide the thrice monthly assessable income file on 20200602 with the following data:
      | RECORD_TYPE | NATURAL_ID | ASSESSABLE_INCOME | RESULT_INDICATOR | ASSESSMENT_YEAR |
      | 1           | T0491551D  | 0003000049        | 01               | 2019            |
      | 1           | T6383387D  | 0003000050        | 01               | 2019            |
      | 1           | T2307477F  | 0012000000        | 01               | 2019            |
      | 1           | T4666665D  | 0000000000        | 02               | 2019            |
      | 1           | T9287323J  | 0000000000        | 04               | 2019            |
      | 1           | G8688152M  | 0000000000        | 04               | 2019            |
      | 1           | G5262765L  | 0000000000        | 04               | 2019            |
      | 1           | F6307232W  | 0000000000        | 05               | 2019            |
      | 1           | S0036843A  | 0000000000        | 05               | 2019            |
      | 1           | T4461874A  | 0000000000        | 04               | 2019            |
      | 1           | T2559298G  | 0000000000        | 04               | 2019            |
    When IRAS sends the IRAS_THRICE_MONTHLY_AI file to Datasource sftp for processing
    Then the Iras Assessable Income batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the IRAS_THRICE_MONTHLY_AI file received date to 20200602
    And I re-run the batch job
    Then the Iras Assessable Income batch job completes running with status CLEANUP
    And I verify the following natural id's income status and value for year 2019 as of 20200602 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T0491551D  | 30000.49          | INCOME_ASSESSED    |
      | T6383387D  | 30000.5           | INCOME_ASSESSED    |
      | T2307477F  | 120000            | INCOME_ASSESSED    |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | T9287323J  | 0                 | PENDING_ASSESSMENT |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | G5262765L  | 0                 | PENDING_ASSESSMENT |
      | F6307232W  | 0                 | PENDING_ASSESSMENT |
      | S0036843A  | 0                 | PENDING_ASSESSMENT |
      | T4461874A  | 0                 | PENDING_ASSESSMENT |
      | T2559298G  | 0                 | PENDING_ASSESSMENT |

  Scenario: Cycle 1D, new citizen file ingress on 20200612
    Given MHA send MHA_NEW_CITIZEN file on 20200610 with the following data:
      | NRIC      | FIN       | NAME    | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR          | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR          | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | T5080649E | G4562923L | Jessica | 19880505 | F      | C            | C             | Bedok North Ave 2 | C            | C             | Bedok North Ave 3 |                      | 20180704            | 20200420     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    Then the Mha New Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the MHA_NEW_CITIZEN file received date to 20200612
    And I re-run the batch job
    Then the Mha New Citizen batch job completes running with status CLEANUP

  Scenario: Cycle 1D, thrice monthly assessable income egress on 14 June 2020
    Given I triggered thrice monthly assessable income egress job on 20200614
    Then I retrieve the egress file
    Then I verify it has the following data:
      | 02020061401            |
      | 1T7431328G        2020 |
      | 1T2307477F        2020 |
      | 1S9431366Z        2020 |
      | 1S4035558H        2020 |
      | 1T0035110A        2020 |
      | 1T4666665D        2020 |
      | 1S8125763I        2020 |
      | 1G8688152M        2020 |
      | 1G5262765L        2020 |
      | 1F6307232W        2020 |
      | 1T4666665D        2019 |
      | 1T9287323J        2019 |
      | 1S0036843A        2019 |
      | 1T4461874A        2019 |
      | 1T2559298G        2019 |
      | 1G8688152M        2019 |
      | 1G5262765L        2019 |
      | 1F6307232W        2019 |
      | 1T0491551D        2020 |
      | 1S0000001K        2020 |
      | 1F0000003C        2020 |
      | 1T5080649E        2020 |
      | 1S0000001K        2019 |
      | 1F0000003C        2019 |
      | 1T5080649E        2019 |
      | 2000000025             |
    And delete the egress file

  Scenario: Cycle 1D, thrice monthly assessable income ingress on 14 June 2020
    Given IRAS provide the thrice monthly assessable income file on 20200614 with the following data:
      | RECORD_TYPE | NATURAL_ID | ASSESSABLE_INCOME | RESULT_INDICATOR | ASSESSMENT_YEAR |
      | 1           | T7431328G  | 0000000000        | 05               | 2020            |
      | 1           | T0491551D  | 0000000000        | 01               | 2020            |
      | 1           | T2307477F  | 0000000000        | 05               | 2020            |
      | 1           | S9431366Z  | 0000000000        | 05               | 2020            |
      | 1           | S4035558H  | 0000000000        | 04               | 2020            |
      | 1           | T0035110A  | 0000000000        | 04               | 2020            |
      | 1           | T4666665D  | 0000000000        | 02               | 2020            |
      | 1           | T4666665D  | 0000000000        | 04               | 2019            |
      | 1           | T9287323J  | 0000000000        | 04               | 2019            |
      | 1           | G8688152M  | 0000000000        | 02               | 2020            |
      | 1           | G8688152M  | 0000000000        | 04               | 2019            |
      | 1           | G5262765L  | 0000000000        | 02               | 2020            |
      | 1           | G5262765L  | 0000000000        | 02               | 2019            |
      | 1           | F6307232W  | 0012000000        | 01               | 2020            |
      | 1           | F6307232W  | 0000000000        | 02               | 2019            |
      | 1           | S0036843A  | 0000000000        | 02               | 2019            |
      | 1           | S8125763I  | 0012000000        | 01               | 2020            |
      | 1           | T4461874A  | 0012000000        | 01               | 2019            |
      | 1           | T2559298G  | 0012000000        | 01               | 2019            |
      | 1           | T5080649E  | 9999999999        | 01               | 2020            |
      | 1           | T5080649E  | 9999999999        | 01               | 2019            |
    When IRAS sends the IRAS_THRICE_MONTHLY_AI file to Datasource sftp for processing
    Then the Iras Assessable Income batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the IRAS_THRICE_MONTHLY_AI file received date to 20200614
    And I re-run the batch job
    Then the Iras Assessable Income batch job completes running with status CLEANUP
    And I verify the following natural id's income status and value for year 2020 as of 20200614 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T7431328G  | 0                 | PENDING_ASSESSMENT |
      | T0491551D  | 0                 | INCOME_ASSESSED    |
      | T2307477F  | 0                 | PENDING_ASSESSMENT |
      | S9431366Z  | 0                 | PENDING_ASSESSMENT |
      | S4035558H  | 0                 | PENDING_ASSESSMENT |
      | T0035110A  | 0                 | PENDING_ASSESSMENT |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | G5262765L  | 0                 | PENDING_ASSESSMENT |
      | F6307232W  | 120000            | INCOME_ASSESSED    |
      | S8125763I  | 120000            | INCOME_ASSESSED    |
      | T5080649E  | 99999999.99       | INCOME_ASSESSED    |
    And I verify the following natural id's income status and value for year 2019 as of 20200614 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | T9287323J  | 0                 | PENDING_ASSESSMENT |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | G5262765L  | 0                 | PENDING_ASSESSMENT |
      | F6307232W  | 0                 | PENDING_ASSESSMENT |
      | S0036843A  | 0                 | PENDING_ASSESSMENT |
      | T4461874A  | 120000            | INCOME_ASSESSED    |
      | T2559298G  | 120000            | INCOME_ASSESSED    |
      | T5080649E  | 99999999.99       | INCOME_ASSESSED    |

  Scenario: Cycle 1E, new citizen file ingress on 20210522
    Given MHA send MHA_NEW_CITIZEN file on 20210522 with the following data:
      | NRIC      | FIN       | NAME      | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR          | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR          | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | T7700475G | G4345241Q | Jessica   | 19881003 | F      | C            | C             | Bedok North Ave 1 | C            | C             | Bedok North Ave 5 |                      | 20180704            | 20201020     |
      | T6386446Z | F5520667T | John Tang | 19860820 | M      | C            | C             | Bedok North Ave 2 | C            | C             | Bedok North Ave 6 |                      | 20180704            | 20210420     |
      | T2430411B |           | Monica    | 20080420 | F      | C            | C             | Bedok North Ave 3 | C            | C             | Bedok North Ave 7 |                      | 20180704            | 20080420     |
      | S0600147E |           | Caesar    | 20080109 | M      | C            | C             | Bedok North Ave 4 | C            | C             | Bedok North Ave 8 |                      | 20180704            | 20080109     |
      | T8255237A |           | Dick      | 19460505 | M      | C            | C             | Bedok North Ave 4 | C            | C             | Bedok North Ave 8 |                      | 20180704            | 20210120     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    Then the Mha New Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the MHA_NEW_CITIZEN file received date to 20210522
    And I re-run the batch job
    Then the Mha New Citizen batch job completes running with status CLEANUP

  Scenario: Cycle 1E, death broadcast file ingress on 20210522
    Given MHA send MHA_DEATH_DATE file on 20210522 with the following data:
      | NATURALID | DOD      |
      | T8255237A | 20210409 |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    Then the Mha Death Date batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that the following error message appeared:
      | Message                                             | Count |
      | Extraction date cannot be after File Received date. | 1     |
    Then I update the MHA_DEATH_DATE file received date to 20210522
    And I re-run the batch job
    Then the Mha Death Date batch job completes running with status CLEANUP

  Scenario: Cycle 1E, income appeal insertion
    Given the following details, insert income appeal cases into database:
      | NATURAL_ID | APPEAL_DATE | APPEAL_YEAR |
      | T2430411B  | 20210522    | 2018        |
      | T2430411B  | 20210522    | 2017        |
      | S0600147E  | 20210522    | 2018        |
      | S0600147E  | 20210522    | 2017        |

  Scenario: Cycle 1E, thrice monthly assessable income egress on 23 May 2021
    Given I triggered thrice monthly assessable income egress job on 20210523
    Then I retrieve the egress file
    Then I verify it has the following data:
      | 02021052301            |
      | 1T7431328G        2020 |
      | 1T2307477F        2020 |
      | 1S9431366Z        2020 |
      | 1S4035558H        2020 |
      | 1T0035110A        2020 |
      | 1T4666665D        2020 |
      | 1G8688152M        2020 |
      | 1G5262765L        2020 |
      | 1T4666665D        2019 |
      | 1T9287323J        2019 |
      | 1S0036843A        2019 |
      | 1G8688152M        2019 |
      | 1G5262765L        2019 |
      | 1F6307232W        2019 |
      | 1S0600147E        2018 |
      | 1T2430411B        2018 |
      | 1T7700475G        2020 |
      | 1T6386446Z        2020 |
      | 1T2430411B        2020 |
      | 1S0600147E        2020 |
      | 1T8255237A        2020 |
      | 1S0000001K        2020 |
      | 1F0000003C        2020 |
      | 1T7700475G        2019 |
      | 1T6386446Z        2019 |
      | 1T2430411B        2019 |
      | 1S0600147E        2019 |
      | 1T8255237A        2019 |
      | 1S0000001K        2019 |
      | 1F0000003C        2019 |
      | 2000000030             |
    And delete the egress file

  Scenario: Cycle 1E, thrice monthly assessable income ingress on 23 May 2021
    Given IRAS provide the thrice monthly assessable income file on 20210523 with the following data:
      | RECORD_TYPE | NATURAL_ID | ASSESSABLE_INCOME | RESULT_INDICATOR | ASSESSMENT_YEAR |
      | 1           | T7431328G  | 0000000000        | 05               | 2020            |
      | 1           | T2307477F  | 0000000000        | 05               | 2020            |
      | 1           | S9431366Z  | 0000000000        | 05               | 2020            |
      | 1           | S4035558H  | 0000000000        | 04               | 2020            |
      | 1           | T0035110A  | 0000000000        | 04               | 2020            |
      | 1           | T4666665D  | 0000000000        | 02               | 2020            |
      | 1           | T4666665D  | 0000000000        | 04               | 2019            |
      | 1           | T9287323J  | 0000000000        | 04               | 2019            |
      | 1           | G8688152M  | 0000000000        | 02               | 2020            |
      | 1           | G8688152M  | 0000000000        | 04               | 2019            |
      | 1           | G5262765L  | 0000120000        | 01               | 2020            |
      | 1           | G5262765L  | 0000000000        | 02               | 2019            |
      | 1           | F6307232W  | 0000000000        | 02               | 2019            |
      | 1           | S0036843A  | 0000120000        | 01               | 2019            |
      | 1           | T7700475G  | 0000000000        | 02               | 2020            |
      | 1           | T7700475G  | 0000120000        | 01               | 2019            |
      | 1           | T8255237A  | 0000000000        | 02               | 2020            |
      | 1           | T8255237A  | 0001120000        | 01               | 2019            |
      | 1           | T6386446Z  | 0000000000        | 02               | 2020            |
      | 1           | T6386446Z  | 0002120000        | 01               | 2019            |
      | 1           | T2430411B  | 0000000000        | 02               | 2020            |
      | 1           | T2430411B  | 0003120000        | 01               | 2019            |
      | 1           | T2430411B  | 0011000000        | 01               | 2018            |
      | 1           | T2430411B  | 0000000000        | 06               | 2017            |
      | 1           | S0600147E  | 0000000000        | 02               | 2020            |
      | 1           | S0600147E  | 0004120000        | 01               | 2019            |
      | 1           | S0600147E  | 0010000000        | 01               | 2018            |
      | 1           | S0600147E  | 0000000011        | 06               | 2017            |
    When IRAS sends the IRAS_THRICE_MONTHLY_AI file to Datasource sftp for processing
    Then the Iras Assessable Income batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the IRAS_THRICE_MONTHLY_AI file received date to 20210523
    And I re-run the batch job
    Then the Iras Assessable Income batch job completes running with status CLEANUP
    And I verify the following natural id's income status and value for year 2020 as of 20210523 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T7431328G  | 0                 | PENDING_ASSESSMENT |
      | T2307477F  | 0                 | PENDING_ASSESSMENT |
      | S9431366Z  | 0                 | PENDING_ASSESSMENT |
      | S4035558H  | 0                 | PENDING_ASSESSMENT |
      | T0035110A  | 0                 | PENDING_ASSESSMENT |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | G5262765L  | 1200              | INCOME_ASSESSED    |
      | T7700475G  | 0                 | PENDING_ASSESSMENT |
      | T8255237A  | 0                 | PENDING_ASSESSMENT |
      | T6386446Z  | 0                 | PENDING_ASSESSMENT |
      | T2430411B  | 0                 | PENDING_ASSESSMENT |
      | S0600147E  | 0                 | PENDING_ASSESSMENT |
    And I verify the following natural id's income status and value for year 2019 as of 20210523 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | T9287323J  | 0                 | PENDING_ASSESSMENT |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | G5262765L  | 0                 | PENDING_ASSESSMENT |
      | F6307232W  | 0                 | PENDING_ASSESSMENT |
      | S0036843A  | 1200              | INCOME_ASSESSED    |
      | T7700475G  | 1200              | INCOME_ASSESSED    |
      | T8255237A  | 11200             | INCOME_ASSESSED    |
      | T6386446Z  | 21200             | INCOME_ASSESSED    |
      | T2430411B  | 31200             | INCOME_ASSESSED    |
      | S0600147E  | 41200             | INCOME_ASSESSED    |
    And I verify the following natural id's income status and value for year 2018 as of 20210523 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS          |
      | T2430411B  | 110000            | INCOME_ASSESSED |
      | S0600147E  | 100000            | INCOME_ASSESSED |

  Scenario: Cycle 2A, new citizen file ingress on 20210528
    Given MHA send MHA_NEW_CITIZEN file on 20210528 with the following data:
      | NRIC      | FIN       | NAME    | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR          | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR          | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | T5874373E | F3703726P | Jessica | 19781003 | F      | C            | C             | Bedok North Ave 1 | C            | C             | Bedok North Ave 5 |                      | 20180704            | 20210520     |
      | S3401364J |           | Jessica | 20080320 | F      | C            | C             | Bedok North Ave 1 | C            | C             | Bedok North Ave 5 |                      | 20180704            | 20080320     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    Then the Mha New Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the MHA_NEW_CITIZEN file received date to 20210528
    And I re-run the batch job
    Then the Mha New Citizen batch job completes running with status CLEANUP

  Scenario: Cycle 2A, ceased citizen file ingress on 20210528
    Given MHA send MHA_CEASED_CITIZEN file on 20210528 with the following data:
      | NRIC      | NAME   | NATIONALITY | CESSATION_DATE |
      | T0035110A | <AUTO> | OO          | 20210520       |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that the following error message appeared:
      | Message                                             | Count |
      | Extraction date cannot be after File Received date. | 1     |
    Then I update the MHA_DEATH_DATE file received date to 20210528
    And I re-run the batch job
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP

  Scenario: Cycle 2A, bulk assessable income egress on 01 June 2021
    Given I triggered bulk assessable income egress job on 20210601
    Then I retrieve the egress file
    And I verify it has the following data:
      | 02021060101            |
      | 1T7700475G        2021 |
      | 1T6386446Z        2021 |
      | 1T2430411B        2021 |
      | 1S0600147E        2021 |
      | 1T8255237A        2021 |
      | 1T5874373E        2021 |
      | 1S3401364J        2021 |
      | 1T4945521B        2021 |
      | 1T7431328G        2021 |
      | 1T0491551D        2021 |
      | 1T6383387D        2021 |
      | 1T2307477F        2021 |
      | 1S9431366Z        2021 |
      | 1S4035558H        2021 |
      | 1S5568671H        2021 |
      | 1S8383595H        2021 |
      | 1S0000001K        2021 |
      | 1T4666665D        2021 |
      | 1T9287323J        2021 |
      | 1S3592339Z        2021 |
      | 1T2559298G        2021 |
      | 1G8688152M        2021 |
      | 1G5262765L        2021 |
      | 1F0000003C        2021 |
      | 1G4563228N        2021 |
      | 1F6307232W        2021 |
      | 1T5080649E        2021 |
      | 2000000027             |
    And delete the egress file

  Scenario: Cycle 2A, bulk assessable income ingress on 01 June 2021
    Given IRAS provide the assessable income file on 20210601 with the following data:
      | RECORD_TYPE | NATURAL_ID | ASSESSABLE_INCOME | RESULT_INDICATOR | ASSESSMENT_YEAR |
      | 1           | T4945521B  | 0000000000        | 00               | 2021            |
      | 1           | T7431328G  | 0000000000        | 00               | 2021            |
      | 1           | T0491551D  | 0000000000        | 00               | 2021            |
      | 1           | T6383387D  | 0000000000        | 02               | 2021            |
      | 1           | T2307477F  | 0000000000        | 02               | 2021            |
      | 1           | S9431366Z  | 0000000000        | 02               | 2021            |
      | 1           | S4035558H  | 0000000000        | 02               | 2021            |
      | 1           | S5568671H  | 0000000000        | 04               | 2021            |
      | 1           | S8383595H  | 0000000000        | 04               | 2021            |
      | 1           | T4666665D  | 0000000000        | 04               | 2021            |
      | 1           | T9287323J  | 0000000000        | 04               | 2021            |
      | 1           | S3592339Z  | 0000000000        | 04               | 2021            |
      | 1           | G8688152M  | 0000000000        | 04               | 2021            |
      | 1           | G5262765L  | 0000000000        | 04               | 2021            |
      | 1           | G4563228N  | 0000000000        | 05               | 2021            |
      | 1           | F6307232W  | 0000000000        | 05               | 2021            |
      | 1           | T2559298G  | 0000000000        | 05               | 2021            |
      | 1           | T5080649E  | 0000000000        | 05               | 2021            |
      | 1           | T7700475G  | 0000000000        | 05               | 2021            |
      | 1           | T8255237A  | 0000000000        | 05               | 2021            |
      | 1           | T6386446Z  | 0001120000        | 01               | 2021            |
      | 1           | T2430411B  | 0002120000        | 01               | 2021            |
      | 1           | S0600147E  | 0003120000        | 01               | 2021            |
      | 1           | T5874373E  | 0004120000        | 01               | 2021            |
      | 1           | S3401364J  | 0005120000        | 01               | 2021            |
    When IRAS sends the IRAS_BULK_AI file to Datasource sftp for processing
    Then the Iras Assessable Income batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the IRAS_BULK_AI file received date to 20210601
    And I re-run the batch job
    Then the Iras Assessable Income batch job completes running with status CLEANUP
    And I verify the following natural id's income status and value for year 2021 as of 20210601 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T4945521B  | 0                 | RECORD_NOT_FOUND   |
      | T7431328G  | 0                 | RECORD_NOT_FOUND   |
      | T0491551D  | 0                 | RECORD_NOT_FOUND   |
      | T6383387D  | 0                 | PENDING_ASSESSMENT |
      | T2307477F  | 0                 | PENDING_ASSESSMENT |
      | S9431366Z  | 0                 | PENDING_ASSESSMENT |
      | S4035558H  | 0                 | PENDING_ASSESSMENT |
      | S5568671H  | 0                 | PENDING_ASSESSMENT |
      | S8383595H  | 0                 | PENDING_ASSESSMENT |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | T9287323J  | 0                 | PENDING_ASSESSMENT |
      | S3592339Z  | 0                 | PENDING_ASSESSMENT |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | G5262765L  | 0                 | PENDING_ASSESSMENT |
      | G4563228N  | 0                 | PENDING_ASSESSMENT |
      | F6307232W  | 0                 | PENDING_ASSESSMENT |
      | T2559298G  | 0                 | PENDING_ASSESSMENT |
      | T5080649E  | 0                 | PENDING_ASSESSMENT |
      | T7700475G  | 0                 | PENDING_ASSESSMENT |
      | T8255237A  | 0                 | PENDING_ASSESSMENT |
      | T6386446Z  | 11200             | INCOME_ASSESSED    |
      | T2430411B  | 21200             | INCOME_ASSESSED    |
      | S0600147E  | 31200             | INCOME_ASSESSED    |
      | T5874373E  | 41200             | INCOME_ASSESSED    |
      | S3401364J  | 51200             | INCOME_ASSESSED    |

  Scenario: Cycle 2A, income appeal insertion
    Given the following details, insert income appeal cases into database:
      | NATURAL_ID | APPEAL_DATE | APPEAL_YEAR |
      | T4945521B  | 20210528    | 2020        |
      | T4945521B  | 20210528    | 2019        |
      | T4945521B  | 20210528    | 2018        |
      | T7431328G  | 20210528    | 2020        |
      | T7431328G  | 20210528    | 2019        |
      | T0491551D  | 20210528    | 2020        |
      | T0491551D  | 20210528    | 2019        |

  Scenario: Cycle 2A, thrice monthly assessable income egress on 02 June 2021
    Given I triggered first thrice monthly assessable income egress job for the month of June on 20210602
    Then I retrieve the egress file
    Then I verify it has the following data:
      | 02021060201            |
      | 1T7700475G        2020 |
      | 1T6386446Z        2020 |
      | 1T2430411B        2020 |
      | 1S0600147E        2020 |
      | 1T8255237A        2020 |
      | 1T4945521B        2020 |
      | 1T7431328G        2020 |
      | 1T0491551D        2020 |
      | 1T2307477F        2020 |
      | 1S9431366Z        2020 |
      | 1S4035558H        2020 |
      | 1T4666665D        2020 |
      | 1G8688152M        2020 |
      | 1T4945521B        2019 |
      | 1T7431328G        2019 |
      | 1T0491551D        2019 |
      | 1T4666665D        2019 |
      | 1T9287323J        2019 |
      | 1G8688152M        2019 |
      | 1G5262765L        2019 |
      | 1F6307232W        2019 |
      | 1T5874373E        2020 |
      | 1S3401364J        2020 |
      | 1S0000001K        2020 |
      | 1F0000003C        2020 |
      | 1T5874373E        2019 |
      | 1S3401364J        2019 |
      | 1S0000001K        2019 |
      | 1F0000003C        2019 |
      | 2000000029             |
    And delete the egress file

  Scenario: Cycle 2A, thrice monthly assessable income ingress on 02 June 2021
    Given IRAS provide the thrice monthly assessable income file on 20210602 with the following data:
      | RECORD_TYPE | NATURAL_ID | ASSESSABLE_INCOME | RESULT_INDICATOR | ASSESSMENT_YEAR |
      | 1           | T4945521B  | 0000120000        | 01               | 2020            |
      | 1           | T4945521B  | 0000120000        | 01               | 2019            |
      | 1           | T7431328G  | 0001120000        | 01               | 2020            |
      | 1           | T7431328G  | 0001120000        | 01               | 2019            |
      | 1           | T0491551D  | 0002120000        | 01               | 2020            |
      | 1           | T0491551D  | 0002120000        | 01               | 2019            |
      | 1           | T2307477F  | 0000000000        | 05               | 2020            |
      | 1           | S9431366Z  | 0000000000        | 05               | 2020            |
      | 1           | S4035558H  | 0000000000        | 04               | 2020            |
      | 1           | T4666665D  | 0000000000        | 02               | 2020            |
      | 1           | T4666665D  | 0000000000        | 04               | 2019            |
      | 1           | T9287323J  | 0000000000        | 04               | 2019            |
      | 1           | G8688152M  | 0000000000        | 02               | 2020            |
      | 1           | G8688152M  | 0000000000        | 04               | 2019            |
      | 1           | G5262765L  | 0000000000        | 02               | 2019            |
      | 1           | F6307232W  | 0000000000        | 02               | 2019            |
      | 1           | T7700475G  | 0000000000        | 02               | 2020            |
      | 1           | T8255237A  | 0000000000        | 02               | 2020            |
      | 1           | T6386446Z  | 0000000000        | 02               | 2020            |
      | 1           | T2430411B  | 0000000000        | 02               | 2020            |
      | 1           | S0600147E  | 0000000000        | 02               | 2020            |
      | 1           | T5874373E  | 0000000000        | 02               | 2020            |
      | 1           | T5874373E  | 0000000000        | 02               | 2019            |
      | 1           | S3401364J  | 0000000000        | 02               | 2020            |
      | 1           | S3401364J  | 0000000000        | 02               | 2019            |
    When IRAS sends the IRAS_THRICE_MONTHLY_AI file to Datasource sftp for processing
    Then the Iras Assessable Income batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the IRAS_THRICE_MONTHLY_AI file received date to 20210602
    And I re-run the batch job
    Then the Iras Assessable Income batch job completes running with status CLEANUP
    And I verify the following natural id's income status and value for year 2020 as of 20210602 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T4945521B  | 1200              | INCOME_ASSESSED    |
      | T7431328G  | 11200             | INCOME_ASSESSED    |
      | T0491551D  | 21200             | INCOME_ASSESSED    |
      | T2307477F  | 0                 | PENDING_ASSESSMENT |
      | S9431366Z  | 0                 | PENDING_ASSESSMENT |
      | S4035558H  | 0                 | PENDING_ASSESSMENT |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | T7700475G  | 0                 | PENDING_ASSESSMENT |
      | T8255237A  | 0                 | PENDING_ASSESSMENT |
      | T6386446Z  | 0                 | PENDING_ASSESSMENT |
      | T2430411B  | 0                 | PENDING_ASSESSMENT |
      | S0600147E  | 0                 | PENDING_ASSESSMENT |
      | T5874373E  | 0                 | PENDING_ASSESSMENT |
      | S3401364J  | 0                 | PENDING_ASSESSMENT |
    And I verify the following natural id's income status and value for year 2019 as of 20210602 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T4945521B  | 1200              | INCOME_ASSESSED    |
      | T7431328G  | 11200             | INCOME_ASSESSED    |
      | T0491551D  | 21200             | INCOME_ASSESSED    |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | T9287323J  | 0                 | PENDING_ASSESSMENT |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | G5262765L  | 0                 | PENDING_ASSESSMENT |
      | F6307232W  | 0                 | PENDING_ASSESSMENT |
      | T5874373E  | 0                 | PENDING_ASSESSMENT |
      | S3401364J  | 0                 | PENDING_ASSESSMENT |

  Scenario: Cycle 2B, new citizen file ingress on 10 June 2021
    Given MHA send MHA_NEW_CITIZEN file on 20210610 with the following data:
      | NRIC      | FIN       | NAME | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR           | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR           | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S0900370C | F0075695N | Tom  | 19880105 | M      | C            | C             | Bedok North Ave 11 | C            | C             | Bedok North Ave 51 |                      | 20180704            | 20210602     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    Then the Mha New Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the MHA_NEW_CITIZEN file received date to 20210610
    And I re-run the batch job
    Then the Mha New Citizen batch job completes running with status CLEANUP

  Scenario: Cycle 2B, ceased citizen file ingress on 10 June 2021
    Given MHA send MHA_CEASED_CITIZEN file on 20210610 with the following data:
      | NRIC      | NAME   | NATIONALITY | CESSATION_DATE |
      | T2307477F | <AUTO> | OO          | 20191231       |
      | S5568671H | <AUTO> | OO          | 20191231       |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that the following error message appeared:
      | Message                                             | Count |
      | Extraction date cannot be after File Received date. | 1     |
    Then I update the MHA_DEATH_DATE file received date to 20210610
    And I re-run the batch job
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP

  Scenario: Cycle 2B, death broadcase file ingress on 10 June 2021
    Given MHA send MHA_DEATH_DATE file on 20210610 with the following data:
      | NATURALID | DOD      |
      | T6383387D | 20191231 |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    Then the Mha Death Date batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that the following error message appeared:
      | Message                                             | Count |
      | Extraction date cannot be after File Received date. | 1     |
    Then I update the MHA_DEATH_DATE file received date to 20210610
    And I re-run the batch job
    Then the Mha Death Date batch job completes running with status CLEANUP

  Scenario: Cycle 2B, income appeal insertion
    Given the following details, insert income appeal cases into database:
      | NATURAL_ID | APPEAL_DATE | APPEAL_YEAR |
      | T5080649E  | 20210610    | 2021        |
      | T5080649E  | 20210610    | 2020        |
      | T5080649E  | 20210610    | 2019        |
      | T6386446Z  | 20210610    | 2021        |

  Scenario: Cycle 2B, thrice monthly assessable income egress on 15 June 2021
    Given I triggered thrice monthly assessable income egress job on 20210615
    Then I retrieve the egress file
    Then I verify it has the following data:
      | 02021061501            |
      | 1T7700475G        2021 |
      | 1T6386446Z        2021 |
      | 1T8255237A        2021 |
      | 1S9431366Z        2021 |
      | 1S4035558H        2021 |
      | 1S8383595H        2021 |
      | 1T4666665D        2021 |
      | 1T9287323J        2021 |
      | 1S3592339Z        2021 |
      | 1T2559298G        2021 |
      | 1G8688152M        2021 |
      | 1G5262765L        2021 |
      | 1G4563228N        2021 |
      | 1F6307232W        2021 |
      | 1T5080649E        2021 |
      | 1T7700475G        2020 |
      | 1T6386446Z        2020 |
      | 1T2430411B        2020 |
      | 1S0600147E        2020 |
      | 1T8255237A        2020 |
      | 1T5874373E        2020 |
      | 1S3401364J        2020 |
      | 1S9431366Z        2020 |
      | 1S4035558H        2020 |
      | 1T4666665D        2020 |
      | 1G8688152M        2020 |
      | 1T5080649E        2020 |
      | 1T5874373E        2019 |
      | 1S3401364J        2019 |
      | 1T4666665D        2019 |
      | 1T9287323J        2019 |
      | 1G8688152M        2019 |
      | 1G5262765L        2019 |
      | 1F6307232W        2019 |
      | 1T5080649E        2019 |
      | 1S0900370C        2021 |
      | 1S0000001K        2021 |
      | 1F0000003C        2021 |
      | 1S0900370C        2020 |
      | 1S0000001K        2020 |
      | 1F0000003C        2020 |
      | 1S0900370C        2019 |
      | 1S0000001K        2019 |
      | 1F0000003C        2019 |
      | 2000000044             |
    And delete the egress file

  Scenario: Cycle 2B, thrice monthly assessable income ingress on 15 June 2021
  """
    Initiate ingress for cycle 2b thrice
    """
    Given IRAS provide the thrice monthly assessable income file on 20210615 with the following data:
      | RECORD_TYPE | NATURAL_ID | ASSESSABLE_INCOME | RESULT_INDICATOR | ASSESSMENT_YEAR |
      | 1           | T7431328G  | 0003145555        | 01               | 2021            |
      | 1           | T7431328G  | 0003145555        | 01               | 2020            |
      | 1           | T7431328G  | 0003145555        | 01               | 2019            |
      | 1           | S9431366Z  | 0000000000        | 02               | 2021            |
      | 1           | S9431366Z  | 0000000000        | 05               | 2020            |
      | 1           | S4035558H  | 0000000000        | 02               | 2021            |
      | 1           | S4035558H  | 0000000000        | 04               | 2020            |
      | 1           | S8383595H  | 0000000010        | 04               | 2021            |
      | 1           | T4666665D  | 0000000000        | 04               | 2021            |
      | 1           | T4666665D  | 0000000000        | 02               | 2020            |
      | 1           | T4666665D  | 0000000000        | 04               | 2019            |
      | 1           | T9287323J  | 0000000000        | 04               | 2021            |
      | 1           | T9287323J  | 0000000000        | 04               | 2019            |
      | 1           | S3592339Z  | 0000000000        | 04               | 2021            |
      | 1           | G8688152M  | 0000000000        | 04               | 2021            |
      | 1           | G8688152M  | 0000000000        | 02               | 2020            |
      | 1           | G8688152M  | 0000000000        | 04               | 2019            |
      | 1           | G5262765L  | 0000000000        | 04               | 2021            |
      | 1           | G5262765L  | 0000000000        | 02               | 2019            |
      | 1           | G4563228N  | 0000000000        | 05               | 2021            |
      | 1           | F6307232W  | 0000000000        | 05               | 2021            |
      | 1           | F6307232W  | 0000000000        | 02               | 2019            |
      | 1           | T2559298G  | 0000000000        | 00               | 2021            |
      | 1           | T5080649E  | 0001110000        | 01               | 2021            |
      | 1           | T5080649E  | 0000000000        | 01               | 2020            |
      | 1           | T5080649E  | 0000000000        | 01               | 2019            |
      | 1           | T7700475G  | 0001120000        | 01               | 2021            |
      | 1           | T7700475G  | 0000000000        | 02               | 2020            |
      | 1           | T8255237A  | 0001130000        | 01               | 2021            |
      | 1           | T8255237A  | 0000000000        | 02               | 2020            |
      | 1           | T6386446Z  | 0001130000        | 01               | 2021            |
      | 1           | T6386446Z  | 0000000000        | 02               | 2020            |
      | 1           | T2430411B  | 0000000000        | 02               | 2020            |
      | 1           | S0600147E  | 0000000000        | 02               | 2020            |
      | 1           | T5874373E  | 0000000000        | 02               | 2020            |
      | 1           | T5874373E  | 0000000000        | 02               | 2019            |
      | 1           | S3401364J  | 0000000001        | 00               | 2020            |
      | 1           | S3401364J  | 0000000000        | 02               | 2019            |
      | 1           | S0900370C  | 0001140000        | 01               | 2021            |
      | 1           | S0900370C  | 0002140000        | 01               | 2020            |
      | 1           | S0900370C  | 0003140000        | 01               | 2019            |
    When IRAS sends the IRAS_THRICE_MONTHLY_AI file to Datasource sftp for processing
    Then the Iras Assessable Income batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the IRAS_THRICE_MONTHLY_AI file received date to 20210615
    And I re-run the batch job
    Then the Iras Assessable Income batch job completes running with status CLEANUP
    And I verify the following natural id's income status and value for year 2021 as of 20210615 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T7431328G  | 31455.55          | INCOME_ASSESSED    |
      | S9431366Z  | 0                 | PENDING_ASSESSMENT |
      | S4035558H  | 0                 | PENDING_ASSESSMENT |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | T9287323J  | 0                 | PENDING_ASSESSMENT |
      | S3592339Z  | 0                 | PENDING_ASSESSMENT |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | G5262765L  | 0                 | PENDING_ASSESSMENT |
      | G4563228N  | 0                 | PENDING_ASSESSMENT |
      | F6307232W  | 0                 | PENDING_ASSESSMENT |
      | T2559298G  | 0                 | RECORD_NOT_FOUND   |
      | T5080649E  | 11100             | INCOME_ASSESSED    |
      | T7700475G  | 11200             | INCOME_ASSESSED    |
      | T8255237A  | 11300             | INCOME_ASSESSED    |
      | T6386446Z  | 11300             | INCOME_ASSESSED    |
      | S0900370C  | 11400             | INCOME_ASSESSED    |
    And I verify the following natural id's income status and value for year 2020 as of 20210615 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T7431328G  | 31455.55          | INCOME_ASSESSED    |
      | S9431366Z  | 0                 | PENDING_ASSESSMENT |
      | S4035558H  | 0                 | PENDING_ASSESSMENT |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | T5080649E  | 0                 | INCOME_ASSESSED    |
      | T7700475G  | 0                 | PENDING_ASSESSMENT |
      | T8255237A  | 0                 | PENDING_ASSESSMENT |
      | T6386446Z  | 0                 | PENDING_ASSESSMENT |
      | T2430411B  | 0                 | PENDING_ASSESSMENT |
      | S0600147E  | 0                 | PENDING_ASSESSMENT |
      | T5874373E  | 0                 | PENDING_ASSESSMENT |
      | S0900370C  | 21400             | INCOME_ASSESSED    |
    And I verify the following natural id's income status and value for year 2019 as of 20210615 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T7431328G  | 31455.55          | INCOME_ASSESSED    |
      | T4666665D  | 0                 | PENDING_ASSESSMENT |
      | T9287323J  | 0                 | PENDING_ASSESSMENT |
      | G8688152M  | 0                 | PENDING_ASSESSMENT |
      | G5262765L  | 0                 | PENDING_ASSESSMENT |
      | F6307232W  | 0                 | PENDING_ASSESSMENT |
      | T5080649E  | 0                 | INCOME_ASSESSED    |
      | T5874373E  | 0                 | PENDING_ASSESSMENT |
      | S3401364J  | 0                 | PENDING_ASSESSMENT |
      | S0900370C  | 31400             | INCOME_ASSESSED    |

  Scenario: Cycle 2C, dual citizen file ingress on 16 June 2021
    Given MHA send MHA_DUAL_CITIZEN on 20210616 with the following data:
      | S3401364J |
      | S0600147E |
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    Then the Mha Dual Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the MHA_DUAL_CITIZEN file received date to 20210616
    And I re-run the batch job
    Then the Mha Dual Citizen batch job completes running with status CLEANUP

  Scenario: Cycle 2C, ceased citizen file ingress on 20210630
    Given MHA send MHA_CEASED_CITIZEN file on 20210630 with the following data:
      | NRIC      | NAME   | NATIONALITY | CESSATION_DATE |
      | S0600147E | <AUTO> | OO          | 20200620       |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the MHA_DEATH_DATE file received date to 20210630
    And I re-run the batch job
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP

  Scenario: Cycle 2C, new citizen file ingress on 01 July 2021
    Given MHA send MHA_NEW_CITIZEN file on 20210701 with the following data:
      | NRIC      | FIN       | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR           | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR           | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | T6012487B | G8688152M | Jennifer | 19880105 | F      | C            | C             | Bedok South Ave 11 | C            | C             | Bedok South Ave 51 |                      | 20180704            | 20210630     |
      | T1772613C | F6307232W | Lucy     | 19880105 | F      | C            | C             | Bedok South Ave 1  | C            | C             | Bedok South Ave 21 |                      | 20180704            | 20210630     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    Then the Mha New Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the MHA_NEW_CITIZEN file received date to 20210701
    And I re-run the batch job
    Then the Mha New Citizen batch job completes running with status CLEANUP

  Scenario: Cycle 2C, death broadcast file ingress on 01 July 2021
    Given MHA send MHA_DEATH_DATE file on 20210701 with the following data:
      | NATURALID | DOD      |
      | S0601258B | 20191231 |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    Then the Mha Death Date batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the MHA_DEATH_DATE file received date to 20210701
    And I re-run the batch job
    Then the Mha Death Date batch job completes running with status CLEANUP

  Scenario: Cycle 2C, thrice monthly assessable income egress on 22 July 2021
    Given I triggered thrice monthly assessable income egress job on 20210722
    Then I retrieve the egress file
    Then I verify it has the following data:
      | 02021072201            |
      | 1S9431366Z        2021 |
      | 1S4035558H        2021 |
      | 1S8383595H        2021 |
      | 1S0000001K        2021 |
      | 1S5478995E        2021 |
      | 1T4666665D        2021 |
      | 1T9287323J        2021 |
      | 1S3592339Z        2021 |
      | 1T6012487B        2021 |
      | 1G5262765L        2021 |
      | 1F0000003C        2021 |
      | 1G4563228N        2021 |
      | 1T1772613C        2021 |
      | 1S0036843A        2021 |
      | 1T0491551D        2020 |
      | 1S9431366Z        2020 |
      | 1S4035558H        2020 |
      | 1S0000001K        2020 |
      | 1S5478995E        2020 |
      | 1T4666665D        2020 |
      | 1T6012487B        2020 |
      | 1F0000003C        2020 |
      | 1S0601258B        2020 |
      | 1S0036843A        2020 |
      | 1T7700475G        2020 |
      | 1T8255237A        2020 |
      | 1T6386446Z        2020 |
      | 1T2430411B        2020 |
      | 1T5874373E        2020 |
      | 1S3401364J        2019 |
      | 1S0000001K        2019 |
      | 1S5478995E        2019 |
      | 1T4666665D        2019 |
      | 1T9287323J        2019 |
      | 1T6012487B        2019 |
      | 1G5262765L        2019 |
      | 1F0000003C        2019 |
      | 1T1772613C        2019 |
      | 1S0601258B        2019 |
      | 1T5874373E        2019 |
      | 1S3401364J        2019 |
      | 2000000041             |
    And delete the egress file

  Scenario: Cycle 2C, thrice monthly assessable income ingress on 22 July 2021
  """
    Initiate ingress for cycle 2c thrice
    """
    Given IRAS provide the thrice monthly assessable income file on 20210722 with the following data:
      | RECORD_TYPE | NATURAL_ID | ASSESSABLE_INCOME | RESULT_INDICATOR | ASSESSMENT_YEAR |
      | 1           | S9431366Z  | 0000000000        | 02               | 2021            |
      | 1           | S4035558H  | 0000000000        | 02               | 2021            |
      | 1           | S8383595H  | 0000000000        | 02               | 2021            |
      | 1           | S5478995E  | 0000000000        | 04               | 2021            |
      | 1           | T4666665D  | 0001140050        | 01               | 2021            |
      | 1           | T9287323J  | 0000000000        | 04               | 2021            |
      | 1           | S3592339Z  | 0000000000        | 04               | 2021            |
      | 1           | G8688152M  | 0000000000        | 04               | 2021            |
      | 1           | G5262765L  | 0000000000        | 04               | 2021            |
      | 1           | G4563228N  | 0000000000        | 05               | 2021            |
      | 1           | F6307232W  | 0000000000        | 05               | 2021            |
      | 1           | S0036843A  | 0001140050        | 01               | 2021            |
      | 1           | T0491551D  | 0000000000        | 02               | 2020            |
      | 1           | S9431366Z  | 9999999999        | 01               | 2020            |
      | 1           | S4035558H  | 0000000045        | 01               | 2020            |
      | 1           | S5478995E  | 0000000051        | 01               | 2020            |
      | 1           | T4666665D  | 0000000000        | 04               | 2020            |
      | 1           | T6012487B  | 0000000000        | 02               | 2020            |
      | 1           | S0601258B  | 0000000000        | 04               | 2020            |
      | 1           | S0036843A  | 0000000000        | 04               | 2020            |
      | 1           | T7700475G  | 0000000000        | 05               | 2020            |
      | 1           | T8255237A  | 0000000000        | 05               | 2020            |
      | 1           | T6386446Z  | 0000000000        | 05               | 2020            |
      | 1           | T2430411B  | 0000000000        | 05               | 2020            |
      | 1           | T5874373E  | 0000000000        | 05               | 2020            |
      | 1           | S3401364J  | 0000000000        | 05               | 2020            |
      | 1           | S5478995E  | 0000000000        | 00               | 2019            |
      | 1           | T4666665D  | 0000000000        | 02               | 2019            |
      | 1           | T9287323J  | 0000000000        | 02               | 2019            |
      | 1           | T6012487B  | 0000000000        | 02               | 2019            |
      | 1           | G5262765L  | 0000000000        | 02               | 2019            |
      | 1           | T1772613C  | 0000000000        | 02               | 2019            |
      | 1           | S0601258B  | 0000888888        | 01               | 2019            |
      | 1           | T5874373E  | 0000000000        | 01               | 2019            |
      | 1           | S3401364J  | 0001200000        | 01               | 2019            |
    When IRAS sends the IRAS_THRICE_MONTHLY_AI file to Datasource sftp for processing
    Then the Iras Assessable Income batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I update the IRAS_THRICE_MONTHLY_AI file received date to 20210722
    And I re-run the batch job
    Then the Iras Assessable Income batch job completes running with status CLEANUP
    And I verify the following natural id's income status and value for year 2021 as of 20210722 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | S9431366Z  | 0000000000        | PENDING_ASSESSMENT |
      | S4035558H  | 0000000000        | PENDING_ASSESSMENT |
      | S8383595H  | 0000000000        | PENDING_ASSESSMENT |
      | S5478995E  | 0000000000        | PENDING_ASSESSMENT |
      | T4666665D  | 0001140050        | INCOME_ASSESSED    |
      | T9287323J  | 0000000000        | PENDING_ASSESSMENT |
      | S3592339Z  | 0000000000        | PENDING_ASSESSMENT |
      | G8688152M  | 0000000000        | PENDING_ASSESSMENT |
      | G5262765L  | 0000000000        | PENDING_ASSESSMENT |
      | G4563228N  | 0000000000        | PENDING_ASSESSMENT |
      | F6307232W  | 0000000000        | PENDING_ASSESSMENT |
      | S0036843A  | 0001140050        | INCOME_ASSESSED    |
    And I verify the following natural id's income status and value for year 2020 as of 20210722 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | T0491551D  | 0000000000        | INCOME_ASSESSED |
      | S9431366Z  | 9999999999        | INCOME_ASSESSED    |
      | S4035558H  | 0000000045        | INCOME_ASSESSED    |
      | S5478995E  | 0000000051        | INCOME_ASSESSED    |
      | T4666665D  | 0000000000        | PENDING_ASSESSMENT |
      | T6012487B  | 0000000000        | PENDING_ASSESSMENT |
      | S0601258B  | 0000000000        | PENDING_ASSESSMENT |
      | S0036843A  | 0000000000        | PENDING_ASSESSMENT |
      | T7700475G  | 0000000000        | PENDING_ASSESSMENT |
      | T8255237A  | 0000000000        | PENDING_ASSESSMENT |
      | T6386446Z  | 0000000000        | PENDING_ASSESSMENT |
      | T2430411B  | 0000000000        | PENDING_ASSESSMENT |
      | T5874373E  | 0000000000        | PENDING_ASSESSMENT |
      | S3401364J  | 0000000000        | PENDING_ASSESSMENT |
    And I verify the following natural id's income status and value for year 2020 as of 20210722 are as follows:
      | NATURAL_ID | ASSESSABLE_INCOME | STATUS             |
      | S5478995E  | 0000000000        | RECORD_NOT_FOUND   |
      | T4666665D  | 0000000000        | PENDING_ASSESSMENT |
      | T9287323J  | 0000000000        | PENDING_ASSESSMENT |
      | T6012487B  | 0000000000        | PENDING_ASSESSMENT |
      | G5262765L  | 0000000000        | PENDING_ASSESSMENT |
      | T1772613C  | 0000000000        | PENDING_ASSESSMENT |
      | S0601258B  | 0000888888        | INCOME_ASSESSED    |
      | T5874373E  | 0000000000        | INCOME_ASSESSED    |
      | S3401364J  | 0001200000        | INCOME_ASSESSED    |











