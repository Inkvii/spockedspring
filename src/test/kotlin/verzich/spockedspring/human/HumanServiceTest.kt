package verzich.spockedspring.human

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class HumanServiceTest(
    val humanService: HumanService
) : BehaviorSpec() {

    init {
        /**
         * Not exactly the greatest example since second test is dependant on first test and will fail if run individually or out of order.
         */
        Given("Human isnt registered in database") {
            val request = HumanController.RegisterHumanRequest("Alois", 1000.toBigDecimal())
            val anotherRequest = HumanController.RegisterHumanRequest("Hugo", 100.toBigDecimal())

            When("User tries to register") {
                val humanId = humanService.registerHumanIfNotFound(request)

                Then("Registration succeeds") {
                    humanId shouldBe 1L
                }
            }
            When("Different user tries to register") {
                val humanId = humanService.registerHumanIfNotFound(anotherRequest)

                Then("Registration succeeds") {
                    humanId shouldBe 2L
                }
            }
        }
        Given("Single unregistered human") {
            val request = HumanController.RegisterHumanRequest("Scammer", 100.toBigDecimal())
            When("User tries to register") {
                val humanId = humanService.registerHumanIfNotFound(request)

                Then("Registration succeeds") {
                    humanId shouldBe 3L
                }
            }
            When("User tries to register again") {
                val humanId = humanService.registerHumanIfNotFound(request)

                Then("Registration returns existing user from database") {
                    humanId shouldBe 3L
                }
            }
        }
    }


}
