Feature: Place order flows for AEO.

  Scenario: Verify successful place order flow when user enters valid card details.
    Given I am on AEO website
    When I search with "jeans" keyword
    And I click on the first product
    And I select the first available color
    And I add the item to my cart
    And I checkout from shopping bag page
    And I enter valid shipping address
    And I enter "valid" credit card details
    And I place the order
    Then I should see that order placed successfully

  Scenario: Verify the error message on checkout when user place an order with invalid credit card.
    Given I am on AEO website
    When I search with "jeans" keyword
    And I add the item to my cart
    And I checkout from shopping bag page
    And I enter valid shipping address
    And I enter "invalid" credit card details
    And I place the order
    Then I should see the error message "Sorry, we don't accept this card type. Please use a different card or payment method."

  Scenario: Verify that user should be able to place order with gift card
    Given I am on AEO website
    When I search with "jeans" keyword
    And I click on the first product
    And I select the first available color
    And I add the item to my cart
    And I checkout from shopping bag page
    And I enter valid shipping address
    And I enter gift card as the payment type
    And I place the order
    Then I should see that order placed successfully