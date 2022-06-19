package com.mac.avmpyon.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mac.avmpyon.R;
import com.mac.avmpyon.databinding.ActivityHaritaBinding;
import com.mac.avmpyon.databinding.ActivityMainBinding;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.util.List;

public class HaritaActivity extends AppCompatActivity {

    private ActivityHaritaBinding binding;

    private String baslangicAd, bitisAd;
    private List<String> araNoktalar;

    private int[] resorce = new int[]{R.drawable.kat_4, R.drawable.kat_3};
    private int gosterilecekHarita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_harita);

        baslangicAd = getIntent().getStringExtra("baslangicAd");
        bitisAd = getIntent().getStringExtra("bitisAd");
        araNoktalar = getIntent().getStringArrayListExtra("araNoktalar");

        try {
            VectorMasterDrawable vectorMasterDrawable_ = new VectorMasterDrawable(this, resorce[0]);
            PathModel pathModel = vectorMasterDrawable_.getPathModelByName(baslangicAd);
            pathModel.setFillColor(Color.parseColor("#F05454"));
            gosterilecekHarita = resorce[0];
        } catch (Exception e) {
            gosterilecekHarita = resorce[1];
        }


        VectorMasterDrawable vectorMasterDrawable = new VectorMasterDrawable(this, gosterilecekHarita);
        ImageView imageView = (ImageView) findViewById(R.id.map_vector);
        imageView.setImageDrawable(vectorMasterDrawable);

        try {
            PathModel pathModel = vectorMasterDrawable.getPathModelByName(baslangicAd);
            pathModel.setFillColor(Color.parseColor("#F05454"));
            for (String a : araNoktalar) {
                PathModel pathModel_ = vectorMasterDrawable.getPathModelByName(a);
                pathModel_.setFillColor(Color.parseColor("#95D1CC"));
            }
            PathModel pathModel2 = vectorMasterDrawable.getPathModelByName(bitisAd);
            pathModel2.setFillColor(Color.parseColor("#06FF00"));
        } catch (NullPointerException e) {
            Log.e("Bulunamadı", baslangicAd + " - " +bitisAd);
        }

        toolbarAyar();
    }

    private boolean toolbarHide = true;
    private void toolbarAyar() {
        binding.mapVector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toolbarHide) {
                    toolbarHide = false;
                    binding.toolbar2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.toolbar_hide));
                } else {
                    toolbarHide = true;
                    binding.toolbar2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.toolbar_show));
                }
            }
        });

        binding.toolbar2.setTitle("Hatira");
        binding.toolbar2.setNavigationOnClickListener(x -> onBackPressed());
        binding.toolbar2.setOnMenuItemClickListener(x -> x.getItemId() == R.id.action_info ? showAlert() : null);
    }

    private boolean showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HaritaActivity.this);
        builder.setView(getLayoutInflater().inflate(R.layout.harita_info_alert_layout, null, false));
        builder.create().show();
        return true;
    }
}