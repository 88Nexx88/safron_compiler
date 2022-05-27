import SyntaxError.SyntaxError;
import compiler.*;
import SyntaxError.*;
import gram.safronLexer;
import gram.safronParser;
import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.util.List;

public class Generation {
    public static void main(String[] args) throws IOException {


        CharStream input = CharStreams.fromFileName(args[0]);
        //System.out.println(input.toString());
        safronLexer lexer = new safronLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        safronParser parser = new safronParser(tokens);

        SyntaxErrorListener listener = new SyntaxErrorListener(); //syntax and lexer error
        parser.addErrorListener(listener);

        ParseTree tree = parser.program(); // parse



        if((args.length > 2 && args[2].equals("-all")) || (args.length > 1 && args[1].equals("-all"))) {
            System.out.println("Tokens: ");
            for (Token t : tokens.getTokens()) {
                System.out.println(t.toString());
            }


            Trees.inspect(tree, parser); //gui отображение
        }


        List<SyntaxError> error = listener.getSyntaxErrors();
        if(error.size() == 0) {
            SymbolTableVisitor st = new SymbolTableVisitor();
            st.visit(tree);
            if((args.length > 2 && args[2].equals("-all")) || (args.length > 1 && args[1].equals("-all"))) {
                st.rootTable.printTable();
            }
            TypeControlVis typeControl = new TypeControlVis(st.rootTable);
            typeControl.visit(tree);
            if (st.errorList.size() != 0 || typeControl.errorList.size() != 0) {
                System.out.println("Error semantic analysis: ");


                for (String s : st.errorList) {
                    System.out.println(s);
                }

                for (String s : typeControl.errorList) {
                    System.out.println(s);
                }
                System.out.println("Fix the bugs and we are waiting for you again, with love from the best compiler in the world, safron++");

            }
            else {
                GenerationVis generationVis;
                if((args.length > 1 && args[1].equals("-m")) || (args.length > 2 && args[2].equals("-m"))){
                    generationVis = new GenerationVis(st.rootTable, typeControl.badIfWhileList, typeControl.optimizationList, typeControl.badAssignList, true);
                    generationVis.visit(tree);
                }
                else {
                    generationVis = new GenerationVis(st.rootTable, typeControl.badIfWhileList, typeControl.optimizationList, typeControl.badAssignList, false);
                    generationVis.visit(tree);
                }

                if(generationVis.errorList.size() == 0) {
                    String res = generationVis.generation();
                    //System.out.println(res);
                    //st.rootTable.printTable();
                    Tools tool = new Tools();
                    tool.writeCodeFile(args[0], res);
                }
                else {
                    System.out.println("Error semantic analysis: ");
                    for(String s : generationVis.errorList){
                        System.out.println(s);
                    }
                    System.out.println("Fix bugs and we are waiting for you again, with love from the best compiler in the world, safron++");

                }

            }
        }
        else{
            System.out.println("Error list lexer | parser : ");
            for (SyntaxError s : error) {
                System.out.println("line " + s.getLine() + ":" + s.getCharPositionInLine() + " " + s.getMessage());
            }
            System.out.println("Fix bugs and we are waiting for you again, with love from the best compiler in the world, safron++");
        }


    }
}

