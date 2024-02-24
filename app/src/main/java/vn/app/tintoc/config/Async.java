package vn.app.tintoc.config;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import vn.app.tintoc.LoginActivity;

import org.json.JSONObject;

/**
 * Created by Admin on 7/28/2017.
 */

public class Async extends AsyncTask<Object, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(Object... objects) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        if (jsonObject == null) {
            Log.e("ERR !=1 " + this.getClass().getName(), this.getClass().getName());
            Toast.makeText(GlobalClass.getActivity(), "Không thể truy cập đến server, vui lòng kiểm tra kết nối của bạn hoặc khởi động lại ứng dụng", Toast.LENGTH_SHORT).show();
        }
        try {
            int code = jsonObject.getInt("code");

            if (code == -1) {
                Intent intent = new Intent(GlobalClass.getActivity(), LoginActivity.class);
                GlobalClass.getActivity().startActivity(intent);
                GlobalClass.getActivity().finish();
                return;
            }

            if (code != 1) {
                Log.e("ERR1 " + this.getClass().getName(), this.getClass().getName());
//                Toast.makeText(GlobalClass.getActivity(), ErrorHelper.getValueByKey(code), Toast.LENGTH_SHORT).show();
                Toast.makeText(GlobalClass.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region Alert dialog
//    public static class TryAgainDialogFragment extends DialogFragment{
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setMessage(R.string.err_access)
//                    .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = getActivity().getBaseContext().getPackageManager()
//                                    .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName() );
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                        }
//                    });
//            return builder.create();
//        }
//    }
    //endregion
}
