package com.vertx.springboot.integration.verticle;

import java.math.BigDecimal;

import com.vertx.springboot.integration.configuration.VertxApplicationContext;
import com.vertx.springboot.integration.jpa.model.PaymentTransaction;
import com.vertx.springboot.integration.request.ApplicationRequest;
import com.vertx.springboot.integration.service.impl.CrudImpl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * 
 * @author pavan
 *
 */
public class WorkerVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		vertx.eventBus().consumer("crudservice", this::handler);
	}

	/**
	 * 
	 * @param message
	 */
	private void handler(Message<String> message) {

		String response;
		switch (message.headers().get("action")) {
		case "create":
			ApplicationRequest request = Json.decodeValue(message.body(), ApplicationRequest.class);
			response = create(request);
			message.reply(response);
			break;

		case "findall":
			response = findall(new Integer(message.headers().get("page")));
			message.reply(response);
			break;

		case "findbyid":
			response = findByTxnId(message.headers().get("txnid"));
			message.reply(response);

		default:
			break;
		}
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	private String create(final ApplicationRequest request) {
		CrudImpl crudImpl = getCrudImpl();
		crudImpl.save(transformToPaymentTransaction(request.getRequest()));
		return request.getRequest();
	}
	
	
	/**
	 * 
	 * @return
	 */
	private CrudImpl getCrudImpl() {
		CrudImpl crudImpl = VertxApplicationContext.getApplicationContext().getBean(CrudImpl.class);
		return crudImpl;
	}

	/**
	 * 
	 * @param page
	 * @return
	 */
	private String findall(int page) {
		CrudImpl crudImpl = getCrudImpl();
		return Json.encode(crudImpl.getTransactions(page));
	}

	/**
	 * 
	 * @param transactionId
	 * @return
	 */
	private String findByTxnId(String transactionId) {
		PaymentTransaction paymentTransaction = getCrudImpl().getByTRansactionId(transactionId);
		if (null != paymentTransaction) {
			return Json.encode(paymentTransaction);
		}
		return "";
	}

	/**
	 * 
	 * @param requestBody
	 * @return
	 */
	private PaymentTransaction transformToPaymentTransaction(String requestBody) {
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		JsonObject jsonObject = new JsonObject(requestBody);
		paymentTransaction.setSender(jsonObject.getString("firstname-") + jsonObject.getString("lastname"));
		paymentTransaction.setAddress(jsonObject.getString("address"));
		paymentTransaction.setIp(jsonObject.getString("ip"));
		paymentTransaction.setBusinessType(jsonObject.getString("ecommerce"));
		paymentTransaction.setCity(jsonObject.getString("city"));
		paymentTransaction.setEmail(jsonObject.getString("email"));
		paymentTransaction.setPinCode(jsonObject.getString("pincode"));
		paymentTransaction.setReceiver(jsonObject.getString("receiver"));
		paymentTransaction.setSender(jsonObject.getString("sender"));
		paymentTransaction.setTransactionId(jsonObject.getString("transactionid"));
		paymentTransaction.setAmount(new BigDecimal(jsonObject.getString("totalamt")));
		return paymentTransaction;
	}

}
