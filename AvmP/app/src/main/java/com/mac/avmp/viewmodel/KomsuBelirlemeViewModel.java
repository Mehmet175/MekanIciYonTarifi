package com.mac.avmp.viewmodel;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mac.avmp.model.Avm;
import com.mac.avmp.model.Dugum;
import com.mac.avmp.model.KomsuDugum;
import com.mac.avmp.model.MagazaRes;

import java.util.ArrayList;

public class KomsuBelirlemeViewModel extends ViewModel {

    private MutableLiveData<String> insertCheck = new MutableLiveData<>();

    public MutableLiveData<String> getInsertCheck() {
        return insertCheck;
    }

    public void dugumleriEkle (Avm avm) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Avmler");
        ref.child(avm.getKey()).setValue(avm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                insertCheck.setValue(ViewModelStatus.basarili);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                insertCheck.setValue(e.getMessage());
            }
        });
    }


}
