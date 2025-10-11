parser grammar MiniJajaParser;
options { tokenVocab=MiniJajaLexer; }

program : classe+ EOF ;

classe : CLASS ident LBRACE decls methmain RBRACE ;

ident : IDENTIFICATEUR ;

decls : (decl SEMI)* ;

decl : var | methode ;

vars : (var SEMI)* ;

var : typemeth ident vexp
    | typemeth ident LBRACK exp RBRACK
    | FINAL type ident vexp ;

vexp : (ASSIGN exp)?;

methode : typemeth ident LPAREN entetes RPAREN LBRACE vars instrs RBRACE ;

methmain : MAIN LBRACE vars instrs RBRACE ;

entetes : (entete (COMMA entetes)?)?;

entete : type ident ;

instrs : (instr SEMI)* ;

instr : ident1 ASSIGN exp
      | ident1 ADDASSIGN exp
      | ident1 INC
      | ident LPAREN listexp RPAREN
      | RETURN exp
      | WRITE LPAREN (ident1 | STRING) RPAREN
      | WRITELN LPAREN (ident1 | STRING) RPAREN
      | IF LPAREN exp RPAREN LBRACE instrs RBRACE (ELSE LBRACE instrs RBRACE)?
      | WHILE LPAREN exp RPAREN LBRACE instrs RBRACE ;

listexp : (exp (COMMA listexp)?)? ;

// Operator precedence (highest → lowest):
// unary (!, -), multiplicative (*, /), additive (+, -), relational (==, >), logical (&&, ||)
exp: expOr;
expOr: expAnd (OR expAnd)*;
expAnd: expEq (AND expEq)*;
expEq: expRel ((EQ | GT) expRel)*;
expRel: expAdd;
expAdd: expMul ((ADD | SUB) expMul)*;
expMul: expUnary ((MUL | DIV) expUnary)*;
expUnary: (NOT | SUB) expUnary | fact;

fact : ident1
     | ident LPAREN listexp RPAREN | LENGTH LPAREN ident RPAREN
     | TRUE | FALSE | NOMBRE
     | LPAREN exp RPAREN ;

ident1 : ident | ident LBRACK exp RBRACK ;

typemeth : VOID | type ;

type : INT | BOOLEAN ;
