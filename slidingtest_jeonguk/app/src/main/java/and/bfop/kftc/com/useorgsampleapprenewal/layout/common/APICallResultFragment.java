package and.bfop.kftc.com.useorgsampleapprenewal.layout.common;

import android.app.Dialog;
import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import and.bfop.kftc.com.useorgsampleapprenewal.util.Constants;

/**
 * API 호출 후 결과 JSON을 보여줄 목적으로 생성한 DialogFragment
 */
public class APICallResultFragment extends DialogFragment {


    //===================================== Fragment Lifecycle Callbacks - start =====================================
    /**
     * Only for DialogFragment
     *
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_apicall_result, null);
        builder.setView(view)
                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // nothing to do
                    }
                });
        Dialog dialog = builder.create();

        // 결과값 채우기
        ((TextView)view.findViewById(R.id.tvJsonResult)).setText(getArguments().getString(Constants.RSP_JSON));

        return dialog;
    }

    @Override
    public void onResume() {

        // Dialog 사이즈 조정
        //  - 이렇게 해도 내용이 없으면 닫기 버튼이 위로 말려 올라가기 때문에 그닥 좋지는 않다. 내용이 없어도 TextView가 MATCH_PARENT가 되면 좋을텐데...
//        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        getDialog().getWindow().setAttributes(params);

        super.onResume();
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

}
