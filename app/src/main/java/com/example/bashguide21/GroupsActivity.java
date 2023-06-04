package com.example.bashguide21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class MyAdapter extends BaseAdapter {

    private static class Item{
        String text;
        int picture;

        public Item(String name, int picture) {
            this.text = name;
            this.picture = picture;
        }
    }

    List<Item> itemList = new ArrayList<>();
    LayoutInflater layoutInflater;

    public MyAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        itemList.add(new Item("Памятники и мемориалы", R.drawable.monument));
        itemList.add(new Item("Интересные места", R.drawable.interes));
        itemList.add(new Item("Парки и фонтаны", R.drawable.park));
        itemList.add(new Item("Музеи и культура", R.drawable.museum));
        itemList.add(new Item("Стадионы и арены", R.drawable.sport));
        itemList.add(new Item("Развлечения и отдых", R.drawable.fun));
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemList.get(position).picture;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ImageView picture;
        TextView text;

        if (v == null){
            v = layoutInflater.inflate(R.layout.grid_item, parent, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        text = (TextView) v.getTag(R.id.text);

        Item item = (Item) getItem(position);
        picture.setImageResource(item.picture);
        text.setText(item.text);

        return v;
    }
}

public class GroupsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(new MyAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupsActivity.this, ListGroupsActivity.class);
                intent.putExtra("GroupID", position);
                startActivity(intent);
            }
        });
    }
}