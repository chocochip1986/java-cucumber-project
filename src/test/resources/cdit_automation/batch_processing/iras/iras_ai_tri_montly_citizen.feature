@development
@bulk_citizen
@iras
Feature: Data processing for IRAS tri-monthly citizen file

#  @set_1
#  Scenario: The first IRAS tri-monthly file should exclude current year of assessment
#    Given the database populated with the following data:
#      | Name  | NRIC      | Type | DOD      | Nationality | CitizenshipRenunciationDate | AIStatus           | AIYear |
#      | Muthu | S9999999A | NRIC | 20201212 | SC          | -                           | PENDING_ASSESSMENT | 2018   |
#      | AUTO  | AUTO      | NRIC | 20201212 | SC          | -                           | -                  | -      |

  Scenario: Create all objects required
    Given the following detail, generate all necessary objects
      | Name  | NaturalId | Type | DOD      | Nationality       | CitizenshipRenunciationDate | AIStatus           | AIYear |
      | Muthu | S9999999A | NRIC | 20201212 | SINGAPORE_CITIZEN | 20191010                    | PENDING_ASSESSMENT | 2018   |

    Then check PersonId object created as specified
    Then check PersonName object created as specified
    Then check PersonDetail object created as specified
    Then check Nationality object created as specified
    Then check Income object created as specified

  Scenario: Create all objects required if AUTO is found for Name
    Given the following detail, generate all necessary objects
      | Name | NaturalId | Type | DOD      | Nationality       | CitizenshipRenunciationDate | AIStatus           | AIYear |
      | AUTO | S9999999A | FIN  | 20201212 | SINGAPORE_CITIZEN | 20191010                    | PENDING_ASSESSMENT | 2018   |

    Then check the name in PersonId object is not AUTO or Null

  Scenario: Create all objects required if AUTO is found for NaturalId
    Given the following detail, generate all necessary objects
      | Name  | NaturalId | Type | DOD      | Nationality       | CitizenshipRenunciationDate | AIStatus           | AIYear |
      | Muthu | AUTO      | FIN  | 20201212 | SINGAPORE_CITIZEN | 20191010                    | PENDING_ASSESSMENT | 2018   |

    Then check the nric in PersonId object is valid

  Scenario Outline: Create all objects required if dash is found for CitizenshipRenunciationDate
    Given the following detail, generate all necessary objects
      | Name  | NaturalId | Type | DOD      | Nationality       | CitizenshipRenunciationDate   | AIStatus           | AIYear |
      | Muthu | AUTO      | FIN  | 20201212 | SINGAPORE_CITIZEN | <CitizenshipRenunciationDate> | PENDING_ASSESSMENT | 2018   |

    Then check the renunciation date in Nationality object is equals to <CitizenshipRenunciationDate>
    Examples:
      | CitizenshipRenunciationDate |
      | 20191010                    |
      | 20191011                    |
      | -                           |

  Scenario Outline: Create all objects required if dash is found for AIStatus
    Given the following detail, generate all necessary objects
      | Name  | NaturalId | Type | DOD      | Nationality       | CitizenshipRenunciationDate | AIStatus   | AIYear   |
      | Muthu | AUTO      | FIN  | 20201212 | SINGAPORE_CITIZEN | 20191010                    | <AIStatus> | <AIYear> |

    Then check the renunciation date in Income object is equals to <AIStatus>
    Then check the income year in Income object is equals to <AIYear>
    Examples:
      | AIStatus           | AIYear |
      | PENDING_ASSESSMENT | 2020   |
      | INCOME_ASSESSED    | 2019   |
      | -                  | -      |
