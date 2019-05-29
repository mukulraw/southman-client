package com.southman.southmanclient;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jsibbold.zoomage.ZoomageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.southman.southmanclient.billPOJO.Datum;
import com.southman.southmanclient.billPOJO.billBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Orders extends Fragment {


    RecyclerView grid;
    GridLayoutManager manager;
    ProgressBar progress;
    List<Datum> list;
    BillAdapter adapter;

    TextView date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_layout , container , false);

        list = new ArrayList<>();
        date = view.findViewById(R.id.date);

        date.setVisibility(View.GONE);

        grid = view.findViewById(R.id.grid);
        manager = new GridLayoutManager(getContext() , 1);
        progress = view.findViewById(R.id.progress);

        adapter = new BillAdapter(getActivity() , list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getActivity().getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<billBean> call = cr.getBills(SharePreferenceUtils.getInstance().getString("id"));

        call.enqueue(new Callback<billBean>() {
            @Override
            public void onResponse(Call<billBean> call, Response<billBean> response) {

                if (response.body().getStatus().equals("1"))
                {
                    adapter.setData(response.body().getData());
                }
                else
                {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<billBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder>
    {
        Context context;
        List<Datum> list = new ArrayList<>();

        public BillAdapter(Context context , List<Datum> list)
        {
            this.context = context;
            this.list = list;
        }

        void setData(List<Datum> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.bill_list_model , viewGroup , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

            final Datum item = list.get(i);

            holder.name.setText(item.getUser());
            holder.date.setText(item.getCreated());

            final DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            final ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getBill() , holder.image , options);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.zoom_dialog);
                    dialog.show();

                    ZoomageView zoom = dialog.findViewById(R.id.zoom);

                    loader.displayImage(item.getBill() , zoom , options);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView name , date;
            ImageView image;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.textView);
                date = itemView.findViewById(R.id.textView2);
                image = itemView.findViewById(R.id.imageView);

            }
        }
    }

}
