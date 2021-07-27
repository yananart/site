package cn.yananart.site.api;

import cn.yananart.framework.annotation.ApiMapping;
import cn.yananart.framework.annotation.HttpApi;
import cn.yananart.framework.annotation.paramter.Param;
import cn.yananart.framework.logging.Logger;
import cn.yananart.framework.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * 版本信息
 *
 * @author yananart
 * @date 2021/7/24
 */
@HttpApi(path = "/version")
public class VersionApi {

    private static final Logger log = LoggerFactory.getLogger(VersionApi.class);

    @ApiMapping(async = false)
    public JsonObject version(@Param("id") String id) {
        log.info("id = {}", id);
        JsonObject info = new JsonObject();
        info.put("Author", "Yananart");
        info.put("Email", "yananart@yananart.cn");
        info.put("Build in", "Yananart Framework V0.1");
        info.put("Power by", "VertX & Guice");
        info.put("Demo version", "V1.0");
        return info;
    }

}
