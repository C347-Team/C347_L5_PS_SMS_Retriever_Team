package sg.edu.rp.c347.id19007966.smsretriever;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

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

public class NumberFragment extends Fragment {

    Button btnSMSRetrieve1;
    TextView tvFrag1;
    EditText et1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_number, container, false);

        btnSMSRetrieve1 = (Button) view.findViewById(R.id.btnRetrieve1);
        et1 = (EditText) view.findViewById(R.id.editNum);
        tvFrag1 = (TextView) view.findViewById(R.id.textView2);

        btnSMSRetrieve1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionCheck = PermissionChecker.checkSelfPermission
                        (getActivity(), Manifest.permission.READ_SMS);

                if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_SMS}, 0);
                    // stops the action from proceeding further as permission not
                    //  granted yet
                    return;
                }




                String word = et1.getText().toString();

                String[] split = word.split(" ");

                for (int i = 0; i < split.length; i++) {
                    Uri uri = Uri.parse("content://sms");
                    String[] reqCols = new String[]{"date", "address", "body", "type"};
                    ContentResolver cr = getActivity().getContentResolver();
                    String filter = "body LIKE ?";
                    String[] filterArgs = {"%" + split[i] + "%"};
                    Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);

                    String smsBody = "";

                    if (cursor.moveToFirst()) {
                        do {
                            long dateInMillis = cursor.getLong(0);
                            String date = (String) DateFormat.format("dd MM yyyy h:mm:ss aa", dateInMillis);
                            String address = cursor.getString(1);
                            String body = cursor.getString(2);
                            String type = cursor.getString(3);

                            if (type.equalsIgnoreCase("1")) {
                                type = "Inbox:";
                                smsBody += type + " " + address + "\n at " + date + "\n\"" + body + "\"\n\n";
                            }

                        }
                        while (cursor.moveToNext());
                    }
                    tvFrag1.setText(smsBody);
                }
            }
        });

        return view;
    }
}