package compiler;

import java.util.List;

public class MethodProgramCodeTable {
    String nameMethod;
    List<GenCodeTable> allTable;

    public MethodProgramCodeTable(String name){
        this.nameMethod = name;
    }

    public List<GenCodeTable> getAllTable() {
        return allTable;
    }

    public void setAllTable(List<GenCodeTable> allTable) {
        this.allTable = allTable;
    }

    public void setNameMethod(String nameMethod) {
        this.nameMethod = nameMethod;
    }

    public String getNameMethod() {
        return nameMethod;
    }

    @Override
    public String toString() {
        String res = nameMethod + ":\n";
        for(GenCodeTable gt : allTable){
            res+="\t"+gt.toString()+"\n";
        }
        return res;
    }

    public String toStringCode() {
        String res = "";
        if(!nameMethod.equals("main")){
            int count = 0;
            for (GenCodeTable gt : allTable) {
                if(!(count == 0)) {
                    res += nameMethod + "_" + gt.toStringCode() + "\n";
                }
                else {
                    res += gt.toStringCode() + "\n";
                    count++;
                }

            }
        }
        else {
            for (GenCodeTable gt : allTable) {
                res += gt.toStringCode() + "\n";
            }
        }
        return res;
    }
}
