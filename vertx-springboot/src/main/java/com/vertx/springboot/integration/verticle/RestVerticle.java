package com.vertx.springboot.integration.verticle;

import com.vertx.springboot.integration.request.ApplicationRequest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * 
 * @author pavan
 *
 */
public class RestVerticle extends AbstractVerticle {
	
	private static final String SPRING_WORKER_SERVICE = "crudservice";

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		
		Router router = Router.router(vertx);	
		router.post().handler(BodyHandler.create());
		router.route("/").handler(this::welcomeMessage);
		
		router.route(HttpMethod.POST,"/payment/authorize/report").handler(this::create);
		router.route(HttpMethod.GET,"/payment/authorize/report").handler(this::findAll);
		router.route(HttpMethod.GET,"/payment/authorize/report/:txnid/").handler(this::get);

		vertx.createHttpServer().requestHandler(router::accept).listen(8080,result->{
			if (result.succeeded()) {
				System.out.println("Server created!!");
				startFuture.complete();
			} else {
				System.out.println(result.toString());
				startFuture.fail("server creation failed");
			}
		});
	}
	
	/**
	 * 
	 * @param context
	 */
	private void welcomeMessage(RoutingContext context) {
		JsonObject responseMessage = new JsonObject();
		responseMessage.put("status", "Api is up and running!!");
		context.response().setStatusCode(200).end(responseMessage.encode());
	}
	
	/**
	 * 
	 * @param context
	 */
	private void create(RoutingContext context) {
		ApplicationRequest request = new ApplicationRequest();
		request.setAction("create");
		request.setRequest(context.getBodyAsString());
		DeliveryOptions deliveryOptions = new DeliveryOptions();
		deliveryOptions.addHeader("action","create");
		vertx.eventBus().send(SPRING_WORKER_SERVICE, request.toString(),deliveryOptions, responseHandler-> {
			if (responseHandler.succeeded()) {
				context.response().setStatusCode(201).putHeader("content-type","application/json").end(responseHandler.result().body().toString());
			} else {
				JsonObject object = new JsonObject().put("500", "Internal server error");
				context.response().setStatusCode(500).end(object.encode());
			}
		});
	}
	
	/**
	 * 
	 * @param context
	 */
	private void findAll(RoutingContext context) {
		DeliveryOptions deliveryOptions = new DeliveryOptions();
		deliveryOptions.addHeader("page",context.request().getParam("page"));
		deliveryOptions.addHeader("action","findall");
		vertx.eventBus().send(SPRING_WORKER_SERVICE, "",deliveryOptions ,responseHandler-> {
			if (responseHandler.succeeded()) {
				context.response().setStatusCode(200).putHeader("content-type","application/json").end(responseHandler.result().body().toString());
			} else {
				JsonObject object = new JsonObject().put("500", "Internal server error");
				context.response().setStatusCode(500).end(object.encode());
			}
		});
	}
	
	
	/**
	 * 
	 * @param context
	 */
	private void get(RoutingContext context) {
		DeliveryOptions deliveryOptions = new DeliveryOptions();
		deliveryOptions.addHeader("txnid",context.request().getParam("txnid"));
		deliveryOptions.addHeader("action","findbyid");
		vertx.eventBus().send(SPRING_WORKER_SERVICE, "",deliveryOptions ,responseHandler-> {
			if (responseHandler.succeeded()) {
				context.response().setStatusCode(200).putHeader("content-type","application/json").end(responseHandler.result().body().toString());
			} else {
				JsonObject object = new JsonObject().put("500", "Internal server error");
				context.response().setStatusCode(500).end(object.encode());
			}
		});
	}
}
