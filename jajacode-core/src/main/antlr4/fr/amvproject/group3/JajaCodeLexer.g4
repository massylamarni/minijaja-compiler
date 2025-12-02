lexer grammar JajaCodeLexer;

VIDE     : 'vide';
INIT     : 'init';
SWAP     : 'swap';
PUSH     : 'push';
NEW      : 'new';
NEWARRAY : 'newarray';
INVOKE   : 'invoke';
GOTO     : 'goto';
LOAD     : 'load';
ALOAD    : 'aload';
STORE    : 'store';
ASTORE   : 'astore';
LENGHT   : 'length';
INC      : 'inc';
AINC     : 'ainc';
IF       : 'if';
JCSTOP   : 'jcstop';
TRUE     : 'true' ;
FALSE    : 'false' ;
INT      : 'entier';
BOOLEAN  : 'boolean';
POP      : 'pop';
RETURN   : 'return';
WRITE    : 'write';
WRITELN  : 'writeln';
VAR      : 'var';
METH     : 'meth';
CST      : 'cst';
TAB      : 'tab';
NEG      : 'neg';
NOT      : 'not';
ADD      : 'add';
SUB      : 'sub';
MUL      : 'mul';
DIV      : 'div';
CMP      : 'cmp';
SUP      : 'sup';
OR       : 'or';
AND      : 'and';
NOP      : 'nop';

COMMA    : ',';
LPAREN   : '(';
RPAREN   : ')';

// ----------------- Identifiers & Literals -----------------
STRING      : '"' ( ~["\\\r\n] | '\\' . )* '"' ;
IDENTIFICATEUR : [a-zA-Z_] [a-zA-Z_0-9]* ;
NOMBRE         : [0-9]+ ;

// --------- Espaces et commentaires ---------
WS        : [ \t\r\n]+ -> channel(HIDDEN);
COMMENT_LINE   : '//' (.)*?[\r\n] -> channel(HIDDEN);
COMMENT_BLOCK : '/*' ( . | '\r' | '\n' )*? '*/' -> channel(HIDDEN);
