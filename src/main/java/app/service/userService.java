package app.service;

import MVC.annotation.Autowired;
import MVC.annotation.RequestParam;
import MVC.annotation.Service;
import app.Dao.StudentDao;
import app.Dao.userMapper;
import app.utils.jdbc;

import java.sql.*;
@Service
public class userService {

    @Autowired
    StudentDao studentDao;


      public void insertuser(String name, String age, String id){
          System.out.println("进入service方法");
          System.out.println(studentDao == null);

          System.out.println(studentDao );
          studentDao.insertuser(name, age, id);


      }





}
