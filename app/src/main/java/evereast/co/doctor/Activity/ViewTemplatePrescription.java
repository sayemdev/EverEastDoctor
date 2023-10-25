package evereast.co.doctor.Activity;

import static evereast.co.doctor.Constants.PATIENT_URL;
import static evereast.co.doctor.Constants.SIGNATURE;
import static evereast.co.doctor.Constants.USER_DATA_PREF;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Objects;

import evereast.co.doctor.R;
import evereast.co.doctor.databinding.ActivityViewTemplatePrescriptionBinding;

public class ViewTemplatePrescription extends AppCompatActivity {
    private static final String TAG = "ViewPrescription";
    public static Bitmap bitScroll;
    private final String prescriptionID = "null";
    ScrollView rootLayout;
    TextView ccShowTV, hoShowTV, ixShowTV, dxShowTV, rxShowTV, adShowTV, drNameTextView, drQualification, patientNameTV, patientAgeTV, entryDateTV, patientGenderTV, patientAddressTV, prescriptionIDTV;
    Button issueBt;
    StringBuilder stringBuilderOfCC, stringBuilderOfAD, stringBuilderOfRX, stringBuilderOfDX, stringBuilderOfIX, stringBuilderOfHO;
    String patientAddress, appointmentId;
    StringBuilder doctorInfoBuilder;
    RelativeLayout mainPrescriptionView;
    String title;
    ActivityViewTemplatePrescriptionBinding binding;
    private ImageView signatureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewTemplatePrescriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appointmentId = getIntent().getStringExtra("appointment_id");
        title = getIntent().getStringExtra("title");

        signatureView = findViewById(R.id.signatureView);
        rootLayout = findViewById(R.id.rootLt);
        ccShowTV = findViewById(R.id.ccShowTV);
        hoShowTV = findViewById(R.id.hoShowTV);
        ixShowTV = findViewById(R.id.ixShowTV);
        dxShowTV = findViewById(R.id.dxShowTV);
        rxShowTV = findViewById(R.id.rxShowTV);
        adShowTV = findViewById(R.id.advicesShow);
        patientNameTV = findViewById(R.id.patientNameTv);
        patientAgeTV = findViewById(R.id.pAgeTV);
        entryDateTV = findViewById(R.id.issueDateTV);
        patientGenderTV = findViewById(R.id.patientGenderTV);
        patientAddressTV = findViewById(R.id.pAddressTV);
        prescriptionIDTV = findViewById(R.id.prescriptionId);
        drNameTextView = findViewById(R.id.drNameTV);
        drQualification = findViewById(R.id.drQualification);
        mainPrescriptionView = findViewById(R.id.mainView);

        SharedPreferences sharedPreferencesDr = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);
        String signatureId = sharedPreferencesDr.getString(SIGNATURE, "sf");
        Glide.with(this).load(PATIENT_URL + "d_signature/" + signatureId).into(signatureView);

        try {
            JSONObject object = new JSONObject(getIntent().getStringExtra("full"));
            Log.d(TAG, "onCreate: "+object);
            drNameTextView.setText(object.getString("doctor_name"));

            String dQualification = object.getString("doctor_info");
            drQualification.setText(dQualification.trim());
            patientNameTV.setText(object.getString("patient_name"));
            patientAgeTV.setText(object.getString("patient_age"));
            patientAddressTV.setText(object.getString("patient_address"));
            entryDateTV.setText(object.getString("issue_date"));
            ccShowTV.setText(object.getString("cc"));
            ixShowTV.setText(object.getString("ix"));
            rxShowTV.setText(object.getString("rx"));
            dxShowTV.setText(object.getString("dx"));
            adShowTV.setText(object.getString("ad"));
            hoShowTV.setText(object.getString("ho"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void SaveToLocal(View view) throws IOException {
        bitScroll = getBitmapFromView(rootLayout, rootLayout.getChildAt(0).getHeight(), rootLayout.getChildAt(0).getWidth());
        saveBitmap(bitScroll);
        gen(bitScroll);
    }

    //create bitmap from the ScrollView
    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    public void saveBitmap(Bitmap bitmap) {
        OutputStream outputStream;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, title + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "/Healthmen/Patient/Prescription/Image/");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                Objects.requireNonNull(outputStream);
                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Image Not Not  Saved: \n " + e, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {

            File imagePathFile = new File((Environment.getExternalStorageDirectory() + File.separator + "/Healthmen/Patient/Prescription/Image/"));
            File imagePath = new File(Environment.getExternalStoragePublicDirectory("") + File.separator + "/Healthmen/Patient/Prescription/Image/" + title + ".jpg");

            if (!imagePathFile.exists()) {
                imagePathFile.mkdirs();
            }

            FileOutputStream fos;
            try {
                fos = new FileOutputStream(imagePath, false);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
//                Toast.makeText(getApplicationContext(), imagePath.getAbsolutePath() + "", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Log.e("GREC", e.getMessage(), e);
                e.printStackTrace();
                Toast.makeText(this, "Image not saved", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("GREC", e.getMessage(), e);
                Toast.makeText(this, "Image not saved", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    void gen(Bitmap bitmap) throws IOException {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);

        pdfDocument.finishPage(page);

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
///Healthmen/Doctor/  /Healthmen/Doctor/
        File imagePathFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/Healthmen/Doctor/Prescription/PDF");
        File imagePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Healthmen/Doctor/Prescription/PDF/" + title + ".pdf");

        boolean success = true;
        if (!imagePathFile.exists()) {
            success = imagePathFile.mkdirs();
        }
        if (success) {
            // Do something on success
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(imagePath, false);
                pdfDocument.writeTo(fos);
                pdfDocument.close();
//                Toast.makeText(getApplicationContext(), imagePath.getAbsolutePath() + "", Toast.LENGTH_LONG).show();
                Log.e("ImageSave", "Saveimage");
            } catch (FileNotFoundException e) {
                Log.e("GREC", e.getMessage(), e);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Do something else on failure
            Log.d("TAG_FILE", "gen: File not saved");
            savePDFFile(bitmap, this, title + ".pdf", "HM");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @NonNull
    private Uri savePDFFile(Bitmap bitmap, @NonNull final Context context,
                            @NonNull final String displayName, @Nullable final String subFolder) throws IOException {
        String relativeLocation = Environment.DIRECTORY_DOCUMENTS;
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);

        pdfDocument.finishPage(page);

        if (!TextUtils.isEmpty(subFolder)) {
            relativeLocation += File.separator + subFolder;
        }

        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "files/pdf");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);
        contentValues.put(MediaStore.Video.Media.TITLE, title);
        contentValues.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        contentValues.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
        final ContentResolver resolver = context.getContentResolver();
        OutputStream stream = null;
        Uri uri = null;
        OutputStream outputStream;
        try {
            final Uri contentUri = MediaStore.Files.getContentUri("external");
            uri = resolver.insert(contentUri, contentValues);
//            ParcelFileDescriptor pfd;
            try {
                assert uri != null;
                outputStream = resolver.openOutputStream(Objects.requireNonNull(uri));
                Objects.requireNonNull(outputStream);
                pdfDocument.writeTo(outputStream);
                pdfDocument.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            contentValues.clear();
            contentValues.put(MediaStore.Video.Media.IS_PENDING, 0);
            getContentResolver().update(uri, contentValues, null, null);
            stream = resolver.openOutputStream(uri);
            if (stream == null) {
                throw new IOException("Failed to get output stream.");
            }
            return uri;
        } catch (IOException e) {
            // Don't leave an orphan entry in the MediaStore
            resolver.delete(uri, null, null);
            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

}