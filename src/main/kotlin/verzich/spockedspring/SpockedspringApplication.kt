package verzich.spockedspring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener

@SpringBootApplication
class SpockedspringApplication(
    val databaseUtils: DatabaseUtils
) {
    @EventListener(ApplicationReadyEvent::class)
    fun fireEventAfterStart() {
        databaseUtils.fillDatabase()
    }
}

fun main(args: Array<String>) {
    runApplication<SpockedspringApplication>(*args)
}
