package com.mac.avmpyon.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mac.avmpyon.R;
import com.mac.avmpyon.adapters.AvmDetayViewPagger2Adapter;
import com.mac.avmpyon.databinding.ActivityAvmDetayBinding;
import com.mac.avmpyon.model.Avm;
import com.mac.avmpyon.viewmodel.AvmDetayActivityViewModel;

import java.util.ArrayList;

public class AvmDetayActivity extends AppCompatActivity {

    private ActivityAvmDetayBinding binding;
    private AvmDetayActivityViewModel viewModel;
    private Avm avm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_avm_detay);
        binding.setActivity(this);
        viewModel = new ViewModelProvider(this).get(AvmDetayActivityViewModel.class);
        avm = (Avm) getIntent().getSerializableExtra("avm");
        toolbarSettings();
        resimGoster();
    }

    public void magazaDetayClick() {
        Intent intent = new Intent(getApplicationContext(), MagazalarActivity.class);
        intent.putExtra("avm", avm);
        startActivity(intent);
    }

    private void resimGoster() {
        AvmDetayViewPagger2Adapter adapter = new AvmDetayViewPagger2Adapter(this, avm.getResler());
        binding.viewPager2.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (avm.getResler() != null){
                    for (int i = 0; i < avm.getResler().size(); i++) {
                        int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.viewPager2.setCurrentItem(finalI);
                            }
                        });

                        if (i == avm.getResler().size() - 1) {
                            i = -1;
                        }

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void toolbarSettings() {
        binding.toolbarAvm.setTitle(avm.getAd());
        binding.toolbarAvm.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24);
        binding.toolbarAvm.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void clickYolTarifi () {
        Intent intent = new Intent(this, KonumAlmaActivity.class);
        intent.putExtra("avm", avm);
        startActivity(intent);
        overridePendingTransition(R.anim.asagidan_acilan_sayfa_anim, R.anim.yok_olan_sayfa);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}