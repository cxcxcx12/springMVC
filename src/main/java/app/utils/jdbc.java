package app.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class jdbc {

   public static void execute(String sql){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=Hongkong";
            String username="root";
            String password="root";
            Connection connection = DriverManager.getConnection(url,username,password);
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

