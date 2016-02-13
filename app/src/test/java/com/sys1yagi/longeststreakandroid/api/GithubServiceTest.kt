package com.sys1yagi.longeststreakandroid.api

import com.google.gson.Gson
import com.sys1yagi.longeststreakandroid.BuildConfig
import com.sys1yagi.longeststreakandroid.LongestStreakApplication
import com.sys1yagi.longeststreakandroid.model.Event
import okhttp3.Headers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config
import rx.observers.TestSubscriber
import org.assertj.core.api.Assertions.assertThat

import org.mockito.Mockito.*;
import org.junit.Assert.*;
import org.hamcrest.CoreMatchers.*;
import org.junit.Rule
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import retrofit2.Response
import rx.Observable
import java.util.*

@RunWith(RobolectricGradleTestRunner::class)
@Config(application = LongestStreakApplication::class, constants = BuildConfig::class, sdk = intArrayOf(21))
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*", "com.jakewharton.threetenabp.*", "org.threeten.bp.*")
@PrepareForTest(Response::class)
class GithubServiceTest {

    @Rule @JvmField
    val rule = PowerMockRule()

    val gson = Gson()

    fun createEvent(id: Long): Event {
        val event = gson.fromJson("{id:${id}}", Event::class.java)
        return event
    }

    @Test
    fun userAllEvents() {
        val github = PowerMockito.mock(Github::class.java)
        `when`(github.userEvents(anyString(), eq(1))).thenReturn(Observable.create {
            val response = mock(Response::class.java) as Response<List<Event>>
            `when`(response.headers()).thenReturn(Headers.of(
                    "Link",
                    "<https://api.github.com/user/749051/events/public?page=2>; rel=\"next\", <https://api.github.com/user/749051/events/public?page=2>; rel=\"last\""
            ))
            `when`(response.body()).thenReturn((1L..3L).map { createEvent(it) })
            it.onNext(response)
            it.onCompleted()
        })
        `when`(github.userEvents(anyString(), eq(2))).thenReturn(Observable.create {
            val response = mock(Response::class.java) as Response<List<Event>>
            `when`(response.headers()).thenReturn(Headers.of(
                    "Link",
                    "<https://api.github.com/user/749051/events/public?page=1>; rel=\"first\", <https://api.github.com/user/749051/events/public?page=1>; rel=\"prev\""
            ))
            `when`(response.body()).thenReturn((4L..6L).map { createEvent(it) })
            it.onNext(response)
            it.onCompleted()
        })

        val service = GithubService(github)

        val testSubscriber = TestSubscriber<List<Event>>()
        service.userAllEvents("sys1yagi").subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNoErrors()
        val nextEvents = testSubscriber.onNextEvents
        assertThat(nextEvents)
                .isNotNull()
                .isNotEmpty()
        assertThat(nextEvents.size)
                .isEqualTo(2)
        assertThat(nextEvents[0].size)
                .isEqualTo(3)
        assertThat(nextEvents[1].size)
                .isEqualTo(3)
    }
}
