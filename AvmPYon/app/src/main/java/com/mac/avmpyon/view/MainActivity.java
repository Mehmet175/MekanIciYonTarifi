package com.mac.avmpyon.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.mac.avmpyon.R;
import com.mac.avmpyon.adapters.MainActivityRecyclerAdapter;
import com.mac.avmpyon.databinding.ActivityMainBinding;
import com.mac.avmpyon.model.Avm;
import com.mac.avmpyon.viewmodel.MainActivityViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;

    private ArrayList<Avm> avms_;
    private MainActivityRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        rvDoldur();
        viewModelListener();
    }

    private void viewModelListener() {
        viewModel.avmleriGetir();
        viewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<Avm>>() {
            @Override
            public void onChanged(ArrayList<Avm> avms) {
                for (Avm a : avms) {
                    avms_.add(a);
                }
                adapter.notifyDataSetChanged();
                binding.progressBarMain.setVisibility(View.GONE);
            }
        });
    }

    private void rvDoldur() {
        avms_ = new ArrayList<>();
        adapter = new MainActivityRecyclerAdapter(this, avms_);
        binding.rvAvmList.setHasFixedSize(true);
        binding.rvAvmList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.rvAvmList.setAdapter(adapter);
    }
}