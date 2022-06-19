package com.mac.avmp.view;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.mac.avmp.R;
import com.mac.avmp.adapter.ViewPagerAdapter;
import com.mac.avmp.databinding.ActivityAvmEklemeBinding;
import com.mac.avmp.model.Avm;
import com.mac.avmp.viewmodel.AvmEklemeActivityViewModel;
import com.mac.avmp.viewmodel.ViewModelStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.Inflater;

public class AvmEklemeActivity extends AppCompatActivity {

    private ActivityAvmEklemeBinding binding;
    private AvmEklemeActivityViewModel viewModel;

    private ArrayList<Uri> uris;
    private ArrayList<String> resAdlari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_avm_ekleme);
        viewModel = new ViewModelProvider(this).get(AvmEklemeActivityViewModel.class);

        toolbarSettings();
        viewModelListener();
        resimSectirme();
    }

    private void resimSectirme() {
        ActivityResultLauncher<String> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.GetMultipleContents()
                        , new ActivityResultCallback<List<Uri>>() {
                            @Override
                            public void onActivityResult(List<Uri> result) {
                                if (result != null || result.size() != 0) {
                                        uris = (ArrayList<Uri>) result;
                                        resimleriGoster();
                                        otomotikKaydirmayiBaslat();
                                } else {
                                    Snackbar.make(binding.getRoot(), "Resim seçilmedi!", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
        binding.imageViewResimSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch("image/*");
            }
        });
    }

    private void otomotikKaydirmayiBaslat() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < uris.size(); i++) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.viewPager2.setCurrentItem(finalI);
                        }
                    });

                    if (i==uris.size() - 1) {
                        i = -1;
                    }
                }
            }
        }).start();
    }

    private void resimleriGoster() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, uris);
        binding.viewPager2.setAdapter(adapter);
    }

    private void viewModelListener() {
        viewModel.getVeriEklemeDurum().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals(ViewModelStatus.basarili)) {
                    binding.progressBarAvmEkle.setVisibility(View.GONE);
                    onBackPressed();
                } else {
                    Snackbar.make(binding.getRoot(), "Hata: " + s, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.avm_ekleme_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_ok){
            binding.progressBarAvmEkle.setVisibility(View.VISIBLE);
            viewModel.avmEkle(bilgileriAl());
            viewModel.resimleriEkle(uris, resAdlari);
        }
        return true;
    }

    private Avm bilgileriAl () {
        Avm avm = new Avm(binding.editTextAvmAd.getText().toString());

        resAdlari = new ArrayList<>();
        if (uris != null) {
            for (int i = 0; i < uris.size(); i++) {
                resAdlari.add(UUID.randomUUID().toString());
            }
            avm.setResler(resAdlari);
        }
        return avm;
    }

    private void toolbarSettings() {
        binding.toolbarAvmEkle.setTitle("");
        binding.toolbarAvmEkle.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(binding.toolbarAvmEkle);
        binding.toolbarAvmEkle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}