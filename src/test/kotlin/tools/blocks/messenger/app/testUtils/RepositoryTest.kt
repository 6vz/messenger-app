package tools.blocks.messenger.app.testUtils

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.FilterType.ANNOTATION
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED
import org.springframework.transaction.annotation.Transactional
import tools.blocks.messenger.app.config.AppConfig
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FILE

@Retention(RUNTIME)
@Target(CLASS, FILE)
@Import(AppConfig::class)
@JdbcTest(includeFilters = [Filter(type = ANNOTATION, value = [Repository::class])])
@Transactional(propagation = NOT_SUPPORTED)
@ExtendWith(ClearDatabaseExtension::class)
@AutoConfigureTestDatabase(replace = NONE)
annotation class RepositoryTest
