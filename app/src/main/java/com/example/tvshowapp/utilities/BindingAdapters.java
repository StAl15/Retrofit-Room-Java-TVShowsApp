package com.example.tvshowapp.utilities;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

//сеттер картинок в адаптере
public class BindingAdapters {

    @BindingAdapter("android:imageURL")
    public static void setImageUrl(ImageView imageView, String URL) {
        try {
            imageView.setAlpha(0f); //прозрачность
            Picasso.get().load(URL).noFade().into(imageView, new Callback() { //загрузка изображения по юрл, без кратковременного ичсезновения
                @Override
                public void onSuccess() {
                    imageView.animate().setDuration(300).alpha(1f).start();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        } catch (Exception ignored) {

        }
    }
}
