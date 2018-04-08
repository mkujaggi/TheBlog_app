package com.crackeddish.theblog.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crackeddish.theblog.Model.Blog;
import com.crackeddish.theblog.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Blog blog=blogList.get(position);
        String imageUrl=null;
        holder.title.setText(blog.getTitle());
        holder.desc.setText(blog.getDesc());
        java.text.DateFormat dateFormat=java.text.DateFormat.getDateInstance();
        String formattedDate=dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());
        holder.timestamp.setText(formattedDate);
        imageUrl=blog.getImage();
        Picasso.get().load(imageUrl).into(holder.image);
        //TODO: use picasso to load image

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView desc;
        public TextView timestamp;
        public ImageView image;
        String userId;
        public ViewHolder(View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            title=(TextView)itemView.findViewById(R.id.postTitleList);
            desc=(TextView)itemView.findViewById(R.id.postTextList);
            image=(ImageView)itemView.findViewById(R.id.postImageList);
            timestamp=(TextView)itemView.findViewById(R.id.timestampList);
            userId=null;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                }
            });
        }
    }
}
