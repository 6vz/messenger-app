package tools.blocks.messenger.app.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*
import javax.sql.DataSource

typealias GenerateId = () -> UUID

@Configuration
class AppConfig {
    @Bean
    fun generateId(): GenerateId = UUID::randomUUID

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    fun dataSourceProperties() = DataSourceProperties()

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    fun dataSource(): DataSource =
        dataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource::class.java)
            .build()

    @Bean
    @Primary
    fun jdbcTemplate() =
        JdbcTemplate(dataSource()).apply { fetchSize = 1_000 }

    @Bean
    @Primary
    fun namedJdbcTemplate() =
        NamedParameterJdbcTemplate(jdbcTemplate())
}