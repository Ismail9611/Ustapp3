package org.ginlevel.ustapp3.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.ginlevel.ustapp3.R;
import org.ginlevel.ustapp3.model.Category;
import java.util.List;


public class CategoryAdapter extends BaseAdapter {

    private static final String TAG = "CATEGORY_LOG";

    private List<Category> categoriesList;
    private LayoutInflater layoutInflater;

    public CategoryAdapter(Context context, List<Category> categoriesList) {
        this.categoriesList = categoriesList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categoriesList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoriesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = layoutInflater.inflate(R.layout.category_item, parent, false);
        }

        Category category = categoriesList.get(position);
        ((TextView) view.findViewById(R.id.tvCategoryName)).setText(category.getName());
        ((ImageView) view.findViewById(R.id.ivCategoryImg)).setImageURI(Uri.parse(category.getImageUrl()));
//        ((ImageView) view.findViewById(R.id.ivCategoryImg)).setImageDrawable(view.getResources().getDrawable(R.drawable.master));
        return view;
    }
}
