Feature: Minijaja interpret invalid program


    Scenario: L'interpreteur Minijaja avec un invalide programme
    Given Un programme Minijaja du fichier error_operation_invalid.mjj
    When J'essaie d'interpreter le programme minijaja
    Then une erreur d'interpretation est levee

