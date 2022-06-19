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

import java.util.ArrayList;

public class AvmEklemeActivityViewModel extends ViewModel {

    private MutableLiveData<String> veriEklemeDurum = new MutableLiveData<>();

    public MutableLiveData<String> getVeriEklemeDurum() {
        return veriEklemeDurum;
    }

    public void avmEkle (Avm avm) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Avmler");
        String key = reference.push().getKey();
        avm.setKey(key);

        reference.child(key).setValue(avm);
    }


    int success = 0;
    public void resimleriEkle(ArrayList<Uri> uris, ArrayList<String> resAdlari) {
       if (uris != null) {
           for (int i = 0; i < uris.size(); i++) {
               Uri res = uris.get(i);
               String ad = resAdlari.get(i);

               StorageReference ref = FirebaseStorage.getInstance().getReference("avm_resimleri/" + ad);
               ref.putFile(res).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       success++;
                       if (success == uris.size()) {
                           veriEklemeDurum.setValue(ViewModelStatus.basarili);
                       }
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       veriEklemeDurum.setValue(e.getMessage());
                   }
               });
           }
       }
       else {
           veriEklemeDurum.setValue(ViewModelStatus.basarili);
       }
    }
}
