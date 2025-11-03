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
 * Two-pass size calculator: donne le nombre d'instructions Jaja-Code
 * qu'un nœud AST produira (en se basant sur la convention d'émission).
 */
public class SizeCalculator implements AstVisitor<Integer> {
    @Override
    public Integer visitVarDecl(VarDeclNode node) {
        return 0;
    }

    public int size(AstNode node) {
        return node == null ? 0 : node.accept(this);
    }

    public boolean hasReturn(AstNode node) {
        if (node == null) return false;
        if (node instanceof InstrsNode) return containsReturn((InstrsNode) node);
        if (node instanceof MethodNode) return containsReturn(((MethodNode) node).getInstrs());
        if (node instanceof MainNode) return containsReturn(((MainNode) node).getInstrs());
        return false;
    }

    // safe helper
    private int safe(AstNode n) { return n == null ? 0 : n.accept(this); }

    private boolean containsReturn(InstrsNode instrs) {
        if (instrs == null || instrs.getList() == null) return false;
        for (InstrNode in : instrs.getList()) {
            if (in instanceof ReturnNode) return true;
            if (in instanceof IfNode) {
                IfNode ifn = (IfNode) in;
                if (containsReturn(ifn.getThenInstrs())) return true;
                if (containsReturn(ifn.getElseInstrs())) return true;
            }
            if (in instanceof WhileNode) {
                WhileNode w = (WhileNode) in;
                if (containsReturn(w.getInstrs())) return true;
            }
        }
        return false;
    }

    // ---------- Visitors (taille en instructions) ----------
    @Override public Integer visitProgram(ProgramNode node) {
        int s = 1; // init
        if (node.getClasses() != null) {
            for (ClassNode c : node.getClasses()) s += safe(c);
        }
        s += 1; // pop
        s += 1; // jcstop
        return s;
    }

    @Override public Integer visitClass(ClassNode node) {
        int s = 0;
        s += safe(node.getDecls()); // création des decls (new(...))
        s += safe(node.getMain());  // main body (contient push0 + return + retrait locaux)
        // retrait des decls (2 instructions par decl : swap + pop)
        int nDecls = node.getDecls() != null && node.getDecls().getDecls() != null ? node.getDecls().getDecls().size() : 0;
        s += 2 * nDecls;
        return s;
    }

    @Override public Integer visitDecls(DeclsNode node) {
        int sum = 0;
        if (node.getDecls() != null) for (DeclNode d : node.getDecls()) sum += safe(d);
        return sum;
    }

    @Override public Integer visitDecl(DeclNode node) { return 0; } // abstract

    @Override public Integer visitVar(VarNode node) {
        int s = 0;
        if (node.getInitExp() != null) s += safe(node.getInitExp());
        else s += 1; // push 0
        s += 1; // new(...)
        return s;
    }

    @Override public Integer visitCst(CstNode node) {
        int s = 0;
        if (node.getInitExp() != null) s += safe(node.getInitExp());
        else s += 1;
        s += 1; // new(...)
        return s;
    }

    @Override public Integer visitTable(TableNode node) {
        int s = 0;
        if (node.getSizeExp() != null) s += safe(node.getSizeExp());
        else s += 1;
        s += 1; // new(... array ...)
        return s;
    }

    @Override public Integer visitVars(VarsNode node) {
        int s = 0;
        if (node.getList() != null) for (VarDeclNode v : node.getList()) s += safe(v);
        return s;
    }

    @Override public Integer visitMain(MainNode node) {
        int s = 0;
        s += safe(node.getVars());   // allocation locaux
        s += safe(node.getInstrs()); // body
        s += 1; // push 0 (main return)
        int nLocals = node.getVars() != null && node.getVars().getList() != null ? node.getVars().getList().size() : 0;
        s += 2 * nLocals; // retrait locaux (swap + pop) pour chaque
        s += 1; // return
        return s;
    }

    @Override public Integer visitMethod(MethodNode node) {
        // push meta (1) + new(method) (1) + ujp (1)  => total 3
        // + pens (params new => 1 each) + pdvs + piss + prdvs + (swap+return if needed)
        int s = 0;
        s += 3; // push/new/ujp
        int nParams = node.getParams() != null && node.getParams().getList() != null ? node.getParams().getList().size() : 0;
        s += nParams * 1; // each param new(...) is 1 instruction
        s += safe(node.getVars()); // allocation local vars
        s += safe(node.getInstrs()); // body
        int nLocals = node.getVars() != null && node.getVars().getList() != null ? node.getVars().getList().size() : 0;
        s += 2 * nLocals; // retrait locaux
        boolean hasRet = containsReturn(node.getInstrs());
        if (!hasRet) s += 2; // swap + return
        return s;
    }

    @Override public Integer visitInstrs(InstrsNode node) {
        int s = 0;
        if (node.getList() != null) for (InstrNode in : node.getList()) s += safe(in);
        return s;
    }

    @Override public Integer visitAssign(AssignNode node) {
        int s = 0;
        s += safe(node.getExp());
        LeftValueNode left = node.getLeftValue();
        if (left instanceof ArrayAccessNode) {
            ArrayAccessNode a = (ArrayAccessNode) left;
            s += safe(a.getIndexExp());
            s += 1; // astore
        } else {
            s += 1; // store
        }
        return s;
    }

    @Override public Integer visitAddAssign(AddAssignNode node) {
        LeftValueNode lv = node.getLeftValueNode();
        int s = 0;
        s += safe(node.getExp());
        if (lv instanceof ArrayAccessNode) {
            ArrayAccessNode a = (ArrayAccessNode) lv;
            int idx = safe(a.getIndexExp());
            s += 1; // load arr
            s += idx;
            s += 1; // loadref
            s += 1; // add
            s += idx; // re-evaluate index for astore
            s += 1; // astore
        } else {
            s += 1; // load var
            s += 1; // swap
            s += 1; // add
            s += 1; // store
        }
        return s;
    }

    @Override public Integer visitInc(IncNode node) { return 1; } // load,push1,add,store

    @Override public Integer visitCallInstr(CallInstrNode node) {
        int s = 0;
        if (node.getListExp() != null) {
            s += safe(node.getListExp());
            int nArgs = node.getListExp().getList() != null ? node.getListExp().getList().size() : 0;
            s += 1; // invoke
            s += 2 * nArgs; // cleanup swap+pop per arg
        } else {
            s += 1; // invoke
        }
        s += 1; // pop (discard return)
        return s;
    }

    @Override public Integer visitWrite(WriteNode node) {
        int s = 0;
        if (node.getExp() != null) s += safe(node.getExp());
        s += 1; // write
        if (node.isLn()) s += 1;
        return s;
    }

    @Override public Integer visitReturn(ReturnNode node) {
        int s = 0;
        if (node.getExp() != null) s += safe(node.getExp());
        s += 2; // swap + return
        return s;
    }

    @Override public Integer visitIf(IfNode node) {
        int ne = safe(node.getCondExp());
        int nThen = safe(node.getThenInstrs());
        int nElse = safe(node.getElseInstrs());
        // convention: cond + iffalse( kIf ) + then + ujp (kGoto) + else
        return ne + 1 + nThen + 1 + nElse;
    }

    @Override public Integer visitWhile(WhileNode node) {
        int ne = safe(node.getCondExp());
        int nBody = safe(node.getInstrs());
        // cond + iffalse end + body + ujp back
        return ne + 1 + nBody + 1;
    }

    @Override public Integer visitCallExp(CallExpNode node) {
        int s = 0;
        if (node.getListExp() != null) {
            s += safe(node.getListExp());
            int nArgs = node.getListExp().getList() != null ? node.getListExp().getList().size() : 0;
            s += 1; // invoke
            s += 2 * nArgs; // cleanup
        } else s += 1;
        return s;
    }

    @Override public Integer visitListExp(ListExpNode node) {
        int s = 0;
        if (node.getList() != null) for (ExpNode e : node.getList()) s += safe(e);
        return s;
    }

    @Override public Integer visitBinaryExp(BinaryExpNode node) {
        return safe(node.getLeftExp()) + safe(node.getRightExp()) + 1;
    }

    @Override public Integer visitUnaryExp(UnaryExpNode node) {
        return safe(node.getExp()) + 1;
    }

    @Override public Integer visitLength(LengthNode node) { return 1 + 1; } // load + length
    @Override public Integer visitArrayAccess(ArrayAccessNode node) { return 1 + safe(node.getIndexExp()) + 1; }
    @Override public Integer visitIntLiteral(IntLiteralNode node) { return 1; }
    @Override public Integer visitBoolLiteral(BoolLiteralNode node) { return 1; }
    @Override public Integer visitStringLiteral(StringLiteralNode node) { return 1; }
    @Override public Integer visitIdent(IdentNode node) { return 1; }

    // stubs
    @Override public Integer visitBinaryOp(BinaryOpNode node) { return 0; }
    @Override public Integer visitUnaryOp(UnaryOpNode node) { return 0; }
    @Override public Integer visitParam(ParamNode node) { return 0; }
    @Override public Integer visitParams(ParamsNode node) { return 0; }
    @Override public Integer visitType(TypeNode node) { return 0; }

    @Override
    public Integer visitTypeMeth(TypeMethNode node) {
        return 0;
    }
}
