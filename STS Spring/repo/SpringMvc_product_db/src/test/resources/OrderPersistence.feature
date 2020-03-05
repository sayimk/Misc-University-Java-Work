Feature: Order.Persistence
  I want to persist orders for accounting purposes

  @repo
  Scenario: Order.Repo.Create
    When I add a new order using "/order/add"
    Then a new order is stored in the repository 

  @repo
  Scenario: Order.Repo.Edit
    Given I have a product with name "Banana", description "yellow" and price "0.16" in the catalogue
    And an order X without any items 
    When I do a post "/item/add" for the order X with an item with product "Banana" and amount "4"
    Then the order X contains an item with "4" products "Banana" and the total cost of the order is "0.64"

  @repo
  Scenario: Order.Repo.Delete
    Given an order X without any items 
    When I delete the order X using "/order/delete"
    Then The order X no longer exists in the repository

  @repo
  Scenario: Item.Repo.Create
    Given I have a product with name "Banana", description "yellow" and price "0.16" in the catalogue
    And an order X without any items 
    When I do a post "/item/add" for the order X with an item with product "Banana" and amount "2"
    Then the order X contains an item with "2" products "Banana" and the total cost of the order is "0.32"

  @repo
  Scenario: Item.Repo.Edit
    Given I have a product with name "Banana", description "yellow" and price "0.16" in the catalogue
    And an order X without any items 
    When I do a post "/item/add" for the order X with an item with product "Banana" and amount "4"
    Then the order X contains an item with "4" products "Banana" and the total cost of the order is "0.64"

  @repo
  Scenario: Item.Repo.Delete
    Given I have a product with name "Banana", description "yellow" and price "0.16" in the catalogue
    And an order X with with an item with product "Banana" and amount "2" 
    When I do a get "/item/delete" for the order X and item containing "Banana"
    Then the order X does not contain the item with "Banana" and the total cost of the order is "0.0"

        