parser grammar JajaCodeParser;
options { tokenVocab=JajaCodeLexer; }

program : classe+ EOF ;

classe : adresse instr ;

instr
    :INIT
    |SWAP
    |NEW LPAREN ident COMMA type COMMA sorte COMMA valeur RPAREN
    |NEWARRAY LPAREN ident COMMA type RPAREN
    |INVOKE LPAREN ident RPAREN
    |RETURN
    |PUSH LPAREN valeur RPAREN
    |POP
    |LOAD LPAREN ident RPAREN
    |ALOAD LPAREN ident RPAREN
    |STORE LPAREN ident RPAREN
    |ASTORE LPAREN ident RPAREN
    |WRITE
    |WRITELN
    |LENGHT LPAREN ident RPAREN
    |IF LPAREN adresse RPAREN
    |GOTO LPAREN adresse RPAREN
    |INC LPAREN ident RPAREN
    |AINC LPAREN ident RPAREN
    |oper
    |NOP
    |JCSTOP
;

ident : IDENTIFICATEUR;
valeur :  (NOMBRE | TRUE | FALSE | STRING)? ;
adresse : NOMBRE ;
oper : oper2 | oper1;
oper1 : NEG | NOT;
oper2 : ADD | SUB | MUL | DIV | CMP | SUP | OR | AND ;
type : INT | BOOLEAN;
sorte : VAR | METH | CST | TAB;

