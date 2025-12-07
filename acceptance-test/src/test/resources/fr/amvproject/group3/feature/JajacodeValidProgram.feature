# Created at 25/11/2025
@jajacode @valid
Feature: Jajacode Valid Programs
  Vérifie que l'interpréteur JajaCode exécute correctement les programmes compilés et produit le bon résultat.

  Scenario Outline: Execution Jajacode produit le résultat attendu pour <file>
    Given Un programme Minijaja du fichier <file>
    When Compilation du programme
    And Execution du programme compilé
    Then le résultat final doit être correct jcc <out>

  Examples:
    | file          | out           |
    | minijaja1.mjj | jajacode1.out |
