package compiler;

import gram.safronBaseVisitor;
import gram.safronParser;

import java.util.ArrayList;
import java.util.List;

public class SymbolTableVisitor extends safronBaseVisitor {
    public SymbolTable rootTable;
    ChildTable currentChild;
    boolean errorFlag;
    int root_c = 0;
    public List<String> errorList;
    public SymbolTableVisitor () {
        this.rootTable = new SymbolTable();
        this.currentChild = null;
        this.errorFlag = false;
        errorList = new ArrayList<>();
    }


    @Override
    public Object visitVars_(safronParser.Vars_Context ctx) {
        int n = ctx.getChildCount();
        String type = "";
        for(int i = 0; i < n; i++){
            if(ctx.getChild(i) instanceof safronParser.FormatContext){
                type = ctx.getChild(i).getText();
            }
            if(ctx.getChild(i) instanceof safronParser.IdentContext){
                if(root_c > 0){
                    if(!currentChild.addElement(ctx.getChild(i).getText(), type)){
                            errorList.add("ERROR IDENT " + ctx.getChild(i).getText() + " TYPE " + type + " already in use line:" + ctx.start.getLine());

                    }
                }
                else {
                    if(!rootTable.addElement(ctx.getChild(i).getText(), type)){
                        errorList.add("ERROR IDENT " + ctx.getChild(i).getText() + " TYPE " + type + " already in use line:" + ctx.start.getLine());
                    }
                }
            }
        }
        root_c++;
        return super.visitVars_(ctx);
    }


    @Override
    public Object visitFunction(safronParser.FunctionContext ctx) {
        int n = ctx.getChildCount();
        int c = 0;
        c++;
        String typeFun = ctx.getChild(c).getText();
        c++;
        currentChild = new ChildTable(ctx.getChild(c).getText(), rootTable, typeFun);
        rootTable.childTables.add(currentChild);
        if(!rootTable.addElement(ctx.getChild(c).getText(), "FUN")){
            errorList.add("ERROR IDENT " + ctx.getChild(c).getText() + " TYPE " + "FUN" + " already in use line:" + ctx.start.getLine());
        }
        c+=2;
        while(!ctx.getChild(c).getText().equals(")")){
            String type = ctx.getChild(c).getText();
            String name = ctx.getChild(++c).getText();
            if(!currentChild.addElement(name, type)){
                errorList.add("ERROR IDENT " + name + " TYPE " + type + " already in use line:" + ctx.start.getLine());
            }
            currentChild.parametrs.add(name);

            if(ctx.getChild(++c).getText().equals(",")){
                c++;
            }
        }
        return super.visitFunction(ctx);
    }
}
