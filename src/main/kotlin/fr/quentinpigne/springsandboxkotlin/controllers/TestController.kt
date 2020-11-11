package fr.quentinpigne.springsandboxkotlin.controllers

import fr.quentinpigne.springsandboxkotlin.services.TestService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotEmpty

@Validated
@RestController
@RequestMapping("test")
class TestController(
    private val testService: TestService
) {

    @GetMapping(value = [""], params = ["test"])
    fun test(@NotEmpty(message = "Test parameter cannot be empty.") @RequestParam("test") test: List<Int>): List<Int?> {
        return testService.longRunningTreatment(test)
    }
}
