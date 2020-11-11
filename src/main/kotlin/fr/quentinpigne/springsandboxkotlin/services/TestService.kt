package fr.quentinpigne.springsandboxkotlin.services

import org.springframework.stereotype.Service
import fr.quentinpigne.springsandboxkotlin.utils.cache.cacheablelist.CacheableList

@Service
class TestService {

    @CacheableList("longRunningTreatment")
    fun longRunningTreatment(param: List<Int>): List<Int?> {
        Thread.sleep(1000 * param.size.toLong())
        return param
    }
}
