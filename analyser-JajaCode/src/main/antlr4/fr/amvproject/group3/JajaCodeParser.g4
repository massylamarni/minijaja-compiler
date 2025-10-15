parser grammar JajaCodeParser;
options { tokenVocab=JajaCodeLexer; }

program : adresse instr program EOF | ;

adresse : NOMBRE ;

instr
    :INIT
    |SWAP
    |new
    |newarray
    |invoke
    |RETURN
    |push
    |POP
    |load
    |aload
    |store
    |astore
    |WRITE
    |WRITELN
    |length
    |if
    |goto
    |inc
    |ainc
    |oper
    |NOP
    |JCSTOP
;

new : NEW LPAREN IDENT COMMA type COMMA sorte COMMA val RPAREN ;
newarray : NEWARRAY LPAREN ident COMMA type RPAREN;
invoke: INVOKE LPAREN ident RPAREN;
push: PUSH LPAREN valeur RPAREN;
load: LOAD LPAREN IDENT RPAREN;
aload: ALOAD LPAREN IDENT RPAREN;
store : STORE LPAREN ident RPAREN;
astore : ASTORE LPAREN ident RPAREN;
length: LENGHT LPAREN IDENT RPAREN;
ident : IDENT;
if: IF LPAREN adresse RPAREN;
goto: GOTO LPAREN adresse RPAREN;
inc: INC LPAREN IDENT RPAREN;
ainc: AINC LPAREN IDENT RPAREN;
valeur:  NOMBRE | TRUE |FALSE | STRING | ;
type : INTTYPE | BOOLEAN;
sorte: VAR | METH;
val:  TRUE | FALSE | NOMBRE;
oper: oper2 | oper1;
oper1: NEG | NOT;
oper2: ADD | SUB | MUL | DIV | CMP | SUP | OR | AND ;
