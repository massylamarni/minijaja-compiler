# Created by Gemini at 17/12/2025
Feature: Minijaja interpret invalid program

  Scenario: L'interpreteur Minijaja avec un invalide programme
    Given Un programme Minijaja du fichier error_syntaxe.mjj
    When J'essaie d'interpreter le programme minijaja
    Then une erreur d'interpretation est levee

    Scenario: L'interpreteur Minijaja avec un invalide programme
    Given Un programme Minijaja du fichier error_return_type.mjj
    When J'essaie d'interpreter le programme minijaja
    Then une erreur d'interpretation est levee

    Scenario: L'interpreteur Minijaja avec un invalide programme
    Given Un programme Minijaja du fichier error_type.mjj
    When J'essaie d'interpreter le programme minijaja
    Then une erreur d'interpretation est levee

    Scenario: L'interpreteur Minijaja avec un invalide programme
    Given Un programme Minijaja du fichier error_operation_invalid.mjj
    When J'essaie d'interpreter le programme minijaja
    Then une erreur d'interpretation est levee

    Scenario: L'interpreteur Minijaja avec un invalide programme
    Given Un programme Minijaja du fichier error_duplicate.mjj
    When J'essaie d'interpreter le programme minijaja
    Then une erreur d'interpretation est levee

    Scenario: L'interpreteur Minijaja avec un invalide programme
    Given Un programme Minijaja du fichier error_undefined_var.mjj
    When J'essaie d'interpreter le programme minijaja
    Then une erreur d'interpretation est levee


