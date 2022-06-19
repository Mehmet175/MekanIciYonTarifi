package com.mac.avmpyon.adapters;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
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
import com.mac.avmpyon.model.KomsuDugum;
import com.mac.avmpyon.view.FotografGostermeActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YolTarifiRvAdapter extends RecyclerView.Adapter<YolTarifiRvAdapter.YolTarifiRvAdapterViewHolde> {

    private Context mContext;
    private List<Dugum> yolTarifiDugum;
    private ImageView imageViewYonGostergesi;
    private ArrayList<Integer> kerterizler;
    private ArrayList<Integer> uzakliklar;
    private int gosterilecekKerteriz = 0;

    public YolTarifiRvAdapter(Context mContext, List<Dugum> yolTarifiDugum, ImageView imageViewYonGostergesi) {
        this.mContext = mContext;
        this.yolTarifiDugum = yolTarifiDugum;
        this.imageViewYonGostergesi = imageViewYonGostergesi;
        kerterizler = new ArrayList<>();
        uzakliklar = new ArrayList<>();
    }

    public List<Dugum> getYolTarifiDugum() {
        return yolTarifiDugum;
    }

    @NonNull
    @Override
    public YolTarifiRvAdapterViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new YolTarifiRvAdapterViewHolde(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.yol_tarifi_rv_card, null, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull YolTarifiRvAdapterViewHolde holder, int position) {
        holder.onBind(yolTarifiDugum.get(position), position);
    }


    @Override
    public int getItemCount() {
        return yolTarifiDugum == null ? 0 : yolTarifiDugum.size();
    }

    public class YolTarifiRvAdapterViewHolde extends RecyclerView.ViewHolder {

        private YolTarifiRvCardBinding binding;

        public YolTarifiRvAdapterViewHolde(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void onBind(Dugum dugum, int position) {
            String yolTarifiMetni = yolTarifiYap(dugum);
            binding.setTarifString(yolTarifiMetni);
            if (position < yolTarifiDugum.size() - 1) {
                int kerteriz = 0;
                Dugum sonrakiDugum = yolTarifiDugum.get(position + 1);
                for (KomsuDugum kd : dugum.getKomsuDugums()) {
                    if (kd.getKomsuDugumAd().equals(sonrakiDugum.getAd())) {
                        kerteriz = kd.getKerteriz();
                    }
                }
                kerterizler.add(kerteriz);
            }

            if (position == 0) {
                gosterilecekKerteriz = kerterizler.get(0);
                imageViewGuncelle();
                binding.getRoot().setVisibility(View.GONE);
            }
            resYukle(binding.imageView4, dugum.getPhoto());
        }

        private String yolTarifiYap(Dugum dugum) {
            String metin = dugum.getAd() + " önüne git.";
            return metin;
        }

        private void resYukle(ImageView imageView, String photoName) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference islandRef = storage.getReference().child("magaza_resimleri/" + photoName);

            File localFile = null;
            try {
                localFile = File.createTempFile("images", "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageResource(R.drawable.ic_baseline_image_24);
            File finalLocalFile = localFile;
            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    imageView.setImageURI(Uri.fromFile(finalLocalFile));
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

    public void sonrakiAdım() {
        yolTarifiDugum.remove(1);
        this.notifyItemRemoved(1);
        kerterizler.remove(0);
        gosterilecekKerteriz = kerterizler.get(0);
    }

    private void imageViewGuncelle() {
        SensorManager sensorManager = null;
        Sensor sensorAccelerometer;
        Sensor sensorMagneticField;

        final float[][] floatGravity = {new float[3]};
        final float[][] floatGeoMagnetic = {new float[3]};

        float[] floatOrientation = new float[3];
        float[] floatRotationMatrix = new float[9];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sensorManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);
        }

        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        SensorEventListener sensorEventListenerAccelrometer = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                floatGravity[0] = event.values;

                SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity[0], floatGeoMagnetic[0]);
                SensorManager.getOrientation(floatRotationMatrix, floatOrientation);

                imageViewYonGostergesi.setRotation((float) (-floatOrientation[0]*180/3.14159) + gosterilecekKerteriz);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        SensorEventListener sensorEventListenerMagneticField = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                floatGeoMagnetic[0] = event.values;

                SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity[0], floatGeoMagnetic[0]);
                SensorManager.getOrientation(floatRotationMatrix, floatOrientation);

                imageViewYonGostergesi.setRotation((float) (-floatOrientation[0]*180/3.14159) + gosterilecekKerteriz);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(sensorEventListenerAccelrometer, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListenerMagneticField, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
