package app.service;

import MVC.annotation.RequestParam;
import MVC.annotation.Service;
import app.utils.jdbc;

import java.sql.*;
@Service
public class userService {


      public void insertuser(String name, String age, String id){

          String sql = "INSERT INTO user(name,id,age) values ('"+name+"',"+id+","+age+")";
              jdbc.execute(sql);

      }




}
