package floragabor.chameleon;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;

public class MainActivity extends AppCompatActivity {

    Integer[] iconIDs = {
            R.drawable.shopping_logo,
            R.drawable.books_logo,
            R.drawable.lightbulb_logo,
            R.drawable.events_logo
    };

    static String[] category = {
            "Shopping",
            "Work",
            "Ideas",
            "Events"
    };

    private ImageView imgBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gv = (GridView) findViewById(R.id.grid_view);


        gv.setAdapter(new IconAdapter(this));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToDetailView(category[position]);
            }
        });

        ImageView cham_iv = (ImageView) findViewById(R.id.chameleon_blind_img);
        ImageView cham_eye_iv = (ImageView) findViewById(R.id.chameleon_eye_img);

        final Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.eye_move);
        cham_eye_iv.startAnimation(anim2);

        ImageView btnCamera = (ImageView) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCameraApp();
            }
        });

        imgBackground = (ImageView) findViewById(R.id.imgBackground);

        showBackground();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constans.REQUEST_CODE_TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            showBackground();
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private int getOrientation() {

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(getPhotoPath());
        } catch (IOException e) {
            return 0;
        }

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                return 0;
        }
    }

    private void showBackground() {
        String photoPath = getPhotoPath();
        if(photoPath.isEmpty()) {
            return;
        }
        Bitmap bitmap = decodeSampledBitmapFromResource(photoPath, 400, 400);
        imgBackground.setImageBitmap(rotateImage(bitmap, getOrientation()));
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String photoPath,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(photoPath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == Constans.REQUEST_EXTERNAL_STORAGE_RESULT) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callCameraApp();
            } else {
                Toast.makeText(this,
                        "External write permission has not been granted, cannot saved images",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void callCameraApp() {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = createImageFile();
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(callCameraApplicationIntent, Constans.REQUEST_CODE_TAKE_PICTURE);
    }

    private File createImageFile() {
        String filePath = getPhotoPath();
        File file = new File(filePath);
        if(file.exists()) {
            return file;
        } else {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/saved_images");
            myDir.mkdirs();
            String fname = "Image-background.jpg";
            File photoFile = new File(myDir, fname);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(Constans.PREF_PHOTO_PATH, photoFile.getAbsolutePath()).commit();
            return photoFile;
        }
    }

    @NonNull
    private String getPhotoPath() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(Constans.PREF_PHOTO_PATH,"");
    }

    public void goToDetailView(String category) {
        Intent intent = new Intent(this, DetailView.class);
        intent.putExtra(Constans.ARG_CATEGORY, category);
        startActivity(intent);

        getSupportFragmentManager().popBackStack();
    }

    public class IconAdapter extends BaseAdapter {

        private Context context;

        public IconAdapter(Context ctx) {
            context = ctx;
        }

        @Override
        public int getCount() {
            return iconIDs.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                view = new View(context);
                view = inflater.inflate(R.layout.tile, null);

                TextView tv = (TextView) view.findViewById(R.id.grid_text);
                Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Fonty.ttf");
                tv.setTypeface(externalFont);


                ImageView iv = (ImageView) view.findViewById(R.id.grid_image);

                iv.setImageResource(iconIDs[position]);
                tv.setText(category[position]);
                view.setTag(category[position]);
            } else {
                view = convertView;
            }

            return view;

        }
    }

}