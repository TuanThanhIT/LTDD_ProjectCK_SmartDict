package com.example.project_ltdd.utils;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class AutoScrollViewPager2 {
    private final ViewPager2 viewPager2;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isGoingForward = true;
    private final int delayMillis;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager2.getCurrentItem();
            int nextItem;

            if (isGoingForward) {
                if (currentItem == viewPager2.getAdapter().getItemCount() - 1) {
                    isGoingForward = false;
                    nextItem = currentItem - 1;
                } else {
                    nextItem = currentItem + 1;
                }
            } else {
                if (currentItem == 0) {
                    isGoingForward = true;
                    nextItem = currentItem + 1;
                } else {
                    nextItem = currentItem - 1;
                }
            }
            viewPager2.setPageTransformer(new FadeInPageTransformer());
            viewPager2.setCurrentItem(nextItem, true);
            handler.postDelayed(this, delayMillis);
        }
    };

    public AutoScrollViewPager2(ViewPager2 viewPager2, int delayMillis) {
        this.viewPager2 = viewPager2;
        this.delayMillis = delayMillis;
        this.viewPager2.setPageTransformer(new FadeInPageTransformer());
    }

    public void start() {
        handler.postDelayed(runnable, delayMillis);
        viewPager2.setPageTransformer(new FadeInPageTransformer());
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }

    private class FadeInPageTransformer implements ViewPager2.PageTransformer {
        @Override
        public void transformPage(View view, float position) {
            if (position <= -1 || position >= 1) {
                // Trang ngoài phạm vi hiển thị
                view.setAlpha(0f);
            } else if (position == 0) {
                // Trang đang hiển thị chính
                view.setAlpha(1f);
            } else {
                // Trang mới xuất hiện dần đè lên trang cũ và fade-out trang cũ
                float alpha = 1 - Math.abs(position);
                view.setAlpha(alpha);
                view.setTranslationX(-position * view.getWidth());
            }
        }
    }
}
