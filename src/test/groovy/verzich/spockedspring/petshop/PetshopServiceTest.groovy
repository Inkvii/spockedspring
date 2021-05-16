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
    @Autowired
    PetshopService service


    /**
     * To work with springboot test with spock, all autowired dependencies will have to be of type "var" otherwise you cant stub/mock/spy them
     * That can be quite unfortunate + all of those dependencies will have to be public and thus interactive from outside.
     *
     * @return
     */
    def "buy pet"() {
        given:
        service.humanRepository = Stub(HumanRepository)
        service.petRepository = Stub(PetRepository)
        service.petshopAdministrationService = Spy(PetshopAdministrationService)

        BuyPetRequest request = new BuyPetRequest(1L, 1L)
        HumanEntity humanEntity = new HumanEntity("Alois", new BigDecimal("1000.12"), new ArrayList<Pet>())
        humanEntity.setId(1L)
        Cat cat = new Cat("name", 1, BigDecimal.ONE, BigDecimal.ONE, null, new PetshopEntity("name", new ArrayList<Pet>(), BigDecimal.ONE))
        cat.setId(1L)

        service.humanRepository.findById(request.humanId) >> Optional.of(humanEntity)
        service.petRepository.findByIdAndOwnerIsNull(request.chosenPetId) >> Optional.of(cat)

        service.petshopAdministrationService.checkHumanCanAffordBuyingPet(humanEntity, cat) >> true
        service.petshopAdministrationService.checkHumanCanAffordFeedingAllPets(humanEntity, cat) >> true
        when:
        service.buyPet(request)
        then:
        1 * service.petshopAdministrationService.checkHumanCanAffordBuyingPet(humanEntity, cat)
        1 * service.petshopAdministrationService.checkHumanCanAffordFeedingAllPets(humanEntity, cat)
        1 * service.petshopAdministrationService.transferOwnershipToHuman(humanEntity, cat)
    }
}
