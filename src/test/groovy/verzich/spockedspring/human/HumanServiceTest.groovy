package verzich.spockedspring.human


import spock.lang.Specification
import verzich.spockedspring.human.HumanController.RegisterHumanRequest
import verzich.spockedspring.pet.Pet

/**
 * This is example of spock test using mocks
 */
class HumanServiceTest extends Specification {

    HumanService service
    HumanRepository humanRepository = Mock()

    void setup() {
        service = new HumanService(humanRepository)
    }

    def "register new human"() {
        given: "Request is valid"
        RegisterHumanRequest request = new RegisterHumanRequest("Alois", new BigDecimal("123.45"))

        HumanEntity responseAfterSave = new HumanEntity(request.name, request.money, new ArrayList<Pet>())
        responseAfterSave.id = 1L

        and: "User with given name doesnt exist in database"
        humanRepository.findByName(request.name) >> Optional.empty()
        when: "User is being registered"
        def response = service.registerHumanIfNotFound(request)
        then: "User is saved to database and its id is returned"
        1 * humanRepository.save(_) >> responseAfterSave
        1 * humanRepository.flush()
        response == 1L
    }
}
