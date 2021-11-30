package com.example.smartcity_app.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.mappers.EventMapper;
import com.example.smartcity_app.model.Event;
import com.example.smartcity_app.repositories.web.RetrofitConfigurationService;
import com.example.smartcity_app.repositories.web.WalloniaFixedWebService;
import com.example.smartcity_app.repositories.web.dto.EventDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventViewModel extends AndroidViewModel {
    private MutableLiveData<List<Event>> _events = new MutableLiveData<>();
    private LiveData<List<Event>> events = _events;

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

    public void postEventOnWeb(Event event) {
        Log.i("Debug", "Event récupéré : " + event);
        webService.postEvent(eventMapper.mapToEventDto(event)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    Log.i("Debug", "Event créé");
                } else {
                    Log.i("Debug", "Erreur création event");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("Debug", "Event : on failure " + t.getMessage());
            }
        });
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }

}
