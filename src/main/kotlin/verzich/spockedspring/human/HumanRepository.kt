package verzich.spockedspring.human

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface HumanRepository : JpaRepository<HumanEntity, Long> {

    fun findByName(name: String): Optional<HumanEntity>
}
