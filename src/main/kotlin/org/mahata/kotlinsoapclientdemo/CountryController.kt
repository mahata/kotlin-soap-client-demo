package org.mahata.kotlinsoapclientdemo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/countries")
class CountryController(
    private val countryRepository: CountryRepository,
) {
    @GetMapping
    fun get(): String = countryRepository.getCountry("Spain").country.currency.toString()
}
