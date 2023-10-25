package evereast.co.doctor.Activity;

import static evereast.co.doctor.Constants.FILE_PATH;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.github.florent37.tutoshowcase.TutoShowcase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import evereast.co.doctor.Adapter.ImageSliderAdapter;
import evereast.co.doctor.Adapter.SlidedImageAdapter;
import evereast.co.doctor.Model.ImagesList;
import evereast.co.doctor.R;
import evereast.co.doctor.databinding.ActivityZoomImageBinding;

public class ZoomImageActivity extends AppCompatActivity implements ImageSliderAdapter.ClickListener, SlidedImageAdapter.OnClickListener, ViewPager.OnPageChangeListener {
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 150;
    private static final int UI_ANIMATION_DELAY = 150;
    private static final String TAG = "ZoomImageActivity";
    private final Handler mHideHandler = new Handler();
    ActivityZoomImageBinding binding;
    Type type;
    ImagesList imagesList;
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = this::hide;
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityZoomImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        type = new TypeToken<ImagesList>() {
        }.getType();
        imagesList = new Gson().fromJson(getIntent().getStringExtra("imagesList"), type);

        Log.d(TAG, "onCreate: " + imagesList);

        mVisible = true;
        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.fullscreenContent;

        Log.d(TAG, "onCreate: " + getIntent().getStringExtra("imagesList"));

        mContentView.setOnClickListener(view -> {
            toggle();
        });

        binding.viewPager.setOnClickListener(view -> {
            toggle();
        });
        binding.photoView.setOnClickListener(view -> {
            toggle();
        });
        binding.fullscreenContent.setOnClickListener(view -> {
            toggle();
        });

        binding.back.setOnClickListener(view -> {
            finish();
        });

        if (getIntent().getStringExtra("imagesList") == null) {
            binding.slidingList.setVisibility(View.GONE);
            binding.viewPager.setVisibility(View.GONE);
        }

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(this, imagesList.getImages(), this);
        binding.viewPager.setAdapter(imageSliderAdapter);
        binding.viewPager.setCurrentItem(getIntent().getIntExtra("selectedPosition", -1));
        binding.photoView.setImageResource(R.drawable.round_health);
        Glide.with(this).load(FILE_PATH + getIntent().getStringExtra("link")).placeholder(R.drawable.round_health).into(binding.photoView);
        binding.slidingList.setHasFixedSize(true);
        binding.slidingList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.slidingList.setAdapter(new SlidedImageAdapter(this, this, imagesList.getImages(), getIntent().getIntExtra("selectedPosition", -1), binding.viewPager));
        binding.slidingList.getLayoutManager().scrollToPosition(getIntent().getIntExtra("selectedPosition", -1));
        binding.viewPager.addOnPageChangeListener(this);
        TutoShowcase.from(this).setContentView(R.layout.tuto_sample).on(R.id.about).on(R.id.swipeAble).displaySwipableRight().on(R.id.swipeAble).displaySwipableLeft().showOnce("string");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(150);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().show(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        mVisible = true;

        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void OnClick(int position) {
        toggle();
    }

    @Override
    public void OnClick(int position, String imageName) {
        new Handler().postDelayed(() -> binding.slidingList.scrollToPosition(position), 200);
//        binding.slidingList.getLayoutManager().scrollToPosition(position);
    }

    @Override
    public void SelectedImageListener(int position, String title) {
        if (!title.isEmpty()) {
            binding.imageTitleTV.setText(title);
            binding.imageTitleTV.setVisibility(View.VISIBLE);
        } else {
            binding.imageTitleTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        binding.slidingList.setHasFixedSize(true);
        binding.slidingList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.slidingList.setAdapter(new SlidedImageAdapter(this, this, imagesList.getImages(), position, binding.viewPager));
        new Handler().postDelayed(() -> binding.slidingList.scrollToPosition(position), 200);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}