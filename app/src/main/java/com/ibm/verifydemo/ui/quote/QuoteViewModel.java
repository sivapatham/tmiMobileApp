package com.ibm.verifydemo.ui.quote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QuoteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public QuoteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is quote fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}