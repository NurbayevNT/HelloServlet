import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})
public class Servlet extends HttpServlet {

    Statement stmt;

    public void init() throws ServletException {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            //Getting the Connection object
            String URL = "jdbc:derby:sampleDB;create=true";
            Connection conn = DriverManager.getConnection(URL);

            //Creating the Statement object
            stmt = conn.createStatement();

            //Executing the query
            try {
                String query = "CREATE TABLE HELLOTABLE( "
                        + "word VARCHAR(255), "
                        + "value VARCHAR(255), "
                        + "PRIMARY KEY (word))";
                stmt.execute(query);
                System.out.println("Table created");
            } catch (Exception e) {
                System.out.println("ALREADY CREATED");
            }
            try{
                stmt.execute("INSERT INTO HELLOTABLE VALUES('mainWord','HelloWorld!')");
                System.out.println("Word inserted");
            }catch (Exception e){
                stmt.execute("UPDATE HELLOTABLE SET value='Hello' WHERE word='mainWord'");
                System.out.println("Word updated");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        try (PrintWriter out = response.getWriter()) {
            JSONObject jsonEnt = new JSONObject();

                String query = "SELECT * FROM HELLOTABLE WHERE word='mainWord'";
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                String word = rs.getString("value");
                System.out.println("word: "+word);
                jsonEnt.put("serverInfo", word);

            out.print(jsonEnt.toString());
        } catch (Exception e) {
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }//

}