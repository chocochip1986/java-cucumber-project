@development
@mha_new_dual_ceased_citizen
@mha
Feature: Verify Nationality mapping of Mha using New, Dual and Ceased citizen files

  @defect @GRYFFINDOR-1224
  Scenario: Datasource service processes Mha New, Dual and Ceased Citizen file
    Given Mha New Citizen G-1224 file mha_new_citizen_G1224.txt with Date of Run 20190515 and following new citizen details:
      | nric      | fin       | dateOfBirth | name                                                | attainmentDate |
      | S9000000D |           | 19900424    | SC -> DC                                            | 19900424       |
      | S9000001B | F0000001U | 19650304    | SC -> Non SC                                        | 20190104       |
      | S9000002J |           | 19540401    | SC -> DC -> SC                                      |                |
      | S9000003I | F0000003P | 19300101    | SC -> DC -> SC -> Non SC                            | 19650809       |
      | S9000004G |           | 19751212    | SC -> (DC but superseded) -> Non SC                 |                |
      | S9000005E |           | 19330303    | SC -> DC -> Non SC                                  | 19650809       |
      | S9000006C |           | 19330303    | SC -> Non SC -> (Appear in DC but remain as Non SC) |                |
      | S9000007A |           | 19330303    | SC -> Non SC -> SC (not supported yet)              |                |
    And Mha Dual Citizen G-1224 file mha_dual_citizen_G1224_1.txt with Date of Run 20190515 and the following dual citizen details:
      | nric      |
      | S9000000D |
      | S9000002J |
      | S9000003I |
      | S9000004G |
      | S9000005E |
    And Mha Ceased Citizen G-1224 file mha_ceased_citizen_G1224_1.txt with Date of Run 20190520 and the following ceased citizen details:
      | nric      | name                                                | ceasedDate |
      | S9000001B | SC -> Non SC                                        | 20190424   |
      | S9000004G | SC -> (DC but superseded) -> Non SC                 | 20190514   |
      | S9000006C | SC -> Non SC -> (Appear in DC but remain as Non SC) | 20190520   |
      | S9000007A | SC -> Non SC -> SC (not supported yet)              | 20190519   |
    And Mha Dual Citizen G-1224 file mha_dual_citizen_G1224_2.txt with Date of Run 20190522 and the following dual citizen details:
      | nric      |
      | S9000000D |
      | S9000005E |
      | S9000006C |
    And Mha Ceased Citizen G-1224 file mha_ceased_citizen_G1224_2.txt with Date of Run 20190523 and the following ceased citizen details:
      | nric      | name                     | ceasedDate |
      | S9000003I | SC -> DC -> SC -> Non SC | 20190523   |
      | S9000005E | SC -> DC -> Non SC       | 20190523   |
    When uploading these files in sequence to S3:
      | fileName                       | fileType           |
      | mha_new_citizen_G1224.txt      | MHA_NEW_CITIZEN    |
      | mha_dual_citizen_G1224_1.txt   | MHA_DUAL_CITIZEN   |
      | mha_ceased_citizen_G1224_1.txt | MHA_CEASED_CITIZEN |
      | mha_dual_citizen_G1224_2.txt   | MHA_DUAL_CITIZEN   |
      | mha_ceased_citizen_G1224_2.txt | MHA_CEASED_CITIZEN |
    Then these are the following nationality mapping for G-1224 files:
      | nric      | nationality           | attainmentDate      | ceasedDate          | validFrom           | validTill           |
      | S9000000D | SINGAPORE_CITIZEN     | 1990-04-24 00:00:00 |                     | 1990-04-24 00:00:00 | 2019-05-14 23:59:59 |
      | S9000000D | DUAL_CITIZENSHIP      | 1990-04-24 00:00:00 |                     | 2019-05-15 00:00:00 | 9999-12-31 23:59:59 |
      | S9000001B | SINGAPORE_CITIZEN     | 2019-01-04 00:00:00 |                     | 2019-01-04 00:00:00 | 2019-04-23 23:59:59 |
      | S9000001B | NON_SINGAPORE_CITIZEN | 2019-01-04 00:00:00 | 2019-04-24 00:00:00 | 2019-04-24 00:00:00 | 9999-12-31 23:59:59 |
      | S9000002J | SINGAPORE_CITIZEN     | 1954-04-01 00:00:00 |                     | 1954-04-01 00:00:00 | 2019-05-14 23:59:59 |
      | S9000002J | DUAL_CITIZENSHIP      | 1954-04-01 00:00:00 |                     | 2019-05-15 00:00:00 | 2019-05-21 23:59:59 |
      | S9000002J | SINGAPORE_CITIZEN     | 1954-04-01 00:00:00 |                     | 2019-05-22 00:00:00 | 9999-12-31 23:59:59 |
      | S9000003I | SINGAPORE_CITIZEN     | 1965-08-09 00:00:00 |                     | 1965-08-09 00:00:00 | 2019-05-14 23:59:59 |
      | S9000003I | DUAL_CITIZENSHIP      | 1965-08-09 00:00:00 |                     | 2019-05-15 00:00:00 | 2019-05-21 23:59:59 |
      | S9000003I | SINGAPORE_CITIZEN     | 1965-08-09 00:00:00 |                     | 2019-05-22 00:00:00 | 2019-05-22 23:59:59 |
      | S9000003I | NON_SINGAPORE_CITIZEN | 1965-08-09 00:00:00 | 2019-05-23 00:00:00 | 2019-05-23 00:00:00 | 9999-12-31 23:59:59 |
      | S9000004G | SINGAPORE_CITIZEN     | 1975-12-12 00:00:00 |                     | 1975-12-12 00:00:00 | 2019-05-13 23:59:59 |
      | S9000004G | NON_SINGAPORE_CITIZEN | 1975-12-12 00:00:00 | 2019-05-14 00:00:00 | 2019-05-14 00:00:00 | 9999-12-31 23:59:59 |
      | S9000005E | SINGAPORE_CITIZEN     | 1965-08-09 00:00:00 |                     | 1965-08-09 00:00:00 | 2019-05-14 23:59:59 |
      | S9000005E | DUAL_CITIZENSHIP      | 1965-08-09 00:00:00 |                     | 2019-05-15 00:00:00 | 2019-05-22 23:59:59 |
      | S9000005E | NON_SINGAPORE_CITIZEN | 1965-08-09 00:00:00 | 2019-05-23 00:00:00 | 2019-05-23 00:00:00 | 9999-12-31 23:59:59 |
      | S9000006C | SINGAPORE_CITIZEN     | 1933-03-03 00:00:00 |                     | 1933-03-03 00:00:00 | 2019-05-19 23:59:59 |
      | S9000006C | NON_SINGAPORE_CITIZEN | 1933-03-03 00:00:00 | 2019-05-20 00:00:00 | 2019-05-20 00:00:00 | 9999-12-31 23:59:59 |
      | S9000007A | SINGAPORE_CITIZEN     | 1933-03-03 00:00:00 |                     | 1933-03-03 00:00:00 | 2019-05-18 23:59:59 |
      | S9000007A | NON_SINGAPORE_CITIZEN | 1933-03-03 00:00:00 | 2019-05-19 00:00:00 | 2019-05-19 00:00:00 | 9999-12-31 23:59:59 |