@development
@bulk_citizen
@mha
Feature: Data processing for MHA bulk citizen file

  @set_1
  Scenario: Datasource service processes a MHA bulk citizen file
    Given the mha bulk file has the following details:
    | Singaporean,M,Nric:S8764637H,Name:Dao Sa Pia,Fin:F8100327X,DoD:DeathBeforeBirth,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,Quantity:1       |
    | PermanentResident,Nric:T1030149H,DoB:19860901,Name:Roti John,F,DoD:DeathBeforeBirth,Overseas,BlkNo:212,StrtName:faea eae,FlrNo:12,BuildingName:123113afweaafawe aefa121,mhaAddrType:A,Quantity:1 |
    | DualCitizen,M,DoD:DeathBeforeBirth,NCAAddress,BlkNo:212,StrtCode:1h2y3u,LvlNo:131,UnitNo:123ka,Quantity:1                                                                                        |
    And the mha bulk file is created
#    When the mha bulk file is ran
