package verzich.spockedspring

import org.springframework.stereotype.Component
import verzich.spockedspring.pet.Cat
import verzich.spockedspring.pet.Dog
import verzich.spockedspring.pet.PetRepository
import verzich.spockedspring.petshop.PetshopEntity
import verzich.spockedspring.petshop.PetshopRepository
import java.math.BigDecimal

@Component
class DatabaseUtils(
    val petshopRepository: PetshopRepository,
    val petRepository: PetRepository
) {
    fun fillDatabase() {
        val p1 = petshopRepository.save(PetshopEntity("First petshop"))
        val p2 = petshopRepository.save(PetshopEntity("Second petshop"))
        petshopRepository.save(PetshopEntity("Third petshop"))

        petRepository.save(
            Cat(
                name = "Mourek",
                age = 0,
                purchasingPrice = BigDecimal("101.23"),
                initialMoneyConsumptionPerDay = BigDecimal("8.15"),
                petshop = p1
            )
        )
        petRepository.save(
            Cat(
                name = "Mikeš",
                age = 2,
                purchasingPrice = BigDecimal("74.68"),
                initialMoneyConsumptionPerDay = BigDecimal("10.09"),
                petshop = p1
            )
        )
        petRepository.save(
            Dog(
                name = "Vořech",
                age = 6,
                purchasingPrice = BigDecimal("45.49"),
                initialMoneyConsumptionPerDay = BigDecimal("13.09"),
                petshop = p2
            )
        )
    }

}
