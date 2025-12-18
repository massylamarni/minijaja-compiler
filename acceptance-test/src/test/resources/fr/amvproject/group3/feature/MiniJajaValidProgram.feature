# Created by clonestriker at 20/11/2025
Feature: Minijaja interpret valid program

  Scenario: L'interpreteur Minijaja produit un résultat correct pour le fichier tas.mjj
    Given Un programme Minijaja du fichier tas.mjj
    When Interpretation du programme minijaja
    Then le résultat final doit être correct
