package com.mac.avmpyon.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mac.avmpyon.R;
import com.mac.avmpyon.databinding.ActivityKonumAlmaBinding;
import com.mac.avmpyon.model.Avm;
import com.mac.avmpyon.model.Dugum;

import java.util.ArrayList;

public class KonumAlmaActivity extends AppCompatActivity {

    private static String DURUM_1 = "BULUNDUĞUNUZ KONUM";
    private static String DURUM_2 = "GİDECEĞİNİZ KONUM";
    private static String DURUM;

    private ActivityKonumAlmaBinding binding;
    private Avm avm;

    private ArrayList<String> avmAdListesi;
    private ArrayList<String> avmAdListesiGösterilecek;

    private ArrayAdapter<String> arrayAdapter;

    private Dugum bulunulanKonumSecilen, gidelecekKonumSecilen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_konum_alma);
        avm = (Avm) getIntent().getSerializableExtra("avm");
        bulunulanKonumSecilen = (Dugum) getIntent().getSerializableExtra("bk");

        durumAl();


        toolbarSettings();
        listViewDoldurma();
        searchViewDinleme();
        secilenIdexTakip();
    }

    private void secilenIdexTakip() {
        binding.lvMagazalar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (DURUM.equals(DURUM_1)) {
                    String secilenNokta = avmAdListesiGösterilecek.get(position);
                    for (Dugum d : avm.getAvmDugumleri()) {
                        if (d.getAd().equals(secilenNokta)) {
                            bulunulanKonumSecilen = d;
                        }
                    }
                } else{
                    String secilenNokta = avmAdListesiGösterilecek.get(position);
                    for (Dugum d : avm.getAvmDugumleri()) {
                        if (d.getAd().equals(secilenNokta)) {
                            gidelecekKonumSecilen = d;
                        }
                    }
                }
            }
        });
    }

    private void durumAl() {
        DURUM = getIntent().getStringExtra("DURUM");

        if (DURUM == null || DURUM.trim().equals("")) {
            DURUM = "BULUNDUĞUNUZ KONUM";
        }
    }

    private void searchViewDinleme() {
        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                avmAdListesiGösterilecek.clear();
                for (String adListesi : avmAdListesi) {
                    if (adListesi.toLowerCase().contains(s.toString().toLowerCase())) {
                        avmAdListesiGösterilecek.add(adListesi);
                    }
                }
                arrayAdapter.notifyDataSetChanged();

                if (s.length() == 0) {
                    avmAdListesiGösterilecek.clear();
                    for (String ad : avmAdListesi) {
                        avmAdListesiGösterilecek.add(ad);
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void listViewDoldurma() {
        avmAdlariDoldurma();
        arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, avmAdListesiGösterilecek);
        binding.lvMagazalar.setAdapter(arrayAdapter);
    }

    private void avmAdlariDoldurma() {
        avmAdListesi = new ArrayList<>();
        avmAdListesiGösterilecek = new ArrayList<>();
        for (Dugum d : avm.getAvmDugumleri()) {
            avmAdListesi.add(d.getAd());
            avmAdListesiGösterilecek.add(d.getAd());
        }
    }

    private void toolbarSettings() {
        binding.textViewToolbarTitle.setText(DURUM);
        binding.toolbar.setTitle("");
        binding.toolbar.setNavigationIcon(R.drawable.ic_sharp_clear_24);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.konum_alma_toolbar_manu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_next) {
            if (DURUM.equals(DURUM_1)) {
                Intent intent = new Intent(getApplicationContext(), KonumAlmaActivity.class);
                intent.putExtra("DURUM", DURUM_2);
                intent.putExtra("avm", avm);
                intent.putExtra("bk", bulunulanKonumSecilen);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), YolTarifiActivity.class);
                intent.putExtra("avm", avm);
                intent.putExtra("bd", bulunulanKonumSecilen);
                intent.putExtra("gd", gidelecekKonumSecilen);
                startActivity(intent);
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AvmDetayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("avm",avm);
        startActivity(intent);
        overridePendingTransition(R.anim.var_olan_sayfa, R.anim.yukaridan_cikan_sayfa);
    }
}