package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChildTable {
    public SymbolTable root;
    public String nameTable;
    public HashMap<String, Element> table;

    public String typeFun;
    public List<String> parametrs;

    public ChildTable (String nameTable, SymbolTable root, String typeFun){
        this.nameTable = nameTable;
        this.root = root;
        this.typeFun = typeFun;
        this.parametrs = new ArrayList<>();
        this.table = new HashMap<>();
    }

    public boolean addElement(String name, String type){
        if(root.table.get(name) == null){
            if(table.size() > 0){
                if(table.get(name) == null){
                    table.put(name, new Element(type));
                }
            }
            else {
                table.put(name, new Element(type));
            }
        }
        else {
            //System.out.println("ERROR IDENT " + name + " TYPE " + type + " already in use in global");
            return false;
        }


        return true;
    }

}
