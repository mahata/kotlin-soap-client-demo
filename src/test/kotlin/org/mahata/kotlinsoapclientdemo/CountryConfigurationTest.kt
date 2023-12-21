package org.mahata.kotlinsoapclientdemo

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [CountryConfiguration::class])
class CountryConfigurationTest {
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Test
    fun contextLoads() {
        assertNotNull(applicationContext)
    }

    @Test
    fun marshallerBeanExists() {
        val marshaller = applicationContext.getBean(Jaxb2Marshaller::class.java)
        assertNotNull(marshaller)
    }

    @Test
    fun countryClientBeanExists() {
        val countryClient = applicationContext.getBean(CountryRepository::class.java)
        assertNotNull(countryClient)
        assertTrue(countryClient.marshaller is Jaxb2Marshaller)
    }
}
