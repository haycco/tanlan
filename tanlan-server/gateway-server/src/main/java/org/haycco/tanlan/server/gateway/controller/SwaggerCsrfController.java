package org.haycco.tanlan.server.gateway.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FIXME maybe fix next swagger-ui version
 *
 * @author haycco
 **/
@RestController
@ConditionalOnProperty(name="swagger.enable", havingValue="true")
public class SwaggerCsrfController {

    @RequestMapping("/")
    public String index() {
        return "";
    }

    @RequestMapping("/csrf")
    public String csrf() {
        return "{}";
    }
}
