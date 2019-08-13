package za.co.wethinkcode;

import za.co.wethinkcode.controller.GameController;
import javax.swing.*;
import  za.co.wethinkcode.view.cli.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class App extends JFrame {
    public static void main(String[] args) {
        Connection connection = null;
        String sql;
        Statement statement;


        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:swingy.db");
            sql = "CREATE TABLE IF NOT EXISTS heroes (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	name text NOT NULL,\n"
                    + "	class text NOT NULL,\n"
                    + "hp integer NOT NULL,\n"
                    + "attack integer NOT NULL,\n"
                    + "defense integer NOT NULL,\n"
                    + "xp integer NOT NULL,\n"
                    + "level integer NOT NULL,\n"
                    + "artefact text NOT NULL\n"
                    + ");";
            statement = connection.createStatement();
            statement.execute(sql);
            Console console = new Console("Swingy");
            GameController gc = new GameController(console, connection);
            gc.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

