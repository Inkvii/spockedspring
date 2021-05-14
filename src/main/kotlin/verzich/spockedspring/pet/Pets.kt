package verzich.spockedspring.pet

import verzich.spockedspring.AbstractEntity
import verzich.spockedspring.human.HumanEntity
import verzich.spockedspring.petshop.PetshopEntity
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.ManyToOne

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Pet(
    protected val name: String,
    protected var age: Int = 0,
    protected var initialMoneyConsumptionPerDay: BigDecimal,
    @ManyToOne
    protected var owner: HumanEntity? = null,
    @ManyToOne
    protected var petshop: PetshopEntity

) : AbstractEntity() {
    abstract fun doSound(): String

    fun calculateMoneyConsumptionPerDay(): BigDecimal {
        return (initialMoneyConsumptionPerDay * (age / 10 + 1).toBigDecimal()).setScale(2)
    }
}


class Cat(name: String, age: Int, initialMoneyConsumptionPerDay: BigDecimal, owner: HumanEntity?, petshop: PetshopEntity) :
    Pet(name, age, initialMoneyConsumptionPerDay, owner, petshop) {

    override fun doSound(): String {
        return "Meow"
    }
}

class Dog(name: String, age: Int, initialMoneyConsumptionPerDay: BigDecimal, owner: HumanEntity?, petshop: PetshopEntity) :
    Pet(name, age, initialMoneyConsumptionPerDay, owner, petshop) {
    override fun doSound(): String {
        when {
            age < 0 -> {
                throw Error("Bad age")
            }
            age in 0..1 -> {
                return "Yip"
            }
            age in 2..4 -> {
                return "Nuff"
            }
            age in 5..8 -> {
                return "Bork"
            }
            else -> return "Wuff"
        }
    }
}

