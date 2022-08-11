package MVC;

import java.lang.reflect.Method;

public class Handlerr {
    String  url;
    Object target;
    Method method;

    public Handlerr(String url, Object target, Method method) {
        this.url = url;
        this.target = target;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
