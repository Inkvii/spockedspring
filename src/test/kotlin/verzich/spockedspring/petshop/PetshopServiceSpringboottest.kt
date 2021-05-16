package verzich.spockedspring.petshop

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.springframework.boot.test.context.SpringBootTest
import verzich.spockedspring.human.HumanEntity
import verzich.spockedspring.human.HumanRepository
import verzich.spockedspring.pet.Cat
import verzich.spockedspring.pet.PetRepository
import java.math.BigDecimal
import java.util.*


@SpringBootTest
class PetshopServiceSpringboottest(
    val petshopRepository: PetshopRepository,
    val petshopAdministrationService: PetshopAdministrationService
) : FunSpec() {

    // test subject - if we need to mock something, we have to declare this manually.
    lateinit var petshopService: PetshopService

    // mocked repositories because otherwise it would interact with database
    val humanRepository: HumanRepository = mockk()
    val petRepository: PetRepository = mockk()

    // do your own autowiring for the service
    override fun beforeTest(testCase: TestCase) {
        petshopService = PetshopService(petshopRepository, petRepository, humanRepository, petshopAdministrationService)
    }

    init {
        test("Buy pet") {

            val request = PetshopController.BuyPetRequest(1L, 1L)

            val humanEntity = HumanEntity("Alois", BigDecimal("1000.12"), mutableListOf())
            humanEntity.id = 1L

            val cat = Cat("name", 1, BigDecimal.ONE, BigDecimal.ONE, null, PetshopEntity("name", mutableListOf(), BigDecimal.ONE))
            cat.id = 1L

            every { humanRepository.findById(request.humanId) } returns Optional.of(humanEntity)
            every { petRepository.findByIdAndOwnerIsNull(request.chosenPetId) } returns Optional.of(cat)

            // when
            val response = petshopService.buyPet(request)

            // then
            verifyAll {
                petshopService.petRepository.findByIdAndOwnerIsNull(request.chosenPetId)
                humanRepository.findById(request.humanId)
                petshopService.petshopAdministrationService.checkHumanCanAffordBuyingPet(humanEntity, cat)
                petshopService.petshopAdministrationService.checkHumanCanAffordFeedingAllPets(humanEntity, cat)
                petshopService.petshopAdministrationService.transferOwnershipToHuman(humanEntity, cat)
                response shouldNotBe null
            }
        }
    }
}
