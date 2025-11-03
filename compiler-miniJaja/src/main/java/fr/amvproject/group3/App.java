package fr.amvproject.group3;

import fr.amvproject.group3.ast.*;
import org.antlr.v4.runtime.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        String inputFile = "compiler-miniJaja/test-cours.mj";
        if (args.length > 0) inputFile = args[0];

        try {
            // vérifie que le fichier existe
            if (!Files.exists(Paths.get(inputFile))) {
                System.err.println("Fichier introuvable : " + inputFile);

                return;
            }

            // 1) lecture + lexer + parser
            CharStream input = CharStreams.fromFileName(inputFile);
            MiniJajaLexer lexer = new MiniJajaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MiniJajaParser parser = new MiniJajaParser(tokens);

            // optionnel : activer un error listener personnalisé si besoin
            parser.removeErrorListeners();
            parser.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg, RecognitionException e) {
                    System.err.printf("Erreur syntaxe (%d:%d) : %s%n", line, charPositionInLine, msg);
                }
            });

            // 2) parse
            MiniJajaParser.ProgramContext tree = null;
            try {
                tree = parser.program();
            } catch (Exception ex) {
                System.err.println("Erreur lors de l'analyse syntaxique : " + ex.getMessage());
            }

            // si le parse a produit des erreurs, signale et stoppe proprement
            if (parser.getNumberOfSyntaxErrors() > 0 || tree == null) {
                System.err.println("Le parse a échoué : nombre d'erreurs = " + parser.getNumberOfSyntaxErrors());
                System.err.println("Vérifie la syntaxe du fichier " + inputFile);
                return;
            }

            // 3) construire l'AST
            MiniJajaAstBuilder builder = new MiniJajaAstBuilder();
            AstNode ast = builder.visit(tree); // ou builder.parse(...) selon votre API

            if (ast == null) {
                System.err.println("Le constructeur d'AST a renvoyé null.");
                return;
            }

            // (optionnel) afficher l'AST pour debug :
            // PrintVisitor printer = new PrintVisitor();
            // System.out.println(ast.accept(printer));

            // 4) compiler vers JajaCode
            CodeEmitter emitter = new CodeEmitter();
            LabelManager labels = new LabelManager();
            JajaCodeCompiler compiler = new JajaCodeCompiler(emitter, labels);
            PrintVisitor printer = new PrintVisitor();
            System.out.println(ast.accept(printer));
            ast.accept(compiler);

            // 5) afficher le JajaCode
            System.out.println("\n===== JajaCode généré =====\n");
            emitter.print();
            System.out.println("\n===== Fin =====");

            // 6) simple vérif d'usage de variables non déclarées (optionnel)
            // Si tu veux, on peut ajouter un TypeChecker/Environment pour lever des erreurs plus tôt.

        } catch (Exception e) {
            System.err.println("Exception lors de l'exécution : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
