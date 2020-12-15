package tools.blocks.messenger.app.util

import tools.blocks.messenger.app.domain.user.User
import java.sql.ResultSet
import java.time.LocalTime
import java.util.*

fun ResultSet.getNullableLong(columnName: String): Long? =
    getLong(columnName).takeIf { !wasNull() }

fun ResultSet.getNullableInt(columnName: String): Int? =
    getInt(columnName).takeIf { !wasNull() }

fun ResultSet.getNullableBoolean(columnName: String): Boolean? =
    getBoolean(columnName).takeIf { !wasNull() }

fun ResultSet.getNullableString(columnName: String): String? =
    getString(columnName).takeIf { !wasNull() }

fun ResultSet.getNullableStringList(columnName: String): List<String>? =
    (getArray(columnName)?.array as Array<*>?)
        ?.map { it.toString() }

fun ResultSet.getNullableLocalTime(columnName: String): LocalTime? =
    getTime(columnName)?.toLocalTime()

fun ResultSet.getId(): UUID =
    getObject("id", UUID::class.java)

fun ResultSet.getUser(columnName: String): User =
    User(userName = getString(columnName))