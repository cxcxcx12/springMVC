package MVC;

import MVC.annotation.*;
import Mybatis.SqlSession;
import Mybatis.annotation.Mapper;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebApplicationContext {
    private String  packageUrl="classpath:springmvc.xml";

    private List<String> filelist= new ArrayList<>();
    private Map<String, Object> ioC=new HashMap<>();
   private List<Handlerr> handlers = new ArrayList<Handlerr>();
    private SqlSession session;

    public WebApplicationContext(String packageUrl) {
        this.packageUrl = packageUrl;
        session=new SqlSession();

    }



    //根据url  加载包内的类入容器， 并且依赖注入关系弄好
    public void refresh() {
        try {
            String s = xmlPrase.getPackage(packageUrl);
            String[] split = s.split(",");      //拿到com。contro，com。service这些包 ，我们要获取这个几包下的所有class的路径。并且用。号
            for (String s1 : split) {
                getAllFile(s1);
            }


            //对象注入容器中
            for (String s1 : filelist) {

                    Class<?> aClass = Class.forName(s1);    //类名
                    if (aClass.isAnnotationPresent(Service.class)||aClass.isAnnotationPresent(Controller.class)) {
                        Object o = aClass.newInstance();
                        ioC.put(aClass.getSimpleName(), o);
                    }

                    //mybatis的代理对象的注入过程  是接口  并且有mapper注解
                if (aClass.isInterface()&&aClass.isAnnotationPresent(Mapper.class)) {
                    Object proxy = session.getProxy(aClass);
                    ioC.put(aClass.getSimpleName(), proxy);
                }


            }


          //依赖注入
            for (Map.Entry<String, Object> objectEntry : ioC.entrySet()) {
                Object value = objectEntry.getValue();
                Field[] fields = value.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        field.setAccessible(true);

                                              //field.getType().getSimpleName() 这里只是名字而已，
                       // 他是 包名和类包的实例对象
                               // 如果是只有接口，那映射关系就是 接口所在包名，接口名  和 代理对象的映射关系而已
                        field.set(value, ioC.get(field.getType().getSimpleName()));
                    }
                }
                if (value.getClass().isAnnotationPresent(Controller.class)) {
                    Method[] methods = value.getClass().getDeclaredMethods();
                    for (Method method : methods) {
                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        String url = annotation.value();
                       handlers.add(new Handlerr(url, value,method));
                    }
                }


            }







        } catch (Exception e) {
            e.printStackTrace();
        }

    }    //在容器map里面放入对象和名字的引用，然后遍历对象，查看属性上面是否有注解，存在注解，对这个属性进行赋值，也就是依赖注入。



    private void getAllFile(String s1) {
        String replace = "/"+s1.replace(".", "/");
        File file = new File( WebApplicationContext.class.getResource(replace).getPath());
        File[] files = file.listFiles();
        for (File file1 : files) {
            String name= s1+"."+file1.getName();
            if (file1.isDirectory()) {

                getAllFile(name);   //这里 组装的非常巧妙！！
            }else {
                filelist.add(name.replace(".class",""));
            }



        }
    }

    public List<Handlerr> getHandlers() {
        return handlers;
    }

    /*public static void main(String[] args) {
        WebApplicationContext context = new WebApplicationContext();
        context.refresh();
    }
*/

}
