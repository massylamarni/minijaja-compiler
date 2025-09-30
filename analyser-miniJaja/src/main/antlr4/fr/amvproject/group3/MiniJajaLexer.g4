lexer grammar MiniJajaLexer;

// --------- Mots-clés ---------
CLASS     : 'class' ;
VOID      : 'void' ;
MAIN      : 'main' ;
RETURN    : 'return' ;
IF        : 'if' ;
ELSE      : 'else' ;
WHILE     : 'while' ;
FINAL     : 'final' ;

INTTYPE   : 'int' ;
BOOLTYPE  : 'boolean' ;
TRUE      : 'true' ;
FALSE     : 'false' ;
WRITE     : 'write' ;
WRITELN   : 'writeln' ;
LENGTH    : 'length' ;

// --------- Opérateurs ---------
ASSIGN    : '=' ;
PLUS      : '+' ;
MINUS     : '-' ;
MUL       : '*' ;
DIV       : '/' ;
INCR      : '++' ;
PLUSEQ    : '+=' ;

LT        : '<' ;
LE        : '<=' ;
GT        : '>' ;
GE        : '>=' ;
EQ        : '==' ;
NEQ       : '!=' ;

AND       : '&&' ;
OR        : '||' ;
NOT       : '!' ;

// --------- Délimiteurs ---------
LBRACK    : '[' ;
RBRACK    : ']' ;
LPAREN    : '(' ;
RPAREN    : ')' ;
LBRACE    : '{' ;
RBRACE    : '}' ;
COMMA     : ',' ;
SEMI      : ';' ;

// --------- Identificateurs & Littéraux ---------
IDENT     : [a-zA-Z][a-zA-Z0-9]* ;
INT       : [0-9]+ ;
STRING    : '"' ( ~["\\\r\n] | '\\' . )* '"' ;

// --------- Espaces et commentaires ---------
WS        : [ \t\r\n]+ -> skip ;
COMMENT   : '//' ~[\r\n]* -> skip ;
