Feature: Product.Persistence
  I want to create and manage products for my catalogue

  @repo
  Scenario: Product.Repo.Create
    Given I have a product X with name "Banana", description "yellow" and price "0.16"
    When I add the product using "/product/add"
    Then a new product is stored in the repository 

  @repo
  Scenario: Product.Repo.Edit
    Given I have a product X with name "Banana", description "yellow" and price "0.16" in the catalogue
    When I do a post "/product/add" for the product X description "green, ripen at home" 
    Then the product  "Banana" contains the description "green, ripen at home"

  @repo
  Scenario: Product.Repo.Delete
    Given I have a product X with name "Banana", description "yellow" and price "0.16" in the catalogue
    When I delete the product X using "/product/delete"
    Then The product "Banana" no longer exists in the repository

        