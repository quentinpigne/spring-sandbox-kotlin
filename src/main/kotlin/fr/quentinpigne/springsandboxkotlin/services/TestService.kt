package fr.quentinpigne.springsandboxkotlin.services

import org.springframework.stereotype.Service

@Service
class TestService {

    fun longRunningTreatment(param: List<Int?>): List<Int?> {
        Thread.sleep(1000 * param.size.toLong())
        return param
    }
}
