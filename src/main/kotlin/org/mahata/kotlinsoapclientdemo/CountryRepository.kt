package org.mahata.kotlinsoapclientdemo

import org.mahata.kotlinsoapclientdemo.wsdl.GetCountryRequest
import org.mahata.kotlinsoapclientdemo.wsdl.GetCountryResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ws.client.core.support.WebServiceGatewaySupport
import org.springframework.ws.soap.client.core.SoapActionCallback

class CountryRepository : WebServiceGatewaySupport() {
    fun getCountry(country: String): GetCountryResponse {
        val request = GetCountryRequest().apply { name = country }

        log.info("Requesting location for $country")

        return webServiceTemplate
            .marshalSendAndReceive(
                "http://localhost:18081/ws/countries",
                request,
                SoapActionCallback("http://spring.io/guides/gs-producing-web-service/GetCountryRequest"),
            ) as GetCountryResponse
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CountryRepository::class.java)
    }
}
