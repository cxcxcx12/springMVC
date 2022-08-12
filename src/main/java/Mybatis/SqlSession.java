package Mybatis;



import Mybatis.annotation.Param;
import app.Dao.StudentDao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlSession {

    Configuration config;
    Map<String, sqlMapping> mapping;


    public SqlSession() {
        config = new Configuration();
    }

    public Object getProxy(Class<?> clazz) {

        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Map<String, String> params = new HashMap<>();
                String methodName = method.getName();
                String simpleName = method.getGenericReturnType().getClass().getSimpleName();

                /*method.get*/
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                for (int i = 0; i < parameterAnnotations.length; i++) {
                    Param param = (Param) parameterAnnotations[i][0];
                    String paramName = param.value();
                    String paramValue = (String) args[i];
                    params.put(paramName, paramValue);                          //Todo
                }
                sqlMapping sq = ParaseXml.parase(clazz.getSimpleName(), params, methodName);
                return excute(simpleName, sq);
            }
        });
    }

    private Object excute(String simpleName, sqlMapping sm) {


        try {

            if (simpleName.equals("ParameterizedTypeImpl")) {
                ResultSet rs = config.getStatement().executeQuery(sm.getSql());
                String resultType = sm.getResultType();
                Class<?> aClass = Class.forName(resultType);


                Field[] fields = aClass.getDeclaredFields();

                List<Object> list = new ArrayList<>();

                while (rs.next()) {
                    Object cast = aClass.cast(aClass.newInstance());

                    for (int i = 0; i < fields.length; i++) {
                        fields[i].set(cast, rs.getString(i + 1));
                        list.add(cast);
                    }
                }


                return list;
            } else {
                config.getStatement().execute(sm.getSql());
                return null;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

       return null;
    }


    public static void main(String[] args)  throws Exception {
        SqlSession sqlSession = new SqlSession();
        Class<?> aClass = Class.forName("app.Dao.StudentDao");
        Object proxy = sqlSession.getProxy(aClass);
        StudentDao proxy1 = (StudentDao) proxy;
        proxy1.insertuser("c44433","2020211","18");

    }
}

