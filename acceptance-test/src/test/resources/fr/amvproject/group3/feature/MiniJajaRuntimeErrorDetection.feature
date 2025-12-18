# Created by clonestriker at 20/11/2025
Feature: Minijaja runtime error detection
  Vérifie que l'interpréteur Minijaja lève les erreurs d'exécution attendues

  Scenario: Division par zéro déclenche une erreur d'exécution
    Given Un programme Minijaja du fichier error_division_zero.mjj
    When J'essaie d'interpreter le programme minijaja
    Then une erreur d'interpretation est levee
    And l'erreur d'interpretation est de type runtime
    And l'erreur d'interpretation contient "division by zero"

  Scenario: Boucle infinie détectée
    Given Un programme Minijaja du fichier error_loop.mjj
    When J'essaie d'interpreter le programme minijaja
    Then une erreur d'interpretation est levee
    And l'erreur d'interpretation est de type runtime
    And l'erreur d'interpretation contient "Infinite loop detected"
