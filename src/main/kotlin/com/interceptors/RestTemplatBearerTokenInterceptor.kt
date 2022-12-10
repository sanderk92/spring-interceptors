package com.interceptors

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Service

@Service
class RestTemplatBearerTokenInterceptor(
    @Value("\${feature.authenticate:false}")
    private val enabled: Boolean,
) : ClientHttpRequestInterceptor {

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution,
    ): ClientHttpResponse {
        if (enabled) {
            fetchToken().let { token -> request.headers.add(HttpHeaders.AUTHORIZATION, "Bearer $token") }
        }
        return execution.execute(request, body)
    }

    private fun fetchToken(): String {
        throw NotImplementedError("Fetch token must first be implemented")
    }
}
