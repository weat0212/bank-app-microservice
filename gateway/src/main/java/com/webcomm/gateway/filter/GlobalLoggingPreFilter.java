package com.webcomm.gateway.filter;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.webcomm.gateway.model.HttpMessageLog;
import com.webcomm.gateway.service.HttpMessageLogService;

import org.slf4j.LoggerFactory;
import org.bouncycastle.util.Strings;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GlobalLoggingPreFilter implements GlobalFilter, Ordered {

	final Logger logger = LoggerFactory.getLogger(GlobalLoggingPreFilter.class);
	public static final String CACHE_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";

	@Autowired
	HttpMessageLogService httpMessageLogService;

	// DB log
	HttpMessageLog msg = new HttpMessageLog(System.currentTimeMillis());

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		String requestPath = exchange.getRequest().getPath().toString();
		logger.info("Request Path [path: " + requestPath + "]");

		// Body
		Flux<DataBuffer> cachedBody = exchange.getAttribute(CACHE_REQUEST_BODY_OBJECT_KEY);
		
		if(cachedBody != null) {
			String raw = toRaw(cachedBody);

			// DB log
			msg.setMsgBody(raw);

			// console log
			logger.info(raw);

			// Headers
			HttpHeaders headers = exchange.getRequest().getHeaders();

			// DB log
			msg.setMessage("Request Path: " + requestPath + ", " + headers.toString());
			httpMessageLogService.save(msg);

			Set<String> headerNames = headers.keySet();

			headerNames.forEach((headerName) -> {

				String headerValue = headers.getFirst(headerName);
				logger.info("Request Header [" + headerName + ": " + headerValue + "]");

			});
		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return 0;
	}

	private static String toRaw(Flux<DataBuffer> body) {
		AtomicReference<String> rawRef = new AtomicReference<>();
		body.subscribe(buffer -> {
			byte[] bytes = new byte[buffer.readableByteCount()];
			buffer.read(bytes);
			DataBufferUtils.release(buffer);
			rawRef.set(Strings.fromUTF8ByteArray(bytes));
		});
		return rawRef.get();
	}
}
