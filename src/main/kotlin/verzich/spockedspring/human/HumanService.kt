package verzich.spockedspring.human

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class HumanService(val humanRepository: HumanRepository) {

    val log = LoggerFactory.getLogger(this.javaClass)

    @Transactional
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
        log.debug("Registered new human with id ${human.id}")
        return human.id
    }

    @Transactional
    fun doPetSounds(humanId: Long): List<String> {
        val human = humanRepository.findById(humanId).orElseThrow { IllegalArgumentException("Human not found") }
        return human.pets.map { "Pet: ${it.name} sounds like ${it.doSound()}" }.toList()
    }
}
