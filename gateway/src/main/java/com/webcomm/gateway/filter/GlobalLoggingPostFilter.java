package com.webcomm.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.webcomm.gateway.model.HttpMessageLog;
import com.webcomm.gateway.repository.HttpMessageLogRepository;
import com.webcomm.gateway.service.HttpMessageLogService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GlobalLoggingPostFilter implements GlobalFilter, Ordered {

	final Logger logger = LoggerFactory.getLogger(GlobalLoggingPostFilter.class);
	
	@Autowired
	HttpMessageLogService httpMessageLogService;
	
	// DB log
	HttpMessageLog msg = new HttpMessageLog(System.currentTimeMillis());

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		return chain.filter(
				exchange.mutate().response(logResponse(exchange)).build())
				.then(Mono.fromRunnable(()-> {
			
					
			HttpStatus status = exchange.getResponse().getStatusCode();
			logger.info("Response Message [status code: " + status + "]");
			
			// Header
			HttpHeaders headers = exchange.getResponse().getHeaders();
			
			// DB log
			msg.setMessage("status code: " + status + ", " + headers.toString());
			httpMessageLogService.save(msg);
			
			Set<String> headerNames = headers.keySet();
			
			headerNames.forEach((headerName)-> {
				
				String headerValue = headers.getFirst(headerName);
				logger.info("Response Header [" + headerName + ": " + headerValue + "]");
				
			});
		}));
	}

	@Override
	public int getOrder() {
		return -1;
	}
	
	private ServerHttpResponseDecorator logResponse(ServerWebExchange exchange) {
        ServerHttpResponse origResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = origResponse.bufferFactory();

        return new ServerHttpResponseDecorator(origResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        try {
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            var bodyContent = new String(content, StandardCharsets.UTF_8);
                            // DB log
                            msg.setMsgBody(bodyContent);
                            // console log
                            logger.info("{}", bodyContent);
                            
                            return bufferFactory.wrap(content);
                        } finally {
                            DataBufferUtils.release(dataBuffer);
                        }
                    }));

                }
                return super.writeWith(body);
            }
        };

    }

}
