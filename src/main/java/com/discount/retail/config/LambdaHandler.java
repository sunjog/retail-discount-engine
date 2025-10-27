package com.discount.retail.config;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.discount.retail.RetailDiscountEngineApplication;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private final SpringBootProxyHandlerBuilder<AwsProxyRequest> handler;

    public LambdaHandler() throws ContainerInitializationException {
        handler = new SpringBootProxyHandlerBuilder<AwsProxyRequest>()
                .defaultProxy()
                .springBootApplication(RetailDiscountEngineApplication.class);
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) throws RuntimeException {
        try {
            return handler.build().proxy(input, context);
        } catch (ContainerInitializationException e) {
            throw new RuntimeException(e);
        }
    }
}
