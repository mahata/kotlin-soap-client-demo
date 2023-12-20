package org.mahata.kotlinsoapclientdemo

import com.example.consumingwebservice.wsdl.GetCountryRequest
import com.example.consumingwebservice.wsdl.GetCountryResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ws.client.core.support.WebServiceGatewaySupport
import org.springframework.ws.soap.client.core.SoapActionCallback

class CountryClient : WebServiceGatewaySupport() {

    fun getCountry(country: String): GetCountryResponse {
        val request = GetCountryRequest().apply { name = country }

        log.info("Requesting location for $country")

        return webServiceTemplate
            .marshalSendAndReceive(
                "http://localhost:8080/ws/countries", request,
                SoapActionCallback("http://spring.io/guides/gs-producing-web-service/GetCountryRequest")
            ) as GetCountryResponse
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CountryClient::class.java)
    }
}
