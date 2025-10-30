package fr.amvproject.group3;

import fr.amvproject.group3.ast.*;
import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.finals.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.intermediate.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.program.*;

import java.util.List;

/**
 * Calcule le nombre d'instructions Jaja-Code qui seront émises pour un AST (two-pass).
 * Doit rester cohérent avec JajaCodeCompiler (ordre et instructions réellement émises).
 */
@SuppressWarnings("java:S6539") // Visitor pattern: large by design
public class SizeCalculator implements AstVisitor<Integer> {

    // utilitaire
    public int safe(AstNode n) {
        return n == null ? 0 : n.accept(this);
    }

    private int sumList(List<? extends AstNode> l) {
        if (l == null) return 0;
        int s = 0;
        for (AstNode a : l) s += safe(a);
        return s;
    }

    // ----- Program / Class -----
    @Override
    public Integer visitProgram(ProgramNode node) {
        // init + classes + pop + jcstop
        int s = 1; // init
        if (node.getClasses() != null) {
            for (ClassNode c : node.getClasses()) s += safe(c);
        }
        s += 1; // pop
        s += 1; // jcstop
        return s;
    }

    @Override
    public Integer visitClass(ClassNode node) {
        // semantics: decls (creation) + main + retraitDecls (swap/pops)
        int s = 0;
        s += safe(node.getDecls()); // création des decls (new(...))
        s += safe(node.getMain());  // main body (inclut push0 + return + retrait vars locaux)
        // retrait des declarations globales : for each decl => swap + pop = 2
        if (node.getDecls() != null && node.getDecls().getDecls() != null) {
            s += 2 * node.getDecls().getDecls().size();
        }
        return s;
    }

    // ----- Declarations -----
    @Override
    public Integer visitDecls(DeclsNode node) {
        int s = 0;
        if (node.getDecls() != null) {
            for (DeclNode d : node.getDecls()) s += safe(d);
        }
        return s;
    }

    @Override public Integer visitDecl(DeclNode node) { return 0; } // abstract

    // Var / Cst / Table
    @Override
    public Integer visitVar(VarNode node) {
        // global or local var: if initExp present -> size(init) + new
        // otherwise push 0 + new
        int s = 0;
        if (node.getInitExp() != null) s += safe(node.getInitExp());
        else s += 1; // push 0
        s += 1; // new(...)
        return s;
    }

    @Override
    public Integer visitCst(CstNode node) {
        int s = 0;
        if (node.getInitExp() != null) s += safe(node.getInitExp());
        else s += 1;
        s += 1; // new(... cst ...)
        return s;
    }

    @Override
    public Integer visitTable(TableNode node) {
        int s = 0;
        if (node.getSizeExp() != null) s += safe(node.getSizeExp());
        else s += 1;
        s += 1; // new(...,array,...)
        return s;
    }

    @Override
    public Integer visitVars(VarsNode node) {
        int s = 0;
        if (node.getList() != null) {
            for (VarDeclNode v : node.getList()) s += safe(v);
        }
        return s;
    }

    // VarDeclNode is abstract parent for Var/Cst/Table -> we already covered in visitVar/visitCst/visitTable
    @Override public Integer visitVarDecl(VarDeclNode node) { return 0; }

    // ----- Main / Method -----
    @Override
    public Integer visitMain(MainNode node) {
        // main:
        // vars allocation (each var: push0/init + new)
        int s = 0;
        s += safe(node.getVars());   // allocation of locals
        s += safe(node.getInstrs()); // body instructions
        s += 1; // push 0 (value for main)
        // retrait des vars locales : 2 per var (swap + pop)
        int nLocals = (node.getVars() != null && node.getVars().getList() != null) ? node.getVars().getList().size() : 0;
        s += 2 * nLocals;
        s += 1; // return
        return s;
    }

    @Override
    public Integer visitMethod(MethodNode node) {
        // Pattern used in compiler:
        // push meta (1) + new(meth) (1) + ujp after (1)
        // params: each param -> push0 + new => 2 * nparams
        // local vars: safe(vars)
        // body: safe(instrs)
        // if method has explicit return inside body -> we DO NOT add extra swap+return.
        // otherwise add swap + return (2)
        int s = 0;
         // push meta
        s += 1; // new(method)
        s += 1; // ujp after
        int nParams = (node.getParams() != null && node.getParams().getList() != null) ? node.getParams().getList().size() : 0;
        s += 2 * nParams; // each param: push0 + new
        s += safe(node.getVars()); // locals allocation
        s += safe(node.getInstrs()); // body
        // check if method body contains at least one ReturnNode
        boolean hasReturn = containsReturn(node.getInstrs());
        if (!hasReturn) s += 2; // swap + return
        return s;
    }

    // helper: detect if any ReturnNode in Instrs (recursively)
    private boolean containsReturn(InstrsNode instrs) {
        if (instrs == null || instrs.getList() == null) return false;
        for (InstrNode in : instrs.getList()) {
            if (in instanceof ReturnNode) return true;
            if (in instanceof IfNode) {
                IfNode ifn = (IfNode) in;
                if (containsReturn(ifn.getThenInstrs())) return true;
                if (containsReturn(ifn.getElseInstrs())) return true;
            } else if (in instanceof WhileNode) {
                WhileNode w = (WhileNode) in;
                if (containsReturn(w.getInstrs())) return true;
            } // extend if needed for nested constructs
        }
        return false;
    }

    // ----- Instructions -----
    @Override
    public Integer visitInstrs(InstrsNode node) {
        int s = 0;
        if (node.getList() != null) {
            for (InstrNode instr : node.getList()) s += safe(instr);
        }
        return s;
    }

    @Override
    public Integer visitAssign(AssignNode node) {
        // compile RHS then store or astore
        int s = 0;
        s += safe(node.getExp());
        LeftValueNode left = node.getLeftValue();
        if (left instanceof ArrayAccessNode) {
            // need index evaluation + astore
            ArrayAccessNode a = (ArrayAccessNode) left;
            s += safe(a.getIndexExp());
            s += 1; // astore
        } else {
            s += 1; // store
        }
        return s;
    }

    @Override
    public Integer visitAddAssign(AddAssignNode node) {
        // pattern used in compiler for simple var:
        // RHS size + load + swap + add + store  => sizeR + 4
        // for array: more complex (load arr + index + loadref + RHS + add + index + astore)
        LeftValueNode lv = node.getLeftValueNode();
        int s = 0;
        s += safe(node.getExp()); // RHS (which can be a BinaryExp built in AddAssignNode ctor)
        if (lv instanceof ArrayAccessNode) {
            ArrayAccessNode a = (ArrayAccessNode) lv;
            int idxSize = safe(a.getIndexExp());
            s += 1; // load arr
            s += idxSize;
            s += 1; // loadref
            s += 1; // swap/add etc (we'll count explicitly)
            s += 1; // add
            s += idxSize; // re-evaluate index for astore
            s += 1; // astore
        } else {
            s += 1; // load var
            s += 1; // swap
            s += 1; // add
            s += 1; // store
        }
        return s;
    }

    @Override
    public Integer visitInc(IncNode node) {
        // load, push 1, add, store
        return 4;
    }

    @Override
    public Integer visitCallInstr(CallInstrNode node) {
        // args code + invoke + cleanupArgs (2*nArgs) + pop
        int s = 0;
        if (node.getListExp() != null) {
            s += safe(node.getListExp());
            int nArgs = node.getListExp().getList() != null ? node.getListExp().getList().size() : 0;
            s += 1; // invoke
            s += 2 * nArgs; // swap+pop per arg (cleanup)
        } else {
            s += 1; // invoke with no args
        }
        s += 1; // pop (drop return)
        return s;
    }

    @Override
    public Integer visitWrite(WriteNode node) {
        int s = 0;
        if (node.getExp() != null) s += safe(node.getExp());
        s += 1; // write
        if (node.isLn()) s += 1; // writeln if separate
        return s;
    }

    @Override
    public Integer visitReturn(ReturnNode node) {
        int s = 0;
        if (node.getExp() != null) s += safe(node.getExp());
        s += 2; // swap + return
        return s;
    }

    @Override
    public Integer visitIf(IfNode node) {
        // We'll follow the "then-first" numeric layout:
        // pe | iffalse (nThen + 1) | pThen | ujp (nElse) | pElse
        int ne = safe(node.getCondExp());
        int nThen = safe(node.getThenInstrs());
        int nElse = safe(node.getElseInstrs());
        // total = ne + 1(iffalse) + nThen + 1(ujp) + nElse
        return ne + 1 + nThen + 1 + nElse;
    }

    @Override
    public Integer visitWhile(WhileNode node) {
        // layout numeric:
        // label: cond (ne) | iffalse (niss + 2) ??? Simpler: we use general pattern:
        // start: pe | iffalse end | body | ujp start
        int ne = safe(node.getCondExp());
        int nBody = safe(node.getInstrs());
        // total = ne + 1(iffalse) + nBody + 1(ujp)
        return ne + 1 + nBody + 1;
    }

    @Override
    public Integer visitCallExp(CallExpNode node) {
        // args size + invoke + cleanupArgs (2*nArgs); return value stays on stack
        int s = 0;
        if (node.getListExp() != null) {
            s += safe(node.getListExp());
            int nArgs = node.getListExp().getList() != null ? node.getListExp().getList().size() : 0;
            s += 1; // invoke
            s += 2 * nArgs; // cleanup
        } else {
            s += 1; // invoke
        }
        return s;
    }

    @Override
    public Integer visitListExp(ListExpNode node) {
        int s = 0;
        if (node.getList() != null) {
            for (ExpNode e : node.getList()) s += safe(e);
        }
        return s;
    }

    // ----- Expressions -----
    @Override
    public Integer visitBinaryExp(BinaryExpNode node) {
        // left + right + 1 (op)
        return safe(node.getLeftExp()) + safe(node.getRightExp()) + 1;
    }

    @Override
    public Integer visitUnaryExp(UnaryExpNode node) {
        return safe(node.getExp()) + 1;
    }

    @Override
    public Integer visitLength(LengthNode node) {
        // load arr + length
        return 1 + 1;
    }

    @Override
    public Integer visitArrayAccess(ArrayAccessNode node) {
        // load arr + index + loadref
        return 1 + safe(node.getIndexExp()) + 1;
    }

    @Override
    public Integer visitIntLiteral(IntLiteralNode node) { return 1; }
    @Override
    public Integer visitBoolLiteral(BoolLiteralNode node) { return 1; }
    @Override
    public Integer visitStringLiteral(StringLiteralNode node) { return 1; }

    // id usage in expression => load(id)
    @Override
    public Integer visitIdent(IdentNode node) { return 1; }

    // stub implementations for other visitors (return 0 by default)
    @Override public Integer visitBinaryOp(BinaryOpNode node) { return 0; }
    @Override public Integer visitUnaryOp(UnaryOpNode node) { return 0; }
    @Override public Integer visitParam(ParamNode node) { return 0; }
    @Override public Integer visitParams(ParamsNode node) { return 0; }
    @Override public Integer visitType(TypeNode node) { return 0; }
    @Override public Integer visitTypeMeth(TypeMethNode node) { return 0; }

    // If AST contains other node types that appear in your project, add handlers similarly.
}
