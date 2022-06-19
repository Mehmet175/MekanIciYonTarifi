package com.mac.avmp.viewmodel;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mac.avmp.model.MagazaRes;

import java.util.ArrayList;

public class AvmMagazaEklemeActivityViewModel extends ViewModel {
    private MutableLiveData<String> insertCheck = new MutableLiveData<>();

    public MutableLiveData<String> getInsertCheck() {
        return insertCheck;
    }

    public void resimleriEkle(ArrayList<MagazaRes> resler) {
        final int[] eklemeSayisi = {0};
        for (int i = 0; i < resler.size(); i++) {
            Uri res = resler.get(i).getRes();
            String ad = resler.get(i).getName();
            StorageReference ref = FirebaseStorage.getInstance().getReference("magaza_resimleri/" + ad);
            ref.putFile(res).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    eklemeSayisi[0] += 1;
                    if (eklemeSayisi[0] == resler.size()) {
                        insertCheck.setValue(ViewModelStatus.basarili);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    insertCheck.setValue(e.getMessage());
                }
            });
        }
    }


}
