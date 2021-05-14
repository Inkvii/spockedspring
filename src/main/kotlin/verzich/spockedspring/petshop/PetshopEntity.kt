package verzich.spockedspring.petshop

import com.fasterxml.jackson.annotation.JsonManagedReference
import verzich.spockedspring.AbstractEntity
import verzich.spockedspring.pet.Pet
import java.math.BigDecimal
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Entity
class PetshopEntity(
    val name: String,
    @OneToMany(mappedBy = "petshop", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val petsHistory: MutableList<Pet> = mutableListOf(),
    var accumulatedWealth: BigDecimal = 0.toBigDecimal()
) : AbstractEntity()
