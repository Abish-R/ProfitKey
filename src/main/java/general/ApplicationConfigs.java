package general;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 *  ProfitKey v1.0
 * 	Purpose	   : Set custom fonts in application level
 *  Created by : Abish
 *  Created Dt : 5/4/2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
public class ApplicationConfigs extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /** Change the navigation drawer fonts**/
        setDefaultFont(this, "MONOSPACE", "fonts/Montserrat-Bold.ttf");
    }

        private Typeface normalFont;
        private Typeface boldFont;

    /**Set custom font regular in TextView**/
    public void setTypefaceTextView(TextView textView) {
        textView.setTypeface(getNormalFont());
    }
    /**Set custom font bold in TextView**/
    public void setTypefaceTextViewBold(TextView textView) {
        textView.setTypeface(getBoldFont());
    }

    /**Set custom font regular in Button**/
    public void setTypefaceButton(Button button) {
        button.setTypeface(getNormalFont());
    }
    /**Set custom font bold in Button**/
    public void setTypefaceButtonBold(Button button) {
        button.setTypeface(getBoldFont());
    }

    /**Set custom font regular in EditText**/
    public void setTypefaceEditText(EditText button) {
        button.setTypeface(getNormalFont());
    }
    /**Set custom font bold in EditText**/
    public void setTypefaceEditTextBold(EditText button) {
        button.setTypeface(getBoldFont());
    }

    /**Set custom font regular in RadioButton**/
    public void setTypefaceRadio(RadioButton radio) {
        radio.setTypeface(getNormalFont());
    }
    /**Set custom font bold in RadioButton**/
    public void setTypefaceRadioBold(RadioButton radio) {
        radio.setTypeface(getBoldFont());
    }

    /**Get Custom font regular from resources folder**/
    private Typeface getNormalFont() {
        if(normalFont == null) {
            normalFont = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        }
        return this.normalFont;
    }
    /**Get Custom font bold from resources folder**/
    private Typeface getBoldFont() {
        if(boldFont == null) {
            boldFont = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        }
        return this.boldFont;
    }

    /**Set Custom font from resources folder for Navigation drawer**/
    public static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }
    /**Function to replace the fonts in navigation drawer**/
    protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
