package com.technopradyumn.evstationmap;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.technopradyumn.evstationmap.databinding.ActivityFullImageBinding;

public class FullImageActivity extends AppCompatActivity implements View.OnTouchListener {
    private ActivityFullImageBinding binding;
    private ImageView fullImageView;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDistance = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFullImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Uri imageUri = getIntent().getParcelableExtra("imageUri");

        fullImageView = binding.fullImageView;
        fullImageView.setOnTouchListener(this);

        // Load image into ImageView using Glide
        Glide.with(this)
                .asBitmap()
                .load(imageUri)
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(fullImageView);
    }

    private Handler handler = new Handler();

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // Save the initial touch position
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDistance = spacing(event);
                if (oldDistance > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                } else if (mode == ZOOM) {
                    float newDistance = spacing(event);
                    if (newDistance > 10f) {
                        matrix.set(savedMatrix);
                        scale = newDistance / oldDistance;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                        float[] values = new float[9];
                        matrix.getValues(values);
                        float currentScale = values[Matrix.MSCALE_X];
                        if (currentScale < 0.5f) {
                            matrix.setScale(0.5f, 0.5f, mid.x, mid.y);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                handler.postDelayed(() -> checkImageSize(), 3000);
                break;
        }

        view.setImageMatrix(matrix);

        return true;
    }

    private void checkImageSize() {
        if (fullImageView.getDrawable() != null) {
            Matrix imageMatrix = fullImageView.getImageMatrix();
            RectF drawableRect = new RectF(0, 0,
                    fullImageView.getDrawable().getIntrinsicWidth(),
                    fullImageView.getDrawable().getIntrinsicHeight());
            imageMatrix.mapRect(drawableRect);

            float drawableWidth = drawableRect.width();
            float drawableHeight = drawableRect.height();

            // Get the dimensions of the screen
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

            // Check if the image is larger than the screen
            if (drawableWidth > screenWidth || drawableHeight > screenHeight) {
                // Reset image position and size if it's larger than the screen
                matrix.reset();
                fullImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if (drawableWidth < screenWidth / 2 || drawableHeight < screenHeight / 2) {
                // Reset image position and size if it's smaller than half the screen
                matrix.reset();
                fullImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Reset image position and size when the activity is paused
        resetImagePositionAndSize();
    }

    private void resetImagePositionAndSize() {
        matrix.reset();
        fullImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}