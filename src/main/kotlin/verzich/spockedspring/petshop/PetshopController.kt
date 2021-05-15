package verzich.spockedspring.petshop

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import verzich.spockedspring.exceptions.InsufficientFundsException
import verzich.spockedspring.pet.Pet
import java.math.BigDecimal
import javax.validation.Valid
import javax.validation.constraints.*

@RestController
@RequestMapping("/petshop", produces = ["application/json"])
class PetshopController(
    val petshopService: PetshopService,
) {
    val log = LoggerFactory.getLogger(this.javaClass)

    data class BrowseAvailablePetsResponse(val availablePets: List<Pet>)
    data class BuyPetRequest(val humanId: Long, val chosenPetId: Long)
    data class BuyPetResponse(val humanId: Long, val numberOfPets: Int?, val purchasingPrice: BigDecimal?, val status: String = "OK")
    enum class PetTypeEnum {
        CAT,
        DOG
    }

    data class PetDto(
        @get:NotBlank
        @get:Size(min = 1, max = 20, message = "Name length limitation")
        @Schema(description = "Name of the pet", example = "Mourek")
        val name: String,
        @get:PositiveOrZero
        @Schema(description = "Age of the pet which then determines how much it will cost on daily maintenance", example = "2")
        val age: Int = 0,
        @get:Positive
        @Schema(
            description = "Purchasing price from petshop store. Price is predetermined in the petshop and will not change",
            example = "101.58"
        )
        val purchasingPrice: BigDecimal,
        @get:Positive
        @Schema(
            description = "Initial money consumption per day which is set as a base for calculating daily costs formula.",
            example = "5.247"
        )
        val initialMoneyConsumptionPerDay: BigDecimal
    )

    data class RegisterNewPetRequest(
        @NotNull
        val pet: PetDto,
        @NotBlank
        val petTypeEnum: PetTypeEnum
    )

    data class RegisterNewPetResponse(
        @NotNull
        val pet: PetDto,
        @NotBlank
        val petId: Long
    )

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

    @Operation(summary = "Registers new pet to the database under specific petshop")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "New pet is successfully registered in the system.",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = RegisterNewPetResponse::class))
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Could not find data with current request", content = [Content()])
        ]
    )
    @PostMapping("/{petshopId}/register")
    fun registerNewPet(
        @PathVariable("petshopId") petshopId: Long,
        @RequestBody @Valid registerNewPetRequest: RegisterNewPetRequest
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
