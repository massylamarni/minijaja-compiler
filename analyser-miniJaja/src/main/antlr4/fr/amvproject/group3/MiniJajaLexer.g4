lexer grammar MiniJajaLexer;

// ----------------- Keywords -----------------
CLASS       : 'class';
MAIN        : 'main';
FINAL       : 'final';
VOID        : 'void';
INT         : 'int';
BOOLEAN     : 'boolean';
RETURN      : 'return';
WRITE       : 'write';
WRITELN     : 'writeln';
IF          : 'if';
ELSE        : 'else';
WHILE       : 'while';
TRUE        : 'true';
FALSE       : 'false';
LENGTH      : 'length';

// ----------------- Delimiters -----------------
LBRACE      : '{';
RBRACE      : '}';
LPAREN      : '(';
RPAREN      : ')';
LBRACK      : '[';
RBRACK      : ']';
SEMI        : ';';
COMMA       : ',';

// ----------------- Operators -----------------
ASSIGN      : '=';
ADDASSIGN   : '+=';
INC         : '++';
ADD         : '+';
SUB         : '-';
MUL         : '*';
DIV         : '/';
NOT         : '!';
AND         : '&&';
OR          : '||';
EQ          : '==';
GT          : '>';

// ----------------- Identifiers & Literals -----------------
STRING      : '"' ( ~["\\\r\n] | '\\' . )* '"' ;
IDENTIFICATEUR : [a-zA-Z_] [a-zA-Z_0-9]* ;
NOMBRE         : [0-9]+ ;

// ----------------- Whitespaces and Comments -----------------
WS : [ \t\r\n]+ -> skip ;
LINE_COMMENT      : '//' ~[\r\n]* -> skip;
MULTILINE_COMMENT : '/*' .*? '*/' -> skip ;