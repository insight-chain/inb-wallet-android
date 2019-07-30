package io.insightchain.inbwallet.mvps.view.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.presenter.ReceiveCoinsPresenter;
import io.insightchain.inbwallet.mvps.presenter.ReceiveCoinsView;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;
import io.insightchain.inbwallet.utils.CommonUtils;
import io.insightchain.inbwallet.utils.ScreenUtils;
import io.insightchain.inbwallet.utils.ToastUtils;

import static io.insightchain.inbwallet.common.Constants.PERMISSION_WRITE_FILE_CODE;

@CreatePresenter(presenter = ReceiveCoinsPresenter.class)
public class ReceiveCoinsActivity extends BaseMvpActivity implements ReceiveCoinsView {

    @BindView(R.id.topbar_common)
    TopBarCommon topBar;
    @BindView(R.id.iv_qrcode_receive_coins_activity)
    ImageView qrcodeImage;
    @BindView(R.id.tv_wallet_address_receive_coins)
    TextView walletAddressTextView;

    Bitmap qrBitmap;
    @PresenterVariable
    ReceiveCoinsPresenter presenter;

    @Override
    protected int getLayoutId() {
        isBelowStatus = false;
        return R.layout.activity_receive_coins;
    }

    @Override
    public void initView() {
        initTopBar();
        String address = BaseApplication.getCurrentWallet().getAddress();
        presenter.getQrImage(address);
        walletAddressTextView.setText(address);
    }

    private void initTopBar(){
        int statusBarHeight = ScreenUtils.getStatusBarHeight(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(topBar.getLayoutParams());
        lp.topMargin = statusBarHeight;
        topBar.setLayoutParams(lp);
        topBar.setTitle(getString(R.string.receive_coins));
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
        topBar.setLeftView(null,R.mipmap.icon_back_arrow);
    }

    @OnClick({R.id.view_cover_receive_coins_activity, R.id.btn_copy_address, R.id.btn_save_qrcode ,R.id.ll_center_layout})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.view_cover_receive_coins_activity:
                finish();
                break;
            case R.id.btn_copy_address:
                CommonUtils.copyToClipboard(this, walletAddressTextView.getText().toString());
                ToastUtils.showToast(context, "已复制到剪贴板");
                break;
            case R.id.btn_save_qrcode:
                if (!CommonUtils.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_WRITE_FILE_CODE)) {
                    return;
                }
                presenter.saveImage();
                break;
            case R.id.ll_center_layout:
                break;
        }
    }

//    @Override
//    protected void setStatusBarFullTransparent() {
//        if (Build.VERSION.SDK_INT >= 21) {
//            //21表示5.0W
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            //19表示4.4
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//虚拟键盘也透明
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//    }

    /**
     * 请求手机弹出权限框后，用户点击是否授权的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("rrt", "requestCode=======================" + requestCode);
        presenter.onRequestPermissionsResult(requestCode,permissions,grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setQrImage(Bitmap bitmap) {
        qrcodeImage.setImageBitmap(bitmap);
    }

//    @Override
//    public void sendBroadcast(File file) {
//        //用广播通知相册进行更新相册
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri uri = Uri.fromFile(file);
//        intent.setData(uri);
//        sendBroadcast(intent);
//    }

}
