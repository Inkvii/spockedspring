package verzich.spockedspring.petshop

import org.springframework.data.jpa.repository.JpaRepository

interface PetshopRepository : JpaRepository<PetshopEntity, Long> {

}
