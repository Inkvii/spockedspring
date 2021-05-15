package verzich.spockedspring.human

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
        val id = humanService.registerHumanIfNotFound(registerHumanRequest)
        return ResponseEntity.ok(RegisterHumanResponse(id))
    }

    @GetMapping("/{humanId}/soundboard")
    fun soundboard(@PathVariable("humanId") humanId: Long): ResponseEntity<List<String>> {
        try {
            val resultList = humanService.doPetSounds(humanId)
            return ResponseEntity.ok(resultList)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.notFound().build()
        }
    }
}
