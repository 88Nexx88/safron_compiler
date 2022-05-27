public class Test {
    public static void main(String [] args){
        test(2);
    }

    public static void test(int q){
        int j = 0;
        /*
        if((j + 8 * j) > 3 || j < 0) {
            q = q + 1;
            if(j<0) {
                j = q + j;
            }
            q = q +2;
        }

         */
        while(j < 5){
            j++;
        }
        q = 3 + q;
        System.out.println(j);
        System.out.println(q);
    }
}
