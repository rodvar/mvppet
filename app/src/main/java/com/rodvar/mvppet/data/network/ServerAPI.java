package com.rodvar.mvppet.data.network;

import com.rodvar.mvppet.domain.Event;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by rodrigo on 24/07/16.
 */
public interface ServerAPI {

    String ENDPOINT = "http://192.168.0.6:3000";

    @GET("/events")
    Observable<List<Event>> getUpcomingEvents();

    @GET("/events/{id}")
    Observable<Event> getItem(@Path("id") int id);
}
