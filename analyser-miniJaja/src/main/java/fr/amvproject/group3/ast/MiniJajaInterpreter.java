package fr.amvproject.group3.ast;

import java.util.*;

import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.params.*;
import fr.amvproject.group3.ast.nodes.program.*;


public class MiniJajaInterpreter {

    // ========== MÉMOIRE ==========
    private Map<String, Object> memory = new HashMap<>();           // Variables globales et locales
    private Map<String, int[]> arrays = new HashMap<>();            // Tableaux
    private Map<String, MethodeNode> methods = new HashMap<>();         // Méthodes définies
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
    public void execute(AstNode ast) {
        if (ast instanceof ClasseNode) {
            executeClasse((ClasseNode) ast);
        } else {
            throw new RuntimeException("Le nœud racine doit être une Classe");
        }
    }

    // ========== EXÉCUTION DE LA CLASSE ==========
    private void executeClasse(ClasseNode classe) {
        debug("=== Exécution de la classe " + classe.getIdent().getName() + " ===");

        // 1. Charger les méthodes définies dans la classe
        for (DeclNode decl : classe.getDecls().getDecls()) {
            if (decl instanceof MethodeNode) {
                MethodeNode method = (MethodeNode) decl;
                methods.put(method.getIdent().getName(), method);
                debug("Méthode chargée : " + method.getIdent().getName());
            }
        }

        // 2. Initialiser les variables globales
        for (DeclNode decl : classe.getDecls().getDecls()) {
            if (decl instanceof VarNode) {
                initializeDecl(decl);
            }
        }

        // 3. Exécuter le main
        debug("\n=== Exécution du main ===");
        executeMainBlock(classe.getMain());

        debug("\n=== Fin de l'exécution ===");
    }

    // ========== EXÉCUTION DU MAIN ==========
    private void executeMainBlock(MethMainNode main) {
        // Initialiser les variables locales du main
        for (VarNode var : main.getVars().getList()) {
            initializeDecl(var);
        }

        // Exécuter les instructions
        for (InstrNode instr : main.getInstrs().getList()) {
            executeInstr(instr);
            if (hasReturned) break;  // Si return dans le main
        }
    }

    // ========== INITIALISATION DES DÉCLARATIONS ==========
    private void initializeDecl(DeclNode decl) {
        String varName = decl.getIdent().getName();
        VarNode var = (VarNode) decl;

        // Tableau
        if (var.getSizeExp() != null) {
            int size = (Integer) evaluateExp(var.getSizeExp());
            arrays.put(varName, new int[size]);
            debug("Tableau créé : " + varName + "[" + size + "]");
            return;
        }

        // Variable avec initialisation
        if (var.getInitExp() != null) {
            Object value = evaluateExp(var.getInitExp());
            memory.put(varName, value);
            debug("Variable initialisée : " + varName + " = " + value);
        } else {
            // Variable sans initialisation : valeur par défaut
            Object defaultValue = getDefaultValue(var.getType());
            memory.put(varName, defaultValue);
            debug("Variable déclarée : " + varName + " = " + defaultValue + " (défaut)");
        }
    }

    private Object getDefaultValue(TypeNode type) {
        if (type.isInt()) {
            return 0;
        } else if (type.isBoolean()) {
            return false;
        }
        return null;
    }

    // ========== EXÉCUTION DES INSTRUCTIONS ==========
    private void executeInstr(InstrNode instr) {
        if (hasReturned) return;  // Ne rien faire après un return

        // Affectation simple
        if (instr instanceof AssignNode) {
            AssignNode aff = (AssignNode) instr;
            Object value = evaluateExp(aff.getExp());
            memory.put(aff.getIdent().getName(), value);
            debug("Affectation : " + aff.getIdent().getName() + " = " + value);
            return;
        }

        // Affectation tableau
        if (instr instanceof AssignNode) {
            AssignNode arrAff = (AssignNode) instr;
            String arrayName = arrAff.getIdent().getName();
            int index = (Integer) evaluateExp(arrAff.getIndex());
            Object value = evaluateExp(arrAff.getExp());

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

        // AddAssignNode (équivalent de +=)
        if (instr instanceof AddAssignNode) {
            AddAssignNode somme = (AddAssignNode) instr;
            String varName = somme.getIdent().getName();

            if (!memory.containsKey(varName)) {
                throw new RuntimeException("Variable '" + varName + "' non déclarée");
            }

            int currentValue = (Integer) memory.get(varName);
            int addValue = (Integer) evaluateExp(somme.getExp());
            memory.put(varName, currentValue + addValue);
            debug("AddAssignNode : " + varName + " += " + addValue + " = " + (currentValue + addValue));
            return;
        }

        // Incrémentation
        if (instr instanceof IncNode) {
            IncNode incr = (IncNode) instr;
            String varName = incr.getIdent().getName();

            if (!memory.containsKey(varName)) {
                throw new RuntimeException("Variable '" + varName + "' non déclarée");
            }

            int currentValue = (Integer) memory.get(varName);
            memory.put(varName, currentValue + 1);
            debug("Incrémentation : " + varName + " = " + (currentValue + 1));
            return;
        }

        // If
        if (instr instanceof IfNode) {
            IfNode ifInstr = (IfNode) instr;
            boolean condition = (Boolean) evaluateExp(ifInstr.getCondExp());
            debug("Condition IF : " + condition);

            if (condition) {
                debug("Exécution du THEN");
                for (InstrNode i : ifInstr.getThenInstrs().getList()) {
                    executeInstr(i);
                    if (hasReturned) break;
                }
            } else if (!ifInstr.getElseInstrs().getList().isEmpty()) {
                debug("Exécution du ELSE");
                for (InstrNode i : ifInstr.getElseInstrs().getList()) {
                    executeInstr(i);
                    if (hasReturned) break;
                }
            }
            return;
        }

        // While
        if (instr instanceof WhileNode) {
            WhileNode whileInstr = (WhileNode) instr;
            int iterations = 0;

            while ((Boolean) evaluateExp(whileInstr.getCondExp())) {
                debug("Itération WHILE #" + (++iterations));

                for (InstrNode i : whileInstr.getInstrs().getList()) {
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
        if (instr instanceof MethodCallInstrNode) {
            MethodCallInstrNode call = (MethodCallInstrNode) instr;
            executeMethodCall(call.getIdent().getName(), call.getListExp().getList());
            return;
        }

        // Return
        if (instr instanceof RetourNode) {
            RetourNode ret = (RetourNode) instr;
            returnValue = evaluateExp(ret.getExp());
            hasReturned = true;
            debug("Return : " + returnValue);
            return;
        }

        // Write / Writeln
        if (instr instanceof WriteNode) {
            WriteNode write = (WriteNode) instr;

            if (write.getExp() instanceof IdentExp) {
                IdentExp id = (IdentExp) write.getExp();
                Object value = memory.get(id.getIdent().getName());
                System.out.print(value);
            } else if (write.getExp() instanceof StringLiteralNode) {
                String str = ((StringLiteralNode) write.getExp()).getValue();
                System.out.print(str);
            } else if (write.getExp() instanceof ArrayAccessNode) {
                ArrayAccessNode arr = (ArrayAccessNode) write.getExp();
                int index = (Integer) evaluateExp(arr.getIndexExp());
                int value = arrays.get(arr.getIdent().getName())[index];
                System.out.print(value);
            } else {
                // Pour les expressions générales (ex: 5 + 3)
                Object value = evaluateExp(write.getExp());
                System.out.print(value);
            }

            if (write.isLn()) {
                System.out.println();
            }
            return;
        }
    }

    // ========== ÉVALUATION DES EXPRESSIONS ==========
    private Object evaluateExp(ExpNode exp) {
        // Entiers
        if (exp instanceof IntLiteralNode) {
            return ((IntLiteralNode) exp).getValue();
        }

        // Booléens
        if (exp instanceof BoolLiteralNode) {
            return ((BoolLiteralNode) exp).getValue();
        }

        // Chaines
        if (exp instanceof StringLiteralNode) {
            return ((StringLiteralNode) exp).getValue();
        }

        // Identifiants (variables)
        if (exp instanceof IdentExp) {
            String varName = ((IdentExp) exp).getIdent().getName();

            if (!memory.containsKey(varName)) {
                throw new RuntimeException("Variable '" + varName + "' non déclarée");
            }

            return memory.get(varName);
        }

        // Accès tableau
        if (exp instanceof ArrayAccessNode) {
            ArrayAccessNode arr = (ArrayAccessNode) exp;
            String arrayName = arr.getIdent().getName();
            int index = (Integer) evaluateExp(arr.getIndexExp());

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
        if (exp instanceof LengthNode) {
            String arrayName = ((LengthNode) exp).getIdent().getName();

            if (!arrays.containsKey(arrayName)) {
                throw new RuntimeException("Tableau '" + arrayName + "' non déclaré");
            }

            return arrays.get(arrayName).length;
        }

        // Expressions binaires
        if (exp instanceof BinaryExpNode) {
            BinaryExpNode binExp = (BinaryExpNode) exp;
            Object left = evaluateExp(binExp.getLeftExp());
            Object right = evaluateExp(binExp.getRightExp());

            return evaluateBinaryOp(binExp.getOp(), left, right);
        }

        // Expressions unaires
        if (exp instanceof UnaryExpNode) {
            UnaryExpNode unExp = (UnaryExpNode) exp;
            Object value = evaluateExp(unExp.getExp());

            return evaluateUnaryOp(unExp.getOp(), value);
        }

        // Appel de méthode (expression)
        if (exp instanceof MethodCallExpNode) {
            MethodCallExpNode call = (MethodCallExpNode) exp;
            return executeMethodCall(call.getIdent().getName(), call.getListExp().getList());
        }

        throw new RuntimeException("Type d'expression non supporté : " + exp.getClass().getName());
    }

    // ========== OPÉRATEURS BINAIRES ==========
    private Object evaluateBinaryOp(BinaryOpNode op, Object left, Object right) {
        // Opérateurs arithmétiques
        if (left instanceof Integer && right instanceof Integer) {
            int l = (Integer) left;
            int r = (Integer) right;

            if (op.isAdd()) {
                return l + r;
            } else if (op.isSub()) {
                return l - r;
            } else if (op.isMul()) {
                return l * r;
            } else if (op.isDiv()) {
                if (r == 0) throw new RuntimeException("Division par zéro");
                return l / r;
            } else if (op.isGt()) {
                return l > r;
            } else if (op.isEq()) {
                return l == r;
            } else {
                throw new RuntimeException("Opérateur arithmetique invalide pour le type: " + op.getOp());
            }
        }

        // Opérateurs logiques
        if (left instanceof Boolean && right instanceof Boolean) {
            boolean l = (Boolean) left;
            boolean r = (Boolean) right;

            if (op.isAnd()) {
                return l && r;
            } else if (op.isOr()) {
                return l || r;
            } else {
                throw new RuntimeException("Opérateur logique invalide pour le type: " + op.getOp());
            }
        }

        throw new RuntimeException("Types incompatibles pour l'opérateur : " + op.getOp());
    }

    // ========== OPÉRATEURS UNAIRES ==========
    private Object evaluateUnaryOp(UnaryOpNode op, Object value) {
        if (op.equals("-") && value instanceof Integer) {
            return -(Integer) value;
        }

        if (op.equals("!") && value instanceof Boolean) {
            return !(Boolean) value;
        }

        throw new RuntimeException("Opération unaire invalide : " + op + " " + value);
    }

    // ========== APPEL DE MÉTHODE ==========
    private Object executeMethodCall(String methodName, List<ExpNode> args) {
        debug("Appel de méthode : " + methodName);

        if (!methods.containsKey(methodName)) {
            throw new RuntimeException("Méthode '" + methodName + "' non définie");
        }

        MethodeNode method = methods.get(methodName);

        // Vérifier le nombre d'arguments
        if (args.size() != method.getEntetes().getList().size()) {
            throw new RuntimeException("Méthode '" + methodName + "' : " +
                    method.getEntetes().getList().size() + " arguments attendus, " + args.size() + " fournis");
        }

        // Sauvegarder le contexte actuel
        Map<String, Object> savedMemory = new HashMap<>(memory);
        callStack.push(savedMemory);

        // Créer un nouveau contexte pour la méthode
        memory.clear();

        // Lier les paramètres
        for (int i = 0; i < method.getEntetes().getList().size(); i++) {
            EnteteNode param = method.getEntetes().getList().get(i);
            Object argValue = evaluateExp(args.get(i));
            memory.put(param.getIdent().getName(), argValue);
            debug("Paramètre : " + param.getIdent().getName() + " = " + argValue);
        }

        // Initialiser les variables locales
        for (DeclNode var : method.getVars().getList()) {
            initializeDecl(var);
        }

        // Exécuter le corps de la méthode
        hasReturned = false;
        returnValue = null;

        for (InstrNode instr : method.getInstrs().getList()) {
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