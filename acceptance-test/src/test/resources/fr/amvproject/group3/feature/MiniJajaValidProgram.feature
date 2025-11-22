# Created by clonestriker at 20/11/2025
Feature: Minijaja interpret valid program


  Scenario: L'interpreteur Minijaja produit un résultat correct pour le fichier 0.mjj
    Given Un programme Minijaja du fichier 0.mjj
    When Interpretation du programme minijaja
    Then le résultat final doit être correct

  Scenario: L'interpreteur Minijaja produit un résultat correct pour le fichier 1.mjj
    Given Un programme Minijaja du fichier 1.mjj
    When Interpretation du programme minijaja
    Then le résultat final doit être correct


  Scenario: L'interpreteur Minijaja produit un résultat correct pour le fichier fact.mjj
    Given Un programme Minijaja du fichier fact.mjj
    When Interpretation du programme minijaja
    Then le résultat final doit être correct

  Scenario: L'interpreteur Minijaja produit un résultat correct pour le fichier quick_sort.mjj
    Given Un programme Minijaja du fichier quick_sort.mjj
    When Interpretation du programme minijaja
    Then le résultat final doit être correct

  Scenario: L'interpreteur Minijaja produit un résultat correct pour le fichier synonymie.mjj
    Given Un programme Minijaja du fichier synonymie.mjj
    When Interpretation du programme minijaja
    Then le résultat final doit être correct

  Scenario: L'interpreteur Minijaja produit un résultat correct pour le fichier tas.mjj
    Given Un programme Minijaja du fichier tas.mjj
    When Interpretation du programme minijaja
    Then le résultat final doit être correct
