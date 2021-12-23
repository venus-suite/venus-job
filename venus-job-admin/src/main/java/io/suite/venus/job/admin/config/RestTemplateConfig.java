package io.suite.venus.job.admin.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 *
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
		ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler() {
			public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
				return true;
			}

			public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {

			}
		};
		restTemplate.setErrorHandler(responseErrorHandler);
		return restTemplate;
	}

	@Bean
	public ClientHttpRequestFactory httpRequestFactory() {
		return new HttpComponentsClientHttpRequestFactory(httpClient());

	}

	@Bean
	public HttpClient httpClient() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000) // 服务器返回数据(response)的时间，超过该时间抛出read
				// timeout
				.setConnectTimeout(2000)// 连接上服务器(握手成功)的时间，超出该时间抛出connect
				// timeout
				.setConnectionRequestTimeout(5000)// 从连接池中获取连接的超时时间，超过该时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException:
				// Timeout waiting for
				// connection from pool
				.build();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		// 设置整个连接池最大连接数 根据自己的场景决定
		connectionManager.setMaxTotal(400);
		// 路由是对maxTotal的细分,每个域名200个连接
		connectionManager.setDefaultMaxPerRoute(200);

		return HttpClientBuilder.create().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
				.setRetryHandler(new HttpRequestRetryHandler() {

					public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
						if (exception instanceof Exception) {
							// 超时重试3次
							if (executionCount >= 3) {
								return false;
							}
							return true;
						}
						return false;

					}

				}).build();
	}

}