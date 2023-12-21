package org.mahata.kotlinsoapclientdemo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.oxm.jaxb.Jaxb2Marshaller


@Configuration
class CountryConfiguration {

    @Bean
    fun marshaller(): Jaxb2Marshaller = Jaxb2Marshaller().apply {
        // this package must match the package name in `build.gradle.kts` (ant.withGroovyBuilder section)
        contextPath = "org.mahata.kotlinsoapclientdemo.wsdl"
    }

    @Bean
    fun countryClient(marshaller: Jaxb2Marshaller): CountryClient = CountryClient().apply {
        defaultUri = "http://localhost:18081/ws"
        this.marshaller = marshaller
        unmarshaller = marshaller
    }
}
