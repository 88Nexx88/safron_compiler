package compiler;

import java.util.ArrayList;
import java.util.List;

public class Element {
    public String type;
    public int assignVarLine;
    public List<Integer> usingVarLine;


    public Element(String type){
        this.type = type;
        this.assignVarLine = 0;
        this.usingVarLine = new ArrayList<>();
    }

    public void setAssignVar(int assignVar) {
        this.assignVarLine = assignVar;
    }



    public void setUsingVarLine(Integer usingVarLine) {
        this.usingVarLine.add(usingVarLine);
    }


    @Override
    public String toString() {
        String str = String.format("%-15s %-15s %-15s", type, assignVarLine, usingVarLine);
        return str;
    }
}
