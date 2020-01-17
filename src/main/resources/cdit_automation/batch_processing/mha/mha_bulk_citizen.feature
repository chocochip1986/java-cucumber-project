@development
@bulk_citizen
@mha
Feature: Data processing for MHA bulk citizen file

  @set_1 @GRYFFINDOR-912
  Scenario: Datasource service processes a MHA bulk citizen file
    Given the mha bulk file is being created
    Given the mha bulk file has the following details:
    | Singaporean,M,Name:Dao Sa Pia,Fin:F8100327X,DoD:DeathBeforeBirth,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag: ,Quantity:1       |
    | PermanentResident,DoB:19860901,Name:Roti John,F,DoD:DeathBeforeBirth,Overseas,BlkNo:212,StrtName:faea eae,FlrNo:12,BuildingName:123113afweaafawe aefa121,mhaAddrType:A,Quantity:1                     |
    | DualCitizen,M,DoD:DeathBeforeBirth,NCAAddress,BlkNo:212,StrtCode:1h2y3u,LvlNo:131,UnitNo:123ka,Quantity:1                                                                                             |
    | Singaporean,F,Name:Bak Chor Mee,MHAAddress,PostalCode:123456,BuildingName:Shenton way,UnitNo:12,BlkNo:212,StrtName:Kfc street,FlrNo:12,ctzIssDate:        ,Quantity:1                                                     |
#    | RandomPeople,Quantity:100000                                                                                                                                                                          |
    And the mha bulk file is created
#    And I notify in slack Running MHA Bulk Citizen File
#    And I upload the file mha_bulk_citizen to bucket gds-cpfb-ftp-trial at path ready/mha
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
#    And slack notify Starting MHA Bulk file test
    When the mha bulk file is ran
    And the Mha Bulk batch job completes running with status FILE_CHECK_AGAINST_PREP_DATA
