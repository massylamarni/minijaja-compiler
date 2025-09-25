parser grammar MiniJaJaParser;
options { tokenVocab=MiniJaJaLexer; }

program : CLASS IDENT LBRACE decls methmain RBRACE ;

decls : decl SEMI decls | ;
decl  : var | methode;

// --------- Main ---------
methmain : MAIN LBRACE vars instrs RBRACE;
vars     : var SEMI vars | ;
instrs   : instr SEMI instrs | ;

instr
    : ident1 ASSIGN exp
    | ident1 PLUSEQ exp
    | ident1 INCR
    | IDENT LPAREN listexp RPAREN
    | RETURN exp
    | WRITE LPAREN (ident1 | STRING) RPAREN
    | WRITELN LPAREN (ident1 | STRING) RPAREN
    | IF LPAREN exp RPAREN LBRACE instrs RBRACE ( ELSE LBRACE instrs RBRACE )?
    | WHILE LPAREN exp RPAREN LBRACE instrs RBRACE
    ;

var
    : typemeth IDENT vexp
    | typemeth IDENT LBRACK exp RBRACK
    | FINAL type IDENT vexp
    ;

methode
    : typemeth IDENT LPAREN entetes RPAREN LBRACE vars instrs RBRACE
    ;

typemeth: type | VOID;
entetes : entete SEMI entetes | entete | ;
entete  : type IDENT;
vexp    : ASSIGN exp | ;
type    : INTTYPE | BOOLTYPE;

exp      : expAnd ( OR expAnd )* ;
expAnd   : expEq ( AND expEq )* ;
expEq    : expAdd ( (EQ | GT) expAdd )* ;
expAdd   : MINUS expAdd | expNot ( (PLUS | MINUS) expNot )* ;
expNot   : NOT expNot | terme ;
terme    : fact ( (MUL | DIV) fact )* ;

fact
    : ident1
    | IDENT LPAREN listexp RPAREN
    | LENGHT LPAREN IDENT RPAREN
    | TRUE
    | FALSE
    | INT
    | STRING
    | LPAREN exp RPAREN
    ;

ident1 : IDENT | IDENT LBRACK exp RBRACK ;

listexp : exp COMMA listexp | exp | ;
