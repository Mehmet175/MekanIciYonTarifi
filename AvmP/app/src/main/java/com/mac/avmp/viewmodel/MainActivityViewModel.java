package com.mac.avmp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;
import com.mac.avmp.model.Avm;

import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Avm>> avmsMutableData = new MutableLiveData<>();
    private MutableLiveData<String> hataMutableData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Avm>> getAvmsMutableData() {
        return avmsMutableData;
    }

    public MutableLiveData<String> getHataMutableData() {
        return hataMutableData;
    }

    public void getAvm () {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Avmler");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Avm> avms = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Avm avm = ds.getValue(Avm.class);
                    avms.add(avm);
                }
                avmsMutableData.setValue(avms);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hataMutableData.setValue(error.getMessage());
            }
        });
    }

    public void silAvm(String avmKey) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Avmler");
        ref.child(avmKey).removeValue();
    }
}
