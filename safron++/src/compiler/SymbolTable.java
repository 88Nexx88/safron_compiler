package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SymbolTable {

    public List<ChildTable> childTables;

    public HashMap<String, Element> table;

    public SymbolTable (){
        childTables = new ArrayList<>();
        table = new HashMap<>();
    }

    public boolean addElement(String name, String type){

        if(table != null){
            if(table.get(name) == null){
                table.put(name, new Element(type));
            }
            else {
                //System.out.println("ERROR IDENT " + name + " TYPE " + type + " already in use");
                return false;
            }
        }

        return true;
    }

    public void printTable(){
        System.out.println("______________________________");
        System.out.println("MAIN TABLE: ");
        String str = String.format("%-15s %-15s %-15s %-15s", "name", "type", "assignVarLine", "usingVarLine");
        System.out.println(str);
        for(String s : table.keySet()){
            System.out.printf("%-15s %s\n", s , table.get(s).toString());
        }

        System.out.println("______________________________");
        for(ChildTable ct : childTables){
            System.out.println(ct.nameTable+ " TABLE ");
            System.out.println(str);
            for(String s : ct.table.keySet()){
                System.out.printf("%-15s %s\n", s , ct.table.get(s).toString());
            }
            System.out.println("______________________________");
        }
    }

}
