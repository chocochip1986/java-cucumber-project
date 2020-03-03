@development
@datasourceui
@trending_records
Feature: Details of Trending Records for files

  @set_1
  Scenario: CPF officer accesses file trail page
    Given There is a MHA BULK CITIZEN file at Load step with Pending status processed 5 days ago
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    And I search for the file
    Then I verify that I see the file trail page
    And I click on the reasonableness trending link
    And I logout of CDS Intranet

  @set_2
  Scenario: CPF officer views reasonableness trending card after Bulk Citizen file is processed
    Given the mha bulk file is being created
    Given the mha bulk file has the following details:
      | Singaporean,M,Name:Laksa Boy,Fin:F8100327X,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag: ,Quantity:1       |
      | PermanentResident,Nric:S3450033I,DoB:19860901,Name:Mee Siam,F,Overseas,BlkNo:212,StrtName:faea eae,FlrNo:12,BuildingName:123113afweaafawe aefa121,mhaAddrType:A,Quantity:1                          |
      | DualCitizen,Name:Wan Mo Peh,Nric:S0743815Z,M,NCAAddress,BlkNo:212,StrtCode:1h2y3u,LvlNo:131,UnitNo:123ka,Quantity:1                                                                                 |
#      | RandomPeople,Quantity:10                                                                                                                                                                            |
    And the mha bulk file is created
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    And the Mha Bulk batch job completes running with status CLEANUP
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    And I search for the file
    Then I verify that I see the file trail page
#    TODO: Uncomment after story to remove header and footer from total validated count is done
#    Then I verify that content validation count matches Datasource validated record count
    When I click on the reasonableness trending link
    And I logout of CDS Intranet

  @set_3
  Scenario: CPF officer views reasonableness trending card
    Given There is a MHA BULK CITIZEN file at Load step with Pending status processed 40 days ago
    And the below MHA BULK CITIZEN reasonableness statistics with CLEANUP status inserted previously with year - 1 month + 0 day + 0
      | data_item                | data_item_value     |
      | No of new 13 year old    | 100                 |
    And the below MHA BULK CITIZEN reasonableness statistics with CLEANUP status inserted previously with year - 1 month + 2 day + 0
      | data_item                | data_item_value     |
      | No of new 13 year old    | 200                 |
    And the below MHA BULK CITIZEN reasonableness statistics with CLEANUP status inserted previously with year - 2 month + 0 day + 2
      | data_item                | data_item_value     |
      | No of new 13 year old    | 300                 |
    And the below MHA BULK CITIZEN reasonableness statistics with CLEANUP status inserted previously with year - 2 month + 0 day + 1
      | data_item                | data_item_value     |
      | No of new 13 year old    | 400                 |
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    And I search for the file
    Then I verify that I see the file trail page
    And I click on the reasonableness trending link
    Then I can see the statistics for MHA BULK CITIZEN
    And I logout of CDS Intranet


