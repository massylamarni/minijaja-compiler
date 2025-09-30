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

// --------- Opérateurs ---------
ASSIGN    : '=' ;
PLUS      : '+' ;
MINUS     : '-' ;
MUL       : '*' ;
DIV       : '/' ;

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
IDENT        : [a-zA-Z_][a-zA-Z0-9_]* ;
INT       : [0-9]+ ;

// --------- Espaces et commentaires ---------
WS        : [ \t\r\n]+ -> skip ;
COMMENT   : '//' ~[\r\n]* -> skip ;

