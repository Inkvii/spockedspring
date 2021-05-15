package verzich.spockedspring.petshop

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Ignore
import spock.lang.Specification
import verzich.spockedspring.human.HumanEntity
import verzich.spockedspring.human.HumanRepository
import verzich.spockedspring.pet.Pet
import verzich.spockedspring.petshop.PetshopController.BuyPetRequest

@SpringBootTest
class PetshopServiceTest extends Specification {
    @Autowired
    PetshopService service

    @Autowired
    PetshopAdministrationService petshopAdministrationService

    /**
     * Using Spy with kotlin and spock can be a bit problematic since you cant change only one property and you cant use kotlin's copy
     * method because well... this is groovy
     * @return
     */
    @Ignore
    def "buy pet"() {
        given:
        HumanRepository humanRepository = Spy()

        BuyPetRequest request = new BuyPetRequest(1L, 1L)
        HumanEntity humanEntity = new HumanEntity("Alois", new BigDecimal("1000.12"), new ArrayList<Pet>())
        humanEntity.setId(1L)
        humanRepository.findById(1L) >> humanEntity
        when:
        service.buyPet(request)
        then:
        1 * petshopAdministrationService.checkHumanCanAffordBuyingPet(humanEntity, _) >> true
        1 * petshopAdministrationService.checkHumanCanAffordFeedingAllPets(humanEntity, _) >> true
    }
}
