package com.roxwin.imagefilter.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roxwin.imagefilter.AppApplication;
import com.roxwin.imagefilter.R;
import com.roxwin.imagefilter.filters.GPUImageFilter;
import com.roxwin.imagefilter.filters.GPUImageView;
import com.roxwin.imagefilter.filters.util.GPUImageFilterTools;
import com.roxwin.imagefilter.listeners.EffectListener;
import com.roxwin.imagefilter.models.EffectModel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by PhucTV on 4/19/16.
 */
public class EffectListAdapter extends RecyclerView.Adapter<EffectListAdapter.ViewHolder> {
    List<EffectModel> mEffects;
    EffectListener mListener;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    public EffectListAdapter(List<EffectModel> mEffects, EffectListener mListener) {
        mPref = AppApplication.getContext().getSharedPreferences("indexeffect", Context.MODE_PRIVATE);
        mEditor = mPref.edit();
        this.mEffects = mEffects;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_effect_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final EffectModel trip = mEffects.get(position);
        holder.ivEffectPreview.setImageBitmap(trip.getEffectPreview());
        holder.tvEffectPreview.setText(trip.getEffectName());
        if (mEffects.get(position).isSelected()) {
            holder.tvEffectPreview.setBackgroundColor(AppApplication.getContext().getResources().getColor(R.color.filter_title_selected_bg50));
        } else {
            holder.tvEffectPreview.setBackgroundColor(AppApplication.getContext().getResources().getColor(R.color.black50));
        }

        holder.ivEffectPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onItemClick(position, trip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEffects == null ? 0 : mEffects.size();
    }

    public void  setSelected(int pos) {
        try {
            if (mEffects.size() > 1) {
                mEffects.get(mPref.getInt("position", 0)).setSelected(false);
                mEditor.putInt("position", pos);
                mEditor.commit();
            }
            for (EffectModel eff : mEffects) {
                eff.setSelected(false);

            }
            mEffects.get(pos).setSelected(true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tvEffectPreview)
        TextView tvEffectPreview;
        @InjectView(R.id.ivEffectPreview)
        ImageView ivEffectPreview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
