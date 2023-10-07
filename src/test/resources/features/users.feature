@Users
Feature: Create user
  Scenario: Create user and save to database
    Given I create an user with id "1223", name "DJ" and address "Bangalore"
    When I search for that user by the id "1223"
    Then I should find at least one result with id "1223"
