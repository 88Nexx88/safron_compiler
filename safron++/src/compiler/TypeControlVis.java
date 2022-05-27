package compiler;

import gram.safronBaseVisitor;
import gram.safronParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class TypeControlVis extends safronBaseVisitor {

    SymbolTable st;
    ChildTable ct;


    public List<String> errorList;
    public List<String> optimizationList = new ArrayList<>();
    public List<Integer> badIfWhileList = new ArrayList<>();
    public List<Integer> badAssignList = new ArrayList<>();

    boolean check = false;
    boolean beginstmt = false;
    int assignControl = 0;


    public TypeControlVis(SymbolTable st) {
        this.st = st;
        errorList = new ArrayList<>();

    }

    @Override
    public Object visitBlock(safronParser.BlockContext ctx) {
//        for(safronParser.ProcedureContext procedureContext : ctx.procedure()){
//            currentName = procedureContext.getChild(1).getText();
//            visitProcedure(procedureContext);
//        }
        return super.visitBlock(ctx);
    }



    @Override
    public Object visitFunction(safronParser.FunctionContext ctx) {
        for (ChildTable childTable : st.childTables) {
            if (childTable.nameTable.equals(ctx.getChild(2).getText())) {
                ct = childTable;
            }
        }

        for (int i = 0; i < ctx.getChildCount(); i++) {
            if(ct.table.get(ctx.getChild(i).getText()) != null){
                ct.table.get(ctx.getChild(i).getText()).assignVarLine = ctx.start.getLine();
            }
        }

        for (int i = 0; i < ctx.getChildCount(); i++) {
            visit(ctx.getChild(i));
        }
        ct = null;
        return null;
    }

    @Override
    public Object visitBeginstmt(safronParser.BeginstmtContext ctx) {
        boolean checkMainBegin = false;
        if(!beginstmt){
            beginstmt = true;
            checkMainBegin = true;
        }
        for(int i = 0; i < ctx.getChildCount(); i++){
            visit(ctx.getChild(i));
        }
        if(checkMainBegin){

            beginstmt = false;
        }

        return null;
    }

    @Override
    public Object visitSignedIdent(safronParser.SignedIdentContext ctx) {
        if(ct == null){
            if(st.table.get(ctx.getText()) == null){
                errorList.add("ERROR VAR [unknown var "+ ctx.getText() +"] line : " + ctx.start.getLine());
                //System.out.println("UNKNOWN VARIABLE "+ ctx.getText() +" line: " + ctx.start.getLine());
            }
        }
        else {
            if(ct != null && ct.table.get(ctx.getText()) == null) {
                if (st != null && st.table.get(ctx.getText()) == null) {
                    errorList.add("ERROR VAR [unknown var "+ ctx.getText() +"] line : " + ctx.start.getLine());
                    //System.out.println("UNKNOWN VARIABLE " + ctx.getText() + " line: " + ctx.start.getLine());
                }
            }
        }
        if(beginstmt) {
            if (st != null && st.table.get(ctx.getText()) != null) {
                if ((st.table.get(ctx.getText()).assignVarLine == 0)) {

                    errorList.add("ERROR VAR [var not assign "+ ctx.getText() +"] line : " + ctx.start.getLine());
                }
                st.table.get(ctx.getText()).setUsingVarLine(ctx.start.getLine());
            } else {
                if (ct != null && ct.table.get(ctx.getText()) != null) {
                    if ((ct.table.get(ctx.getText()).assignVarLine == 0)) {

                        errorList.add("ERROR VAR [var not assign "+ ctx.getText() +"] line : " + ctx.start.getLine());
                    }

                    ct.table.get(ctx.getText()).setUsingVarLine(ctx.start.getLine());
                }
            }


        }
        return super.visitSignedIdent(ctx);
    }

    @Override
    public Object visitIdent(safronParser.IdentContext ctx) {
        if(ct == null){
            if(st.table.get(ctx.getText()) == null){
                errorList.add("ERROR VAR [unknown var "+ ctx.getText() +"] line : " + ctx.start.getLine());
                //System.out.println("UNKNOWN VARIABLE "+ ctx.getText() +" line: " + ctx.start.getLine());
            }
        }
        else {
            if(ct != null && ct.table.get(ctx.getText()) == null) {
                if (st != null && st.table.get(ctx.getText()) == null) {
                    errorList.add("ERROR VAR [unknown var "+ ctx.getText() +"] line : " + ctx.start.getLine());
                    //System.out.println("UNKNOWN VARIABLE " + ctx.getText() + " line: " + ctx.start.getLine());
                }
            }
        }
        if(beginstmt) {

                if (st != null && st.table.get(ctx.getText()) != null) {
                    if ((st.table.get(ctx.getText()).assignVarLine == 0)) {

                        errorList.add("ERROR VAR [var not assign "+ ctx.getText() +"] line : " + ctx.start.getLine());
                    }
                    st.table.get(ctx.getText()).setUsingVarLine(ctx.start.getLine());
                } else {
                    if (ct != null && ct.table.get(ctx.getText()) != null) {
                        if ((ct.table.get(ctx.getText()).assignVarLine == 0)) {

                            errorList.add("ERROR VAR [var not assign "+ ctx.getText() +"] line : " + ctx.start.getLine());
                        }
                        ct.table.get(ctx.getText()).setUsingVarLine(ctx.start.getLine());
                    }
                }


        }


        return super.visitIdent(ctx);
    }


    @Override
    public Object visitIfstmt(safronParser.IfstmtContext ctx) {
        if(ctx.beginstmt().statements().statement().size() == 1){
            optimizationList.add("OPTIMIZATION [ifstmt line : "+ctx.start.getLine()+" is an empty]");
            badIfWhileList.add(ctx.start.getLine());
            return null;
        }
        return super.visitIfstmt(ctx);
    }

    @Override
    public Object visitWhilestmt(safronParser.WhilestmtContext ctx) {
        check = true;
        if(ctx.beginstmt().statements().statement().size() == 1){
            optimizationList.add("OPTIMIZATION [whilestmt line : "+ctx.start.getLine()+" is an empty]");
            badIfWhileList.add(ctx.start.getLine());
            return null;
        }
        for (int i = 0; i < ctx.getChildCount(); i++) {
            visit(ctx.getChild(i));
        }
        check = false;
        return null;

    }

    @Override
    public Object visitBreakstmt(safronParser.BreakstmtContext ctx) {
        if(!check){
            errorList.add("ERROR BREAK [break not in WHILEstmt] line : "+ctx.start.getLine());
        }
        return null;
    }

    @Override
    public Object visitContinuestmt(safronParser.ContinuestmtContext ctx) {
        if(!check){
            errorList.add("ERROR CONTINUE [continue not in WHILEstmt] line : "+ctx.start.getLine());
        }
        return null;
    }

    @Override
    public Object visitAssignstmt(safronParser.AssignstmtContext ctx) {
        String name =  ctx.getChild(0).getText();
        assignControl = 1;
        for(int i = 2; i < ctx.getChildCount(); i++){
            visit(ctx.getChild(i));
        }

        if(ctx.callstmt() != null){
            if(st != null && st.table.get(name) != null){

                if(st.table.get(name).assignVarLine != 0 && st.table.get(name).usingVarLine.size() == 0 && !(st.table.get(name).type.equals("FUN"))){
                    optimizationList.add("OPTIMIZATION [var "+name+" not use from "+st.table.get(name).assignVarLine+" to "+ctx.start.getLine()+" the compiler has removed unnecessary]");
                    badAssignList.add(st.table.get(name).assignVarLine);
                    //System.out.println("NOT USE"+ctx.start.getLine());
                }
                st.table.get(name).usingVarLine.clear();
                st.table.get(name).setAssignVar(ctx.start.getLine());


                for(ChildTable childTable : st.childTables){
                    if(childTable.nameTable.equals(ctx.callstmt().getChild(0).getText())){
                        if(childTable.typeFun != null) {
                            if (!childTable.typeFun.equals(st.table.get(name).type)) {
                                errorList.add("ERROR ASSIGN [type var not equal type return] line : " + ctx.start.getLine());
                                return null;
                            }
                        }
                        else {
                            errorList.add("ERROR ASSIGN [function void] line: " + ctx.start.getLine());
                            return null;
                        }
                    }
                }
            }
           if (ct != null && ct.table.get(name) != null){
               if(ct.table.get(name).assignVarLine != 0 && ct.table.get(name).usingVarLine.size() == 0 && !(ct.table.get(name).type.equals("FUN"))){
                   optimizationList.add("OPTIMIZATION [var "+name+" not use from "+ct.table.get(name).assignVarLine+" to "+ctx.start.getLine()+" the compiler has removed unnecessary]");
                   badAssignList.add(ct.table.get(name).assignVarLine);
                   //System.out.println("NOT USE"+ctx.start.getLine());
               }
               ct.table.get(name).usingVarLine.clear();

                   //System.out.println(ctx.start.getLine());
                   ct.table.get(name).setAssignVar(ctx.start.getLine());


               for(ChildTable childTable : st.childTables) {
                   if (childTable.nameTable.equals(ctx.callstmt().getChild(0).getText())) {
                       if(childTable.typeFun != null) {
                           if (!childTable.typeFun.equals(ct.table.get(name).type)) {
                               errorList.add("ERROR ASSIGN [type var not equal type return] line : " + ctx.start.getLine());
                               return null;
                           }
                       }
                       else {
                           errorList.add("ERROR ASSIGN [function void] line: " + ctx.start.getLine());
                           return null;
                       }
                   }
               }
           }

        }
        else {

                if (st != null && st.table.get(name) != null) {
                    if(st.table.get(name).assignVarLine != 0 && st.table.get(name).usingVarLine.size() == 0 ){
                        optimizationList.add("OPTIMIZATION [var "+name+" not use from "+st.table.get(name).assignVarLine+" to "+ctx.start.getLine()+" the compiler has removed unnecessary]");
                        badAssignList.add(st.table.get(name).assignVarLine);
                        //System.out.println("NOT USE"+ctx.start.getLine());
                    }
                    st.table.get(name).usingVarLine.clear();
                    //System.out.println(ctx.start.getLine());
                    st.table.get(name).setAssignVar(ctx.start.getLine());
                }
                if(ct != null && ct.table.get(name) != null) {
                    if(ct.table.get(name).assignVarLine != 0 && ct.table.get(name).usingVarLine.size() == 0 ){
                        optimizationList.add("OPTIMIZATION [var "+name+" not use from "+ct.table.get(name).assignVarLine+" to "+ctx.start.getLine()+" the compiler has removed unnecessary]");
                        badAssignList.add(ct.table.get(name).assignVarLine);
                        //.out.println("NOT USE"+ctx.start.getLine());
                    }
                    ct.table.get(name).usingVarLine.clear();
                    ct.table.get(name).setAssignVar(ctx.start.getLine());


                }

        }
        assignControl = 0;
        return null;
    }


    @Override
    public Object visitCallstmt(safronParser.CallstmtContext ctx) {
        if(ctx.getText().contains("+") || ctx.getText().contains("-") || ctx.getText().contains("/") || ctx.getText().contains("%") || ctx.getText().contains("*") || ctx.getText().contains("OR") || ctx.getText().contains("AND") || ctx.getText().contains("=") || ctx.getText().contains("<") || ctx.getText().contains(">"))
        {
           errorList.add("ERROR CALL [you can use only var | number | num_float when calling] line : "+ctx.start.getLine());
        }
        for(String s : st.table.keySet()){
            if(!st.table.get(s).type.equals("FUN")){
                st.table.get(s).setUsingVarLine(ctx.start.getLine());
            }
        }

        String name = ctx.getChild(0).getText();
        ChildTable cur = null;
        if (st != null && st.table.get(name) != null) {
            for (ChildTable ch : st.childTables) {
                if (ch.nameTable.equals(name)) {
                    cur = ch;
                    break;
                }
            }
        }
        int param_count = 0;

        for(int i = 0; i < ctx.getChildCount(); i++){
            if(ctx.getChild(i) instanceof safronParser.ExpressionContext){
                visit(ctx.getChild(i));
                param_count++;
            }
        }
        //System.out.println(param_count);

        if (cur != null) {
            //System.out.println(ctx.ident().size() - 1 + " " + cur.parametrs.size() + " " + ctx.getText());
            if (param_count != cur.parametrs.size()) {
                errorList.add("Call ERROR [too many parameters] line: " + ctx.start.getLine());
                //System.out.println("Call ERROR count parametrs ERROR line: " + ctx.start.getLine());
            }
            return null;
        }

        return null;

}

    @Override
    public Object visitReturnstmt(safronParser.ReturnstmtContext ctx) {
        String nameIdent = ctx.getChild(1).getText();
        if (ct != null ) {
            if (ct.typeFun == null | ct.typeFun.equals("VOID")) {
                errorList.add("ERROR RETURN [return not in function | type void] line: " + ctx.start.getLine());
                //System.out.println("Return not in Function " + ctx.start.getLine());
            }
            else {
                if (ct.table.get(nameIdent) != null) {
                    if (!((ct.typeFun.equals(ct.table.get(nameIdent).type)))) {
                        errorList.add("ERROR RETURN [type return incorrect] line: " + ctx.start.getLine());
                        //System.out.println("Return type ERROR " + ctx.start.getLine());
                    }
                }
                if (st.table.get(nameIdent) != null) {
                    if (!((ct.typeFun.equals(st.table.get(nameIdent).type)))) {
                        errorList.add("\"ERROR RETURN [type return incorrect] line: " + ctx.start.getLine());
                        //System.out.println("Return type ERROR " + ctx.start.getLine());
                    }
                }
            }

        } else {
            errorList.add("ERROR RETURN [return not in function | type void] line: " + ctx.start.getLine());
            //System.out.println("Return not in Function line: " + ctx.start.getLine());
        }

        return super.visitReturnstmt(ctx);
    }


}
