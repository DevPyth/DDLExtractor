package dmk.dbutil;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/*
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
*/
import static java.util.stream.Collectors.toMap;


public class ObjExctractor {
    private DDLObject usrObject;
    //private static final Logger logger = LogManager.getLogger(ObjExctractor.class);

    private enum objTypes {
        otTable,
        otView,
        otTrigger,
        otFunction,
        otProcedure,
        otPackage,
        otSequence
    };
    private Map<objTypes, String> states = new HashMap<objTypes, String>();
    private objTypes objType;
    private String outDir;
    private String login;
    private String pwd;
    private String owner;
    private String objName;
    private static final String optionFile = "options.ini";
    private String dbConfigName;
    private String configPath;
    private String connectionString;
    Connection con;

    public ObjExctractor(String dbConfigName, String owner, String objName, String outDir)  {
        this.dbConfigName = dbConfigName;
        this.owner = owner;
        this.objName = objName;
        this.outDir = outDir;
        usrObject = new DDObjectTable();
        configPath = System.getProperty("user.dir") + "\\" + optionFile;

        if (!this.loadProperties()) return;
        if (!this.getObjectInfo()) return;
      //  extractObjToFile();
    }
    private boolean loadProperties(){
        File f = new File(outDir);
        if (!(f.exists() && f.isDirectory())) {
            System.out.printf("Out folder not found (%s) \\n", outDir);
            return false;
        }
        File fileToParse = new File(configPath);
        try {
            Ini ini = new Ini(fileToParse);
            connectionString = ini.get(dbConfigName, "DBstr");
        }
        catch (FileNotFoundException e){
            System.out.printf("File not found (%s) \n", configPath);
            return false;
        } catch (IOException e) {
            System.out.printf("io error (%s) \n", e.getMessage());
            return false;
        }
        return true;
    }

    private boolean getObjectInfo()
    {
        con=null;
        try {
            // https://razorsql.com/articles/oracle_jdbc_connect.html
            // https://www.studentstutorial.com/jdbc/oracle-connect.php
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "roborep", "roborep");
            String sQueryObject = "select max(object_type) obtype from dba_objects where owner = upper(?) and object_name = upper(?)";
            PreparedStatement st = con.prepareStatement(sQueryObject);
            st.setString(1, this.owner);
            st.setString(2, this.objName);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                System.out.println("object not found");
                return false;
            }
            System.out.println(rs.getNString(1));

            CallableStatement x = con.prepareCall("");
            x.registerOutParameter(1, Types.CLOB);
            Clob s = con.createClob();
            s = x.getClob(1);
            s.toString();
        }
        catch (java.sql.SQLException e)
        {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private boolean extractObjToFile(){
        switch (objType) {
            case otTable:
                extractTableToFile();
                break;
            case otView:
                extractViewToFile();
                break;
            case otTrigger:
                extractTriggerToFile();
                break;
            case otFunction:
                extractFunctionToFile();
                break;
            case otProcedure:
                extractProcedureToFile();
                break;
            case otPackage:
                extractPackageToFile();
                break;
        }
        return false;
    }

    private void extractTableToFile(){

    }
    private void extractViewToFile(){

    }
    private void extractTriggerToFile(){

    }
    private void extractFunctionToFile(){

    }
    private void extractProcedureToFile(){

    }
    private void extractPackageToFile(){

    }

}
