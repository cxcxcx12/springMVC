package app.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class jdbc {

   public static void execute(String sql){
        try {
            //第一步    加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
             //建立连接
            String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=Hongkong";
            String username="root";
            String password="root";
            Connection connection = DriverManager.getConnection(url,username,password);

            //statement 执行sql语句
            Statement statement = connection.createStatement();
            statement.execute(sql);
            //关闭连接
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

