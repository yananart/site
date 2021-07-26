package cn.yananart.framework.handler;

import cn.yananart.framework.handler.impl.TemplateHandlerImpl;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;

/**
 * 模版
 *
 * @author zhouye25337
 * @date 2021/7/26
 */
public interface TemplateHandler extends Handler<RoutingContext> {

    String DEFAULT_TEMPLATE_DIRECTORY = "templates";

    /**
     * The default content type header to be used in the response
     */
    String DEFAULT_CONTENT_TYPE = "text/html";

    /**
     * The default index page
     */
    String DEFAULT_INDEX_TEMPLATE = "index.html";


    /**
     * Set the index template
     *
     * @param indexTemplate the index template
     * @return a reference to this, so the API can be used fluently
     */
    TemplateHandler setIndexTemplate(String indexTemplate);

    /**
     * Create a handler
     *
     * @param engine the template engine
     * @return the handler
     */
    static TemplateHandler create(TemplateEngine engine) {
        return new TemplateHandlerImpl(engine, DEFAULT_TEMPLATE_DIRECTORY, DEFAULT_CONTENT_TYPE);
    }

    /**
     * Create a handler
     *
     * @param engine            the template engine
     * @param templateDirectory the template directory where templates will be looked for
     * @param contentType       the content type header to be used in the response
     * @return the handler
     */
    static TemplateHandler create(TemplateEngine engine, String templateDirectory, String contentType) {
        return new TemplateHandlerImpl(engine, templateDirectory, contentType);
    }
}
