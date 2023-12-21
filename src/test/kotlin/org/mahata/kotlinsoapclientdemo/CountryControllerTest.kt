package org.mahata.kotlinsoapclientdemo

import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mahata.kotlinsoapclientdemo.wsdl.Country
import org.mahata.kotlinsoapclientdemo.wsdl.Currency
import org.mahata.kotlinsoapclientdemo.wsdl.GetCountryResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class CountryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var countryRepository: CountryRepository

    @Test
    fun testGetCountryEndpoint() {
        every { countryRepository.getCountry("Spain") } returns
            GetCountryResponse().apply {
                country =
                    Country().apply {
                        currency = Currency.EUR
                    }
            }

        mockMvc.perform(get("/api/v1/countries"))
            .andExpect(status().isOk)
            .andExpect { result ->
                assertEquals("EUR", result.response.contentAsString)
            }
    }
}
