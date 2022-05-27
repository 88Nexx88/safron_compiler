package compiler;

import gram.safronBaseVisitor;
import gram.safronParser;

import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GenerationVis extends safronBaseVisitor implements CodesMIPS{
    int count = 0;
    int code = 0;

    List<String> constString = new ArrayList<>();
    List<String> floatConst = new ArrayList<>();
    ChildTable currentCT;

    int typeControl = 0;
    Stack<String> labelCurWhile = new Stack<>();

    public List<MethodProgramCodeTable> allsTable; // all program code list
    public List<GenCodeTable> allTable; // method program code list
    GenCodeTable CodeTable; // one label-block code list


    public List<String> errorList = new ArrayList<>();
    SymbolTable symbolTable;
    public List<String> optimizationList;
    public List<Integer> badIfWhileList;
    public List<Integer> badAssignList;
    boolean mars;

    public GenerationVis(SymbolTable symbolTable, List<Integer> badIfWhileList, List<String> optimizationList, List<Integer> badAssignList, boolean mars){

        this.symbolTable = symbolTable;
        this.badIfWhileList = badIfWhileList;
        this.optimizationList = optimizationList;
        this.badAssignList = badAssignList;
        this.mars = mars;
    }


    public String generation(){
        if(optimizationList.size() != 0) {
            System.out.println("Optimization program: ");
            for (String s : optimizationList) {
                System.out.println(s);
            }
            System.out.println("\n");
        }


        String res = "";
        res+=".data";
        for(int i = 0; i < constString.size();i++){
            res+="\n";
            res+="str"+(i+1)+" :\t.asciiz "+"\""+constString.get(i)+"\"";
        }
        res+="\n";
        for(String key : symbolTable.table.keySet()){
            if(!symbolTable.table.get(key).type.equals("FUN")){
                if(symbolTable.table.get(key).type.equals("INT")){
                    res+=key+" : \t.word " + "0\n";
                }
                else {
                    res+=key+" : \t.float " + "0.0\n";
                }
            }
        }
        for(ChildTable ct : symbolTable.childTables) {
            for (String key : ct.table.keySet()) {
                if(ct.table.get(key).type.equals("INT")){
                        res+=ct.nameTable +"_"+key+" : \t.word " + "0\n";
                    }
                    else {
                        res+=ct.nameTable +"_"+key+" : \t.float " + "0.0\n";
                    }

            }
        }
        res+="\n";
        for(int i = 0; i < floatConst.size();i++){
            res+="fl"+(i+1)+" : \t.float "+floatConst.get(i)+"\n";
        }
        res+="\n";
        res+=".text\n";
        res+=".globl main\n";
        res+="\n";
        for(int i = 0; i < allsTable.size();i++){
            allsTable.get(i).allTable.get(0).label = allsTable.get(i).nameMethod;
        }

        for(MethodProgramCodeTable t : allsTable){
            res+=(t.toStringCode());
        }



        return res;
    }







    @Override
    public Object visitProgram(safronParser.ProgramContext ctx) {
        allsTable = new ArrayList<>();
        MethodProgramCodeTable current = new MethodProgramCodeTable("main");
        code = 0;
        allTable = new ArrayList<>();
        visit(ctx.getChild(0).getChild(ctx.getChild(0).getChildCount()-1));
        current.setAllTable(allTable);
        CodeTable = new GenCodeTable("L"+code);
        CodeTable.addElement(loadImmediate, "10", null, "$v0", null);
        CodeTable.addElement("syscall", null, null, null, null);
        allTable.add(CodeTable);
        allsTable.add(current);
        for(int i = ctx.getChild(0).getChildCount()-2; i > 0; i--){
            visit(ctx.getChild(0).getChild(i));

        }
        return null;
    }

    @Override
    public Object visitFunction(safronParser.FunctionContext ctx) {
        MethodProgramCodeTable current = new MethodProgramCodeTable(ctx.getChild(2).getText());
        code = 0;
        allTable = new ArrayList<>();
        CodeTable = new GenCodeTable("L"+0);
        code++;
        for(ChildTable ct : symbolTable.childTables){
            if(ct.nameTable.equals(ctx.getChild(2).getText())){
                currentCT = ct;
            }
        }
        int c = 0;
        for(int i = 3; i < ctx.getChildCount();i++){
            if(ctx.getChild(i) instanceof safronParser.IdentContext){
                if(currentCT.table.get(ctx.getChild(i).getText()).type.equals("INT")) {
                    CodeTable.addElement(storeWord, currentCT.nameTable + "_" + ctx.getChild(i).getText(), null, "$a" + c, null);
                    c++;
                }
                else {
                    CodeTable.addElement("s.s", currentCT.nameTable + "_" + ctx.getChild(i).getText(), null, "$f" + (31-c), null);
                    c++;
                }
            }
        }
        allTable.add(CodeTable);
        visit(ctx.getChild(ctx.getChildCount()-2));
        CodeTable = new GenCodeTable("L"+code);
        CodeTable.addElement(jumpRegist, "$ra", null, null, null);
        allTable.add(CodeTable);
        current.setAllTable(allTable);
        allsTable.add(current);
        typeControl = 0;
        currentCT = null;
        return null;
    }

    @Override
    public Object visitWhilestmt(safronParser.WhilestmtContext ctx) {
        for(Integer i : badIfWhileList) {
            if (ctx.start.getLine() == i){
                return null;
            }
        }
        CodeTable = new GenCodeTable("L" + code);
        count = 0;
        Object arg1 = visit(ctx.getChild(1)); // gen_if_exp
        String nameCur;

        if(code == 0){
            nameCur = "main";
        }
        else {
            nameCur = "L" + code;
        }
        if(arg1 != null){

            CodeTable.addElement(IFeq, (String) arg1, "$zero", null, null);
        }
        labelCurWhile.add(nameCur);
        allTable.add(CodeTable);
        code++;//count do_st
        visit(ctx.getChild(3));
        labelCurWhile.pop();

        for(int i = 0; i < allTable.size(); i++){
            for (int j = 0; j < allTable.get(i).table.size(); j++){
                if(allTable.get(i).table.get(j).getArg1().equals("break")){
                    allTable.get(i).table.get(j).setArg1(null);
                    if(currentCT == null) {
                        allTable.get(i).table.get(j).setGoTo("L" + code);
                    }
                    else {
                        allTable.get(i).table.get(j).setGoTo(currentCT.nameTable+"_L" + code);
                    }
                }
            }
        }
        if(currentCT == null){
            CodeTable.addElement(jump, null, null, null, nameCur);
        }
        else {
            CodeTable.addElement(jump, null, null, null, currentCT.nameTable+"_"+nameCur);
        }
        for(GenCodeTable ct : allTable){
            if(ct.label.equals(nameCur) || (ct.label.equals("L0") && (ct.table.get(ct.table.size()-1).op.equals("bc1t") || ct.table.get(ct.table.size()-1).op.equals("bc1f") || ct.table.get(ct.table.size()-1).op.equals("j")))) {
                if (currentCT != null) {
                    ct.table.get(ct.table.size() - 1).setGoTo(currentCT.nameTable + "_L" + code);

                }
                else {
                    ct.table.get(ct.table.size() - 1).setGoTo("L" + code);
                }
            }
        }
        typeControl = 0;
        return null;
    }



    @Override
    public Object visitCallstmt(safronParser.CallstmtContext ctx) {

        int c = 0;
        for(int i = 1; i < ctx.getChildCount(); i++){
            Object arg1 = visit(ctx.getChild(i));
            int flag = 0;
            if(arg1 != null) {
                if (arg1 instanceof safronParser.SignedIdentContext) {
                    arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
                    if(symbolTable.table.get(arg1) != null){
                        if(symbolTable.table.get(arg1).type.equals("INT")){
                            typeControl = 1;
                            flag = 4;
                        }
                        else {
                            typeControl = 2;
                            flag = 5;
                        }
                    }
                    else {
                        if(currentCT.table.get(arg1) != null){
                            if(currentCT.table.get(arg1).type.equals("INT")){
                                arg1 = currentCT.nameTable+"_"+arg1;
                                typeControl = 1;
                                flag = 4;
                            }
                            else {
                                arg1 = currentCT.nameTable+"_"+arg1;
                                typeControl = 2;
                                flag = 5;
                            }
                        }
                    }
                }

                if (arg1 instanceof safronParser.SignedNumberContext) {
                    arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
                    flag = 2;
                }

                if (arg1 instanceof safronParser.SignedFloatContext) {
                    arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
                    flag = 3;
                }
                if(flag == 0){

                }
                if(flag == 2){
                    CodeTable.addElement(loadImmediate, (String) arg1, null, "$a" + c, null);
                }

                if(flag == 3){
                    if(mars) {
                        floatConst.add((String) arg1);
                        CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f" + (31 - c), null);
                    }
                    else {
                        CodeTable.addElement("li.s", (String) arg1, null, "$f" + (31 - c), null);
                    }
                }

                if(flag == 4){
                    CodeTable.addElement(loadWord, (String) arg1, null, "$a" + c, null);
                }

                if(flag == 5){
                    CodeTable.addElement("l.s", (String) arg1, null, "$f"+(31-c), null);
                }

                c++;
            }


        }
        if(currentCT != null){ //save arguments and ra
            int size = (currentCT.table.size()+1)*4;

            CodeTable.addElement(addi, "$sp", "-"+size, "$sp", null);
            size-=4;
            CodeTable.addElement(storeWord, size+"($sp)", null, "$ra", null);
            for(String s : currentCT.table.keySet()){
                size-=4;
                if( currentCT.table.get(s).type.equals("INT")){
                    CodeTable.addElement(loadWord, currentCT.nameTable+"_"+s, null, "$s0", null);
                    CodeTable.addElement(storeWord, size+"($sp)", null, "$s0", null);
                }
                else {
                    CodeTable.addElement("l.s", currentCT.nameTable + "_" + s, null, "$f0", null);
                    CodeTable.addElement("s.s", size + "($sp)", null, "$f0", null);
                }
            }
        }

        count++;
        CodeTable.addElement(jumpLink, null, null, null, ctx.getChild(0).getText());

        if(currentCT != null){//load arguments and ra
            int size = (currentCT.table.size()+1)*4;

            size-=4;
            CodeTable.addElement(loadWord, size+"($sp)", null, "$ra", null);
            for(String s : currentCT.table.keySet()){
                size-=4;
                if( currentCT.table.get(s).type.equals("INT")){
                    CodeTable.addElement(loadWord, size+"($sp)", null, "$s0", null);
                    CodeTable.addElement(storeWord, currentCT.nameTable+"_"+s, null, "$s0", null);
                }
                else {
                    CodeTable.addElement("l.s", size+"($sp)", null, "$f0", null);
                    CodeTable.addElement("s.s", currentCT.nameTable+"_"+s, null, "$f0", null);
                }

            }
            CodeTable.addElement(addi, "$sp", ""+(currentCT.table.size()+1)*4, "$sp", null);
        }
        return "$s"+count;
    }

    @Override
    public Object visitBreakstmt(safronParser.BreakstmtContext ctx) {
        CodeTable = new GenCodeTable("L" + code);
        CodeTable.addElement(jumpLink, "break", null, null, labelCurWhile.peek());
        allTable.add(CodeTable);
        code++;
        return null;
    }

    @Override
    public Object visitReturnstmt(safronParser.ReturnstmtContext ctx) {
        int flag = 0;
        if(currentCT == null){
            errorList.add("ERROR RETURN [not in function] line : "+ctx.start.getLine());
        }
        else {
            if(symbolTable.table.get(ctx.getChild(1).getText()) != null){
                if(symbolTable.table.get(ctx.getChild(1).getText()).equals("INT")){
                    flag = 1;
                }
                else {
                    flag = 3;
                }

            }
            else {
                if(currentCT.table.get(ctx.getChild(1).getText()) != null){
                    if(currentCT.table.get(ctx.getChild(1).getText()).type.equals("INT")){
                        flag = 2;
                    }
                    else {
                        flag = 4;
                    }

                }
            }
            if(flag == 1){
                CodeTable.addElement(loadWord, ctx.getChild(1).getText(), null, "$s0", null);
            }
            if(flag == 3){
                CodeTable.addElement("l.s", ctx.getChild(1).getText(), null, "$f0", null);
            }
            if(flag == 4){
                CodeTable.addElement("l.s", currentCT.nameTable+"_"+ctx.getChild(1).getText(), null, "$f0", null);
            }
            if(flag == 2){
                CodeTable.addElement(loadWord, currentCT.nameTable+"_"+ctx.getChild(1).getText(), null, "$s0", null);
            }
            if(flag == 1 || flag == 2) {
                CodeTable.addElement(move, "$s0", null, "$v1", null);
            }
            else {
                CodeTable.addElement("mov.s", "$f0", null, "$f26", null);
            }
            CodeTable.addElement(jumpRegist, "$ra", null, null, null);
        }
        return null;
    }

    @Override
    public Object visitContinuestmt(safronParser.ContinuestmtContext ctx) {
        CodeTable = new GenCodeTable("L" + code);
        CodeTable.addElement(jumpLink, null, null, null, labelCurWhile.peek());
        allTable.add(CodeTable);
        code++;
        return null;
    }

    @Override
    public Object visitIfstmt(safronParser.IfstmtContext ctx) {
        for(Integer i : badIfWhileList) {
            if (ctx.start.getLine() == i){
                return null;
            }
        }
        CodeTable = new GenCodeTable("L" + code);


        count = 0;
        Object arg1 = visit(ctx.getChild(1)); // gen_if_exp
        String nameCur = "L" + code;
        CodeTable.addElement(IFeq, (String) arg1, "$zero", null, null);
        allTable.add(CodeTable);
        code++;//count then_st
        visit(ctx.getChild(3));
        GenCodeTable t = null;
        CodeElement e = null;
        for(GenCodeTable ct : allTable){
            if(ct.label.equals(nameCur)){
                for(CodeElement ce : ct.table){
                    if(ce.op.equals("beq")){
                        if(arg1 != null) {
                            if (currentCT != null) {
                                ce.setGoTo(currentCT.nameTable + "_L" + code);
                            } else ce.setGoTo("L" + code);
                        }
                        else {
                            t = ct;
                            e = ce;
                        }
                    }
                    if(arg1 == null) {
                        if (ce.op.contains("bc1")) {
                            if (currentCT != null) {
                                ce.setGoTo(currentCT.nameTable + "_L" + code);
                            } else ce.setGoTo("L" + code);
                        }
                    }
                }
            }
        }
        if(t != null && e != null) t.table.remove(e);
        typeControl = 0;
        return null;
    }

    @Override
    public Object visitPrintstmt(safronParser.PrintstmtContext ctx) {
        CodeTable = new GenCodeTable("L" + code);
        //String arg1 = ctx.getChild(2).getText();
        String arg1 = ctx.getChild(2).getText();
        if(arg1.contains("'") || arg1.contains("\"")){
            constString.add(arg1.substring(1, arg1.length()-1));
            CodeTable.addElement(loadImmediate, "4", null, "$v0", null);
            CodeTable.addElement(loadAddress, "str"+constString.size(), null, "$a0", null);
        }
        else {

                if(symbolTable.table.get(arg1) != null){
                    if(symbolTable.table.get(arg1).type.equals("INT")){
                        CodeTable.addElement(loadImmediate, "1", null, "$v0", null);
                        CodeTable.addElement(loadWord, arg1, null, "$a0", null);
                    }
                    else {
                        CodeTable.addElement(loadImmediate, "2", null, "$v0", null);
                        CodeTable.addElement("l.s", arg1, null, "$f12", null);
                    }
                }
                else {
                    if(currentCT.table.get(arg1) != null){
                        if(currentCT.table.get(arg1).type.equals("INT")){
                            CodeTable.addElement(loadImmediate, "1", null, "$v0", null);
                            CodeTable.addElement(loadWord, currentCT.nameTable +"_"+arg1, null, "$a0", null);
                        }
                        else {
                            CodeTable.addElement(loadImmediate, "2", null, "$v0", null);
                            CodeTable.addElement("l.s", currentCT.nameTable +"_"+arg1, null, "$f12", null);
                        }
                    }

                }


        }

        CodeTable.addElement("syscall",  null, null, null, null);
        allTable.add(CodeTable);
        code++;//count then_st
        return null;
    }

    @Override
    public Object visitAndExpression(safronParser.AndExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
        }




        //

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
        }
        if(typeControl == 2){
            errorList.add("ERROR IFstmt | whilestmt [many float logical operation in ifstmt; pls using nested ifstmt | whilestmt] line : "+ctx.start.getLine());
            return null;
        }

        count++;
        CodeTable.addElement(and, (String) arg1, (String) arg2, "$t"+count, null);

        return "$t"+count;
    }

    @Override
    public Object visitOrExpression(safronParser.OrExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
        }




        //

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
        }

        if(typeControl == 2){
            errorList.add("ERROR IFstmt | whilestmt [many float logical operation in ifstmt; pls using nested ifstmt | whilestmt] line : "+ctx.start.getLine());
            return null;
        }

            count++;
            CodeTable.addElement(or, (String) arg1, (String) arg2, "$t" + count, null);


        return "$t"+count;
    }

    @Override
    public Object visitNotExpression(safronParser.NotExpressionContext ctx) {


        Object arg1 = visit(ctx.getChild(1));

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
        }
        if(typeControl == 2){
            errorList.add("ERROR IFstmt | whilestmt [many float logical operation in ifstmt; pls using nested ifstmt | whilestmt] line : "+ctx.start.getLine());
            return null;
        }


            count++;
            CodeTable.addElement(not, (String) arg1, null, "$t" + count, null);
            CodeTable.addElement(add, "$t" + count, "2", "$t" + count, null);




        return "$t"+count;
    }

    @Override
    public Object visitGeExpression(safronParser.GeExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        int flag1 = 0;
        int flag2 = 0;

        int type1 = 0;
        int type2 = 0;

        if(arg1 instanceof String){
            type1 = typeControl;
            flag1 = 1;//string
        }

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
            flag1 = 2;//int
            type1 = 1;//int
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
            flag1 = 3;//float
            type1 = 2;
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
            if(symbolTable.table.get(arg1) != null){
                if(symbolTable.table.get(arg1).type.equals("INT")){
                    flag1 = 4;//ident_int
                    type1 = 1;
                }
                else {
                    flag1 = 5;//ident_float
                    type1 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg1) != null){
                    if(currentCT.table.get(arg1).type.equals("INT")){
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 4;
                        type1 = 1;
                    }
                    else {
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 5;
                        type1 = 2;
                    }
                }
            }
        }




        //
        if(arg2 instanceof String){
            type2 = typeControl;
            flag2 = 1;
        }

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
            flag2 = 2;
            type2 = 1;
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
            flag2 = 3;
            type2 = 2;
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
            if(symbolTable.table.get(arg2) != null){
                if(symbolTable.table.get(arg2).type.equals("INT")){
                    flag2 = 4;
                    type2 = 1;
                }
                else {
                    flag2 = 5;
                    type2 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg2) != null){
                    if(currentCT.table.get(arg2).type.equals("INT")){
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 4;
                        type2 = 1;
                    }
                    else {
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 5;
                        type2 = 2;
                    }
                }
            }
        }
        count++;
        String res= "";
        if(type1 == type2){
            if(type1 == 1){
                typeControl = 1;
                res = "$t"+count;
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement(sge, (String) arg1, (String) arg2, res, null);
                }
                else {
                    if(flag1 == 2 && flag2 == 1){//num t2
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(sge, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 2){//t1 num
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sge, (String) arg1, "$s1", res, null);
                    }
                    if(flag1 == 4 && flag2 == 1){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(sge, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 4){
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sge, (String) arg1, "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 4){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sge, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 2){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sge, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 2){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sge, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 4){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sge, "$s0", "$s1", res, null);
                    }

                }
            }
            else {
                if (flag1 == 1 && flag2 == 1) {
                    CodeTable.addElement("c.lt.s", (String) arg1, (String) arg2, null, null);
                } else {
                    if (flag1 == 3 && flag2 == 1) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("c.lt.s", "$f0", (String) arg2, null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.lt.s", "$f0", (String) arg2, null, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("c.lt.s", (String) arg1, "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.lt.s", (String) arg1, "$f1", null, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("c.lt.s", (String) arg1, "$f1", null, null);
                    }

                    if (flag1 == 5 && flag2 == 1) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("c.lt.s", "$f0", (String) arg2, null, null);
                    }

                    if (flag1 == 5 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 5) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 5 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                    }
                    CodeTable.addElement("bc1t", null, null, null, null);
                    typeControl = 2;
                    return null;
                }
            }
        }
        else {
            errorList.add("ERROR TYPE [use operation float op float | int op int] line : "+ctx.start.getLine());
        }

        return res;
    }


    @Override
    public Object visitGtExpression(safronParser.GtExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        int flag1 = 0;
        int flag2 = 0;

        int type1 = 0;
        int type2 = 0;

        if(arg1 instanceof String){
            type1 = typeControl;
            flag1 = 1;//string
        }

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
            flag1 = 2;//int
            type1 = 1;//int
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
            flag1 = 3;//float
            type1 = 2;
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
            if(symbolTable.table.get(arg1) != null){
                if(symbolTable.table.get(arg1).type.equals("INT")){
                    flag1 = 4;//ident_int
                    type1 = 1;
                }
                else {
                    flag1 = 5;//ident_float
                    type1 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg1) != null){
                    if(currentCT.table.get(arg1).type.equals("INT")){
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 4;
                        type1 = 1;
                    }
                    else {
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 5;
                        type1 = 2;
                    }
                }
            }
        }




        //
        if(arg2 instanceof String){
            type2 = typeControl;
            flag2 = 1;
        }

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
            flag2 = 2;
            type2 = 1;
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
            flag2 = 3;
            type2 = 2;
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
            if(symbolTable.table.get(arg2) != null){
                if(symbolTable.table.get(arg2).type.equals("INT")){
                    flag2 = 4;
                    type2 = 1;
                }
                else {
                    flag2 = 5;
                    type2 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg2) != null){
                    if(currentCT.table.get(arg2).type.equals("INT")){
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 4;
                        type2 = 1;
                    }
                    else {
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 5;
                        type2 = 2;
                    }
                }
            }
        }
        count++;
        String res= "";
        if(type1 == type2){
            if(type1 == 1){
                typeControl = 1;
                res = "$t"+count;
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement(sgt, (String) arg1, (String) arg2, res, null);
                }
                else {
                    if(flag1 == 2 && flag2 == 1){//num t2
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(sgt, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 2){//t1 num
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sgt, (String) arg1, "$s1", res, null);
                    }
                    if(flag1 == 4 && flag2 == 1){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(sgt, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 4){
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sgt, (String) arg1, "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 4){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sgt, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 2){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sgt, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 2){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sgt, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 4){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sgt, "$s0", "$s1", res, null);
                    }

                }
            }
            else {
                if (flag1 == 1 && flag2 == 1) {
                    CodeTable.addElement("c.le.s", (String) arg1, (String) arg2, null, null);
                } else {
                    if (flag1 == 3 && flag2 == 1) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("c.le.s", "$f0", (String) arg2, null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.le.s", "$f0", (String) arg2, null, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("c.le.s", (String) arg1, "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.le.s", (String) arg1, "$f1", null, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("c.le.s", (String) arg1, "$f1", null, null);
                    }

                    if (flag1 == 5 && flag2 == 1) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("c.le.s", "$f0", (String) arg2, null, null);
                    }

                    if (flag1 == 5 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 5) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 5 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                    }
                    CodeTable.addElement("bc1t", null, null, null, null);
                    typeControl = 2;
                    return null;
                }
            }
        }
        else {
            errorList.add("ERROR TYPE [use operation float op float | int op int] line : "+ctx.start.getLine());
        }
        return res;
    }

    @Override
    public Object visitLeExpression(safronParser.LeExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        int flag1 = 0;
        int flag2 = 0;

        int type1 = 0;
        int type2 = 0;

        if(arg1 instanceof String){
            type1 = typeControl;
            flag1 = 1;//string
        }

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
            flag1 = 2;//int
            type1 = 1;//int
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
            flag1 = 3;//float
            type1 = 2;
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
            if(symbolTable.table.get(arg1) != null){
                if(symbolTable.table.get(arg1).type.equals("INT")){
                    flag1 = 4;//ident_int
                    type1 = 1;
                }
                else {
                    flag1 = 5;//ident_float
                    type1 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg1) != null){
                    if(currentCT.table.get(arg1).type.equals("INT")){
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 4;
                        type1 = 1;
                    }
                    else {
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 5;
                        type1 = 2;
                    }
                }
            }
        }




        //
        if(arg2 instanceof String){
            type2 = typeControl;
            flag2 = 1;
        }

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
            flag2 = 2;
            type2 = 1;
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
            flag2 = 3;
            type2 = 2;
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
            if(symbolTable.table.get(arg2) != null){
                if(symbolTable.table.get(arg2).type.equals("INT")){
                    flag2 = 4;
                    type2 = 1;
                }
                else {
                    flag2 = 5;
                    type2 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg2) != null){
                    if(currentCT.table.get(arg2).type.equals("INT")){
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 4;
                        type2 = 1;
                    }
                    else {
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 5;
                        type2 = 2;
                    }
                }
            }
        }
        count++;
        String res= "";
        if(type1 == type2){
            if(type1 == 1){
                typeControl = 1;
                res = "$t"+count;
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement(sle, (String) arg1, (String) arg2, res, null);
                }
                else {
                    if(flag1 == 2 && flag2 == 1){//num t2
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(sle, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 2){//t1 num
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sle, (String) arg1, "$s1", res, null);
                    }
                    if(flag1 == 4 && flag2 == 1){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(sle, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 4){
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sle, (String) arg1, "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 4){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sle, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 2){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sle, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 2){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sle, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 4){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sle, "$s0", "$s1", res, null);
                    }

                }
            }
            else {
                if (flag1 == 1 && flag2 == 1) {
                    CodeTable.addElement("c.le.s", (String) arg1, (String) arg2, null, null);
                } else {
                    if (flag1 == 3 && flag2 == 1) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("c.le.s", "$f0", (String) arg2, null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.le.s", "$f0", (String) arg2, null, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("c.le.s", (String) arg1, "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.le.s", (String) arg1, "$f1", null, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("c.le.s", (String) arg1, "$f1", null, null);
                    }

                    if (flag1 == 5 && flag2 == 1) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("c.le.s", "$f0", (String) arg2, null, null);
                    }

                    if (flag1 == 5 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 5) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 5 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("c.le.s", "$f0", "$f1", null, null);
                    }
                    CodeTable.addElement("bc1f", null, null, null, null);
                    typeControl = 2;
                    return null;
                }
            }
        }
        else {
            errorList.add("ERROR TYPE [use operation float op float | int op int] line : "+ctx.start.getLine());
        }

        return res;
    }

    @Override
    public Object visitLtExpression(safronParser.LtExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        int flag1 = 0;
        int flag2 = 0;

        int type1 = 0;
        int type2 = 0;

        if(arg1 instanceof String){
            type1 = typeControl;
            flag1 = 1;//string
        }

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
            flag1 = 2;//int
            type1 = 1;//int
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
            flag1 = 3;//float
            type1 = 2;
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
            if(symbolTable.table.get(arg1) != null){
                if(symbolTable.table.get(arg1).type.equals("INT")){
                    flag1 = 4;//ident_int
                    type1 = 1;
                }
                else {
                    flag1 = 5;//ident_float
                    type1 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg1) != null){
                    if(currentCT.table.get(arg1).type.equals("INT")){
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 4;
                        type1 = 1;
                    }
                    else {
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 5;
                        type1 = 2;
                    }
                }
            }
        }




        //
        if(arg2 instanceof String){
            type2 = typeControl;
            flag2 = 1;
        }

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
            flag2 = 2;
            type2 = 1;
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
            flag2 = 3;
            type2 = 2;
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
            if(symbolTable.table.get(arg2) != null){
                if(symbolTable.table.get(arg2).type.equals("INT")){
                    flag2 = 4;
                    type2 = 1;
                }
                else {
                    flag2 = 5;
                    type2 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg2) != null){
                    if(currentCT.table.get(arg2).type.equals("INT")){
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 4;
                        type2 = 1;
                    }
                    else {
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 5;
                        type2 = 2;
                    }
                }
            }
        }
        count++;
        String res= "";
        if(type1 == type2){
            if(type1 == 1){
                typeControl = 1;
                res = "$t"+count;
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement(slt, (String) arg1, (String) arg2, res, null);
                }
                else {
                    if(flag1 == 2 && flag2 == 1){//num t2
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(slt, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 2){//t1 num
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(slt, (String) arg1, "$s1", res, null);
                    }
                    if(flag1 == 4 && flag2 == 1){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(slt, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 4){
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(slt, (String) arg1, "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 4){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(slt, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 2){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(slt, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 2){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(slt, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 4){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(slt, "$s0", "$s1", res, null);
                    }

                }
            }
            else {
                if (flag1 == 1 && flag2 == 1) {
                    CodeTable.addElement("c.lt.s", (String) arg1, (String) arg2, null, null);
                } else {
                    if (flag1 == 3 && flag2 == 1) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("c.lt.s", "$f0", (String) arg2, null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.lt.s", "$f0", (String) arg2, null, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("c.lt.s", (String) arg1, "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.lt.s", (String) arg1, "$f1", null, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("c.lt.s", (String) arg1, "$f1", null, null);
                    }

                    if (flag1 == 5 && flag2 == 1) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("c.lt.s", "$f0", (String) arg2, null, null);
                    }

                    if (flag1 == 5 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 5) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 5 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("c.lt.s", "$f0", "$f1", null, null);
                    }
                    CodeTable.addElement("bc1f", null, null, null, null);
                    typeControl = 2;
                    return null;
                }
            }
        }
        else {
            errorList.add("ERROR TYPE [use operation float op float | int op int] line : "+ctx.start.getLine());
        }

        return res;
    }

    @Override
    public Object visitEqExpression(safronParser.EqExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        int flag1 = 0;
        int flag2 = 0;

        int type1 = 0;
        int type2 = 0;

        if(arg1 instanceof String){
            type1 = typeControl;
            flag1 = 1;//string
        }

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
            flag1 = 2;//int
            type1 = 1;//int
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
            flag1 = 3;//float
            type1 = 2;
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
            if(symbolTable.table.get(arg1) != null){
                if(symbolTable.table.get(arg1).type.equals("INT")){
                    flag1 = 4;//ident_int
                    type1 = 1;
                }
                else {
                    flag1 = 5;//ident_float
                    type1 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg1) != null){
                    if(currentCT.table.get(arg1).type.equals("INT")){
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 4;
                        type1 = 1;
                    }
                    else {
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 5;
                        type1 = 2;
                    }
                }
            }
        }




        //
        if(arg2 instanceof String){
            type2 = typeControl;
            flag2 = 1;
        }

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
            flag2 = 2;
            type2 = 1;
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
            flag2 = 3;
            type2 = 2;
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
            if(symbolTable.table.get(arg2) != null){
                if(symbolTable.table.get(arg2).type.equals("INT")){
                    flag2 = 4;
                    type2 = 1;
                }
                else {
                    flag2 = 5;
                    type2 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg2) != null){
                    if(currentCT.table.get(arg2).type.equals("INT")){
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 4;
                        type2 = 1;
                    }
                    else {
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 5;
                        type2 = 2;
                    }
                }
            }
        }
        count++;
        String res= "";
        if(type1 == type2){
            if(type1 == 1){
                typeControl = 1;
                res = "$t"+count;
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement(seq, (String) arg1, (String) arg2, res, null);
                }
                else {
                    if(flag1 == 2 && flag2 == 1){//num t2
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(seq, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 2){//t1 num
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(seq, (String) arg1, "$s1", res, null);
                    }
                    if(flag1 == 4 && flag2 == 1){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(seq, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 4){
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(seq, (String) arg1, "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 4){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(seq, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 2){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(seq, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 2){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(seq, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 4){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(seq, "$s0", "$s1", res, null);
                    }

                }
            }
            else {
                if (flag1 == 1 && flag2 == 1) {
                    CodeTable.addElement("c.eq.s", (String) arg1, (String) arg2, null, null);
                } else {
                    if (flag1 == 3 && flag2 == 1) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("c.eq.s", "$f0", (String) arg2, null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.eq.s", "$f0", (String) arg2, null, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("c.eq.s", (String) arg1, "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.eq.s", (String) arg1, "$f1", null, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("c.eq.s", (String) arg1, "$f1", null, null);
                    }

                    if (flag1 == 5 && flag2 == 1) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("c.eq.s", "$f0", (String) arg2, null, null);
                    }

                    if (flag1 == 5 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 5) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 5 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                    }
                    CodeTable.addElement("bc1f", null, null, null, null);
                    typeControl = 2;
                    return null;
                }
            }
        }
        else {
            errorList.add("ERROR TYPE [use operation float op float | int op int] line : "+ctx.start.getLine());
        }

        return res;
    }

    @Override
    public Object visitNoteqExpression(safronParser.NoteqExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        int flag1 = 0;
        int flag2 = 0;

        int type1 = 0;
        int type2 = 0;

        if(arg1 instanceof String){
            type1 = typeControl;
            flag1 = 1;//string
        }

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
            flag1 = 2;//int
            type1 = 1;//int
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
            flag1 = 3;//float
            type1 = 2;
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
            if(symbolTable.table.get(arg1) != null){
                if(symbolTable.table.get(arg1).type.equals("INT")){
                    flag1 = 4;//ident_int
                    type1 = 1;
                }
                else {
                    flag1 = 5;//ident_float
                    type1 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg1) != null){
                    if(currentCT.table.get(arg1).type.equals("INT")){
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 4;
                        type1 = 1;
                    }
                    else {
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 5;
                        type1 = 2;
                    }
                }
            }
        }




        //
        if(arg2 instanceof String){
            type2 = typeControl;
            flag2 = 1;
        }

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
            flag2 = 2;
            type2 = 1;
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
            flag2 = 3;
            type2 = 2;
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
            if(symbolTable.table.get(arg2) != null){
                if(symbolTable.table.get(arg2).type.equals("INT")){
                    flag2 = 4;
                    type2 = 1;
                }
                else {
                    flag2 = 5;
                    type2 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg2) != null){
                    if(currentCT.table.get(arg2).type.equals("INT")){
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 4;
                        type2 = 1;
                    }
                    else {
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 5;
                        type2 = 2;
                    }
                }
            }
        }
        count++;
        String res= "";
        if(type1 == type2){
            if(type1 == 1){
                typeControl = 1;
                res = "$t"+count;
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement(sne, (String) arg1, (String) arg2, res, null);
                }
                else {
                    if(flag1 == 2 && flag2 == 1){//num t2
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(sne, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 2){//t1 num
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sne, (String) arg1, "$s1", res, null);
                    }
                    if(flag1 == 4 && flag2 == 1){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(sne, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 4){
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sne, (String) arg1, "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 4){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sne, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 2){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sne, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 2){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sne, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 4){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sne, "$s0", "$s1", res, null);
                    }

                }
            }
            else {
                if (flag1 == 1 && flag2 == 1) {
                    CodeTable.addElement("c.eq.s", (String) arg1, (String) arg2, null, null);
                } else {
                    if (flag1 == 3 && flag2 == 1) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("c.eq.s", "$f0", (String) arg2, null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.eq.s", "$f0", (String) arg2, null, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("c.eq.s", (String) arg1, "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.eq.s", (String) arg1, "$f1", null, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("c.eq.s", (String) arg1, "$f1", null, null);
                    }

                    if (flag1 == 5 && flag2 == 1) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("c.eq.s", "$f0", (String) arg2, null, null);
                    }

                    if (flag1 == 5 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 5) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                        }
                    }

                    if (flag1 == 5 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("c.eq.s", "$f0", "$f1", null, null);
                    }
                    CodeTable.addElement("bc1t", null, null, null, null);
                    typeControl = 2;
                    return null;
                }
            }
        }
        else {
            errorList.add("ERROR TYPE [use operation float op float | int op int] line : "+ctx.start.getLine());
        }

        return res;
    }

    @Override
    public Object visitAssignstmt(safronParser.AssignstmtContext ctx) {
        for(int i = 0; i < badAssignList.size();i++){
            if(ctx.start.getLine() == badAssignList.get(i)){
                return null;
            }
        }
        CodeTable = new GenCodeTable("L" + code);
        code ++;
        int flag = 0;
        count = 0;
        Object arg1 = "";
        if(ctx.getChild(2) instanceof safronParser.CallstmtContext){
            visit(ctx.getChild(2));
            flag = 10;
        }
        else {
            arg1 = visit(ctx.getChild(2));
            flag = 1;
        }

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
            typeControl = 1;
            flag = 2;
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
            typeControl = 2;
            flag = 3;
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
            if(symbolTable.table.get(arg1) != null){
                if(symbolTable.table.get(arg1).type.equals("INT")){
                    typeControl = 1;
                    flag = 4;
                }
                else {
                    typeControl = 2;
                    flag = 5;
                }
            }
            else {
                if(currentCT.table.get(arg1) != null){
                    if(currentCT.table.get(arg1).type.equals("INT")){
                        arg1 = currentCT.nameTable+"_"+arg1;
                        typeControl = 1;
                        flag = 4;
                    }
                    else {
                        arg1 = currentCT.nameTable+"_"+arg1;
                        typeControl = 2;
                        flag = 5;
                    }
                }
            }
        }
        String res = "";
        int typeF = 0;
        if(symbolTable.table.get(ctx.getChild(0).getText()) != null){
            if(flag != 10) {
                if (typeControl == ((symbolTable.table.get(ctx.getChild(0).getText()).type.equals("INT")) ? 1 : 2)) {
                    res = ctx.getChild(0).getText();
                } else {
                    errorList.add("ERROR TYPE [use operation float op float | int op int] line : " + ctx.start.getLine());
                }
            }
            else {
                res = ctx.getChild(0).getText();
            }

        }
        else {
            if(currentCT.table.get(ctx.getChild(0).getText()) != null){
                if(flag != 10) {
                    if (typeControl == ((currentCT.table.get(ctx.getChild(0).getText()).type.equals("INT")) ? 1 : 2)) {
                        res = currentCT.nameTable + "_" + ctx.getChild(0).getText();
                    } else {
                        errorList.add("ERROR TYPE [use operation float op float | int op int] line : " + ctx.start.getLine());
                    }
                }
                else {
                    res = currentCT.nameTable + "_" + ctx.getChild(0).getText();
                }
            }
        }
        if(flag == 1) {
            if(typeControl == 1) {
                CodeTable.addElement(storeWord, res, null, (String) arg1, null);
            }
            else {
                CodeTable.addElement("s.s", res, null, (String) arg1, null);
            }
        }
        if(flag == 2){
            CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
            CodeTable.addElement(storeWord, res, null, "$s0", null);
        }
        if(flag == 3){
            if(mars) {
                floatConst.add((String) arg1);
                CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                CodeTable.addElement("s.s", res, null, "$f0", null);
            }
            else {
                CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                CodeTable.addElement("s.s", res, null, "$f0", null);
            }
        }
        if(flag == 4){
            CodeTable.addElement(loadWord, "$s0", null, (String) arg1, null);
            CodeTable.addElement(storeWord, res, null, "$s0", null);
        }
        if(flag == 5){
            CodeTable.addElement("l.s", "$f0", null, (String) arg1, null);
            CodeTable.addElement("s.s", res, null, "$f0", null);
        }
        //System.out.println(typeControl + " "+ res + " "+ctx.start.getLine());
        if(flag == 10){
            if(typeControl == 1) {
                CodeTable.addElement(storeWord, res, null, "$v1", null);
            }
            else {
                CodeTable.addElement("s.s", res, null, "$f26", null);
            }
        }

        allTable.add(CodeTable);
        typeControl = 0;
        return null;
    }

    @Override
    public Object visitPlusExpression(safronParser.PlusExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        int flag1 = 0;
        int flag2 = 0;

        int type1 = 0;
        int type2 = 0;

        if(arg1 instanceof String){
            type1 = typeControl;
            flag1 = 1;//string
        }

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
            flag1 = 2;//int
            type1 = 1;//int
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
            flag1 = 3;//float
            type1 = 2;
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
            if(symbolTable.table.get(arg1) != null){
                if(symbolTable.table.get(arg1).type.equals("INT")){
                    flag1 = 4;//ident_int
                    type1 = 1;
                }
                else {
                    flag1 = 5;//ident_float
                    type1 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg1) != null){
                    if(currentCT.table.get(arg1).type.equals("INT")){
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 4;
                        type1 = 1;
                    }
                    else {
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 5;
                        type1 = 2;
                    }
                }
            }
        }




        //
        if(arg2 instanceof String){
            type2 = typeControl;
            flag2 = 1;
        }

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
            flag2 = 2;
            type2 = 1;
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
            flag2 = 3;
            type2 = 2;
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
            if(symbolTable.table.get(arg2) != null){
                if(symbolTable.table.get(arg2).type.equals("INT")){
                    flag2 = 4;
                    type2 = 1;
                }
                else {
                    flag2 = 5;
                    type2 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg2) != null){
                    if(currentCT.table.get(arg2).type.equals("INT")){
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 4;
                        type2 = 1;
                    }
                    else {
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 5;
                        type2 = 2;
                    }
                }
            }
        }
        count++;
        String res= "";
        if(type1 == type2){
            if(type1 == 1){
                typeControl = 1;
                res = "$t"+count;
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement(add, (String) arg1, (String) arg2, res, null);
                }
                else {
                    if(flag1 == 2 && flag2 == 1){//num t2
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(add, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 2){//t1 num
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(add, (String) arg1, "$s1", res, null);
                    }
                    if(flag1 == 4 && flag2 == 1){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(add, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 4){
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(add, (String) arg1, "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 4){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(add, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 2){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(add, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 2){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(add, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 4){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(add, "$s0", "$s1", res, null);
                    }

                }
            }
            else {
                res = "$f" + (count+1);
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement("add.s", (String) arg1, (String) arg2, res, null);
                }
                else {
                    if (flag1 == 3 && flag2 == 1) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("add.s", "$f0", (String) arg2, res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("add.s", "$f0", (String) arg2, res, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("add.s", (String) arg1, "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("add.s", (String) arg1, "$f1", res, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("add.s", (String) arg1, "$f1", res, null);
                    }

                    if (flag1 == 5 && flag2 == 1) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("add.s", "$f0", (String) arg2, res, null);
                    }

                    if (flag1 == 5 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("add.s", "$f0", "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("add.s", "$f0", "$f1", res, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 5) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("add.s", "$f0", "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("add.s", "$f0", "$f1", res, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("add.s", "$f0", "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("add.s", "$f0", "$f1", res, null);
                        }
                    }

                    if (flag1 == 5 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("add.s", "$f0", "$f1", res, null);
                    }

                    typeControl = 2;

                }
            }
        }
        else {
            errorList.add("ERROR TYPE [use operation float op float | int op int] line : "+ctx.start.getLine());
        }

        return res;
    }

    @Override
    public Object visitMinusExpression(safronParser.MinusExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        int flag1 = 0;
        int flag2 = 0;

        int type1 = 0;
        int type2 = 0;

        if(arg1 instanceof String){
            type1 = typeControl;
            flag1 = 1;//string
        }

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
            flag1 = 2;//int
            type1 = 1;//int
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
            flag1 = 3;//float
            type1 = 2;
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
            if(symbolTable.table.get(arg1) != null){
                if(symbolTable.table.get(arg1).type.equals("INT")){
                    flag1 = 4;//ident_int
                    type1 = 1;
                }
                else {
                    flag1 = 5;//ident_float
                    type1 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg1) != null){
                    if(currentCT.table.get(arg1).type.equals("INT")){
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 4;
                        type1 = 1;
                    }
                    else {
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 5;
                        type1 = 2;
                    }
                }
            }
        }




        //
        if(arg2 instanceof String){
            type2 = typeControl;
            flag2 = 1;
        }

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
            flag2 = 2;
            type2 = 1;
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
            flag2 = 3;
            type2 = 2;
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
            if(symbolTable.table.get(arg2) != null){
                if(symbolTable.table.get(arg2).type.equals("INT")){
                    flag2 = 4;
                    type2 = 1;
                }
                else {
                    flag2 = 5;
                    type2 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg2) != null){
                    if(currentCT.table.get(arg2).type.equals("INT")){
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 4;
                        type2 = 1;
                    }
                    else {
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 5;
                        type2 = 2;
                    }
                }
            }
        }
        count++;
        String res= "";
        if(type1 == type2){
            if(type1 == 1){
                typeControl = 1;
                res = "$t"+count;
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement(sub, (String) arg1, (String) arg2, res, null);
                }
                else {
                    if(flag1 == 2 && flag2 == 1){//num t2
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(sub, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 2){//t1 num
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sub, (String) arg1, "$s1", res, null);
                    }
                    if(flag1 == 4 && flag2 == 1){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(sub, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 4){
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sub, (String) arg1, "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 4){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sub, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 2){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sub, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 2){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sub, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 4){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(sub, "$s0", "$s1", res, null);
                    }

                }
            }
            else {
                res = "$f" + (count+1);
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement("sub.s", (String) arg1, (String) arg2, res, null);
                }
                else {
                    if (flag1 == 3 && flag2 == 1) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("sub.s", "$f0", (String) arg2, res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("sub.s", "$f0", (String) arg2, res, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("sub.s", (String) arg1, "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("sub.s", (String) arg1, "$f1", res, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("sub.s", (String) arg1, "$f1", res, null);
                    }

                    if (flag1 == 5 && flag2 == 1) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("sub.s", "$f0", (String) arg2, res, null);
                    }

                    if (flag1 == 5 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("sub.s", "$f0", "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("sub.s", "$f0", "$f1", res, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 5) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("sub.s", "$f0", "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("sub.s", "$f0", "$f1", res, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("sub.s", "$f0", "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("sub.s", "$f0", "$f1", res, null);
                        }
                    }

                    if (flag1 == 5 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("sub.s", "$f0", "$f1", res, null);
                    }

                    typeControl = 2;

                }
            }
        }
        else {
            errorList.add("ERROR TYPE [use operation float op float | int op int] line : "+ctx.start.getLine());
        }
        return res;
    }

    @Override
    public Object visitDivExpression(safronParser.DivExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        int flag1 = 0;
        int flag2 = 0;

        int type1 = 0;
        int type2 = 0;

        if(arg1 instanceof String){
            type1 = typeControl;
            flag1 = 1;//string
        }

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
            flag1 = 2;//int
            type1 = 1;//int
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
            flag1 = 3;//float
            type1 = 2;
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
            if(symbolTable.table.get(arg1) != null){
                if(symbolTable.table.get(arg1).type.equals("INT")){
                    flag1 = 4;//ident_int
                    type1 = 1;
                }
                else {
                    flag1 = 5;//ident_float
                    type1 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg1) != null){
                    if(currentCT.table.get(arg1).type.equals("INT")){
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 4;
                        type1 = 1;
                    }
                    else {
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 5;
                        type1 = 2;
                    }
                }
            }
        }




        //
        if(arg2 instanceof String){
            type2 = typeControl;
            flag2 = 1;
        }

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
            flag2 = 2;
            type2 = 1;
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
            flag2 = 3;
            type2 = 2;
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
            if(symbolTable.table.get(arg2) != null){
                if(symbolTable.table.get(arg2).type.equals("INT")){
                    flag2 = 4;
                    type2 = 1;
                }
                else {
                    flag2 = 5;
                    type2 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg2) != null){
                    if(currentCT.table.get(arg2).type.equals("INT")){
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 4;
                        type2 = 1;
                    }
                    else {
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 5;
                        type2 = 2;
                    }
                }
            }
        }
        count++;
        String res= "";
        if(type1 == type2){
            if(type1 == 1){
                typeControl = 1;
                res = "$t"+count;
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement(div, (String) arg1, (String) arg2, res, null);
                }
                else {
                    if(flag1 == 2 && flag2 == 1){//num t2
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(div, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 2){//t1 num
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(div, (String) arg1, "$s1", res, null);
                    }
                    if(flag1 == 4 && flag2 == 1){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(div, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 4){
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(div, (String) arg1, "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 4){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(div, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 2){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(div, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 2){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(div, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 4){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(div, "$s0", "$s1", res, null);
                    }

                }
            }
            else {
                res = "$f" + (count+1);
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement("div.s", (String) arg1, (String) arg2, res, null);
                }
                else {
                    if (flag1 == 3 && flag2 == 1) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("div.s", "$f0", (String) arg2, res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("div.s", "$f0", (String) arg2, res, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("div.s", (String) arg1, "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("div.s", (String) arg1, "$f1", res, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("div.s", (String) arg1, "$f1", res, null);
                    }

                    if (flag1 == 5 && flag2 == 1) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("div.s", "$f0", (String) arg2, res, null);
                    }

                    if (flag1 == 5 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("div.s", "$f0", "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("div.s", "$f0", "$f1", res, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 5) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("div.s", "$f0", "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("div.s", "$f0", "$f1", res, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("div.s", "$f0", "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("div.s", "$f0", "$f1", res, null);
                        }
                    }

                    if (flag1 == 5 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("div.s", "$f0", "$f1", res, null);
                    }

                    typeControl = 2;

                }
            }
        }
        else {
            errorList.add("ERROR TYPE [use operation float op float | int op int] line : "+ctx.start.getLine());
        }
        return res;
    }

    @Override
    public Object visitModExpression(safronParser.ModExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        int flag1 = 0;
        int flag2 = 0;

        int type1 = 0;
        int type2 = 0;

        if(arg1 instanceof String){
            type1 = typeControl;
            flag1 = 1;//string
        }

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
            flag1 = 2;//int
            type1 = 1;//int
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
            flag1 = 3;//float
            type1 = 2;
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
            if(symbolTable.table.get(arg1) != null){
                if(symbolTable.table.get(arg1).type.equals("INT")){
                    flag1 = 4;//ident_int
                    type1 = 1;
                }
                else {
                    flag1 = 5;//ident_float
                    type1 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg1) != null){
                    if(currentCT.table.get(arg1).type.equals("INT")){
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 4;
                        type1 = 1;
                    }
                    else {
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 5;
                        type1 = 2;
                    }
                }
            }
        }




        //
        if(arg2 instanceof String){
            type2 = typeControl;
            flag2 = 1;
        }

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
            flag2 = 2;
            type2 = 1;
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
            flag2 = 3;
            type2 = 2;
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
            if(symbolTable.table.get(arg2) != null){
                if(symbolTable.table.get(arg2).type.equals("INT")){
                    flag2 = 4;
                    type2 = 1;
                }
                else {
                    flag2 = 5;
                    type2 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg2) != null){
                    if(currentCT.table.get(arg2).type.equals("INT")){
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 4;
                        type2 = 1;
                    }
                    else {
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 5;
                        type2 = 2;
                    }
                }
            }
        }
        count++;
        String res= "";
        if(type1 == type2){
            if(type1 == 1){
                typeControl = 1;
                res = "$t"+count;
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement(div, (String) arg1, (String) arg2, res, null);
                    CodeTable.addElement(movehi, null, null, res, null);
                }
                else {
                    if(flag1 == 2 && flag2 == 1){//num t2
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(div, "$s0", (String) arg2, res, null);
                        CodeTable.addElement(movehi, null, null, res, null);
                    }
                    if(flag1 == 1 && flag2 == 2){//t1 num
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(div, (String) arg1, "$s1", res, null);
                        CodeTable.addElement(movehi, null, null, res, null);
                    }
                    if(flag1 == 4 && flag2 == 1){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(div, "$s0", (String) arg2, res, null);
                        CodeTable.addElement(movehi, null, null, res, null);
                    }
                    if(flag1 == 1 && flag2 == 4){
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(div, (String) arg1, "$s1", res, null);
                        CodeTable.addElement(movehi, null, null, res, null);
                    }

                    if(flag1 == 2 && flag2 == 4){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(div, "$s0", "$s1", res, null);
                        CodeTable.addElement(movehi, null, null, res, null);
                    }

                    if(flag1 == 4 && flag2 == 2){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(div, "$s0", "$s1", res, null);
                        CodeTable.addElement(movehi, null, null, res, null);
                    }

                    if(flag1 == 2 && flag2 == 2){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(div, "$s0", "$s1", res, null);
                        CodeTable.addElement(movehi, null, null, res, null);
                    }

                    if(flag1 == 4 && flag2 == 4){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(div, "$s0", "$s1", res, null);
                        CodeTable.addElement(movehi, null, null, res, null);
                    }

                }
            }
            else {
                errorList.add("ERROR TYPE [use operation float op float | int op int] line : "+ctx.start.getLine());
            }
        }
        else {
            errorList.add("ERROR TYPE [use operation float op float | int op int] line : "+ctx.start.getLine());
        }
        return "$t"+count;
    }

    @Override
    public Object visitStarExpression(safronParser.StarExpressionContext ctx) {
        Object arg1 = visit(ctx.getChild(0));
        Object arg2 = visit(ctx.getChild(2));

        int flag1 = 0;
        int flag2 = 0;

        int type1 = 0;
        int type2 = 0;

        if(arg1 instanceof String){
            type1 = typeControl;
            flag1 = 1;//string
        }

        if(arg1 instanceof safronParser.SignedNumberContext){
            arg1 = ((safronParser.SignedNumberContext) arg1).NUMBER().getText();
            flag1 = 2;//int
            type1 = 1;//int
        }

        if(arg1 instanceof safronParser.SignedFloatContext){
            arg1 = ((safronParser.SignedFloatContext) arg1).NUM_FLOAT().getText();
            flag1 = 3;//float
            type1 = 2;
        }

        if(arg1 instanceof safronParser.SignedIdentContext){
            arg1 = ((safronParser.SignedIdentContext) arg1).IDENT().getText();
            if(symbolTable.table.get(arg1) != null){
                if(symbolTable.table.get(arg1).type.equals("INT")){
                    flag1 = 4;//ident_int
                    type1 = 1;
                }
                else {
                    flag1 = 5;//ident_float
                    type1 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg1) != null){
                    if(currentCT.table.get(arg1).type.equals("INT")){
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 4;
                        type1 = 1;
                    }
                    else {
                        arg1 = currentCT.nameTable+"_"+arg1;
                        flag1 = 5;
                        type1 = 2;
                    }
                }
            }
        }




        //
        if(arg2 instanceof String){
            type2 = typeControl;
            flag2 = 1;
        }

        if(arg2 instanceof safronParser.SignedNumberContext){
            arg2 = ((safronParser.SignedNumberContext) arg2).NUMBER().getText();
            flag2 = 2;
            type2 = 1;
        }

        if(arg2 instanceof safronParser.SignedFloatContext){
            arg2 = ((safronParser.SignedFloatContext) arg2).NUM_FLOAT().getText();
            flag2 = 3;
            type2 = 2;
        }

        if(arg2 instanceof safronParser.SignedIdentContext){
            arg2 = ((safronParser.SignedIdentContext) arg2).IDENT().getText();
            if(symbolTable.table.get(arg2) != null){
                if(symbolTable.table.get(arg2).type.equals("INT")){
                    flag2 = 4;
                    type2 = 1;
                }
                else {
                    flag2 = 5;
                    type2 = 2;
                }
            }
            else {
                if(currentCT.table.get(arg2) != null){
                    if(currentCT.table.get(arg2).type.equals("INT")){
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 4;
                        type2 = 1;
                    }
                    else {
                        arg2 = currentCT.nameTable+"_"+arg2;
                        flag2 = 5;
                        type2 = 2;
                    }
                }
            }
        }
        count++;
        String res= "";
        if(type1 == type2){
            if(type1 == 1){
                typeControl = 1;
                res = "$t"+count;
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement(mul, (String) arg1, (String) arg2, res, null);
                }
                else {
                    if(flag1 == 2 && flag2 == 1){//num t2
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(mul, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 2){//t1 num
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(mul, (String) arg1, "$s1", res, null);
                    }
                    if(flag1 == 4 && flag2 == 1){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(mul, "$s0", (String) arg2, res, null);
                    }
                    if(flag1 == 1 && flag2 == 4){
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(mul, (String) arg1, "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 4){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(mul, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 2){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(mul, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 2 && flag2 == 2){
                        CodeTable.addElement(loadImmediate, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadImmediate, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(mul, "$s0", "$s1", res, null);
                    }

                    if(flag1 == 4 && flag2 == 4){
                        CodeTable.addElement(loadWord, (String) arg1, null, "$s0", null);
                        CodeTable.addElement(loadWord, null, (String) arg2, "$s1", null);
                        CodeTable.addElement(mul, "$s0", "$s1", res, null);
                    }

                }
            }
            else {
                res = "$f" + (count+1);
                if(flag1 == 1 && flag2 == 1){
                    CodeTable.addElement("mul.s", (String) arg1, (String) arg2, res, null);
                }
                else {
                    if (flag1 == 3 && flag2 == 1) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("mul.s", "$f0", (String) arg2, res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("mul.s", "$f0", (String) arg2, res, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("mul.s", (String) arg1, "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("mul.s", (String) arg1, "$f1", res, null);
                        }
                    }

                    if (flag1 == 1 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("mul.s", (String) arg1, "$f1", res, null);
                    }

                    if (flag1 == 5 && flag2 == 1) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("mul.s", "$f0", (String) arg2, res, null);
                    }

                    if (flag1 == 5 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("mul.s", "$f0", "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("mul.s", "$f0", "$f1", res, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 5) {
                        if(mars) {
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("mul.s", "$f0", "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("mul.s", "$f0", "$f1", res, null);
                        }
                    }

                    if (flag1 == 3 && flag2 == 3) {
                        if(mars) {
                            floatConst.add((String) arg2);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f1", null);
                            floatConst.add((String) arg1);
                            CodeTable.addElement("l.s", "fl" + floatConst.size(), null, "$f0", null);
                            CodeTable.addElement("mul.s", "$f0", "$f1", res, null);
                        }
                        else {
                            CodeTable.addElement("li.s", (String) arg2, null, "$f1", null);
                            CodeTable.addElement("li.s", (String) arg1, null, "$f0", null);
                            CodeTable.addElement("mul.s", "$f0", "$f1", res, null);
                        }
                    }

                    if (flag1 == 5 && flag2 == 5) {
                        CodeTable.addElement("l.s", (String) arg1, null, "$f0", null);
                        CodeTable.addElement("l.s", (String) arg2, null, "$f1", null);
                        CodeTable.addElement("mul.s", "$f0", "$f1", res, null);
                    }

                    typeControl = 2;

                }
            }
        }
        else {
            errorList.add("ERROR TYPE [use operation float op float | int op int] line : "+ctx.start.getLine());
        }
        return res;
    }

    @Override
    public Object visitParenExpression(safronParser.ParenExpressionContext ctx) {
        return visit(ctx.getChild(1));
    }

    @Override
    public Object visitSignedNumber(safronParser.SignedNumberContext ctx) {
        return ctx;
    }

    @Override
    public Object visitSignedIdent(safronParser.SignedIdentContext ctx) {
        return ctx;
    }

    @Override
    public Object visitSignedFloat(safronParser.SignedFloatContext ctx) {
        return ctx;
    }

}
