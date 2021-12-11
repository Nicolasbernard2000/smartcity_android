package com.example.smartcity_app.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.model.Event;
import com.example.smartcity_app.model.NetworkError;
import com.example.smartcity_app.model.Participation;
import com.example.smartcity_app.repository.web.RetrofitConfigurationService;
import com.example.smartcity_app.repository.web.WalloniaFixedWebService;
import com.example.smartcity_app.repository.web.dto.ParticipationDto;
import com.example.smartcity_app.service.mappers.ParticipationMapper;
import com.example.smartcity_app.util.errors.NoConnectivityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParticipationViewModel extends AndroidViewModel {
    private MutableLiveData<Participation> _participation = new MutableLiveData<>();
    private LiveData<Participation> participation = _participation;

    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> statusCode = _statusCode;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private WalloniaFixedWebService webService;
    private ParticipationMapper participationMapper;

    public ParticipationViewModel(@NonNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService.getInstance(application).webService();
        this.participationMapper = ParticipationMapper.getInstance();
    }

    public void getParticipationFromWeb(Integer userID, Integer eventID) {
        webService.getParticipationForUserAndEvent(userID, eventID).enqueue(new Callback<ParticipationDto>() {
            @Override
            public void onResponse(Call<ParticipationDto> call, Response<ParticipationDto> response) {
                if(response.isSuccessful()) {
                    _participation.setValue(participationMapper.mapToParticipation(response.body()));
                } else {
                    _participation.setValue(new Participation(null, null));
                }
            }

            @Override
            public void onFailure(Call<ParticipationDto> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void postParticipationOnWeb(Participation participation) {
        webService.postParticipation(participationMapper.mapToParticipationDto(participation)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                _statusCode.setValue(response.code());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void deleteParticipationOnWeb(Participation participation) {
        webService.deleteParticipation(participationMapper.mapToParticipationDto(participation)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                _statusCode.setValue(response.code());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public LiveData<Integer> getStatusCode() {return statusCode;}
    public LiveData<Participation> getParticipation() {
        return participation;
    }
    public LiveData<NetworkError> getError() {
        return error;
    }
}
