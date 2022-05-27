package compiler;

public class CodeElement implements Codes{
    String op;
    String arg1;
    String arg2;
    String res;
    String GoTo;

    public CodeElement(String op, String arg1, String arg2, String res, String GoTo){

        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.res = res;
        this.GoTo = GoTo;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg1() {
        if(arg1 == null) return "";
        else return arg1;
    }

    public void setGoTo(String GoTo){
        this.GoTo = GoTo;
    }

    public String getGoTo() {
        if(GoTo == null) return "";
        else return GoTo;
    }

    @Override
    public String toString() {
        return ""+op+" "+res+" "+arg1+" "+arg2+" "+/*((GoTo == null) ? "" : */GoTo;

    }


    public String toStringCode() {
        return ""+((op == null) ? "" : op)+((res == null) ? "" : ", "+res)+((arg1 == null) ? "" : ", "+arg1)+((arg2 == null) ? "" : ", "+arg2)+((GoTo == null) ? "" : ", "+GoTo);

    }
}
