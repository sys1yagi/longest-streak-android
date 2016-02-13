package com.sys1yagi.longeststreakandroid.api

import com.sys1yagi.longeststreakandroid.model.Event
import retrofit2.Response
import rx.Observable
import rx.Subscriber
import javax.inject.Inject

class GithubService
@Inject
constructor(private var client: Github) {
    fun userEvents(userName: String, page: Int = 1): Observable<Response<List<Event>>> {
        return client.userEvents(userName, page)
    }

    fun userAllEvents(userName: String): Observable<List<Event>> {
        return Observable.create<List<Event>> {
            scanAllEventPage(userName, 1, it)
        }
    }

    private fun scanAllEventPage(userName: String, page: Int, subscriber: Subscriber<in List<Event>>) {
        userEvents(userName, page)
                .subscribe(
                        {
                            subscriber.onNext(it.body())
                            val links = it.headers().get("Link")
                            nextOrComplete(userName, links, subscriber)
                            subscriber.onCompleted()
                        },
                        {
                            subscriber.onError(it)
                        })
    }

    private fun nextOrComplete(userName:String, links:String?, subscriber: Subscriber<in List<Event>>){
        if(links == null){
            subscriber.onCompleted()
            return
        }
        links.split(",").forEach {
            if(it.indexOf("rel=\"next\"") > 0 ){
                Regex("page=([0-9]+)").findAll(it)
                .forEach {
                    val page = it.groups[1]!!.value.toInt()
                    scanAllEventPage(userName, page, subscriber)
                    return
                }
            }
        }
        subscriber.onCompleted()
    }
}
