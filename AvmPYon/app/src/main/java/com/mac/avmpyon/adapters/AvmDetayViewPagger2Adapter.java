package com.mac.avmpyon.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.mac.avmpyon.R;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;

public class AvmDetayViewPagger2Adapter extends RecyclerView.Adapter<AvmDetayViewPagger2Adapter.AvmDetayViewPagger2AdapterViewHolder> {
    private Context mContext;
    private List<String> dosyaAdlari;

    public AvmDetayViewPagger2Adapter(Context mContext, List<String> dosyaAdlari) {
        this.mContext = mContext;
        this.dosyaAdlari = dosyaAdlari;
    }

    @NonNull
    @Override
    public AvmDetayViewPagger2AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AvmDetayViewPagger2AdapterViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_layout, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AvmDetayViewPagger2AdapterViewHolder holder, int position) {
        holder.onBind(dosyaAdlari.get(position));
    }

    @Override
    public int getItemCount() {
        if (dosyaAdlari == null) {
            return 0;
        }
        return dosyaAdlari.size();
    }

    public class AvmDetayViewPagger2AdapterViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageViewAvmRes;
        private ProgressBar progressBarAvmRes;

        public AvmDetayViewPagger2AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBarAvmRes = itemView.findViewById(R.id.progressBarAvmRes);
            imageViewAvmRes = itemView.findViewById(R.id.imageViewAvmRes);
        }

        public void onBind(String res) {
            try {
                resimIndir(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void resimIndir (String resAd) throws IOException {
            StorageReference ref = FirebaseStorage.getInstance().getReference("avm_resimleri/" + resAd);
            File localFile = File.createTempFile("images", "jpg");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    imageViewAvmRes.setImageURI(Uri.fromFile(localFile));
                    progressBarAvmRes.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    imageViewAvmRes.setImageResource(R.drawable.ic_sharp_clear_24);
                    progressBarAvmRes.setVisibility(View.GONE);
                }
            });

        }
    }

}
