package fr.quentinpigne.springsandboxkotlin.services

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class TestService {

    @Cacheable("longRunningTreatment")
    fun longRunningTreatment(param: List<Int?>): List<Int?> {
        Thread.sleep(1000 * param.size.toLong())
        return param
    }
}
