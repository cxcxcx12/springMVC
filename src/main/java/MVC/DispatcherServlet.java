package MVC;

import MVC.annotation.RequestBody;
import MVC.annotation.RequestParam;
import com.alibaba.fastjson.JSONObject;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    List<Handlerr> handlers;


    @Override
    public void init(ServletConfig config) throws ServletException {
        String packageUrl = config.getInitParameter("packageurl");
        System.out.println(packageUrl);


        WebApplicationContext wc = new WebApplicationContext(packageUrl);
        wc.refresh();
      //绑定  url，方法，类名的映射关系
       handlers = wc.getHandlers();
        for (Handlerr handler : handlers) {
            System.out.println(handler.getUrl());
            System.out.println(handler.getMethod().getName());
        }
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入get请求");
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入post请求");
       dipatchRequet(req,resp);
    }

    private void dipatchRequet(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入请求分发的方法体当中");
        String s = req.getRequestURI().toString();
        System.out.println("请求的路径url为："+s);
        Map<String,String[]> parameterMap = req.getParameterMap();
        System.out.println("插个嘴");
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            System.out.println(entry.getKey());
        }
        System.out.println("插个嘴结束，看看中间有没有值");
        for (Handlerr handler : handlers) {
            if (handler.getUrl().equals(s)) {
                System.out.println("有符合条件的url哦！！");
                Object controller = handler.getTarget();

                //1.实现前端请求的url与对应的方法匹配上  2.实现 前端请求提交过来的参数与 方法当中的形参进行匹配对接。 然后执行方法！！
                Method method = handler.getMethod();
                Class<?>[] parameterTypes = method.getParameterTypes();  //只能拿到修饰符
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                Object[] params=new Object[parameterAnnotations.length];
                System.out.println(parameterAnnotations.length);
                for (int i = 0; i < parameterAnnotations.length; i++) {
                    System.out.println(parameterAnnotations[i][0].toString());
                    if (parameterAnnotations[i][0]!=null) {
                        if ((parameterAnnotations[i][0].annotationType().equals(RequestParam.class))){
                            RequestParam requestParam = (RequestParam) parameterAnnotations[i][0];
                            String value = requestParam.value();
                            System.out.println("拿到第"+i+"个索引上注解的值为："+value);
                            String[] valuee = parameterMap.get(value);
                            System.out.println("拿到前端对于参数的值的集合："+valuee.length);
                            String s1 = valuee[0];
                            System.out.println(s1);
                            params[i] =parameterTypes[i].cast(s1);   //cast 就是强制转换！！ 有待商榷


                            //注意这里可能会根据参数类型 进行强制装换！！


                        }
                        else if ((parameterAnnotations[i][0].annotationType().equals(RequestBody.class))){
                            try {
                                JSONObject json = JsonUtils.getJsonReq(req);
                                Object o = parameterTypes[i].newInstance();
                                Field[] fields = parameterTypes[i].getDeclaredFields();
                                for (Field field : fields) {
                                    field.setAccessible(true);
                                    if (json.get(field.getName()) != null) {
                                        String filed_value = String.valueOf(json.get(field.getName()));
                                         field.set(o, filed_value);
                                    }

                                }
                                params[i]=o;
                            } catch (Exception e) {
                                e.printStackTrace();}



                        }

                    }
                    //我这里默认是 一个参数用param ，二个及以上的用requestbody。  所有剩下的只会有httpservlet
                    else{
                        if (parameterTypes[i].equals(HttpServletRequest.class)) {
                            params[i]=req;
                        }
                        else if (parameterTypes[i].equals(HttpServletResponse.class)){
                            params[i]=resp;
                        }
                    }

                }
                try {
                    //出问题了
                    Object result = method.invoke(controller, params);
                    if(result instanceof String){
                        //跳转JSP
                        String viewName=(String)result;
                        // forward:/success.jsp
                        if(viewName.contains(":")){
                            String viewType=viewName.split(":")[0];
                            String viewPage=viewName.split(":")[1];
                            if(viewType.equals("forward")){
                                req.getRequestDispatcher(viewPage).forward(req,resp);
                            }else{
                                // redirect:/user.jsp
                                resp.sendRedirect(viewPage);
                            }
                        }else{
                            //默认就转发
                            req.getRequestDispatcher(viewName).forward(req,resp);
                        }
                    }/*else{
                        //返回JSON格式数据
                        Method method = myHandler.getMethod();
                        if(method.isAnnotationPresent(ResponseBody.class)){
                            //将返回值转换成 json格式数据
                            ObjectMapper objectMapper = new ObjectMapper();
                            String json = objectMapper.writeValueAsString(result);
                            resp.setContentType("text/html;charset=utf-8");
                            PrintWriter writer = resp.getWriter();
                            writer.print(json);
                            writer.flush();
                            writer.close();

                        }
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();}



            }
        }


    }


}
