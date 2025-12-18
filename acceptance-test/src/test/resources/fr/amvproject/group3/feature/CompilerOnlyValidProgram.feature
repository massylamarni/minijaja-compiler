Feature: Compilator Only - Verify compilation succeeds for valid programs

  @compilation
  Scenario Outline: Le compilateur produit un jajaCode compilable pour le fichier <fichier>
    Given Un programme Minijaja du fichier <fichier>
    When Compilation du programme
    Then La compilation doit réussir

    Examples:
      | fichier         |
      | 0.mjj           |
      | 1.mjj           |
      | fact.mjj        |
      | quick_sort.mjj  |
      | synonymie.mjj   |
      | tas.mjj         |
