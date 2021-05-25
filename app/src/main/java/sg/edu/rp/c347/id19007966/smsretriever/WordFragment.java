package sg.edu.rp.c347.id19007966.smsretriever;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WordFragment extends Fragment {

    TextView smsTextView;
    EditText filteringEditText;
    Button retrieveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word, container, false);

        smsTextView = view.findViewById(R.id.textViewWordFilteredData);
        filteringEditText = view.findViewById(R.id.editTextWordFilter);
        retrieveButton = view.findViewById(R.id.buttonRetrieveWord);

        retrieveButton.setOnClickListener((View v) -> {

            // PERMISSIONS
            int permissionCheck = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);

            if (permissionCheck != PermissionChecker.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, 0);
                return;
            }

            // SMS
            Uri uri = Uri.parse("content://sms");
            String[] reqCols = new String[]{"date", "address", "body", "type"};

            ContentResolver contentResolver = getActivity().getContentResolver();
            String filter = "body LIKE ?";
            String[] filterArgs = {"%" + filteringEditText.getText().toString().trim() + "%"};

            Cursor cursor = contentResolver.query(uri, reqCols, filter, filterArgs, null);
            String smsBody = "";
            if (cursor.moveToFirst()) {
                do {
                    long dateInMillis = cursor.getLong(0);
                    String date = (String) DateFormat.format("dd MMM yyy h:mm:ss aa", dateInMillis);
                    String address = cursor.getString(1);
                    String body = cursor.getString(2);
                    String type = cursor.getString(3);
                    type = type.equalsIgnoreCase("1") ? "Inbox: " : "Sent: ";

                    smsBody += type;
                    smsBody += address;
                    smsBody += "\nat ";
                    smsBody += date;
                    smsBody += "\n\"";
                    smsBody += body;
                    smsBody += "\"\n\n";
                }
                while (cursor.moveToNext());
            }
            smsTextView.setText(smsBody);


        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    retrieveButton.performClick();
                }
                else {
                    Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_SHORT).show();
                }
        }
    }
}