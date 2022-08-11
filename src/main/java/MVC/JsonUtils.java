package MVC;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonUtils {

      public static JSONObject getJsonReq(HttpServletRequest req){
          try {
              BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
              StringBuilder responseStrBuilder = new StringBuilder();
              String inputStr="";
              while (true) {

                      if (!((inputStr = streamReader.readLine()) != null)) break;

                  responseStrBuilder.append(inputStr);
              }
              JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
              return jsonObject;
          } catch (IOException e) {
              e.printStackTrace();
          }
          return null;
      }

}
