package Mybatis;


import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ParaseXml {

    public static sqlMapping parase(String location, Map<String,String> params,String methodName) throws Exception {

        SAXReader saxReader = new SAXReader();
        InputStream stream = ParaseXml.class.getClassLoader().getResourceAsStream(location+".xml");
        Document read = saxReader.read(stream);
        Element mapper = read.getRootElement();
        String namespace = mapper.attributeValue("namespace");
        List<Element> elements = mapper.elements();
        sqlMapping sm=null;
        for (Element select : elements) {
            if (select.getName().equals("select")&& select.attributeValue("id").equals(methodName)) {
                String nsMethod = namespace+":"+select.attributeValue("id");
                String resultType = select.attributeValue("resultType");
                String selectText = select.getText().trim();

                while (selectText.contains("$") || selectText.contains("#")){

                    char[] chars = selectText.toCharArray();
                    for (int i = 0; i < chars.length; i++) {

                        if (chars[i]=='#'||chars[i]=='$') {
                            int j;
                            for (j = i+1; j < chars.length; j++) {
                                if (chars[j]=='}') {
                                    break;
                                }
                            }
                            String property = selectText.substring(i+2, j);  //拿到一个属性。
                            for (Map.Entry<String, String> entry : params.entrySet()) {
                                if (entry.getKey().equals(property)) {
                                    String property2 = selectText.substring(i, j+1);
                                    selectText = selectText.replace(property2, "'"+entry.getValue()+"'");

                                    System.out.println(selectText);
                                    break;
                                }
                            }
                            break;

                        }

                    }
                }
                //拿到 where 和if
                String last=null;
                if (select.element("where")!=null) {
                    List<Element> elements1 = select.element("where").elements("if");
                    String extracted = combineIF(elements1,params);
                    String trim="";
                    if (extracted!=null) {
                        trim =extracted.trim();
                        if (trim.substring(0,2).equals("AND")||trim.substring(0,2).equals("and")) {
                            trim = trim.substring(3);

                        }

                    }
                    last="where "+trim;
                }else {
                    List<Element> elements1 = select.elements("if");
                    String extracted = combineIF(elements1, params);
                    last=extracted;

                }
                String sql=selectText+last;
                System.out.println("最终的sql语句是："+sql);
                sm=new sqlMapping(nsMethod,sql,resultType);

            }
            }
        return sm;
        }

    private static String combineIF( List<Element> elements1,Map<String,String> params) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Element element : elements1) {
            String s = element.attributeValue("test");
            //把s中的属性值要替换掉
            for (Map.Entry<String, String> entry : params.entrySet()) {
               s=s.replace(entry.getKey(), entry.getValue());
            }
            boolean b = stringtoBoolen(s);
            if (b){
                String anIf = element.getText();
                 stringBuffer.append(element.getText()) ;
            }
        }
        return stringBuffer.toString();
    }

    public static boolean stringtoBoolen( String str) {
        JexlBuilder jexlBuilder = new JexlBuilder();
            JexlEngine jexl = jexlBuilder.create();
            JexlExpression jexlExpression = jexl.createExpression(str);
            MapContext jexlContext = new MapContext();
            Object result = jexlExpression.evaluate(jexlContext);
           return (boolean) result;
    }

    }


