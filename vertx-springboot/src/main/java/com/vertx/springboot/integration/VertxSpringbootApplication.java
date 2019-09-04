package com.vertx.springboot.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.vertx.springboot.integration.verticle.RestVerticle;
import com.vertx.springboot.integration.verticle.WorkerVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * 
 * @author pavan
 *
 */
@SpringBootApplication
public class VertxSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(VertxSpringbootApplication.class, args);
	}

	@EventListener
	public void deployVerticles(ApplicationReadyEvent event) {

		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(RestVerticle.class.getName(), completionHandler -> {
			if (completionHandler.succeeded()) {
				System.out.println("RestVerticle successfully deployed");
			} else {
				System.err.println("Error while deploying RestVerticle " + completionHandler.cause().getMessage());
			}
		});

		DeploymentOptions deploymentOptions = new DeploymentOptions();
		deploymentOptions.setWorker(true);
		vertx.deployVerticle(WorkerVerticle.class.getName(), deploymentOptions ,completionHandler -> {
			if (completionHandler.succeeded()) {
				System.out.println("WorkerVerticle successfully deployed");
			} else {
				System.err.println("Error while deploying WorkerVerticle " + completionHandler.cause().getMessage());
			}
		});
	}
}
