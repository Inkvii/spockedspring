package verzich.spockedspring.human

import org.springframework.stereotype.Service

@Service
class HumanService(val humanRepository: HumanRepository) {
    fun registerHuman(humanRequest: HumanController.RegisterHumanRequest): Long? {
        if (humanRequest.name.isEmpty()) {
            throw IllegalArgumentException("Bad name")
        }
        if (humanRequest.money < 0.toBigDecimal()) {
            throw IllegalArgumentException("Money cannot be negative")
        }


        val human = humanRepository.save(HumanEntity(humanRequest.name, humanRequest.money))
        humanRepository.flush()

        return human.id
    }
}
