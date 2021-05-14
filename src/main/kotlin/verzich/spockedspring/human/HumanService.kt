package verzich.spockedspring.human

import org.springframework.stereotype.Service
import java.util.*

@Service
class HumanService(val humanRepository: HumanRepository) {
    fun registerHumanIfNotFound(humanRequest: HumanController.RegisterHumanRequest): Long? {
        if (humanRequest.name.isEmpty()) {
            throw IllegalArgumentException("Bad name")
        }
        if (humanRequest.money < 0.toBigDecimal()) {
            throw IllegalArgumentException("Money cannot be negative")
        }

        val similarHuman: Optional<HumanEntity> = humanRepository.findByName(humanRequest.name)
        if (similarHuman.isPresent) {
            return similarHuman.get().id
        }

        val human = humanRepository.save(HumanEntity(humanRequest.name, humanRequest.money))
        humanRepository.flush()
        return human.id
    }
}
