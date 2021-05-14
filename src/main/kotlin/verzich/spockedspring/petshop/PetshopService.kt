package verzich.spockedspring.petshop

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import verzich.spockedspring.exceptions.InsufficientFundsException
import verzich.spockedspring.human.HumanRepository
import verzich.spockedspring.pet.Cat
import verzich.spockedspring.pet.Dog
import verzich.spockedspring.pet.Pet
import verzich.spockedspring.pet.PetRepository
import javax.transaction.Transactional

@Service
class PetshopService(
    val petshopRepository: PetshopRepository,
    val petRepository: PetRepository,
    val humanRepository: HumanRepository,
    val petshopAdministrationService: PetshopAdministrationService
) {
    val log = LoggerFactory.getLogger(this.javaClass)


    fun browseAvailablePets(petshopId: Long): List<Pet> {
        if (petshopId < 1) {
            throw IllegalArgumentException("Petshop id must be positive number")
        }
        if (!petshopRepository.existsById(petshopId)) {
            throw IllegalArgumentException("Petshop does not exist")
        }

        return petRepository.findAvailablePets(petshopId)
    }

    @Transactional
    fun buyPet(buyPetRequest: PetshopController.BuyPetRequest): PetshopController.BuyPetResponse {
        val pet = petRepository.findById(buyPetRequest.chosenPetId).orElseThrow { IllegalArgumentException("Bad pet id") }
        val human = humanRepository.findById(buyPetRequest.humanId).orElseThrow { IllegalArgumentException("Bad human id") }

        if (!petshopAdministrationService.checkHumanCanAffordBuyingPet(human, pet)) {
            throw InsufficientFundsException("Human cannot afford to buy this pet")
        }
        if (!petshopAdministrationService.checkHumanCanAffordFeedingAllPets(human, pet)) {
            throw InsufficientFundsException("Human cannot afford feeding all pets")
        }

        petshopAdministrationService.transferOwnershipToHuman(human, pet)

        return PetshopController.BuyPetResponse(human.id!!, human.pets.size, pet.purchasingPrice)
    }

    @Transactional
    fun petFactory(request: PetshopController.RegisterNewPetRequest, petshopId: Long): Pet {

        val petshop = petshopRepository.findById(petshopId).orElseThrow { IllegalArgumentException("Bad petshop id") }
        val pet: Pet
        when (request.petTypeEnum) {
            PetshopController.PetTypeEnum.DOG -> pet = Dog(
                request.pet.name,
                request.pet.age,
                request.pet.purchasingPrice,
                request.pet.initialMoneyConsumptionPerDay,
                petshop = petshop
            )

            PetshopController.PetTypeEnum.CAT -> pet = Cat(
                request.pet.name,
                request.pet.age,
                request.pet.purchasingPrice,
                request.pet.initialMoneyConsumptionPerDay,
                petshop = petshop
            )
        }
        log.info("Request transformed into entity: $pet")
        petRepository.save(pet)
        petRepository.flush()
        return pet
    }
}
