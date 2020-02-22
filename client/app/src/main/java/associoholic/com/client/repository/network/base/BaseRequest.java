package associoholic.com.client.repository.network.base;

import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import associoholic.com.client.BuildConfig;
import associoholic.com.client.common.OkHttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

public abstract class BaseRequest<T> extends LiveData<Resource<T>> implements Callback {
    protected static final String
            BASE_URL = BuildConfig.BASE_URL,
            CREATE_GAME = "/create_game",
            JOIN_GAME="/join_game";
    protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    protected static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");
    protected static final MediaType MEDIA_TYPE_WEBP = MediaType.parse("image/webp");
    public static final Gson gson = new GsonBuilder().create();

    protected Request.Builder request;

    public Response response;

    @Override
    public final void onFailure(@NonNull Call call, @NonNull IOException e) {
        e.printStackTrace();
        onFailure(null, e.getMessage(), e);
    }

    @Override
    public final void onResponse(@NonNull Call call, @NonNull Response response) {
        if (!response.isSuccessful()) {
            IOException ioException = new IOException("Unexpected code " + response);
            //if (shouldLog(response)) Crashlytics.logException(ioException);
            ioException.printStackTrace();
            onFailure(response, response.message(), ioException);
            response.body().close();
            return;
        }

        try {
            onSuccess(response);
        } catch (Exception e) {
            //Crashlytics.logException(e);
            e.printStackTrace();
            onFailure(null, e.getMessage(), e);
        } finally {
            response.body().close();
        }
    }

    public LiveData<Resource<T>> runAsync(boolean force) {
        if (getValue() == null || (getValue().getStatus() != Status.LOADING && (getValue().getStatus() == Status.ERROR || force))) {
            setData(Resource.loading(getData(), this));
            OkHttp.Companion.getInstance().newCall(buildRequest()).enqueue(this);
        }
        return this;
    }

    public final Resource<T> run() {
        try {
            response = OkHttp.Companion.getInstance().newCall(buildRequest()).execute();
            if (!response.isSuccessful()) {
                IOException ioException = new IOException("Unexpected code " + response);
                //if (shouldLog(response)) Crashlytics.logException(ioException);
                ioException.printStackTrace();
                try {
                    return onFailure(response, response.message(), ioException);
                } finally {
                    response.body().close();
                }
            }

            try {
                return onSuccess(response);
            } catch (Exception e) {
                //Crashlytics.logException(e);
                e.printStackTrace();
                return onFailure(null, e.getMessage(), e);
            } finally {
                response.body().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return onFailure(null, e.getMessage(), e);
        }
    }

    protected JsonElement parseToJson(Response response) throws IOException, JsonIOException {
        JsonReader jsonReader = gson.newJsonReader(response.body().charStream());
        JsonElement json = new JsonParser().parse(jsonReader);
        if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
            throw new JsonIOException("JSON document was not fully consumed.");
        }
        return json;
    }

    @Nullable
    public T getData() {
        return getValue() == null ? null : getValue().getData();
    }

    public void setData(@Nullable Resource<T> data) {
        if (Looper.getMainLooper() == Looper.myLooper())
            setValue(data);
        else
            postValue(data);
    }

    private static final List<Integer> skipLogCodes = Arrays.asList(401, 404, 422);

    private boolean shouldLog(Response response) {
        return !skipLogCodes.contains(response.code());
    }

    protected Request buildRequest() {
        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type", "application/type");
        return request.build();
    }

    protected Resource<T> onFailure(Response response, String message, Exception e) {
        if (response != null) {
            try {
                message = parseToJson(response).getAsJsonObject().get("error").getAsString();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        final Resource<T> r = Resource.error(getData(), e, message, this);
        postValue(r);
        return r;
    }

    protected abstract Resource<T> onSuccess(@NonNull Response response) throws Exception;

}

