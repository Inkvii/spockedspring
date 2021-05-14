package verzich.spockedspring.human

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/human", produces = ["application/json"])
class HumanController(
    val humanService: HumanService
) {

    data class RegisterHumanRequest(val name: String, val money: BigDecimal = 100.toBigDecimal())
    data class RegisterHumanResponse(val id: Long?)

    @PostMapping("/register", consumes = ["application/json"])
    fun registerHuman(@RequestBody registerHumanRequest: RegisterHumanRequest): ResponseEntity<RegisterHumanResponse> {
        val id = humanService.registerHuman(registerHumanRequest)
        return ResponseEntity.ok(RegisterHumanResponse(id))
    }
}
