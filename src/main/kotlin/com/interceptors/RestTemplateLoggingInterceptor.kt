package com.interceptors

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

private val log = KotlinLogging.logger {}

@Service
class RestTemplateLoggingInterceptor(
    @Value("\${feature.rest-template-log-all:false}")
    private val enabled: Boolean,
) : ClientHttpRequestInterceptor {

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution,
    ): ClientHttpResponse = execution.execute(request, body)
        .also { response ->
            if (enabled) {
                log(request, body, response)
            }
        }

    private fun log(
        request: HttpRequest,
        body: ByteArray,
        response: ClientHttpResponse,
    ) {
        log.info {
            """
            Request sent:
            METHOD: '${request.method}',
            URI: '${request.uri}',
            HEADERS: '${request.headers}',
            BODY: '${String(body, StandardCharsets.UTF_8)}'
            """.cleanLogging()
        }
        log.info {
            """
            Response received:
            STATUS: '${response.statusCode}',
            REASON: '${response.statusText}'
            """.cleanLogging()
        }
    }
}

private fun String.cleanLogging() = this
    .replace("\n", "")
    .replace(" ", "")
    .trimIndent()
