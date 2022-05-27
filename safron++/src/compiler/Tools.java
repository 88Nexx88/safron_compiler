package compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Tools {
    public void writeCodeFile(String path, String text){
        path = path.replace(".s", ".asm");
        String writePath = "MIPS/"+path;
        try(FileWriter writer = new FileWriter(writePath, false))
        {
            // запись всей строки
            writer.write(text);
            // запись по символам
            writer.append('\n');

            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        System.out.println("The result of the compiler's work in "+writePath);
        System.out.println("Use MARS | Qtspim for further work!");
    }
}
