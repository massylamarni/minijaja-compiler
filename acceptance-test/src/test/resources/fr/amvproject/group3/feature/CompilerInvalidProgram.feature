# Created by clonestriker at 20/11/2025
Feature: Compilator Test Jajacode Error
  # Enter feature description here

  Scenario: Le compilateur produit une erreur pour le fichier .....mjj
    Given Un programme Minijaja du fichier .....mjj
    When Compilation du programme
    Then Une erreur est donnée

