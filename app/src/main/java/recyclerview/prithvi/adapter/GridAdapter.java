package recyclerview.prithvi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import recyclerview.prithvi.R;
import recyclerview.prithvi.model.UnsplashData;

/**
 * Created by Prithvi on 8/23/2016.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolderUnsplash>{

    private LayoutInflater layoutInflater;
    private ArrayList<UnsplashData> unsplashList = new ArrayList<>();
    private Context context;

    public GridAdapter(ArrayList<UnsplashData> unsplashList, Context context){
        layoutInflater = LayoutInflater.from(context);
        this.unsplashList = unsplashList;
        this.context = context;
    }

    @Override
    public ViewHolderUnsplash onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_grid, parent, false);
        ViewHolderUnsplash viewHolder = new ViewHolderUnsplash(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderUnsplash holder, int position) {

        UnsplashData currentImage = unsplashList.get(position);
        String image = currentImage.getUrlRegular();

        if(image != null) {
            Glide.with(context)
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(holder.imageUnsplash);
        }
    }

    @Override
    public int getItemCount() {
        return unsplashList.size();
    }

    public class ViewHolderUnsplash extends RecyclerView.ViewHolder{
        private ImageView imageUnsplash;
        public ViewHolderUnsplash(View view) {
            super(view);
            imageUnsplash = (ImageView) view.findViewById(R.id.ivImage);
        }
    }
}
