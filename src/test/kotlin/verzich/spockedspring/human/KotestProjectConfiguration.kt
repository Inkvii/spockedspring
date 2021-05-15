package verzich.spockedspring.human

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.extensions.spring.SpringExtension

/**
 * This class is automatically located when kotest is run and removes need for overriding extensions for each test class
 * Configuration can be used to specify many properties, one of which is parallel execution of unit tests
 * @see <a href="https://kotest.io/docs/framework/project-config.html">Documentation</a>
 */
class KotestProjectConfiguration : AbstractProjectConfig() {
    override fun extensions() = listOf(SpringExtension)
}
