 @jajacode @invalid
 Feature: Jajacode Invalid Bytecode
   Vérifie que l'interpréteur JajaCode rejette bien le bytecode invalide.

   Scenario Outline: L'interpréteur doit échouer sur du JajaCode invalide <file>
     Given Un programme JajaCode du fichier <file>
     When J'essaie d'exécuter le programme compilé
     Then Une erreur de compilation doit être détectée

   Examples:
     | file            |
    | InvalideJcc.jjc |
