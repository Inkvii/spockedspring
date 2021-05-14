package verzich.spockedspring.human

import org.springframework.data.jpa.repository.JpaRepository

interface HumanRepository : JpaRepository<HumanEntity, Long> {
}
