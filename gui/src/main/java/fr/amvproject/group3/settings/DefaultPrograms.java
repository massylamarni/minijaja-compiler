package fr.amvproject.group3.settings;

public class DefaultPrograms {
  public static final String mjjProgram = """
        /********************************************/
        /* Programme MiniJaja                       */
        /* Validation de toutes les instructions	*/
        /* sans tableau                             */
        /********************************************/

        class C {
          int nt[3] ;
          int i = 0 ;

          main {
            while (3 > i) {
              nt[i] = i * 10 ;
              i++ ;
            };
            write(nt[2]);
          }
        }
      """;
  public static final String jjcProgram = """
        1 init
        2 push(0)
        3 new(x,entier,var,0)
        4 push(7)
        5 new(f,entier,meth,0)
        6 goto(11)
        7 new(p,entier,var,1)
        8 load(p) // val. résultat en sommet de pile
        9 swap // adr retour ↔ val. résultat
        10 return
        11 push(3)
        12 store(x)
        13 load(x)
        14 invoke(f)
        15 swap // retrait du paramètre
        16 pop
        17 inc(x)
        18 push(0) //valeur pour les méthodes void
        19 swap // retrait de f
        20 pop
        21 swap // retrait de x
        22 pop
        23 pop // retrait 0 du main
        24 jcstop
      """;

  public static final String newMjjProgram = """
      
      """;

  public static final String newJjcProgram = """
      
      """;
}
