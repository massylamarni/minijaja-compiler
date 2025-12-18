@jajacode @runtime
Feature: Jajacode Runtime Error Detection
  Tests pour détecter les erreurs d'exécution (division par zéro, boucles infinies, débordement de pile, etc.) sur la version JajaCode compilée.

  Scenario Outline: L'exécution du programme <file> déclenche une erreur d'exécution
    Given Un programme Minijaja du fichier <file>
    When Compilation du programme
    When J'essaie d'exécuter le programme compilé
    Then Une erreur d'exécution doit être détectée

  Examples:
    | file                         |
    | error_division_zero.mjj      |
    | error_loop.mjj               |
    | stack_overflow.mjj           |
