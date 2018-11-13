package MyTools;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
public class GetVersion {
    public static String getversion(Context context) throws Exception
    {
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }
}
