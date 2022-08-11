package MVC;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

public class xmlPrase {

      public static String getPackage(String xml){
          InputStream resourceAsStream = xmlPrase.class.getClassLoader().getResourceAsStream(xml.split(":")[1]);


          SAXReader saxReader = new SAXReader();
          try {
              Document read = saxReader.read(resourceAsStream);
              Element rootElement = read.getRootElement();
              Element element = rootElement.element("component-scan");
              Attribute attribute = element.attribute("base-package");
              return attribute.getText();
          } catch (DocumentException e) {
              e.printStackTrace();
          }
   return null;

      }
}
