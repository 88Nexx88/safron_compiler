grammar safron;

program
   : block '.'
   ;

block
   :  vars_? function*  statement
   ;


vars_
   : VAR format ident (COMMA ident)*  SEMI (format ident (COMMA ident)*  SEMI)*
   ;



format
   : INT | FLOAT | VOID
   ;


function
   : DEF format ident LPAREN ((format ident)? (COMMA format ident)? (COMMA format ident)? (COMMA format ident)?) RPAREN SEMI block SEMI
   ;

statement
   : (assignstmt | callstmt | printstmt | beginstmt | ifstmt | whilestmt | breakstmt | continuestmt | returnstmt)*
   ;

assignstmt
   : ident ASSIGN expression
   | ident ASSIGN callstmt
   ;

breakstmt
   : BREAK
   ;

continuestmt
   : CONTINUE
   ;

returnstmt
   : RETURN ident
   ;

callstmt
   : ident LPAREN (expression)? (COMMA expression)* RPAREN
   ;

printstmt
   : PRINT LPAREN (STRING | ident) RPAREN
   ;


beginstmt
   : LCURLY statements RCURLY
   ;

statements
   : statement ( SEMI statement)*
   ;

ifstmt
   : IF expression THEN beginstmt
   ;

whilestmt
   : WHILE expression DO beginstmt
   ;


expression

   : NOT expression #notExpression
   | expression STAR expression #starExpression
   | expression DIV expression #divExpression
   | expression MOD expression #modExpression
   | expression PLUS expression #plusExpression
   | expression MINUS expression #minusExpression
   | expression LE expression #leExpression
   | expression GE expression #geExpression
   | expression LT expression #ltExpression
   | expression GT expression #gtExpression
   | expression NOT_EQUAL expression #noteqExpression
   | expression EQUAL expression #eqExpression
   | expression AND expression #andExpression
   | expression OR expression #orExpression
   | LPAREN expression RPAREN #parenExpression
   | (PLUS | MINUS)? NUM_FLOAT #signedFloat
   | (PLUS | MINUS)? NUMBER #signedNumber
   | (PLUS | MINUS)? IDENT #signedIdent
   ;

//simpleExpression
//   : term (additiveoperator simpleExpression)?
//   ;
//
//term
//   : signedFactor (multiplicativeoperator term)?
//   ;
//
//signedFactor
//   : (PLUS | MINUS)? factor
//   ;
//
//bool_operator
//   : EQUAL
//   | NOT_EQUAL
//   | LT
//   | LE
//   | GE
//   | GT
//   ;
//
//additiveoperator
//   : PLUS
//   | MINUS
//   | OR
//   ;
//
//multiplicativeoperator
//   : STAR
//   | SLASH
//   /*| DIV
//   | MOD
//   */
//   | AND
//   ;
//
//factor
//   : ident
//   | number
//   | num_float
//   | LPAREN expression RPAREN
//   | NOT factor
//   ;

ident
   : IDENT
   ;

number
   : NUMBER
   ;

num_float
   : NUM_FLOAT
   ;

MAIN
   : M A I N
   ;

INT
   : I N T
   ;

FLOAT
   : F L O A T
   ;


PRINT
   : P R I N T
   ;


WHILE
   : W H I L E
   ;


DO
   : D O
   ;


IF
   : I F
   ;


THEN
   : T H E N
   ;


BEGIN
   : B E G I N
   ;


END
   : E N D
   ;


CALL
   : C A L L
   ;


CONST
   : C O N S T
   ;


VAR
   : V A R
   ;


PROCEDURE
   : P R O C E D U R E
   ;

FUNCTION
   : F U N C T I O N
   ;

BREAK
   : B R E A K
   ;

CONTINUE
   : C O N T I N U E
   ;

RETURN
   : R E T U R N
   ;

DEF
   : D E F
   ;

VOID
   : V O I D
   ;

/*
fragment A
   : ('a' | 'A')
   ;


fragment B
   : ('b' | 'B')
   ;


fragment C
   : ('c' | 'C')
   ;


fragment D
   : ('d' | 'D')
   ;


fragment E
   : ('e' | 'E')
   ;


fragment F
   : ('f' | 'F')
   ;


fragment G
   : ('g' | 'G')
   ;


fragment H
   : ('h' | 'H')
   ;


fragment I
   : ('i' | 'I')
   ;


fragment J
   : ('j' | 'J')
   ;


fragment K
   : ('k' | 'K')
   ;


fragment L
   : ('l' | 'L')
   ;


fragment M
   : ('m' | 'M')
   ;


fragment N
   : ('n' | 'N')
   ;


fragment O
   : ('o' | 'O')
   ;


fragment P
   : ('p' | 'P')
   ;


fragment Q
   : ('q' | 'Q')
   ;


fragment R
   : ('r' | 'R')
   ;


fragment S
   : ('s' | 'S')
   ;


fragment T
   : ('t' | 'T')
   ;


fragment U
   : ('u' | 'U')
   ;


fragment V
   : ('v' | 'V')
   ;


fragment W
   : ('w' | 'W')
   ;


fragment X
   : ('x' | 'X')
   ;


fragment Y
   : ('y' | 'Y')
   ;


fragment Z
   : ('z' | 'Z')
   ;
*/


AND
   : A N D
   ;

OR
   : O R
   ;

NOT
   : N O T
   ;


ASSIGN
   : '='
   ;


COMMA
   : ','
   ;


SEMI
   : ';'
   ;


COLON
   : ':'
   ;

PLUS
   : '+'
   ;


MINUS
   : '-'
   ;


STAR
   : '*'
   ;


DIV
   : '/'
   ;

MOD
   : '%'
   ;

EQUAL
   : '=='
   ;


NOT_EQUAL
   : '!='
   ;

LE
   : '<='
   ;

LT
   : '<'
   ;



GE
   : '>='
   ;


GT
   : '>'
   ;


LPAREN
   : '('
   ;


RPAREN
   : ')'
   ;


LCURLY
   : '{'
   ;


RCURLY
   : '}'
   ;

IDENT
   :   ('a' .. 'z') ('a' .. 'z')*
   ;


NUMBER
   : [0-9]+
   ;

NUM_FLOAT
   : ('0' .. '9')+ (('.' ('0' .. '9')+))
   ;

STRING
   : '"' .*? '"'
   | '\'' .*? '\''
   ;

COMMENT
   : '/*' .*? '*/' -> skip
   ;


WS
   : [ \t\r\n] -> skip
   ;

ErrorChar
   : .
   ;















   fragment A
      : ('A')
      ;


   fragment B
      : ('B')
      ;


   fragment C
      : ('C')
      ;


   fragment D
      : ('D')
      ;


   fragment E
      : ('E')
      ;


   fragment F
      : ('F')
      ;


   fragment G
      : ('G')
      ;


   fragment H
      : ('H')
      ;


   fragment I
      : ('I')
      ;


   fragment J
      : ('J')
      ;


   fragment K
      : ('K')
      ;


   fragment L
      : ('L')
      ;


   fragment M
      : ('M')
      ;


   fragment N
      : ('N')
      ;


   fragment O
      : ('O')
      ;


   fragment P
      : ('P')
      ;


   fragment Q
      : ('Q')
      ;


   fragment R
      : ('R')
      ;


   fragment S
      : ('S')
      ;


   fragment T
      : ('T')
      ;


   fragment U
      : ('U')
      ;


   fragment V
      : ('V')
      ;


   fragment W
      : ('W')
      ;


   fragment X
      : ('X')
      ;


   fragment Y
      : ('Y')
      ;


   fragment Z
      : ('Z')
      ;