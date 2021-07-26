package cn.yananart.framework.handler.impl;

import cn.yananart.framework.handler.TemplateHandler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.LanguageHeader;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;

import java.util.Locale;

/**
 * @author zhouye25337
 * @date 2021/7/26
 */
public class TemplateHandlerImpl implements TemplateHandler {

    private final TemplateEngine engine;
    private final String templateDirectory;
    private final String contentType;
    private String indexTemplate;

    public TemplateHandlerImpl(TemplateEngine engine, String templateDirectory, String contentType) {
        this.engine = engine;
        this.templateDirectory = templateDirectory == null || templateDirectory.isEmpty() ? "." : templateDirectory;
        this.contentType = contentType;
        this.indexTemplate = DEFAULT_INDEX_TEMPLATE;
    }

    @Override
    public void handle(RoutingContext context) {
        String file = getTemplateFile(context.request().path());
        if (templateDirectory == null || "".equals(templateDirectory)) {
            file = file.substring(1);
        }
        // put the locale if present and not on the context yet into the context.
        if (!context.data().containsKey("lang")) {
            for (LanguageHeader acceptableLocale : context.acceptableLanguages()) {
                try {
                    Locale.forLanguageTag(acceptableLocale.value());
                } catch (RuntimeException e) {
                    // we couldn't parse the locale so it's not valid or unknown
                    continue;
                }
                context.data().put("lang", acceptableLocale.value());
                break;
            }
        }
        // render using the engine
        engine.render(new JsonObject(context.data()), templateDirectory + file, res -> {
            if (res.succeeded()) {
                context.response().putHeader(HttpHeaders.CONTENT_TYPE, contentType).end(res.result());
            } else {
                context.fail(res.cause());
            }
        });
    }


    private String getTemplateFile(String path) {
        if (path.endsWith("/")) {
            path = path + indexTemplate;
        } else {
            path = path + "/" + indexTemplate;
        }
        return path;
    }


    @Override
    public TemplateHandler setIndexTemplate(String indexTemplate) {
        this.indexTemplate = indexTemplate;
        return this;
    }
}
