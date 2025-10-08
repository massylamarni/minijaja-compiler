package fr.amvproject.group3.ast.ast;

import java.util.*;

public class MiniJajaInterpreter {

    // ========== MÉMOIRE ==========
    private Map<String, Object> memory = new HashMap<>();           // Variables globales et locales
    private Map<String, int[]> arrays = new HashMap<>();            // Tableaux
    private Map<String, Methode> methods = new HashMap<>();         // Méthodes définies
    private Stack<Map<String, Object>> callStack = new Stack<>();   // Pile d'appels pour les méthodes

    // ========== ÉTAT D'EXÉCUTION ==========
    private Object returnValue = null;      // Valeur de retour d'une méthode
    private boolean hasReturned = false;    // Flag pour gérer les return

    // ========== MODE DEBUG ==========
    private boolean debugMode = false;

    public MiniJajaInterpreter() {
        this(false);
    }

    public MiniJajaInterpreter(boolean debug) {
        this.debugMode = debug;
    }

    // ========== POINT D'ENTRÉE ==========
    public void execute(ASTNode ast) {
        if (ast instanceof Classe) {
            executeClasse((Classe) ast);
        } else {
            throw new RuntimeException("Le nœud racine doit être une Classe");
        }
    }

    // ========== EXÉCUTION DE LA CLASSE ==========
    private void executeClasse(Classe classe) {
        debug("=== Exécution de la classe " + classe.ident.name + " ===");

        // 1. Charger les méthodes définies dans la classe
        for (Decl decl : classe.decls.decls) {
            if (decl instanceof Methode) {
                Methode method = (Methode) decl;
                methods.put(method.name.name, method);
                debug("Méthode chargée : " + method.name.name);
            }
        }

        // 2. Initialiser les variables globales
        for (Decl decl : classe.decls.decls) {
            if (!(decl instanceof Methode)) {
                initializeDecl(decl);
            }
        }

        // 3. Exécuter le main
        debug("\n=== Exécution du main ===");
        executeMainBlock(classe.main);

        debug("\n=== Fin de l'exécution ===");
    }

    // ========== EXÉCUTION DU MAIN ==========
    private void executeMainBlock(MainBlock main) {
        // Initialiser les variables locales du main
        for (Decl decl : main.decls.decls) {
            initializeDecl(decl);
        }

        // Exécuter les instructions
        for (Instr instr : main.instrs.list) {
            executeInstr(instr);
            if (hasReturned) break;  // Si return dans le main
        }
    }

    // ========== INITIALISATION DES DÉCLARATIONS ==========
    private void initializeDecl(Decl decl) {
        String varName = decl.ident.name;

        // Tableau
        if (decl instanceof Tableau) {
            Tableau tableau = (Tableau) decl;
            int size = (Integer) evaluateExp(tableau.taille);
            arrays.put(varName, new int[size]);
            debug("Tableau créé : " + varName + "[" + size + "]");
            return;
        }

        // Variable avec initialisation
        if (decl.exp != null) {
            Object value = evaluateExp(decl.exp);
            memory.put(varName, value);
            debug("Variable initialisée : " + varName + " = " + value);
        } else {
            // Variable sans initialisation : valeur par défaut
            Object defaultValue = getDefaultValue(decl.type);
            memory.put(varName, defaultValue);
            debug("Variable déclarée : " + varName + " = " + defaultValue + " (défaut)");
        }
    }

    private Object getDefaultValue(String type) {
        if (type.equals("int") || type.startsWith("int")) {
            return 0;
        } else if (type.equals("boolean")) {
            return false;
        }
        return null;
    }

    // ========== EXÉCUTION DES INSTRUCTIONS ==========
    private void executeInstr(Instr instr) {
        if (hasReturned) return;  // Ne rien faire après un return

        // Affectation simple
        if (instr instanceof Affectation) {
            Affectation aff = (Affectation) instr;
            Object value = evaluateExp(aff.exp);
            memory.put(aff.ident.name, value);
            debug("Affectation : " + aff.ident.name + " = " + value);
            return;
        }

        // Affectation tableau
        if (instr instanceof AffectationTableau) {
            AffectationTableau arrAff = (AffectationTableau) instr;
            String arrayName = arrAff.ident.name;
            int index = (Integer) evaluateExp(arrAff.index);
            Object value = evaluateExp(arrAff.valeur);

            if (!arrays.containsKey(arrayName)) {
                throw new RuntimeException("Tableau '" + arrayName + "' non déclaré");
            }

            int[] array = arrays.get(arrayName);
            if (index < 0 || index >= array.length) {
                throw new RuntimeException("Indice hors limites : " + index + " (taille: " + array.length + ")");
            }

            array[index] = (Integer) value;
            debug("Affectation tableau : " + arrayName + "[" + index + "] = " + value);
            return;
        }

        // Somme (équivalent de +=)
        if (instr instanceof Somme) {
            Somme somme = (Somme) instr;
            String varName = somme.ident.name;

            if (!memory.containsKey(varName)) {
                throw new RuntimeException("Variable '" + varName + "' non déclarée");
            }

            int currentValue = (Integer) memory.get(varName);
            int addValue = (Integer) evaluateExp(somme.exp);
            memory.put(varName, currentValue + addValue);
            debug("Somme : " + varName + " += " + addValue + " = " + (currentValue + addValue));
            return;
        }

        // Incrémentation
        if (instr instanceof Incrementation) {
            Incrementation incr = (Incrementation) instr;
            String varName = incr.ident.name;

            if (!memory.containsKey(varName)) {
                throw new RuntimeException("Variable '" + varName + "' non déclarée");
            }

            int currentValue = (Integer) memory.get(varName);
            memory.put(varName, currentValue + 1);
            debug("Incrémentation : " + varName + " = " + (currentValue + 1));
            return;
        }

        // If
        if (instr instanceof IfInstr) {
            IfInstr ifInstr = (IfInstr) instr;
            boolean condition = (Boolean) evaluateExp(ifInstr.cond);
            debug("Condition IF : " + condition);

            if (condition) {
                debug("Exécution du THEN");
                for (Instr i : ifInstr.thenInstrs.list) {
                    executeInstr(i);
                    if (hasReturned) break;
                }
            } else if (!ifInstr.elseInstrs.list.isEmpty()) {
                debug("Exécution du ELSE");
                for (Instr i : ifInstr.elseInstrs.list) {
                    executeInstr(i);
                    if (hasReturned) break;
                }
            }
            return;
        }

        // While
        if (instr instanceof WhileInstr) {
            WhileInstr whileInstr = (WhileInstr) instr;
            int iterations = 0;

            while ((Boolean) evaluateExp(whileInstr.cond)) {
                debug("Itération WHILE #" + (++iterations));

                for (Instr i : whileInstr.body.list) {
                    executeInstr(i);
                    if (hasReturned) break;
                }

                if (hasReturned) break;

                // Sécurité anti-boucle infinie
                if (iterations > 100000) {
                    throw new RuntimeException("Boucle infinie détectée (> 100000 itérations)");
                }
            }
            debug("WHILE terminé après " + iterations + " itérations");
            return;
        }

        // Appel de méthode (instruction)
        if (instr instanceof AppelI) {
            AppelI call = (AppelI) instr;
            executeMethodCall(call.methodName.name, call.args);
            return;
        }

        // Return
        if (instr instanceof Retour) {
            Retour ret = (Retour) instr;
            returnValue = evaluateExp(ret.exp);
            hasReturned = true;
            debug("Return : " + returnValue);
            return;
        }

        // Write / Writeln
        if (instr instanceof Ecrire) {
            Ecrire write = (Ecrire) instr;

            if (write.contenu instanceof IdentExp) {
                IdentExp id = (IdentExp) write.contenu;
                Object value = memory.get(id.ident.name);
                System.out.print(value);
            } else if (write.contenu instanceof Chaine) {
                String str = ((Chaine) write.contenu).valeur;
                System.out.print(str);
            } else if (write.contenu instanceof AccesTableau) {
                AccesTableau arr = (AccesTableau) write.contenu;
                int index = (Integer) evaluateExp(arr.index);
                int value = arrays.get(arr.ident.name)[index];
                System.out.print(value);
            } else {
                // Pour les expressions générales (ex: 5 + 3)
                Object value = evaluateExp(write.contenu);
                System.out.print(value);
            }

            if (write.avecLn) {
                System.out.println();
            }
            return;
        }
    }

    // ========== ÉVALUATION DES EXPRESSIONS ==========
    private Object evaluateExp(Exp exp) {
        // Entiers
        if (exp instanceof IntExp) {
            return ((IntExp) exp).value;
        }

        // Booléens
        if (exp instanceof BoolExp) {
            return ((BoolExp) exp).value;
        }

        // Chaines
        if (exp instanceof Chaine) {
            return ((Chaine) exp).valeur;
        }

        // Identifiants (variables)
        if (exp instanceof IdentExp) {
            String varName = ((IdentExp) exp).ident.name;

            if (!memory.containsKey(varName)) {
                throw new RuntimeException("Variable '" + varName + "' non déclarée");
            }

            return memory.get(varName);
        }

        // Accès tableau
        if (exp instanceof AccesTableau) {
            AccesTableau arr = (AccesTableau) exp;
            String arrayName = arr.ident.name;
            int index = (Integer) evaluateExp(arr.index);

            if (!arrays.containsKey(arrayName)) {
                throw new RuntimeException("Tableau '" + arrayName + "' non déclaré");
            }

            int[] array = arrays.get(arrayName);
            if (index < 0 || index >= array.length) {
                throw new RuntimeException("Indice hors limites : " + index);
            }

            return array[index];
        }

        // Length (Longueur)
        if (exp instanceof Longueur) {
            String arrayName = ((Longueur) exp).ident.name;

            if (!arrays.containsKey(arrayName)) {
                throw new RuntimeException("Tableau '" + arrayName + "' non déclaré");
            }

            return arrays.get(arrayName).length;
        }

        // Expressions binaires
        if (exp instanceof BinaryExp) {
            BinaryExp binExp = (BinaryExp) exp;
            Object left = evaluateExp(binExp.left);
            Object right = evaluateExp(binExp.right);

            return evaluateBinaryOp(binExp.op, left, right);
        }

        // Expressions unaires
        if (exp instanceof UnaryExp) {
            UnaryExp unExp = (UnaryExp) exp;
            Object value = evaluateExp(unExp.expr);

            return evaluateUnaryOp(unExp.op, value);
        }

        // Appel de méthode (expression)
        if (exp instanceof AppelE) {
            AppelE call = (AppelE) exp;
            return executeMethodCall(call.methodName.name, call.args);
        }

        throw new RuntimeException("Type d'expression non supporté : " + exp.getClass().getName());
    }

    // ========== OPÉRATEURS BINAIRES ==========
    private Object evaluateBinaryOp(String op, Object left, Object right) {
        // Opérateurs arithmétiques
        if (left instanceof Integer && right instanceof Integer) {
            int l = (Integer) left;
            int r = (Integer) right;

            switch (op) {
                case "+": return l + r;
                case "-": return l - r;
                case "*": return l * r;
                case "/":
                    if (r == 0) throw new RuntimeException("Division par zéro");
                    return l / r;
                case "%": return l % r;
                case "<": return l < r;
                case "<=": return l <= r;
                case ">": return l > r;
                case ">=": return l >= r;
                case "==": return l == r;
                case "!=": return l != r;
            }
        }

        // Opérateurs logiques
        if (left instanceof Boolean && right instanceof Boolean) {
            boolean l = (Boolean) left;
            boolean r = (Boolean) right;

            switch (op) {
                case "&&": return l && r;
                case "||": return l || r;
                case "==": return l == r;
                case "!=": return l != r;
            }
        }

        throw new RuntimeException("Opération invalide : " + left + " " + op + " " + right);
    }

    // ========== OPÉRATEURS UNAIRES ==========
    private Object evaluateUnaryOp(String op, Object value) {
        if (op.equals("-") && value instanceof Integer) {
            return -(Integer) value;
        }

        if (op.equals("!") && value instanceof Boolean) {
            return !(Boolean) value;
        }

        throw new RuntimeException("Opération unaire invalide : " + op + " " + value);
    }

    // ========== APPEL DE MÉTHODE ==========
    private Object executeMethodCall(String methodName, List<Exp> args) {
        debug("Appel de méthode : " + methodName);

        if (!methods.containsKey(methodName)) {
            throw new RuntimeException("Méthode '" + methodName + "' non définie");
        }

        Methode method = methods.get(methodName);

        // Vérifier le nombre d'arguments
        if (args.size() != method.params.size()) {
            throw new RuntimeException("Méthode '" + methodName + "' : " +
                    method.params.size() + " arguments attendus, " + args.size() + " fournis");
        }

        // Sauvegarder le contexte actuel
        Map<String, Object> savedMemory = new HashMap<>(memory);
        callStack.push(savedMemory);

        // Créer un nouveau contexte pour la méthode
        memory.clear();

        // Lier les paramètres
        for (int i = 0; i < method.params.size(); i++) {
            Entete param = method.params.get(i);
            Object argValue = evaluateExp(args.get(i));
            memory.put(param.ident.name, argValue);
            debug("Paramètre : " + param.ident.name + " = " + argValue);
        }

        // Initialiser les variables locales
        for (Decl decl : method.vars.decls) {
            initializeDecl(decl);
        }

        // Exécuter le corps de la méthode
        hasReturned = false;
        returnValue = null;

        for (Instr instr : method.instrs.list) {
            executeInstr(instr);
            if (hasReturned) break;
        }

        // Récupérer la valeur de retour
        Object result = returnValue;

        // Restaurer le contexte précédent
        memory = callStack.pop();
        hasReturned = false;
        returnValue = null;

        debug("Méthode " + methodName + " retourne : " + result);
        return result;
    }

    // ========== UTILITAIRES ==========
    private void debug(String message) {
        if (debugMode) {
            System.out.println("[DEBUG] " + message);
        }
    }

    public void printMemoryState() {
        System.out.println("\n=== État de la mémoire ===");
        System.out.println("Variables :");
        for (Map.Entry<String, Object> entry : memory.entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }
        System.out.println("\nTableaux :");
        for (Map.Entry<String, int[]> entry : arrays.entrySet()) {
            System.out.print("  " + entry.getKey() + " = [");
            int[] arr = entry.getValue();
            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i]);
                if (i < arr.length - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
        System.out.println("========================\n");
    }
}