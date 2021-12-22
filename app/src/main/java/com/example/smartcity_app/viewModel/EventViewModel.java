package com.example.smartcity_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.R;
import com.example.smartcity_app.service.mappers.EventMapper;
import com.example.smartcity_app.model.Event;
import com.example.smartcity_app.model.NetworkError;
import com.example.smartcity_app.repository.web.RetrofitConfigurationService;
import com.example.smartcity_app.repository.web.WalloniaFixedWebService;
import com.example.smartcity_app.repository.web.dto.EventDto;
import com.example.smartcity_app.util.InputCheck;
import com.example.smartcity_app.util.errors.NoConnectivityException;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventViewModel extends AndroidViewModel {
    private MutableLiveData<List<Event>> _events = new MutableLiveData<>();
    private LiveData<List<Event>> events = _events;

    private MutableLiveData<Integer> _statusCodeCreation = new MutableLiveData<>();
    private LiveData<Integer> statusCodeCreation = _statusCodeCreation;

    private MutableLiveData<Integer> _statusCodeDelete = new MutableLiveData<>();
    private LiveData<Integer> statusCodeDelete = _statusCodeDelete;

    private MutableLiveData<Integer> _statusCodePatch = new MutableLiveData<>();
    private LiveData<Integer> statusCodePatch = _statusCodePatch;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private MutableLiveData<HashMap<String, String>> _inputErrors = new MutableLiveData<>();
    private LiveData<HashMap<String, String>> inputErrors = _inputErrors;

    private WalloniaFixedWebService webService;
    private EventMapper eventMapper;

    public EventViewModel(@NonNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService.getInstance(application).webService();
        this.eventMapper = EventMapper.getInstance();
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

    public void getEventsFromWebWithReportId(Integer id) {
        webService.getEventsWithReportId(id).enqueue(new Callback<List<EventDto>>() {
            @Override
            public void onResponse(Call<List<EventDto>> call, Response<List<EventDto>> response) {
                if(response.isSuccessful()) {
                    _events.setValue(eventMapper.mapToEvents(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<EventDto>> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void postEventOnWeb(Event event) {
        webService.postEvent(eventMapper.mapToEventDto(event)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                _statusCodeCreation.setValue(response.code());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void deleteEventOnWeb(Event event) {
        webService.deleteEvent(eventMapper.mapToEventDto(event)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                _statusCodeDelete.setValue(response.code());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void modifyEventOnWeb(Event event) {
        webService.modifyEvent(eventMapper.mapToEventDto(event)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                _statusCodePatch.setValue(response.code());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }
    public LiveData<Integer> getStatusCodeCreation() {
        return _statusCodeCreation;
    }
    public LiveData<Integer> getStatusCodeDelete() {return statusCodeDelete;}
    public LiveData<Integer> getStatusCodePatch() {
        return statusCodePatch;
    }
    public LiveData<HashMap<String, String>> getInputErrors() {
        return inputErrors;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }
}
