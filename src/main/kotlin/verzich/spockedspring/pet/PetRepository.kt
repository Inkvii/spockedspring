package verzich.spockedspring.pet

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface PetRepository : JpaRepository<Pet, Long> {
    @Query("select pet from Pet pet where pet.owner is null and pet.petshop.id = ?1")
    fun findAvailablePets(petShopId: Long): List<Pet>

    fun findByIdAndOwnerIsNull(petId: Long): Optional<Pet>
}
