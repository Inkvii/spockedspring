package verzich.spockedspring.petshop

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import verzich.spockedspring.human.HumanEntity
import verzich.spockedspring.human.HumanRepository
import verzich.spockedspring.pet.Cat
import verzich.spockedspring.pet.Pet
import verzich.spockedspring.pet.PetRepository
import verzich.spockedspring.petshop.PetshopController.BuyPetRequest

@SpringBootTest
class PetshopServiceTest extends Specification {

    PetshopService service

    HumanRepository humanRepository = Mock(HumanRepository)
    PetRepository petRepository = Mock(PetRepository)

    @Autowired
    PetshopRepository petshopRepository

    // Spy is used here to demonstrate that we can partially mock whatever we want
    // If that was mock or stub, then we could not check the flow in "then" block
    PetshopAdministrationService petshopAdministrationService = Spy(PetshopAdministrationService)

    void setup() {
        service = new PetshopService(petshopRepository, petRepository, humanRepository, petshopAdministrationService)
    }

    def "buy pet"() {
        given:
        BuyPetRequest request = new BuyPetRequest(1L, 1L)
        HumanEntity humanEntity = new HumanEntity("Alois", new BigDecimal("1000.12"), new ArrayList<Pet>())
        humanEntity.setId(1L)
        Cat cat = new Cat("name", 1, BigDecimal.ONE, BigDecimal.ONE, null, new PetshopEntity("name", new ArrayList<Pet>(), BigDecimal.ONE))
        cat.setId(1L)

        humanRepository.findById(request.humanId) >> Optional.of(humanEntity)
        petRepository.findByIdAndOwnerIsNull(request.chosenPetId) >> Optional.of(cat)

        when:
        service.buyPet(request)
        then:
        1 * petshopAdministrationService.checkHumanCanAffordBuyingPet(humanEntity, cat)
        1 * petshopAdministrationService.checkHumanCanAffordFeedingAllPets(humanEntity, cat)
        1 * petshopAdministrationService.transferOwnershipToHuman(humanEntity, cat)
    }
}
