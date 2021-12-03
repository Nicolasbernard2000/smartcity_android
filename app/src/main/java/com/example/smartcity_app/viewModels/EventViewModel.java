package com.example.smartcity_app.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.R;
import com.example.smartcity_app.mappers.EventMapper;
import com.example.smartcity_app.model.Event;
import com.example.smartcity_app.repositories.web.RetrofitConfigurationService;
import com.example.smartcity_app.repositories.web.WalloniaFixedWebService;
import com.example.smartcity_app.repositories.web.dto.EventDto;
import com.example.smartcity_app.utils.InputCheck;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventViewModel extends AndroidViewModel {
    private MutableLiveData<List<Event>> _events = new MutableLiveData<>();
    private LiveData<List<Event>> events = _events;

    private MutableLiveData<HashMap<String, String>> _inputErrors = new MutableLiveData<>();
    private LiveData<HashMap<String, String>> inputErrors = _inputErrors;

    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> statusCode = _statusCode;

    private WalloniaFixedWebService webService;
    private EventMapper eventMapper;

    public EventViewModel(@NonNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService.getInstance(application).webService();
        this.eventMapper = EventMapper.getInstance();
    }

    public void getEventsFromWebWithReportId(Integer id) {
        webService.getEventsWithReportId(id).enqueue(new Callback<List<EventDto>>() {
            @Override
            public void onResponse(Call<List<EventDto>> call, Response<List<EventDto>> response) {
                if(response.isSuccessful()) {
                    _events.setValue(eventMapper.mapToEvents(response.body()));
                    Log.i("Debug", "Récupération events réussie");
                } else {
                    Log.i("Debug", "Erreur récupération events");
                }
            }

            @Override
            public void onFailure(Call<List<EventDto>> call, Throwable t) {
                Log.i("Debug", "Erreur récupération events : " + t.getMessage());
            }
        });
    }

    public void checkData(String date, String hour, String duration, String description) {
        HashMap<String, String> errors = new HashMap<>();

        if(!InputCheck.isFutureDateValid(date))
            errors.put("date", getApplication().getResources().getString(R.string.error_future_date));

        if(!InputCheck.isInputValid(hour))
            errors.put("hour", getApplication().getResources().getString(R.string.error_hour));

        if(!InputCheck.isInputValid(duration))
            errors.put("duration", getApplication().getResources().getString(R.string.error_duration));

        if(!InputCheck.isInputValid(description))
            errors.put("description", getApplication().getResources().getString(R.string.error_description));

        _inputErrors.setValue(errors);
    }

    public void postEventOnWeb(Event event) {
        webService.postEvent(eventMapper.mapToEventDto(event)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                _statusCode.setValue(response.code());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                _statusCode.setValue(500);
            }
        });
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    public LiveData<Integer> getStatusCode() {
        return statusCode;
    }

    public LiveData<HashMap<String, String>> getInputErrors() {
        return inputErrors;
    }

}
