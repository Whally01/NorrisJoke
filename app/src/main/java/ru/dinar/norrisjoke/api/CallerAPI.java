package ru.dinar.norrisjoke.api;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.dinar.norrisjoke.model.Type;

/**
 * Created by Dr on 20-Dec-16.
 */

public interface CallerAPI {

    @GET("jokes/random")
    Call<Type> getJoke();
}
