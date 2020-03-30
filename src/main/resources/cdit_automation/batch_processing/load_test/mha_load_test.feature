@development
@mha
@mha_performance_test
Feature: Performance test of Datasource processing Mha files

  @defect @mha_performance_load_test
  Scenario: Datasource service processes all 7, large Mha files
    Given Mha load testing files:
      | fileName                               | fileType                 | count   |
      | mha_bulk_citizen_load_test.txt         | MHA_BULK_CITIZEN         | 3000000 |
      | mha_dual_citizen_load_test.txt         | MHA_DUAL_CITIZEN         | 10000   |
      | mha_ceased_citizen_load_test.txt       | MHA_CEASED_CITIZEN       | 10000   |
      | mha_death_broadcast_load_test.txt      | MHA_DEATH_DATE           | 10000   |
      | mha_change_address_load_test.txt       | MHA_CHANGE_ADDRESS       | 10000   |
      | mha_person_detail_change_load_test.txt | MHA_PERSON_DETAIL_CHANGE | 10000   |
      | mha_new_citizen_load_test.txt          | MHA_NEW_CITIZEN          | 10000   |
    When uploading these files in sequence to S3 with logging of timestamp:
      | fileName                               | fileType                 |
      | mha_bulk_citizen_load_test.txt         | MHA_BULK_CITIZEN         |
      | mha_dual_citizen_load_test.txt         | MHA_DUAL_CITIZEN         |
      | mha_ceased_citizen_load_test.txt       | MHA_CEASED_CITIZEN       |
      | mha_death_broadcast_load_test.txt      | MHA_DEATH_DATE           |
      | mha_change_address_load_test.txt       | MHA_CHANGE_ADDRESS       |
      | mha_person_detail_change_load_test.txt | MHA_PERSON_DETAIL_CHANGE |
      | mha_new_citizen_load_test.txt          | MHA_NEW_CITIZEN          |