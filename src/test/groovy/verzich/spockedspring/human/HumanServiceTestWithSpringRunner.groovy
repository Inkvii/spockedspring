package verzich.spockedspring.human

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class HumanServiceTestWithSpringRunner extends Specification {
    @Autowired
    HumanService humanService

    def "register new human"() {
        given: "Request is valid"
        HumanController.RegisterHumanRequest request = new HumanController.RegisterHumanRequest("Alois", new BigDecimal("123.45"))
        when: "User is being registered"
        def response = humanService.registerHumanIfNotFound(request)
        then: "User is saved to database and its id is returned"
        response == 1L
    }
}
