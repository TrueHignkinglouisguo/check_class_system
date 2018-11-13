package Method;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
public class GetImageUri {
    public static Bitmap getimguri(Activity activity,String id){
        Bitmap bitmap=null;
        File CropPhoto = new File(Environment.getExternalStorageDirectory(), id+".jpg");
        if (CropPhoto.exists()) {
            Uri imageUri= Uri.fromFile(CropPhoto);
            try {
                bitmap = BitmapFactory.decodeStream(activity.getContentResolver()
                        .openInputStream(imageUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
