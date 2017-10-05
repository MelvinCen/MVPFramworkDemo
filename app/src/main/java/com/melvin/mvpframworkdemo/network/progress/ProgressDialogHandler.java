package com.melvin.mvpframworkdemo.network.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.lang.ref.WeakReference;


/**
 * 显示dialog的handler
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialog pd;

//    private Context context;
    private WeakReference<Context> mContextWeakReference;
    private boolean cancelable;

    private ProgressCancelListener mCancelListener;

    public ProgressDialogHandler(Context context, boolean cancelable) {
        super();
//        this.context = context;
        mContextWeakReference = new WeakReference<Context>(context);
        this.cancelable = cancelable;
    }

    private void initProgressDialog(String title) {
        if (pd == null) {
            if (mContextWeakReference == null) {
                return;
            }
            pd = new ProgressDialog(mContextWeakReference.get());
            if (TextUtils.isEmpty(title)) {
                title = "加载中,请稍后....";
            }
            pd.setMessage(title);
            pd.setCancelable(cancelable);
            if (cancelable) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dismissProgressDialog();
                        if (mCancelListener != null) {
                            mCancelListener.onCancelProgress();
                        }


                    }
                });
            }
            //这个还没实验过
//            pd.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//                        dismissProgressDialog();
//                        mCancelListener.onCancelProgress();
//                    }
//                    return true;
//                }
//            });


            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    public void setOnProgressCancelListener(ProgressCancelListener cancelListener){
        mCancelListener = cancelListener;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                String title = (String) msg.obj;
                initProgressDialog(title);
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

}
