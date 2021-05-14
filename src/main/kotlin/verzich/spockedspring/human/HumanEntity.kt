package verzich.spockedspring.human

import verzich.spockedspring.AbstractEntity
import verzich.spockedspring.pet.Pet
import java.math.BigDecimal
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Entity
class HumanEntity(
    val name: String,
    var money: BigDecimal,
    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val pets: MutableList<Pet> = mutableListOf()
) : AbstractEntity()
