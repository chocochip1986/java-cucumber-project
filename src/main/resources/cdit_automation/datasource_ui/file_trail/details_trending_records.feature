@development
@datasourceui
@trending_records
Feature: Details of Trending Records for files

  @set_1
  Scenario: CPF officer accesses file trail page
    Given there is a MHA BULK CITIZEN file at Load step with Pending status processed 5 days ago
#    And there is reasonableness statistics for the previous run 2019
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    And I search for the file
    Then I verify that I see the file trail page
    And I click on the reasonableness trending link
    And I logout of CDS Intranet


  @set_2
  Scenario: CPF officer views reasonableness trending card
    Given the mha bulk file is being created
    Given the mha bulk file has the following details:
      | Singaporean,M,Name:Laksa Boy,Fin:F8100327X,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag: ,Quantity:1       |
      | PermanentResident,Nric:S3450033I,DoB:19860901,Name:Mee Siam,F,Overseas,BlkNo:212,StrtName:faea eae,FlrNo:12,BuildingName:123113afweaafawe aefa121,mhaAddrType:A,Quantity:1      |
      | DualCitizen,Name:Wan Mo Peh,Nric:S0743815Z,M,NCAAddress,BlkNo:212,StrtCode:1h2y3u,LvlNo:131,UnitNo:123ka,Quantity:1                                                             |
    And the mha bulk file is created
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    And the Mha Bulk batch job completes running with status CLEANUP
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    And I search for the file
    Then I verify that I see the file trail page
    Then I verify that content validation count matches Datasource validated record count
    And I logout of CDS Intranet


