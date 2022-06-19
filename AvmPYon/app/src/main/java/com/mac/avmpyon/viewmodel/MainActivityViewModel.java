package com.mac.avmpyon.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mac.avmpyon.model.Avm;

import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel {

    MutableLiveData<ArrayList<Avm>> arrayListMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ArrayList<Avm>> errorMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Avm>> getArrayListMutableLiveData() {
        return arrayListMutableLiveData;
    }

    public MutableLiveData<ArrayList<Avm>> getErrorMutableLiveData() {
        return errorMutableLiveData;
    }

    public void avmleriGetir() {
        ArrayList<Avm> avms = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Avmler");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                avms.clear();
                for (DataSnapshot ref : snapshot.getChildren()) {
                    Avm avm = ref.getValue(Avm.class);
                    avms.add(avm);
                }
                arrayListMutableLiveData.setValue(avms);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
