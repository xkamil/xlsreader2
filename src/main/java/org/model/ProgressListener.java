package org.model;

public interface ProgressListener {
    void onProgressStarted(double maxValue);

    void onProgress(double currentValue);

    void onProgressEnded();

    void show();

    void setLabel(String label);
}
