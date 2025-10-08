package fr.amvproject.group3.ast.ast;

import fr.amvproject.group3.MiniJajaLexer;
import fr.amvproject.group3.MiniJajaParser;
import org.antlr.v4.runtime.*;

public class MainInterpreter {

    public static void main(String[] args) throws Exception {
        // Vérifier les arguments
        if (args.length == 0) {
            System.err.println("Usage: java MainInterpreter <fichier.mj> [--debug] [--show-ast] [--show-memory]");
            System.err.println("\nOptions:");
            System.err.println("  --debug        : Affiche les étapes d'exécution");
            System.err.println("  --show-ast     : Affiche l'AST avant exécution");
            System.err.println("  --show-memory  : Affiche l'état de la mémoire après exécution");
            System.exit(1);
        }

        String filename = args[0];
        boolean debug = false;
        boolean showAST = false;
        boolean showMemory = false;

        // Parser les options
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--debug")) debug = true;
            if (args[i].equals("--show-ast")) showAST = true;
            if (args[i].equals("--show-memory")) showMemory = true;
        }

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   Interpréteur MiniJaja               ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Fichier : " + filename);
        System.out.println();

        try {
            // ===== PHASE 1 : PARSING =====
            System.out.println("⏳ Phase 1/3 : Analyse lexicale et syntaxique...");

            CharStream input = CharStreams.fromFileName(filename);
            MiniJajaLexer lexer = new MiniJajaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MiniJajaParser parser = new MiniJajaParser(tokens);

            // Capturer les erreurs
            parser.removeErrorListeners();
            ErrorListener errorListener = new ErrorListener();
            parser.addErrorListener(errorListener);

            MiniJajaParser.ProgramContext tree = parser.program();

            if (errorListener.hasErrors()) {
                System.err.println("❌ Erreurs de parsing détectées :");
                for (String error : errorListener.getErrors()) {
                    System.err.println("   " + error);
                }
                System.exit(1);
            }

            System.out.println("✓ Parsing réussi\n");

            // ===== PHASE 2 : CONSTRUCTION DE L'AST =====
            System.out.println("⏳ Phase 2/3 : Construction de l'AST...");

            MiniJajaASTBuilder builder = new MiniJajaASTBuilder();
            ASTNode ast = builder.visit(tree);

            System.out.println("✓ AST construit\n");

            if (showAST) {
                System.out.println("╔════════════════════════════════════════╗");
                System.out.println("║   AST Généré                          ║");
                System.out.println("╚════════════════════════════════════════╝");
                System.out.println(formatAST(ast.toString()));
                System.out.println();
            }

            // ===== PHASE 3 : INTERPRÉTATION =====
            System.out.println("⏳ Phase 3/3 : Exécution du programme...");
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║   Sortie du Programme                 ║");
            System.out.println("╚════════════════════════════════════════╝");

            MiniJajaInterpreter interpreter = new MiniJajaInterpreter(debug);

            long startTime = System.currentTimeMillis();
            interpreter.execute(ast);
            long endTime = System.currentTimeMillis();

            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   Exécution Terminée                  ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("✓ Programme exécuté avec succès");
            System.out.println("⏱  Temps d'exécution : " + (endTime - startTime) + " ms");

            if (showMemory) {
                interpreter.printMemoryState();
            }

        } catch (RuntimeException e) {
            System.err.println("\n╔════════════════════════════════════════╗");
            System.err.println("║   Erreur d'exécution                  ║");
            System.err.println("╚════════════════════════════════════════╝");
            System.err.println("❌ " + e.getMessage());
            if (debug) {
                e.printStackTrace();
            }
            System.exit(1);
        } catch (Exception e) {
            System.err.println("\n╔════════════════════════════════════════╗");
            System.err.println("║   Erreur Inattendue                   ║");
            System.err.println("╚════════════════════════════════════════╝");
            System.err.println("❌ " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Formater l'AST pour un affichage lisible
    private static String formatAST(String ast) {
        StringBuilder formatted = new StringBuilder();
        int indent = 0;
        boolean inString = false;

        for (int i = 0; i < ast.length(); i++) {
            char c = ast.charAt(i);

            if (c == '"') {
                inString = !inString;
                formatted.append(c);
            } else if (!inString && c == '(') {
                formatted.append("\n");
                for (int j = 0; j < indent; j++) formatted.append("  ");
                formatted.append(c);
                indent++;
            } else if (!inString && c == ')') {
                indent--;
                formatted.append(c);
            } else {
                formatted.append(c);
            }
        }

        return formatted.toString();
    }

    // Classe pour capturer les erreurs de parsing
    static class ErrorListener extends BaseErrorListener {
        private boolean hasErrors = false;
        private java.util.List<String> errors = new java.util.ArrayList<>();

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg,
                                RecognitionException e) {
            hasErrors = true;
            errors.add("Ligne " + line + ":" + charPositionInLine + " - " + msg);
        }

        public boolean hasErrors() {
            return hasErrors;
        }

        public java.util.List<String> getErrors() {
            return errors;
        }
    }
}