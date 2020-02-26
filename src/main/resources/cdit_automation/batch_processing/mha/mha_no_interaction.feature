@development
@no_interaction
@mha
Feature: Data processing for MHA no interaction list (NIL)

  @set_1
  Scenario: Datasource service processes a MHA no interaction list file with wrong footer count.
    Given the file have the following header details:
      |2019010120190101|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |20201230          |
    Given the file have the following footer details:
      |000000999|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status FILE_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Footer size does not match body size.         | 1     |
    And I verify number of records in MHA no interaction validated table is 0

  @set_2
  Scenario: Datasource service processes a MHA no interaction list file with wrong footer format.
    Given the file have the following header details:
      |2019010120190101|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |20201230          |
    Given the file have the following footer details:
      |000000abc|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status FILE_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Footer record count must be numeric.          | 1     |
    And I verify number of records in MHA no interaction validated table is 0

  @set_3
  Scenario: Datasource service processes a MHA no interaction list file with no footer provided.
    Given the file have the following header details:
      |2019010120190101|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |20201230          |
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status FILE_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Footer record count must be numeric.          | 1     |
      | Must have at least 1 valid body record.       | 1     |
    And I verify number of records in MHA no interaction validated table is 0

  @set_4 @defect @GRYFFINDOR-1094
  Scenario: Datasource service processes a MHA no interaction list file with empty footer provided.
    Given the file have the following header details:
      |2019010120190101|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |20201230          |
    Given the file have the following footer details:
      |blank    |
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status FILE_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Must have 1 Footer record.                    | 1     |
    And I verify number of records in MHA no interaction validated table is 0
    
  @set_5
  Scenario: Datasource service processes a MHA no interaction list file with wrong header length.
    Given the file have the following header details:
      |20190101201901  |
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |20201230          |
    Given the file have the following footer details:
      |000000001|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status FILE_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Wrong header length.                          | 1     |
    And I verify number of records in MHA no interaction validated table is 0

  @set_6
  Scenario: Datasource service processes a MHA no interaction list file with wrong header extraction date format.
    Given the file have the following header details:
      |201901 120190103|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |20201230          |
    Given the file have the following footer details:
      |000000001|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status RAW_DATA_ERROR
    And I verify that the following error message appeared:
      | Message                                                | Count |
      | {"extractionDate":["Must be in yyyymmdd date format"]} | 1     |
    And I verify number of records in MHA no interaction validated table is 1

  @set_7
  Scenario: Datasource service processes a MHA no interaction list file with wrong header cut-off date format.
    Given the file have the following header details:
      |201901012019ab03|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |20201230          |
    Given the file have the following footer details:
      |000000001|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status RAW_DATA_ERROR
    And I verify that the following error message appeared:
      | Message                                                | Count |
      | {"cutOffDate":["Must be in yyyymmdd date format"]}     | 1     |
    And I verify number of records in MHA no interaction validated table is 1

  @set_8
  Scenario: Datasource service processes a MHA no interaction list file with no header provided.
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |20201230          |
    Given the file have the following footer details:
      |000000001|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status FILE_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Wrong header length.                          | 1     |
      | Footer size does not match body size.         | 1     |
      | Must have at least 1 valid body record.       | 1     |
    And I verify number of records in MHA no interaction validated table is 0

  @set_9
  Scenario: Datasource service processes a MHA no interaction list file with empty header provided.
    Given the file have the following header details:
      |blank           |
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |20201230          |
    Given the file have the following footer details:
      |000000001|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status FILE_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Wrong header length.                          | 1     |
    And I verify number of records in MHA no interaction validated table is 0

  @set_10
  Scenario: Datasource service processes a MHA no interaction list file with extraction date before cut-off date.
    Given the file have the following header details:
      |2019122920191230|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |20201230          |
    Given the file have the following footer details:
      |000000001|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status RAW_DATA_ERROR
    And I verify that the following error message appeared:
      | Message                                                            | Count |
      | {"Others":["Extraction Date must be equal/after Cut-off Date."]}   | 1     |
    And I verify number of records in MHA no interaction validated table is 1

  @set_11
  Scenario: Datasource service processes a MHA no interaction list file with extraction & cut-off date after file received.
    Given the file have the following header details:
      |9999010199990101|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |20201230          |
    Given the file have the following footer details:
      |000000001|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that the following error message appeared:
      | Message                                                  | Count |
      | Extraction date cannot be after File Received date.      | 1     |
      | Cut-off date cannot be after File Received date.         | 1     |
    And I verify number of records in MHA no interaction validated table is 1

  @set_12 @defect @GRYFFINDOR-1095
  Scenario: Datasource service processes a MHA no interaction list file with wrong body length.
    Given the file have the following header details:
      |2019010120190101|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |2020              |
    Given the file have the following footer details:
      |000000001|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status FILE_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Wrong body length.                            | 1     |
      | Must have at least 1 valid body record.       | 1     |
    And I verify number of records in MHA no interaction validated table is 0

  @set_13 @note:order-of-error-message-affect-test
  Scenario: Datasource service processes a MHA no interaction list file with the below combination of body records.
            - Nric is spaces (i.e. '         ')
            - Nric is invalid
            - Nric is valid
    Given the file have the following header details:
      |2019010120190101|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |spaces       |19901001          |20200101          |
      |invalid      |19901001          |20200101          |
      |valid        |19901001          |20200101          |
    Given the file have the following footer details:
      |000000003|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status RAW_DATA_ERROR
    And I verify that the following error message appeared:
      | Message                                                                   | Count |
      | {"nric":["size must be between 9 and 9","NRIC cannot be null/blank."]}    | 1     |
      | {"nric":["Must be valid NRIC in format [S or T]1234567[A-Z]"]}            | 1     |
    And I verify number of records in MHA no interaction validated table is 1

  @set_14
  Scenario: Datasource service processes a MHA no interaction list file with no body provided.
    Given the file have the following header details:
      |2019010120190101|
    Given the file have the following footer details:
      |000000000|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status FILE_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Must have at least 1 valid body record.       | 1     |
    And I verify number of records in MHA no interaction validated table is 0
    
  @set_15 @defect @GRYFFINDOR-1095
  Scenario: Datasource service processes a MHA no interaction list file with empty body provided.
    Given the file have the following header details:
      |2019010120190101|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |blank        |blank             |blank             |
    Given the file have the following footer details:
      |000000001|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status FILE_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Wrong body length.                            | 1     |
      | Must have at least 1 valid body record.       | 1     |
    And I verify number of records in MHA no interaction validated table is 0

  @set_16 @defect @GRYFFINDOR-1096
  Scenario: Datasource service processes a MHA no interaction list file with nric not found in prepared DB.
    Given the file have the following header details:
      |2019010120190101|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19900101          |20200101          |
    Given the file have the following footer details:
      |000000001|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Unable to map to prepared data.               | 1     |
    And I verify number of records in MHA no interaction validated table is 1
    
  @set_17
  Scenario: Datasource service processes a MHA no interaction list file successfully.
    Given the database populated with the following person and person id details:
      |nric         |id_type    |
      |S1501500D    |NRIC       |
      |T1601600J    |NRIC       |
    Given the file have the following header details:
      |2019010120190101|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |S1501500D    |19900101          |20200101          |
      |T1601600J    |20000101          |22220101          |
    Given the file have the following footer details:
      |000000002|
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status CLEANUP
    And I verify number of records in MHA no interaction validated table is 2
    And I verify that the person status is updated correctly
    And I remove the test data from the prepared database
    
  @set_18 @defect @GRYFFINDOR-1097
  Scenario: Datasource service processes a MHA no interaction list file with no content (i.e totally empty file).
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status FILE_ERROR
    And I verify number of records in MHA no interaction validated table is 0
    And the error message contains Must have at least 1 valid body record

  @set_19 @defect
  Scenario: Datasource service processes a MHA no interaction list file with duplicate nric in same file.

  @set_20 @defect
  Scenario: Datasource service processes a MHA no interaction list file with duplicate nric in different batch file.

  @set_21 @defect @RVC-76
  Scenario: Datasource service processes a MHA no interaction list file with footer length exceed expected length.
    Given the file have the following header details:
      |2019010120190101|
    Given the file have the following record details:
      |nric         |valid_from_date   |valid_till_date   |
      |valid        |19901001          |20201230          |
    Given the file have the following footer details:
      |0000000001   |
    And the MHA no interaction file is created
    When MHA sends the MHA_NO_INTERACTION file to Datasource sftp for processing
    And the Mha no interaction batch job completes running with status FILE_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Footer length exceeded expected length.       | 1     |
    And I verify number of records in MHA no interaction validated table is 0
    