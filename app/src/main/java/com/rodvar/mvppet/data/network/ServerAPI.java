package com.rodvar.mvppet.data.network;

import android.text.Html;

import com.google.gson.annotations.SerializedName;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by rodrigo on 24/07/16.
 */
public interface ServerAPI {

    String ENDPOINT = "http://api.icndb.com";

    class Item {

        public int id;

        @SerializedName("joke")
        public String text;

        @Override
        public String toString() {
            return Html.fromHtml(text).toString();
        }
    }

    class Response {
        @SerializedName("value")
        public Item[] items;
    }

    class ItemResponse {
        @SerializedName("value")
        public Item item;
    }

    @GET("/jokes/random/20")
    Observable<Response> getUpcomingEvents(@Query("firstName") String firstName
            , @Query("lastName") String lastName, @Header("pageNumber") int pageNumberIgnored);

    @GET("/jokes/{id}")
    Observable<ItemResponse> getItem(@Query("firstName") String firstName
            , @Query("lastName") String lastName, @Path("id") int id);
}
