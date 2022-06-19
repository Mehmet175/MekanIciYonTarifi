package com.mac.avmpyon.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mac.avmpyon.R;
import com.mac.avmpyon.databinding.ActivityFotografGostermeBinding;

import java.io.File;
import java.io.IOException;

public class FotografGostermeActivity extends AppCompatActivity {

    private ActivityFotografGostermeBinding binding;
    private String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fotograf_gosterme);

        imageName = getIntent().getStringExtra("name");

        binding.imageViewBack.setOnClickListener(view -> onBackPressed());

        resimGetir();
    }

    private void resimGetir() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference islandRef = storage.getReference().child("magaza_resimleri/" + imageName);
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        binding.imageViewMagaza.setImageResource(R.drawable.ic_baseline_image_24);
        File finalLocalFile = localFile;
        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                binding.imageViewMagaza.setImageURI(Uri.fromFile(finalLocalFile));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                binding.imageViewMagaza.setImageResource(R.drawable.ic_baseline_image_24);
            }
        });
    }
}