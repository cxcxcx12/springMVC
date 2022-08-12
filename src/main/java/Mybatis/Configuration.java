package Mybatis;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Configuration {
    String driver;
    String url;
    Statement statement;

    public List<String> getMapperUrls() {
        return mapperUrls;
    }

    public void setMapperUrls(List<String> mapperUrls) {
        this.mapperUrls = mapperUrls;
    }

    String username;
    String password;
    Map<String,sqlMapping> mapping;
     List<String> mapperUrls=new ArrayList<>();
    public Configuration() {
       loadFile();
       statement=getConnection();
    }

    private void loadFile() {

        String location="mybatis-config.xml";
        SAXReader saxReader = new SAXReader();
        InputStream stream =this.getClass().getClassLoader().getResourceAsStream(location);
        Document read = null;
        try {
            read = saxReader.read(stream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = read.getRootElement();

        List<Element> elements = root.element("environments").element("environment").element("dataSource").elements("property");
        for (Element element : elements) {
            if (element.attributeValue("name").equals("driver")) {
                setDriver(element.attributeValue("value"));
            }
            if (element.attributeValue("name").equals("url")) {
                setUrl(element.attributeValue("value"));
            }
            if (element.attributeValue("name").equals("password")) {
                setPassword(element.attributeValue("value"));
            }
            if (element.attributeValue("name").equals("username")) {
                setUsername(element.attributeValue("value"));
            }
        }

        List<Element> elements1 = root.element("mappers").elements("mapper");
        for (Element element : elements1) {
            String locations = element.attributeValue("locations");
            mapperUrls.add(locations);
        }

    }


    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, sqlMapping> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, sqlMapping> mapping) {
        this.mapping = mapping;
    }


    private Statement  getConnection(){
        Statement statement = null;
        Connection conn = null;     // sql语句，
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(getUrl(), getUsername(), getPassword());

            return conn.createStatement();
    }catch (Exception e){
            e.printStackTrace();
        }
      return null;
    }
}
