Feature: Order Management
  I want to create and manage orders for accounting purposes

  @navigation
  Scenario Outline: Order.Navigation.Get
    When I do a get <url>
    Then I should see this view <view>

    Examples: 
      | url              | view               |
      | "/order/"        | "form/orderMaster" |
      | "/order/add/"    | "form/orderDetail" |
      | "/order/delete/" | "form/orderMaster" |
      
  @logic
  Scenario: Order.Logic.Create
    When I add a new order using "/order/add"
    Then a new order is stored in the system 

  @logic
  Scenario: Order.Logic.Delete
    Given I have an order with id "0" in the catalogue
    When I delete the order with id "0" using "/order/delete"
    Then The order with id "0" no longer exists in the catalogue

  @navigation
  Scenario: Item.Navigation.Post 
    Given I have a product with id "0", name "Banana", description "yellow" and price "0.16" in the catalogue
    And an order with id "0"
    When I do a post "/item/add" for the order with id "0" with an item with id "0" with product id "0" and amount "2"
    Then I should see this view "form/orderDetail"

  @navigation
  Scenario Outline: Item.Navigation.Get
    Given I have a product with id "0", name "Banana", description "yellow" and price "0.16" in the catalogue
    And an order with id "0"
    When I do a get <url>
    Then I should see this view <view>

    Examples: 
      | url                      | view              |
      | "/item/detail?orderId=0" | "form/itemDetail" |
      | "/item/delete?orderId=0" | "form/orderDetail" |

  @logic
  Scenario: Item.Logic.Create
    Given I have a product with id "0", name "Banana", description "yellow" and price "0.16" in the catalogue
    And an order with id "0"
    When I do a post "/item/add" for the order with id "0" with an item with id "0" with product id "0" and amount "2"
    Then the order with id "0" contains an item with id "0", "2" products of id "0" and the total cost of the order is "0.32"

  @logic
  Scenario: Item.Logic.Create.Exception
    Given I have a product with id "0", name "Banana", description "yellow" and price "0.16" in the catalogue
    And an order with id "0"
    When I do a post "/item/add" for the order with id "0" with an item with id "0" with product id "0" and amount "-2"
    Then I should get an exception

  @logic
  Scenario: Item.Logic.Edit
    Given I have a product with id "0", name "Banana", description "yellow" and price "0.16" in the catalogue
    And an order with id "0" with an item with id "0", amount "2" and product id "0" 
    When I do a post "/item/add" for the order with id "0" with an item with id "0" with product id "0" and amount "4"
    Then the order with id "0" contains an item with id "0", "4" products of id "0" and the total cost of the order is "0.64"

  @logic
  Scenario: Item.Logic.Edit.Exception
    Given I have a product with id "0", name "Banana", description "yellow" and price "0.16" in the catalogue
    And an order with id "0" with an item with id "0", amount "2" and product id "0" 
    When I do a post "/item/add" for the order with id "0" with an item with id "0" with product id "0" and amount "-4"
    Then I should get an exception

  @logic
  Scenario: Item.Logic.Delete
    Given I have a product with id "0", name "Banana", description "yellow" and price "0.16" in the catalogue
    And an order with id "0" with an item with id "0", amount "2" and product id "0" 
    When I do a get "/item/delete" for the order with id "0" and item with id "0"
    Then the order with id "0" does not contain item with id "0" and the total cost of the order is "0.0"

        