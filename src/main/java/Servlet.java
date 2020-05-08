import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Scanner;
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
                System.out.println("TABLE ALREADY CREATED");
            }
            try {
                stmt.execute("INSERT INTO HELLOTABLE VALUES('mainWord','HelloWorld!')");
                System.out.println("The Server is ready! If you want to change the word just input new one in command line.");
            } catch (Exception e) {
                stmt.execute("UPDATE HELLOTABLE SET value='HelloWorld!' WHERE word='mainWord'");
                System.out.println("The Server is ready! If you want to change the word just input new one in command line.");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner inputReader = new Scanner(System.in);
                while (true) {

                    if (inputReader.hasNext()) {
                        try {
                            String s = inputReader.nextLine();
                            if (s.trim().length() > 0) {
                                stmt.execute("UPDATE HELLOTABLE SET value='" + s + "' WHERE word='mainWord'");
                                System.out.println("Word changed to " + s + ". Please, reload page!");
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }).start();

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
    }
}