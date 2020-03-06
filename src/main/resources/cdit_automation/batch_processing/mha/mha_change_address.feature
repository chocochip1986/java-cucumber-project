@development
@change_address
@mha
Feature: MHA Change address

  @set_1
  Scenario: MHA sends an empty change address file
    Given the mha change address file is empty
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status FILE_ERROR
    And the error message contains Must have at least 1 valid body record

  @set_2
  Scenario: CDS successfully processes a MHA change address file
    Given A singaporean person A owns a LANDED property
    Given A singaporean person B resides in a LANDED property
    But resides in another landed property
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP
