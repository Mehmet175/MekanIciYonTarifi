package com.mac.avmpyon.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mac.avmpyon.R;
import com.mac.avmpyon.databinding.ActivityMainRvCardBinding;
import com.mac.avmpyon.model.Avm;
import com.mac.avmpyon.view.AvmDetayActivity;

import java.util.List;

public class MainActivityRecyclerAdapter extends RecyclerView.Adapter<MainActivityRecyclerAdapter.MainActivityRecyclerAdapterViewHolder> {

    private Context mContext;
    private List<Avm> avms;

    public MainActivityRecyclerAdapter(Context mContext, List<Avm> avms) {
        this.mContext = mContext;
        this.avms = avms;
    }

    @NonNull
    @Override
    public MainActivityRecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainActivityRecyclerAdapterViewHolder
                (LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_main_rv_card,null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityRecyclerAdapterViewHolder holder, int position) {
            holder.onBind(avms.get(position));
    }

    @Override
    public int getItemCount() {
        return avms.size();
    }


    public class MainActivityRecyclerAdapterViewHolder extends RecyclerView.ViewHolder{

        private ActivityMainRvCardBinding binding;

        public MainActivityRecyclerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void onBind(Avm avm) {
            binding.setAvm(avm);
            binding.clAvm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AvmDetayActivity.class);
                    intent.putExtra("avm", avm);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
