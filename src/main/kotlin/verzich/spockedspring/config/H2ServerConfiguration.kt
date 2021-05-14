package verzich.spockedspring.config

import org.h2.tools.Server
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.SQLException

@Configuration
class H2ServerConfiguration {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${h2.tcp.port:9092}")
    private val h2TcpPort: String? = null

    // Web port, default 8082
    @Value("\${h2.web.port:8082}")
    private val h2WebPort: String? = null

    /**
     * TCP connection to connect with SQL clients to the embedded h2 database.
     *
     *
     * Connect to "jdbc:h2:tcp://localhost:9092/mem:testdb" <- change values according to configuration in application.properties
     * <pre>
     * h2.tcp.port=1001
     * spring.datasource.url=jdbc:h2:mem:testdb
     * spring.datasource.driverClassName=org.h2.Driver
     * spring.datasource.username=sa
     * spring.datasource.password=password
     * spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    </pre> *
     */
    @Bean
    @ConditionalOnExpression("\${h2.tcp.enabled:false}")
    @Throws(SQLException::class)
    fun h2TcpServer(): Server? {
        log.info("TCP server is allowed")
        val server: Server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", h2TcpPort).start()
        log.info(server.url)
        log.info(server.port.toString())
        return server
    }

    /**
     * Web console for the embedded h2 database.
     *
     *
     * Go to http://localhost:8082 and connect to the database "jdbc:h2:mem:testdb", <- change values according to configuration in application.properties
     */
    @Bean
    @ConditionalOnExpression("\${h2.web.enabled:false}")
    @Throws(SQLException::class)
    fun h2WebServer(): Server? {
        return Server.createWebServer("-web", "-webAllowOthers", "-webPort", h2WebPort).start()
    }
}
