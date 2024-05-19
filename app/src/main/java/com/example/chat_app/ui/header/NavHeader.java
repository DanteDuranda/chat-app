package com.example.chat_app.ui.header;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chat_app.R;

public class NavHeader extends LinearLayout {
    private ImageView imageView;
    private  TextView nameTextView;
    private TextView subtitleTextView;
    public static String currentUserName;
    public static String currentUserMail;
    public NavHeader(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.nav_header, this, true);

        imageView = view.findViewById(R.id.imageView);
        nameTextView = view.findViewById(R.id.nav_header_name);
        subtitleTextView = view.findViewById(R.id.textView);
    }

    public void setImage(int resId) {
        imageView.setImageResource(resId);
    }

    public void setName(String name) {
        nameTextView.setText(name);
    }

    public void setSubtitle(String subtitle) {
        subtitleTextView.setText(subtitle);
    }

}
