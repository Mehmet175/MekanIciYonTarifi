package com.mac.avmpyon.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mac.avmpyon.R;
import com.mac.avmpyon.databinding.YolTarifiRvCardBinding;
import com.mac.avmpyon.model.Dugum;
import com.mac.avmpyon.view.FotografGostermeActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MagazalarRvAdapter extends RecyclerView.Adapter<MagazalarRvAdapter.MagazlarTasarim> {

    private Context mContext;
    private List<Dugum> magazalar;
    private Uri[] resler;

    public MagazalarRvAdapter(Context mContext, List<Dugum> magazalar) {
        this.mContext = mContext;
        this.magazalar = magazalar;
        resler = new Uri[magazalar.size()];
    }

    @NonNull
    @Override
    public MagazlarTasarim onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MagazlarTasarim(LayoutInflater.from(mContext).inflate(R.layout.yol_tarifi_rv_card, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MagazlarTasarim holder, int position) {
        holder.onBind(magazalar.get(position), position);
    }

    @Override
    public int getItemCount() {
        return magazalar.size();
    }

    public void notiftDataSet__() {
        resler = new Uri[magazalar.size()];
        this.notifyDataSetChanged();
    }

    public class MagazlarTasarim extends RecyclerView.ViewHolder {
        private YolTarifiRvCardBinding binding;

        public MagazlarTasarim(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void onBind(Dugum dugum, int position) {
            binding.setTarifString(dugum.getAd());
            resYukle(binding.imageView4, dugum.getPhoto(), position);
        }

        private void resYukle(ImageView imageView, String photoName, int position) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference islandRef = storage.getReference().child("magaza_resimleri/" + photoName);

            File localFile = null;
            try {
                localFile = File.createTempFile("images", "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageResource(R.drawable.ic_baseline_image_24);

            if (resler[position] != null){
                imageView.setImageURI(resler[position]);
            } else {
                File finalLocalFile = localFile;
                islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        imageView.setImageURI(Uri.fromFile(finalLocalFile));
                        try {
                            resler[position] = Uri.fromFile(finalLocalFile);
                        } catch (Exception e) {

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        imageView.setImageResource(R.drawable.ic_baseline_image_24);
                    }
                });

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, FotografGostermeActivity.class);
                        intent.putExtra("name", photoName);
                        mContext.startActivity(intent);
                    }
                });
            }
        }
    }

}
