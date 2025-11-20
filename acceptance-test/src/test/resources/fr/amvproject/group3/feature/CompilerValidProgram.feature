# Created by clonestriker at 18/11/2025
Feature: Compilator Test Jajacode Executable
  # Enter feature description here

  Scenario: Le compilateur produit un jajaCode exécutable pour le fichier 0.mjj
    Given Un programme Minijaja du fichier 0.mjj
    When Compilation du programme
    And Execution du programme compilé
    Then le résultat final doit être correct

  Scenario: Le compilateur produit un jajaCode exécutable pour le fichier 1.mjj
    Given Un programme Minijaja du fichier 1.mjj
    When Compilation du programme
    And Execution du programme compilé
    Then le résultat final doit être correct


  Scenario: Le compilateur produit un jajaCode exécutable pour le fichier fact.mjj
    Given Un programme Minijaja du fichier fact.mjj
    When Compilation du programme
    And Execution du programme compilé
    Then le résultat final doit être correct

  Scenario: Le compilateur produit un jajaCode exécutable pour le fichier quick_sort.mjj
    Given Un programme Minijaja du fichier quick_sort.mjj
    When Compilation du programme
    And Execution du programme compilé
    Then le résultat final doit être correct

  Scenario: Le compilateur produit un jajaCode exécutable pour le fichier synonymie.mjj
    Given Un programme Minijaja du fichier synonymie.mjj
    When Compilation du programme
    And Execution du programme compilé
    Then le résultat final doit être correct

  Scenario: Le compilateur produit un jajaCode exécutable pour le fichier tas.mjj
    Given Un programme Minijaja du fichier tas.mjj
    When Compilation du programme
    And Execution du programme compilé
    Then le résultat final doit être correct
