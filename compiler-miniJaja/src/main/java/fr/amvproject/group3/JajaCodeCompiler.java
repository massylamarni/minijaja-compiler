package fr.amvproject.group3;



import fr.amvproject.group3.ast.*;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.abstracts.IdentExp;
import fr.amvproject.group3.ast.nodes.params.EnteteNode;
import fr.amvproject.group3.ast.nodes.params.EntetesNode;
import fr.amvproject.group3.ast.nodes.program.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.other.*;

// On implémente le visiteur de l’AST MiniJaja
public class JajaCodeCompiler implements AstVisitor<Void> {

    private final CodeEmitter emitter;
    private final LabelManager labels;

    public JajaCodeCompiler(CodeEmitter emitter, LabelManager labels) {
        this.emitter = emitter;
        this.labels = labels;
    }

    // Exemple simple : visite d’un entier
    @Override
    public Void visitIntLiteral(IntLiteralNode node) {
        emitter.emit("push " + node.getValue());
        return null;
    }

    @Override
    public Void visitLength(LengthNode node) {
        return null;
    }

    @Override
    public Void visitListExp(ListExpNode node) {
        return null;
    }

    @Override
    public Void visitMethodCallExp(MethodCallExpNode node) {
        return null;
    }

    @Override
    public Void visitStringLiteral(StringLiteralNode node) {
        return null;
    }

    @Override
    public Void visitUnaryExp(UnaryExpNode node) {
        return null;
    }

    // TODO : tu ajouteras ici visitBinaryExp, visitAssign, visitIf, visitWhile, etc.

    // Méthodes par défaut (obligatoires pour compilo)
    @Override
    public Void visitClasse(ClasseNode node) {
        emitter.emit("; Début de la classe " + node.getIdent().getName());

        // Déclarations globales
        if (node.getDecls() != null)
            node.getDecls().accept(this);

        // Méthode main
        if (node.getMain() != null)
            node.getMain().accept(this);

        emitter.emit("; Fin de la classe");
        return null;
    }


    @Override
    public Void visitDecl(DeclNode node) {
        return null;
    }

    @Override public Void visitDecls(DeclsNode node) {
        for (DeclNode decl : node.getDecls()) {
            decl.accept(this);
        }
        return null;
    }

    @Override
    public Void visitType(TypeNode node) {
        return null;
    }

    @Override
    public Void visitBinaryOp(BinaryOpNode node) {
        return null;
    }

    @Override
    public Void visitUnaryOp(UnaryOpNode node) {
        return null;
    }

    // Toutes les autres méthodes vides pour le moment
    @Override
    public Void visitBinaryExp(BinaryExpNode node) {
        node.getLeftExp().accept(this);
        node.getRightExp().accept(this);

        BinaryOpNode opNode = node.getOp();

        if (opNode.isAdd()) emitter.emit("add");
        else if (opNode.isSub()) emitter.emit("sub");
        else if (opNode.isMul()) emitter.emit("mul");
        else if (opNode.isDiv()) emitter.emit("div");
        else if (opNode.isEq()) emitter.emit("eq");
        else if (opNode.isGt()) emitter.emit("gt");
        else if (opNode.isAnd()) emitter.emit("and");
        else if (opNode.isOr()) emitter.emit("or");

        return null;
    }
    @Override
    public Void visitVarRef(VarRefNode node) {
        emitter.emit("load " + node.getIdent().getName());
        return null;
    }

    @Override
    public Void visitAddAssign(AddAssignNode node) {
        return null;
    }

    @Override
    public Void visitAssign(AssignNode node) {
        // Compiler l'expression à droite du "="
        node.getExp().accept(this);

        // Affecter la valeur à la variable à gauche
        emitter.emit("store " + node.getIdent().getName());
        return null;
    }


    @Override
    public Void visitIf(IfNode node) {
        String elseLabel = labels.newLabel("else");
        String endLabel = labels.newLabel("endif");

        // condition
        node.getCondExp().accept(this);
        emitter.emit("iffalse " + elseLabel);

        // bloc then
        if (node.getThenInstrs() != null) node.getThenInstrs().accept(this);
        emitter.emit("iftrue " + endLabel);

        // bloc else
        emitter.emit(elseLabel + ":");
        if (node.getElseInstrs() != null) node.getElseInstrs().accept(this);

        emitter.emit(endLabel + ":");
        return null;
    }


    @Override
    public Void visitInc(IncNode node) {
        return null;
    }

    @Override
    public Void visitInstrs(InstrsNode node) {
        System.out.println("[DEBUG] Nombre d’instructions : " + node.getList().size());
        for (InstrNode instr : node.getList()) {
            instr.accept(this);
        }
        return null;
    }


    @Override
    public Void visitMethodCallInstr(MethodCallInstrNode node) {
        return null;
    }

    @Override
    public Void visitRetour(RetourNode node) {
        return null;
    }

    @Override
    public Void visitWhile(WhileNode node) {
        String startLabel = labels.newLabel("while_start");
        String endLabel = labels.newLabel("while_end");

        emitter.emit(startLabel + ":");
        node.getCondExp().accept(this);
        emitter.emit("iffalse " + endLabel);
        node.getInstrs().accept(this);
        emitter.emit("iftrue " + startLabel);
        emitter.emit(endLabel + ":");
        return null;
    }

    @Override public Void visitBoolLiteral(BoolLiteralNode node) { return null; }
    @Override
    public Void visitVar(VarNode node) {
        // Déclaration de variable simple
        emitter.emit("newV " + node.getIdent().getName());

        if (node.getInitExp() != null) {
            node.getInitExp().accept(this);
            emitter.emit("store " + node.getIdent().getName());
        }

        return null;
    }



    @Override
    public Void visitVars(VarsNode node) {
        return null;
    }

    @Override
    public Void visitArrayAccess(ArrayAccessNode node) {
        return null;
    }

    @Override
    public Void visitIdent(Ident node) {
        return null;
    }

    @Override
    public Void visitIdentExp(IdentExp node) {
        return null;
    }

    @Override
    public Void visitMethMain(MethMainNode node) {
        emitter.emit("main:");
        System.out.println("[DEBUG] Entrée dans main");

        if (node.getVars() != null) {
            System.out.println("[DEBUG] Vars non nulles");
            node.getVars().accept(this);
        }

        if (node.getInstrs() != null) {
            System.out.println("[DEBUG] Instrs non nulles");
            node.getInstrs().accept(this);
        } else {
            System.out.println("[DEBUG] Instrs est null");
        }

        emitter.emit("return");
        return null;
    }



    @Override
    public Void visitMethode(MethodeNode node) {
        return null;
    }

    @Override public Void visitWrite(WriteNode node) { return null; }

    @Override
    public Void visitEntete(EnteteNode node) {
        return null;
    }

    @Override
    public Void visitEntetes(EntetesNode node) {
        return null;
    }

    @Override
    public Void visitProgram(ProgramNode node) {
        emitter.emit("; Début du programme");

        // Parcourt toutes les classes du programme
        for (ClasseNode classe : node.getClasses()) {
            classe.accept(this);
        }

        emitter.emit("; Fin du programme");
        return null;
    }



}
