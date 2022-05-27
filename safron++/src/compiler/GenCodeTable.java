package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenCodeTable {
    String label;
    List<CodeElement> table;


    public GenCodeTable(String label){
        this.label = label;
        this.table = new ArrayList<>();
    }

    public void addElement(String op, String arg1, String arg2, String res, String GoTo){
        table.add(new CodeElement(op, arg1, arg2, res, GoTo));
    }

    @Override
    public String toString() {
        String res = label + "\n";
        for(int i = 0; i < table.size(); i++){
            res+="\t"+i+" : "+table.get(i).toString()+"\n";
        }

        return res;
    }

    public String toStringCode(){
        String res = label +" : "+"\n";
        for(int i = 0; i < table.size(); i++){
            res+="\t"+table.get(i).toStringCode()+"\n";
        }

        return res;
    }
}

