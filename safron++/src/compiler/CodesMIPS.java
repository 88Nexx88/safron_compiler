package compiler;

public interface CodesMIPS {

    //math
    String add = "add"; //add $1,$2,$3      $1=$2+$3
    String sub = "sub"; //sub $1,$2,$3      $1=$2-$3
    String addi= "addi"; //addi $1,$2,100   $1=$2+100
    String mul = "mul"; //mul $1,$2,$3      $1=$2*$3 Result is only 32 bits!
    String mult = "mult";//mult $2,$3       $hi,$low=$2*$3 Upper 32 bits stored in specialregister hiLower 32 bits stored in specialregister lo
    String div = "div"; //div $2,$3         $hi,$low=$2/$3

    //logical
    String and = "and";//and $1,$2,$3       $1=$2&$3
    String or = "or";//or $1,$2,$3          $1=$2|$3
    String not = "not";

    //data transfer
    String loadWord = "lw";//lw$1,100($2)   $1=Memory[$2+100]  Copy from memory to register
    String storeWord = "sw";//sw$1,100($2)  Memory[$2+100]=$1 Copy from register to memory
    String loadAddress = "la";//la $1,label $1=Address of label
    String loadImmediate = "li";//li $1,100 $1=100

        //hi and lo reg
        String movehi = "mfhi";//mfhi $2    $2=hi
        String movelo = "mflo";//mflo $2    $2=lo

    String move = "move";//move $1,$2       $1=$2

    //conditional
    String IFeq = "beq";//beq $1,$2,100       if($1==$2) go to PC+4+100
    String IFne = "bne";
    String IFgt = "bgt";
    String IFge = "bge";
    String IFlt = "blt";
    String IFle = "ble";

    String seq = "seq";
    String sne = "sne";
    String sgt ="sgt";
    String sge = "sge";
    String slt = "slt";
    String sle = "sle";

    //jump

    String jump = "j";//j 1000           go to address 1000
    String jumpRegist = "jr";//jr $1     go to address stored in $1
    String jumpLink = "jal";//jal 1000   $ra=PC+4; go to address 1000








}
