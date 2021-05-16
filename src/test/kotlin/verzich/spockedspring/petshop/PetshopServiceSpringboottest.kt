package verzich.spockedspring.petshop

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.verifyAll
import org.springframework.boot.test.context.SpringBootTest
import verzich.spockedspring.human.HumanEntity
import verzich.spockedspring.pet.Cat
import java.math.BigDecimal
import java.util.*


@SpringBootTest
class PetshopServiceSpringboottest(val petshopService: PetshopService) : FunSpec({


    test("Buy pet") {

        val request = PetshopController.BuyPetRequest(1L, 1L)

        val humanEntity = HumanEntity("Alois", BigDecimal("1000.12"), mutableListOf())
        humanEntity.id = 1L

        val cat = Cat("name", 1, BigDecimal.ONE, BigDecimal.ONE, null, PetshopEntity("name", mutableListOf(), BigDecimal.ONE))
        cat.id = 1L

        every { petshopService.humanRepository.findById(request.humanId) } returns Optional.of(humanEntity)
        every { petshopService.petRepository.findByIdAndOwnerIsNull(request.chosenPetId) } returns Optional.of(cat)

        // when
        val response = petshopService.buyPet(request)

        // then
        verifyAll {
            petshopService.petRepository.findByIdAndOwnerIsNull(request.chosenPetId)
            petshopService.humanRepository.findById(request.humanId)
            petshopService.petshopAdministrationService.checkHumanCanAffordBuyingPet(humanEntity, cat)
            petshopService.petshopAdministrationService.checkHumanCanAffordFeedingAllPets(humanEntity, cat)
            petshopService.petshopAdministrationService.transferOwnershipToHuman(humanEntity, cat)
            response shouldNotBe null
        }
    }
})
