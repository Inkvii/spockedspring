package verzich.spockedspring.petshop

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import verzich.spockedspring.exceptions.InsufficientFundsException
import verzich.spockedspring.pet.Pet
import java.math.BigDecimal

@RestController
@RequestMapping("/petshop", produces = ["application/json"])
class PetshopController(val petshopService: PetshopService) {
    val log = LoggerFactory.getLogger(this.javaClass)


    data class BrowseAvailablePetsResponse(val availablePets: List<Pet>)
    data class BuyPetRequest(val humanId: Long, val chosenPetId: Long)
    data class BuyPetResponse(val humanId: Long, val numberOfPets: Int?, val purchasingPrice: BigDecimal?, val status: String = "OK")
    enum class PetTypeEnum {
        CAT,
        DOG
    }

    data class PetDto(
        val name: String,
        val age: Int = 0,
        val purchasingPrice: BigDecimal,
        val initialMoneyConsumptionPerDay: BigDecimal
    )

    data class RegisterNewPetRequest(val pet: PetDto, val petTypeEnum: PetTypeEnum)
    data class RegisterNewPetResponse(val pet: PetDto, val petId: Long)

    @GetMapping("/{petshopId}/browse")
    fun browseAvailablePets(@PathVariable("petshopId") petshopId: Long): ResponseEntity<BrowseAvailablePetsResponse> {
        try {
            val availablePets = petshopService.browseAvailablePets(petshopId)
            return ResponseEntity.ok(BrowseAvailablePetsResponse(availablePets))
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{petshopId}/buy")
    fun buyPet(@PathVariable("petshopId") petshopId: Long, @RequestBody buyPetRequest: BuyPetRequest): ResponseEntity<BuyPetResponse> {
        try {
            val buyPetResponse = petshopService.buyPet(buyPetRequest)
            return ResponseEntity.ok(buyPetResponse)
        } catch (e: InsufficientFundsException) {
            return ResponseEntity.badRequest().body(BuyPetResponse(buyPetRequest.humanId, null, null, "ERROR"))
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{petshopId}/register")
    fun registerNewPet(
        @PathVariable("petshopId") petshopId: Long,
        @RequestBody registerNewPetRequest: RegisterNewPetRequest
    ): ResponseEntity<RegisterNewPetResponse> {
        log.info("Request: $registerNewPetRequest")
        try {
            val pet = petshopService.petFactory(registerNewPetRequest, petshopId)
            return ResponseEntity.ok(RegisterNewPetResponse(registerNewPetRequest.pet, pet.id!!))
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.notFound().build()
        }
    }

}
