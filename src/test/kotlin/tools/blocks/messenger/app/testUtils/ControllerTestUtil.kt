package tools.blocks.messenger.app.testUtils

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

inline fun <reified T> ResultActions.andExpectJsonContent(
    expected: T,
    converter: MappingJackson2HttpMessageConverter
): ResultActions =
    this.andExpect(status().isOk)
        .andDo {
            val actual = converter.objectMapper.readValue(it.response.contentAsString, T::class.java)
            assertThat(actual).isEqualTo(expected)
        }