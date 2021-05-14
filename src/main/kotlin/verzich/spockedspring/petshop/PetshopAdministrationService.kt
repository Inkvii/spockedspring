package verzich.spockedspring.petshop

import org.springframework.stereotype.Service
import verzich.spockedspring.human.HumanEntity
import verzich.spockedspring.pet.Pet
import java.math.BigDecimal

@Service
class PetshopAdministrationService {

    fun checkHumanCanAffordBuyingPet(human: HumanEntity, pet: Pet): Boolean {
        return calculateHumanMoneyAfterBuyingPet(human, pet) > 0.toBigDecimal()
    }

    fun checkHumanCanAffordFeedingAllPets(human: HumanEntity, futurePet: Pet): Boolean {
        var priceToFeedAllPetsPerDay = human.pets.map { it.calculateMoneyConsumptionPerDay() }.sumOf { it }
        priceToFeedAllPetsPerDay += futurePet.calculateMoneyConsumptionPerDay()

        return calculateHumanMoneyAfterBuyingPet(human, futurePet) > priceToFeedAllPetsPerDay
    }

    fun transferOwnershipToHuman(human: HumanEntity, pet: Pet) {
        human.money -= pet.purchasingPrice
        pet.petshop.accumulatedWealth += pet.purchasingPrice
        pet.owner = human
        human.pets.add(pet)
    }


    private fun calculateHumanMoneyAfterBuyingPet(human: HumanEntity, pet: Pet): BigDecimal {
        return human.money - pet.purchasingPrice
    }
}
