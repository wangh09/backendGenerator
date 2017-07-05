package ${packageName}.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import individual.wangh09.test.utils.JWTUtils;
import individual.wangh09.test.utils.ServerUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by ${author} on ${date}.
 */
public class PreFilter extends ZuulFilter {
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public String filterType() {

        return "pre";
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
            //         RestTemplate restTemplate = new RestTemplate();
            HttpServletRequest request = ctx.getRequest();

            String uri = request.getRequestURI();


            if(uri.startsWith("/image/")) return null;


            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ServerUtils.API_LIST_URL)
                    .queryParam("api", uri);

            HttpEntity<Map> response = restTemplate.exchange(
                    builder.build().encode().toUri(),
                    HttpMethod.GET,
                    entity,
                    Map.class);

            Map<String,Object> res = response.getBody();
            List<Map<String,Object>> data = (List<Map<String,Object>>)res.get("data");

            //API 不存在
            if(data.size() < 1) {
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(404);
                Map<String,Object> result = new HashMap<String,Object>();
                result.put("message","未找到该资源api");
                result.put("status",404);
                ctx.setResponseBody(JSON.toJSONString(result));
                ctx.addZuulResponseHeader("Content-Type","application/json;charset=UTF-8");
            }
            else {
                //API 存在，而且不公开
                if(!(Boolean)data.get(0).get("isPublic")) {
                    String jwt = request.getHeader("Access-Token");
                    //没有jwt
                    if(jwt == null) {
                        ctx.setSendZuulResponse(false);
                        ctx.setResponseStatusCode(401);
                        Map<String,Object> result = new HashMap<String,Object>();
                        result.put("message","用户未登陆");
                        result.put("status",401);
                        ctx.setResponseBody(JSON.toJSONString(result));
                        ctx.addZuulResponseHeader("Content-Type","application/json;charset=UTF-8");
                    }
                    else {
                        try {
                            Claims claim = JWTUtils.parseJWT(jwt);
                            ctx.addZuulRequestHeader("accountId",claim.getId());
                            ctx.addZuulRequestHeader("role",claim.getSubject());
                            //读取claim, 增加权限控制
                        }
                        catch (Exception e){
                            //有jwt,但是无法正常解析，
                            ctx.setSendZuulResponse(false);
                            ctx.setResponseStatusCode(401);
                            Map<String,Object> result = new HashMap<String,Object>();
                            result.put("message","登陆异常或登陆状态已过期");
                            result.put("status",401);
                            ctx.setResponseBody(JSON.toJSONString(result));
                            ctx.addZuulResponseHeader("Content-Type","application/json;charset=UTF-8");
                        }
                    }
                }
            }
            return null;
        }
        catch(Exception e) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(500);
            return null;
        }
    }
}
