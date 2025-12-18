# Created by clonestriker at 20/11/2025
Feature: Comparaison result of interpretation Minijaja and jajacode
  # Enter feature description here
  Scenario: Le compilateur produit un jajaCode exécutable pour le fichier 0.mjj
    Given Un programme Minijaja du fichier 0.mjj
    When Compilation du programme
    And Execution du programme compilé
    And Interpretation du programme minijaja
    Then Le résultat des deux exécutions doit être similaire

  Scenario: Le compilateur produit un jajaCode exécutable pour le fichier 1.mjj
    Given Un programme Minijaja du fichier 1.mjj
    When Compilation du programme
    And Execution du programme compilé
    And Interpretation du programme minijaja
    Then Le résultat des deux exécutions doit être similaire


  Scenario: Le compilateur produit un jajaCode exécutable pour le fichier fact.mjj
    Given Un programme Minijaja du fichier fact.mjj
    When Compilation du programme
    And Execution du programme compilé
    And Interpretation du programme minijaja
    Then Le résultat des deux exécutions doit être similaire

  Scenario: Le compilateur produit un jajaCode exécutable pour le fichier quick_sort.mjj
    Given Un programme Minijaja du fichier quick_sort.mjj
    When Compilation du programme
    And Execution du programme compilé
    And Interpretation du programme minijaja
    Then Le résultat des deux exécutions doit être similaire

  Scenario: Le compilateur produit un jajaCode exécutable pour le fichier synonymie.mjj
    Given Un programme Minijaja du fichier synonymie.mjj
    When Compilation du programme
    And Execution du programme compilé
    And Interpretation du programme minijaja
    Then Le résultat des deux exécutions doit être similaire

  Scenario: Le compilateur produit un jajaCode exécutable pour le fichier tas.mjj
    Given Un programme Minijaja du fichier tas.mjj
    When Compilation du programme
    And Execution du programme compilé
    And Interpretation du programme minijaja
    Then Le résultat des deux exécutions doit être similaire
