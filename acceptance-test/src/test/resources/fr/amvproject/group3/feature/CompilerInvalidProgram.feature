
@invalid
Feature: Compilator Test Jajacode Error


  Scenario Outline: Le compilateur produit une erreur pour le fichier <file>
    Given Un programme Minijaja du fichier <file>
    When J'essaie de compiler le programme
    Then Une erreur est donnée

 Examples:
    | file                |
    | error_syntaxe.mjj    |
    | error_type.mjj      |
    | error_undefined_var.mjj   |
    | error_main.mjj    |
    | error_duplicate.mjj      |
    | error_return_type.mjj   |
    | error_operation_invalid.mjj    |