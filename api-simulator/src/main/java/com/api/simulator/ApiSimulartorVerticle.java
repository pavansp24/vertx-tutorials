package com.api.simulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class ApiSimulartorVerticle extends AbstractVerticle {

	Random random = new Random();
	private ConfigRetriever retriever;
	private String response;
	private String apiName = System.getProperty("api","/test");

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		super.start();
		String path = getClass().getResource("server.jks").getPath();
		Router router = Router.router(vertx);
		router.post().handler(BodyHandler.create());
		System.out.println("configuring api : "+apiName);

		router.route("/").handler(this::welcomeMessage);

		router.route("/simulate").method(HttpMethod.POST).blockingHandler(this::captureResponse, false);
		router.route(apiName).method(HttpMethod.POST).blockingHandler(this::respond, false);

		HttpServerOptions serverOptions = new HttpServerOptions();
		serverOptions.setSsl(true);
		serverOptions.setKeyStoreOptions(new JksOptions().setPath(path).setPassword("password"));

		vertx.createHttpServer(serverOptions).requestHandler(router).listen(8443, result -> {
			if (result.succeeded()) {
				System.out.println("Server created!!");
				startFuture.complete();
			} else {
				System.out.println(result.toString());
				startFuture.fail("server creation failed");
			}
		});
		// This to load the desired api response as configuration
		JsonObject jsonObject = new JsonObject();
		jsonObject.put("path", ApiSimulartorVerticle.class.getResource("response.json").getPath());
		ConfigStoreOptions env = new ConfigStoreOptions()
				.setType("file")
				.setFormat("json")
				.setConfig(jsonObject);
		ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(env);
		retriever = ConfigRetriever.create(vertx, options);
		retriever.getConfig(completionHandler -> {
			if (completionHandler.succeeded()) {
				JsonObject result = completionHandler.result();
				System.out.println("Response Configuration loaded");
				System.out.println(result);
			} else {
				System.out.println("Response Configuration not loaded");
			}
		});


	}

	private void captureResponse(RoutingContext context) {
		response = context.getBodyAsString();
		JsonObject jsonResponse = new JsonObject().put("message", "Api response stored");
		context.response().setStatusCode(200).putHeader("Content-Type", "application/json")
				.end(jsonResponse.encodePrettily());
	}
	
	private void welcomeMessage(RoutingContext context) {
		context.response().setStatusCode(200).putHeader("Content-Type", "text/plain")
				.end("Welcome to vertx based simulator");
	}

	private void respond(RoutingContext context) {
		
		System.out.println("Request body: \n" +context.getBodyAsString());

		vertx.executeBlocking(future -> {

			int waitTime = random.nextInt(1000-600) + 600;
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				context.failure();
			}
			
			/*
			 * retriever.getConfig(completionHandler -> { if (completionHandler.succeeded())
			 * { future.complete(completionHandler.result().encodePrettily()); } else {
			 * future.complete(new JsonObject().encodePrettily()); } });
			 */
			
			future.complete(response);
			

		

		}, resultHandler -> {

			if (resultHandler.succeeded()) {
				context.response().setStatusCode(200).putHeader("Content-Type", "application/json").end(resultHandler.result().toString());
			} else {
				context.response().setStatusCode(500).putHeader("Content-Type", "application/json").end(resultHandler.cause().getMessage());
			}

		});

	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(ApiSimulartorVerticle.class.getName(), result -> {
			if (result.succeeded()) {
				System.out.println("Verticle successfully deployed!!");
			} else {
				System.out.println("Verticle deployment failed with the message" + result.cause());
			}
		});

	}

}
