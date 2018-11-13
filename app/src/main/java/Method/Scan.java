package Method;
import android.support.v4.app.Fragment;

import com.example.louis.theclass.ScanActivity;
import com.google.zxing.integration.android.IntentIntegrator;
public class Scan{
    public static void getscan_fragment(Fragment fragment){
        IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(fragment).setCaptureActivity(ScanActivity.class);
        intentIntegrator
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt("将一维码/二维码放入框内，即可自动扫描")//写那句提示的话
                .setOrientationLocked(false)//扫描方向固定
                .initiateScan(); // 初始化扫描*/
    }
}
