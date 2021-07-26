package cn.yananart.site.api;

import cn.yananart.framework.annotation.ApiMapping;
import cn.yananart.framework.annotation.HttpApi;
import io.vertx.core.json.JsonObject;

/**
 * 版本信息
 *
 * @author yananart
 * @date 2021/7/24
 */
@HttpApi(path = "/version")
public class VersionApi {

    @ApiMapping
    public JsonObject version() {
        JsonObject info = new JsonObject();
        info.put("Author", "Yananart");
        info.put("Email", "yananart@yananart.cn");
        info.put("Build in", "Yananart Framework V0.1");
        info.put("Power by", "Spring Boot & VertX Web");
        info.put("Demo version", "V1.0");
        return info;
    }

}
