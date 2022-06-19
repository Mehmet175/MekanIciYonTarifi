package com.mac.avmp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.mac.avmp.R;
import com.mac.avmp.databinding.ActivityKomsuBelirlemeBinding;
import com.mac.avmp.model.Avm;
import com.mac.avmp.model.Dugum;
import com.mac.avmp.model.KomsuDugum;
import com.mac.avmp.model.MagazaRes;
import com.mac.avmp.model.MagazaResimEklemeYArdimciModel;
import com.mac.avmp.viewmodel.KomsuBelirlemeViewModel;
import com.mac.avmp.viewmodel.ViewModelStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KomsuBelirlemeActivity extends AppCompatActivity {

    private ActivityKomsuBelirlemeBinding binding;
    private KomsuBelirlemeViewModel viewModel;

    private int verisiAlinanIndex = 0;
    private ArrayList<Dugum> dugums;
    private ArrayList<String> noktaAdlari, noktaAdlariYedek;
    private Avm avm;

    private ArrayList<View> views;

    private int kerterizDerece;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_komsu_belirleme);
        binding.setActivity(this);
        viewModel = new ViewModelProvider(this).get(KomsuBelirlemeViewModel.class);
        dugums = (ArrayList<Dugum>) getIntent().getSerializableExtra("dugums");
        avm = (Avm) getIntent().getSerializableExtra("avm");
        views = new ArrayList<>();

        toolbarSettings();
        noktaAdlariDoldur();
        oncekiVerileriDoldurma();
    }



    private void oncekiVerileriDoldurma() {
       try {
           if (avm.getAvmDugumleri() != null
                   && avm.getAvmDugumleri().size() != 0
                   && avm.getAvmDugumleri().get(verisiAlinanIndex).getKomsuDugums() != null
                   && avm.getAvmDugumleri().get(verisiAlinanIndex).getKomsuDugums().size() != 0) {
               for (KomsuDugum k : avm.getAvmDugumleri().get(verisiAlinanIndex).getKomsuDugums()) {
                   View view = getLayoutInflater().inflate(R.layout.komsu_bilgileri_alma_view, null, false);
                   view.setAnimation(AnimationUtils.loadAnimation(this, R.anim.komsu_ekleme_card_anim));
                   binding.linearLayoutKomsuEkle.addView(view);
                   ((Button)view.findViewById(R.id.buttonKomsuSec)).setOnClickListener((viewButton) -> komsuBelirlemeAlertAc(view));
                   ((Button)view.findViewById(R.id.buttonKerterizHesapla)).setOnClickListener((viewButton) -> komsuKerterizBelirleme(view));
                   ((TextView)view.findViewById(R.id.textViewKomsu)).setText(k.getKomsuDugumAd());
                   ((TextView)view.findViewById(R.id.textViewKerteriz)).setText(k.getKerteriz() + " derece");
                   ((EditText)view.findViewById(R.id.editTextMesafe)).setText(String.valueOf(k.getMesafe()));
                   views.add(view);
               }
           }
       } catch (IndexOutOfBoundsException e) {

       }
    }


    private void noktaAdlariDoldur() {
        int dugumBoyutu = dugums.size();
        noktaAdlari = new ArrayList<>();
        noktaAdlariYedek = new ArrayList<>();
        for (int i = 0; i < dugumBoyutu; i++) {
            noktaAdlari.add(dugums.get(i).getAd());
            noktaAdlariYedek.add(dugums.get(i).getAd());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.avm_ekleme_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_ok) {
            okAyar();
        }
        return super.onOptionsItemSelected(item);
    }

    private void okAyar() {
        try {
            dugumuDoldur();
            verisiAlinanIndex += 1;
            if (dugums.size()  <= verisiAlinanIndex) {
                showProgress();
                avm.setAvmDugumleri(dugums);
                viewModelListener();
                viewModel.dugumleriEkle(avm);
            } else {
                binding.textViewToolbar.setText(dugums.get(verisiAlinanIndex).getAd() + " Komşuları");
                views.clear();
                binding.linearLayoutKomsuEkle.removeAllViews();
                oncekiVerileriDoldurma();
            }
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(KomsuBelirlemeActivity.this);
            builder.setTitle("Hata");
            builder.setIcon(R.drawable.ic_baseline_info_24);
            builder.setMessage("Lütfen, tüm alanları doldurduğunuza emin olunuz...");
            builder.setPositiveButton("Tamam", null);
            builder.create().show();
        }

    }

    private void viewModelListener() {
        viewModel.getInsertCheck().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals(ViewModelStatus.basarili)) {
                    hideProgress();
                } else {
                    Snackbar.make(binding.getRoot(), s, Snackbar.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void dugumuDoldur() {
        Dugum dugum = dugums.get(verisiAlinanIndex);
        ArrayList<KomsuDugum> komsuDugums = new ArrayList<>();
        for (View view : views) {
            TextView textViewKomsu = view.findViewById(R.id.textViewKomsu);
            EditText editTextMesafe = view.findViewById(R.id.editTextMesafe);
            TextView textViewKerteriz = view.findViewById(R.id.textViewKerteriz);
            int kerterizDuzenlenmis = Integer.parseInt(textViewKerteriz.getText().toString().replace(" derece", ""));
            komsuDugums.add(new KomsuDugum(
                    textViewKomsu.getText().toString(),
                    Integer.parseInt(editTextMesafe.getText().toString()),
                    kerterizDuzenlenmis));
        }

        dugum.setKomsuDugums(komsuDugums);
    }

    public void fabClickKomsuEkle() {
        View view = getLayoutInflater().inflate(R.layout.komsu_bilgileri_alma_view, null, false);
        view.setAnimation(AnimationUtils.loadAnimation(this, R.anim.komsu_ekleme_card_anim));
        binding.linearLayoutKomsuEkle.addView(view);
        ((Button)view.findViewById(R.id.buttonKomsuSec)).setOnClickListener((viewButton) -> komsuBelirlemeAlertAc(view));
        ((Button)view.findViewById(R.id.buttonKerterizHesapla)).setOnClickListener((viewButton) -> komsuKerterizBelirleme(view));
        views.add(view);
        komsuSil(view);
    }

    private void komsuKerterizBelirleme (View viewTasarim) {
        View view = getLayoutInflater().inflate(R.layout.kerteriz_hesaplama_alertdialog_view, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((TextView)viewTasarim.findViewById(R.id.textViewKerteriz)).setText(kerterizDerece + " derece");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        kerterizHesaplaAlertDialog((TextView) view.findViewById(R.id.textViewAlertKerteriz)
                ,(TextView) viewTasarim.findViewById(R.id.textViewAlertKerteriz)
                ,builder);
    }


    float[] floatGravity = new float[3];
    float[] floatGeoMagnetic = new float[3];

    float[] floatOrientation = new float[3];
    float[] floatRotationMatrix = new float[9];

    private void kerterizHesaplaAlertDialog(TextView textViewAlertDialog, TextView sayfaTextView, AlertDialog.Builder builder) {
        SensorManager sensorManager;
        Sensor sensorAccelerometer;
        Sensor sensorMagneticField;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        SensorEventListener sensorEventListenerAccelrometer = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                floatGravity = event.values;

                SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
                SensorManager.getOrientation(floatRotationMatrix, floatOrientation);

                int kert = (int) (floatOrientation[0]*180/3.14159);
                if (kert < 0) {
                    kerterizDerece = kert + 360;
                } else {
                    kerterizDerece = kert;
                }

                textViewAlertDialog.setText( kerterizDerece + " derece");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        SensorEventListener sensorEventListenerMagneticField = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                floatGeoMagnetic = event.values;

                SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
                SensorManager.getOrientation(floatRotationMatrix, floatOrientation);

                int kert = (int) (floatOrientation[0]*180/3.14159);
                if (kert < 0) {
                    kerterizDerece = kert + 360;
                } else {
                    kerterizDerece = kert;
                }
                textViewAlertDialog.setText(kerterizDerece + " derece");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(sensorEventListenerAccelrometer, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListenerMagneticField, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void komsuBelirlemeAlertAc (View viewTasarim) {
        noktaAdlari.clear();
        for (String s : noktaAdlariYedek) {
            noktaAdlari.add(s);
        }

        View view = getLayoutInflater().inflate(R.layout.komsu_belirle_alertview_view, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        listViewAyar((ListView) view.findViewById(R.id.listViewNoktaArama),
                (TextView) viewTasarim.findViewById(R.id.textViewKomsu),
                (EditText) view.findViewById(R.id.editTextKomsuAra),
                alertDialog);
    }

    private void listViewAyar(ListView listView, TextView textView, EditText editText, AlertDialog alertDialog) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, noktaAdlari);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(noktaAdlari.get(position));
                alertDialog.dismiss();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    noktaAdlari.clear();
                    for (int i = 0; i < noktaAdlariYedek.size(); i++) {
                        String nokta = noktaAdlariYedek.get(i);
                        if (nokta.contains(s)) {
                            noktaAdlari.add(nokta);
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                } else {
                    noktaAdlari.clear();
                    for (String string : noktaAdlariYedek) {
                        noktaAdlari.add(string);
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public String[] removeTheElement(String[] arr, int index)
    {
        if (arr == null || index < 0
                || index >= arr.length) {

            return arr;
        }

        String[] anotherArray = new String[arr.length - 1];

        for (int i = 0, k = 0; i < arr.length; i++) {
            if (i == index) {
                continue;
            }
            anotherArray[k++] = arr[i];
        }
        return anotherArray;
    }


    private void toolbarSettings() {
        binding.toolbar.setTitle("");
        binding.textViewToolbar.setText(dugums.get(0).getAd() + " Komşuları");
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Bilgiler kayıt ediliyor, bekleyiniz...");
        progressDialog.show();
    }
    private void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing() ) {
            progressDialog.dismiss();
        }
    }

    private void komsuSil(View view) {
        ImageView imageView = view.findViewById(R.id.imageViewSil);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(KomsuBelirlemeActivity.this);
                builder.setMessage("Komşu noktayı silmek istiyor musunuz?");
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        views.remove(view);
                        binding.linearLayoutKomsuEkle.removeView(view);
                    }
                }).setNegativeButton("İptal", null);
                builder.create().show();
            }
        });
    }


}