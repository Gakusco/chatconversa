package com.jcortiz.chatconversa.Activities.ui.tomarFoto;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TomarFotoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TomarFotoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}