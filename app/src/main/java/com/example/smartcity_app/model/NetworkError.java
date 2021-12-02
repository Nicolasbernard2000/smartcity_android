package com.example.smartcity_app.model;

import com.example.smartcity_app.R;

public enum NetworkError {
    NO_CONNECTION(R.drawable.ic_launcher_background, R.string.http_no_connection),
    REQUEST_ERROR(R.drawable.ic_launcher_background, R.string.error_request),
    TECHNICAL_ERROR(R.drawable.ic_launcher_background, R.string.error_technical);

    private int errorDrawable;
    private int errorMessage;

    NetworkError(int errorDrawable, int errorMessage) {
        this.errorDrawable = errorDrawable;
        this.errorMessage = errorMessage;
    }

    public int getErrorDrawable() {
        return errorDrawable;
    }

    public int getErrorMessage() {
        return errorMessage;
    }
}
