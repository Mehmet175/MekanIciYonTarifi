package com.mac.avmpyon.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mac.avmpyon.R;
import com.mac.avmpyon.adapters.YolTarifiRvAdapter;
import com.mac.avmpyon.databinding.ActivityYolTarifiBinding;
import com.mac.avmpyon.dijsktraAlgorithm.KisaYolHesaplayici;
import com.mac.avmpyon.model.Avm;
import com.mac.avmpyon.model.Dugum;

import java.util.ArrayList;
import java.util.List;

public class YolTarifiActivity extends AppCompatActivity {

    private ActivityYolTarifiBinding binding;

    private Avm avm;
    private Dugum bulunduguDugum, gidelecekDugum;

    private List<Dugum> siraliDugumler;

    private YolTarifiRvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_yol_tarifi);
        binding.setActivity(this);

        avm = (Avm) getIntent().getSerializableExtra("avm");
        bulunduguDugum = (Dugum) getIntent().getSerializableExtra("bd");
        gidelecekDugum = (Dugum) getIntent().getSerializableExtra("gd");

        try {
            yoluHesapla();
        } catch (Exception e) {

        }
        rvAyar();
        haritaAc();
    }

    private void haritaAc() {
        binding.imageViewHaritaAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ArrayList<String> araNoktalar = new ArrayList<>();
                    for (int i = 2; i < siraliDugumler.size() - 1; i++) {
                        araNoktalar.add(adapter.getYolTarifiDugum().get(i).getAd());
                    }

                    String bas = adapter.getYolTarifiDugum().get(1).getAd();

                    Intent intent = new Intent(getApplicationContext(), HaritaActivity.class);
                    intent.putExtra("baslangicAd", bas);
                    intent.putExtra("bitisAd", gidelecekDugum.getAd());
                    intent.putExtra("araNoktalar", araNoktalar);
                    startActivity(intent);

                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Noktalar arası yol tarifi yapılamıyor...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clickSonraki () {
        try {
            if (siraliDugumler.size() == 2) {
                clickAvmDetayaGit();
            }else {
                adapter.sonrakiAdım();
            }
        } catch (NullPointerException e){
            clickAvmDetayaGit();
        }

    }


    private void rvAyar() {
        adapter = new YolTarifiRvAdapter(this, siraliDugumler, binding.imageViewYon);
        binding.rvYolTarifi.setHasFixedSize(true);
        binding.rvYolTarifi.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        binding.rvYolTarifi.setAdapter(adapter);
    }

    private void yoluHesapla() {
        KisaYolHesaplayici kisaYolHesaplayici
                = new KisaYolHesaplayici(avm.getAvmDugumleri(),
                bulunduguDugum.getAd(), gidelecekDugum.getAd());
        siraliDugumler = kisaYolHesaplayici.hesapla();
    }

    public void clickAvmDetayaGit() {
        Intent intent = new Intent(this, AvmDetayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("avm",avm);
        startActivity(intent);
    }
}