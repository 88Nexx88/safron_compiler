package compiler;

public interface Codes {
    /*
    int CONST = 1;       // Push integer value v.
    int STORE = 2;       // Pop value v and store it in local variable n.
    int LOAD = 3;	     // Push integer value stored in local variable n.
    int ADD = 4;         // Pop value v1, Pop value v2, Push v2 + v1.
    int MULTI = 5;       // Pop value v1, Pop value v2, Push v2 * v1.
    int SUB = 6;         // Pop value v1, Pop value v2, Push v2 - v1.
    int DIV = 7;         // Pop value v1, Pop value v2, Push v2 / v1.
    int ILT = 8;         // Pop value v1, Pop value v2, Push 1 if v2 < v1 else Push 0.

    int NO = 9;          // Pop value v, Push 1 if v = 0 else Push 0.
    int AND = 10;        // Pop value v1, Pop value v2, Push 0 if v1 * v2 = 0 else Push 1.
    int OR = 11;	     // Pop value v1, Pop value v2, Push 0 if v1 + v2 = 0 else Push 1.
    int GOTO = 12;       // Jump to instruction i.
    int IF = 13;   // Pop value v, if v = 0 jump to instruction i, else continue with next instruction.
    int INVOKE = 14;     // Push current activation and switch to the method having qualified name m.
    int RETURN = 15;     //	Pop activation and continue.
    int PRINT = 16;      //	Pop value and print.
    int STOP = 17;       //	Execution completed.


    int ILE = 18;
    int IGT = 19;
    int IGE = 20;

    int EQ = 21;
    int NEQ = 22;

     */
    String[] array = {"CONST", "STORE", "LOAD", "ADD", "MULTI", "SUB", "DIV", "ILT", "NO", "AND", "OR", "GOTO", "IF", "INVOKE", "RETURN", "PRINT", "STOP", "ILE", "IGT", "IGE", "EQ", "NEQ", "PARAM"};
    int CONST = 1;       // Push integer value v.
    int STORE = 2;       // Pop value v and store it in local variable n.
    int LOAD = 3;	     // Push integer value stored in local variable n.
    int ADD = 4;         // Pop value v1, Pop value v2, Push v2 + v1.
    int MULTI = 5;       // Pop value v1, Pop value v2, Push v2 * v1.
    int SUB = 6;         // Pop value v1, Pop value v2, Push v2 - v1.
    int DIV = 7;         // Pop value v1, Pop value v2, Push v2 / v1.
    int ILT = 8;         // Pop value v1, Pop value v2, Push 1 if v2 < v1 else Push 0.

    int NO = 9;          // Pop value v, Push 1 if v = 0 else Push 0.
    int AND = 10;        // Pop value v1, Pop value v2, Push 0 if v1 * v2 = 0 else Push 1.
    int OR = 11;	     // Pop value v1, Pop value v2, Push 0 if v1 + v2 = 0 else Push 1.
    int GOTO = 12;       // Jump to instruction i.
    int IF = 13;   // Pop value v, if v = 0 jump to instruction i, else continue with next instruction.
    int INVOKE = 14;     // Push current activation and switch to the method having qualified name m.
    int RETURN = 15;     //	Pop activation and continue.
    int PRINT = 16;      //	Pop value and print.
    int STOP = 17;       //	Execution completed.


    int ILE = 18;
    int IGT = 19;
    int IGE = 20;

    int EQ = 21;
    int NEQ = 22;
    int PARAM = 23;

}
