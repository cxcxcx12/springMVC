package app.controller;

import MVC.annotation.Autowired;
import MVC.annotation.Controller;
import MVC.annotation.RequestMapping;
import MVC.annotation.RequestParam;
import app.service.userService;

@Controller
public class userController {


    @Autowired
    userService service;

    @RequestMapping("/user")
    public String getuser(@RequestParam("name")String name,@RequestParam("age")String age,@RequestParam("id")String id){
        System.out.println("进入方法里面   "+"名字："+name+"    年龄："+age+"   学号："+id);
        System.out.println(service == null);
        service.insertuser(name,age,id);

        return "index.jsp";
    }




}
