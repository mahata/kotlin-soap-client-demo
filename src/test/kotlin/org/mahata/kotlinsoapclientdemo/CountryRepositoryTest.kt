package org.mahata.kotlinsoapclientdemo

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mahata.kotlinsoapclientdemo.wsdl.Country
import org.mahata.kotlinsoapclientdemo.wsdl.Currency
import org.mahata.kotlinsoapclientdemo.wsdl.GetCountryResponse
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.ws.client.core.WebServiceTemplate

class CountryRepositoryTest {
    @Test
    fun testGetCountry() {
        val webServiceTemplateMock = mockk<WebServiceTemplate>()

        val expectedResponse =
            GetCountryResponse().apply {
                country =
                    Country().apply {
                        name = "Spain"
                        currency = Currency.EUR
                        capital = "Madrid"
                        population = 46704314
                    }
            }

        every {
            webServiceTemplateMock.marshalSendAndReceive(any(), any(), any())
        } returns expectedResponse

        val countryRepository =
            CountryRepository().apply {
                marshaller = Jaxb2Marshaller()
                unmarshaller = Jaxb2Marshaller()
                webServiceTemplate = webServiceTemplateMock
            }

        val response = countryRepository.getCountry("Spain")

        assertEquals("Spain", response.country.name)
        assertEquals("EUR", response.country.currency.toString())
        assertEquals("Madrid", response.country.capital)
        assertEquals(46704314, response.country.population)
    }
}
