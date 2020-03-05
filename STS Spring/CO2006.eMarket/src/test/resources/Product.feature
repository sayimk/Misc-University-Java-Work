Feature: Product Management
  I want to create and manage products for using them in my orders

  @navigation
  Scenario: Product.Navigation.Post 
    When I do a post "/product/add" with id "0", name "Banana", description "yellow" and price "0.16"
    Then I should see this view "form/productMaster"

  @navigation
  Scenario Outline: Product.Navigation.Get
    When I do a get <url>
    Then I should see this view <view>

    Examples: 
      | url                       | view                 |
      | "/product/"               | "form/productMaster" |
      | "/product/productDetail" | "form/productDetail" |
      | "/product/delete/" | "form/productMaster" |

  @logic
  Scenario: Product.Logic.Create
    When I add a product with id "0", name "Banana", description "yellow" and price "0.16" using "/product/add"
    Then the product stored with id "0" corresponds to name "Banana", description "yellow" and price "0.16"

  @logic
  Scenario: Product.Logic.Create
    When I add a product with id "0", name "Banana", description "yellow" and price "-0.16" using "/product/add"
    Then I should get an exception 

  @logic
  Scenario: Product.Logic.Create
    When I add a product with id "0", name "", description "yellow" and price "0.16" using "/product/add"
    Then I should get an exception

  @logic
  Scenario: Product.Logic.Edit
    Given I have a product with id "0", name "Banana", description "yellow" and price "0.16" in the catalogue
    When I edit description to "green" using "/product/add"
    Then the description should be "green" for product with id "0" in the catalogue

  @logic
  Scenario: Product.Logic.Delete
    Given I have a product with id "0", name "Banana", description "yellow" and price "0.16" in the catalogue
    When I delete the product with id "0" using "/product/delete"
    Then the product with id "0" no longer exists in the catalogue
