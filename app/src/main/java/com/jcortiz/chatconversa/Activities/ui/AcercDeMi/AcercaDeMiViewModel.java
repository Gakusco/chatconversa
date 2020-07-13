package com.jcortiz.chatconversa.Activities.ui.AcercDeMi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AcercaDeMiViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AcercaDeMiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}