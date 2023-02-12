package dmk.dbutil;

import dmk.dbutil.ObjExctractor;

public class Main {
    public static void main(String[] args)  {
        System.out.println("Hello world!");
        ObjExctractor x = new ObjExctractor( "data","roborep", "tblx", ".\\outdir");
        for (String z : args) {
            System.out.println(z);
        }
        System.out.println(System.getProperty("user.dir"));
    }
}