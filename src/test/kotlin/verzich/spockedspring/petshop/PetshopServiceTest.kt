package verzich.spockedspring.petshop

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import verzich.spockedspring.human.HumanEntity
import verzich.spockedspring.human.HumanRepository
import verzich.spockedspring.pet.Cat
import verzich.spockedspring.pet.PetRepository
import java.util.*

class PetshopServiceTest : FunSpec({

    // Mocking this might be tedious if we use constructor based autowiring - each new autowired component will break tests
    // Might be good prevention from creating god objects though...
    val petshopRepository = mockk<PetshopRepository>()
    val petRepository = mockk<PetRepository>()
    val petshopAdministrationService = mockk<PetshopAdministrationService>()
    val humanRepository = mockk<HumanRepository>()

    val petshopService = PetshopService(
        petRepository = petRepository,
        petshopAdministrationService = petshopAdministrationService,
        humanRepository = humanRepository,
        petshopRepository = petshopRepository
    )
    afterTest {
        // After each test clearing, mocks must be cleared because otherwise they will be reused in next test.
        // That will affect expected number of calls per tests (e.g. expected save() to be called once, but 2 tests call it and so its
        // called twice instead (failing second test)
        clearAllMocks()
    }

    test("buyPet will succeed") {
        val request = PetshopController.BuyPetRequest(1L, 10L)

        val petshop = PetshopEntity("Cheap labor")
        val cat = Cat("testing name", 1, 10.toBigDecimal(), 1.toBigDecimal(), null, petshop)
        cat.id = 10L
        val human = HumanEntity("Alois", 1000.toBigDecimal())
        human.id = 1L

        // mock interactions so they return desired values
        every { petRepository.findByIdAndOwnerIsNull(request.chosenPetId) } returns Optional.of(cat)
        every { humanRepository.findById(request.humanId) } returns Optional.of(human)
        every { petshopAdministrationService.checkHumanCanAffordBuyingPet(human, cat) } returns true
        every { petshopAdministrationService.checkHumanCanAffordFeedingAllPets(human, cat) } returns true
        every { petshopAdministrationService.transferOwnershipToHuman(human, cat) } just runs

        // call what you test
        val response = petshopService.buyPet(request)

        // verify that all calls were called (by default all mocks are strict)
        verifyAll {
            petRepository.findByIdAndOwnerIsNull(request.chosenPetId)
            humanRepository.findById(request.humanId)
            petshopAdministrationService.checkHumanCanAffordBuyingPet(human, cat)
            petshopAdministrationService.checkHumanCanAffordFeedingAllPets(human, cat)
            petshopAdministrationService.transferOwnershipToHuman(human, cat)
            response shouldNotBe null
        }

    }

})
