package tools.blocks.messenger.app.testUtils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.jdbc.core.JdbcTemplate

class ClearDatabaseExtension : BeforeAllCallback , AfterAllCallback {
    private lateinit var datasource: HikariDataSource
    private lateinit var jdbcTemplate: JdbcTemplate

    override fun beforeAll(context: ExtensionContext?) {
        datasource = HikariDataSource(
            HikariConfig().apply
            {
                jdbcUrl = System.getProperty(
                    "spring.datasource.url",
                    "jdbc:postgresql://localhost:5432/postgres?currentSchema=messenger"
                )
                poolName = "messenger-test-clear-database"
                username = "postgres"
                password = "messenger"
                maximumPoolSize = 1
            }

        )
        jdbcTemplate = JdbcTemplate(datasource)

        val tablesExist = jdbcTemplate.queryForObject(
            "select exists (select 1 from information_schema.tables where table_schema = 'messenger' and table_name = 'flyway_schema_history')",
            Boolean::class.java
        )!!
        if (tablesExist)
            jdbcTemplate.update(
                """
                truncate
                  usr,
                  message
                """
            )
    }

    override fun afterAll(p0: ExtensionContext?) {
        datasource.close()
    }
}