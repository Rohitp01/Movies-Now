package com.example.rohit.moviesnow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Rohit on 22/12/2015.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MyviewHolder> {

    LayoutInflater inflater;
    List<Trailers>data= Collections.emptyList();

    public TrailersAdapter(Context mcontext, List<Trailers>data){
        inflater = LayoutInflater.from(mcontext);
        this.data=data;
    }
    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view=inflater.inflate(R.layout.trailer_row, viewGroup, false);
        MyviewHolder holder=new MyviewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyviewHolder viewHolder, int i) {

        Trailers currentT=data.get(i);
        viewHolder.title.setText(currentT.title);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{

        TextView title;
        public MyviewHolder(View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.trailer_name);
        }
    }
}
