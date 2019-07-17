package org.haycco.tanlan.order.api;

import reactivefeign.spring.config.ReactiveFeignClient;

/**
 * Order micro-service REST-ful API for openfeign client
 *
 * @author haycco
 */
@ReactiveFeignClient(name = "tanlan-order-service")
public interface OrderServiceClient {

}
