package ${packageName}.filter;

import com.alibaba.fastjson.JSON;
import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import individual.wangh09.test.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by ${author} on ${date}.
 */
public class PostFilter extends ZuulFilter {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String filterType() {

        return "post";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            HttpServletRequest request = ctx.getRequest();
            String uri = request.getRequestURI();
            if (uri.equals("/account-service/account/login")) {
                if (ctx.getResponseStatusCode() == 200) {
                    try (final InputStream responseDataStream = ctx.getResponseDataStream()) {
                        Map<String,Object> response = JSON.parseObject(CharStreams.toString(new InputStreamReader(responseDataStream, "UTF-8")),Map.class);
                        if ((Integer) response.get("status") == 200) {
                            Map<String, Object> account = (Map<String, Object>) response.get("data");
                            String role = (String) account.get("dicDefaultRoleType").toString();
                            String id = (String) account.get("id");
                            if (role == null) role = "2";
                            if (id == null) id = "wrong";
                            String jwt = JWTUtils.createJWT(id, role, -1);
                            //  ctx.addZuulResponseHeader("Access-Token",jwt);
                            response.put("accessToken", jwt);
                        }
                        ctx.setResponseBody(JSON.toJSONString(response));
                    } catch (IOException e) {
                        JSON jb = JSON.parseObject("{\"status\":210,\"message\":\"签名失败\"}");
                        ctx.setResponseBody(jb.toString());
                        e.printStackTrace();
                    }
                    return null;
                }
                return null;
            }
            return null;
        } catch (Exception e) {
            JSON jb = JSON.parseObject("{\"status\":210,\"message\":\"签名失败\"}");
            ctx.setResponseBody(jb.toString());
            e.printStackTrace();
            return null;
        }
    }
}