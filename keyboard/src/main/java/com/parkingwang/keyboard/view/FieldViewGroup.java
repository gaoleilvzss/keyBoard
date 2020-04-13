package com.parkingwang.keyboard.view;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import com.parkingwang.keyboard.AppGlobals;
import com.parkingwang.vehiclekeyboard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈小锅 (yoojiachen@gmail.com)
 */
abstract class FieldViewGroup {

    private static final String TAG = "InputView.ButtonGroup";

    private final Button[] mFieldViews = new Button[9];
    private final Button[] mFieldViews_delete = new Button[8];

    public FieldViewGroup() {
        final int[] resIds = new int[]{
                R.id.number_0,
                R.id.number_1,
                R.id.number_2,
                R.id.number_3,
                R.id.number_4,
                R.id.number_5,
                R.id.number_6,
                R.id.number_7,
                R.id.number_8
        };
        for (int i = 0; i < resIds.length; i++) {
            mFieldViews[i] = findViewById(resIds[i]);
            mFieldViews[i].setTag("[RAW.idx:" + i + "]");
        }
        for (int i = 0; i < resIds.length - 1; i++) {
            mFieldViews_delete[i] = findViewById(resIds[i]);
            mFieldViews_delete[i].setTag("[RAW.idx:" + i + "]");
        }
        // 默认时，显示8位
        changeTo8Fields();
    }

    protected abstract Button findViewById(int id);

    public void setTextToFields(String text) {
        // cleanup
        for (Button f : mFieldViews) {
            f.setText(null);
        }

        final char[] chars = text.toCharArray();
        if (chars.length >= 8) {
            changeTo8Fields();
        } else {
            changeTo7Fields();
        }
        // 显示到对应键位
        final Button[] fields = getAvailableFields();
        for (int i = 0; i < fields.length; i++) {
            final String txt;
            if (i < chars.length) {
                txt = String.valueOf(chars[i]);
            } else {
                txt = null;
            }
            fields[i].setText(txt);
        }
    }

    public Button[] getAvailableFields() {
        final List<Button> output = new ArrayList<>(8);
        final int lastIndex = mFieldViews_delete.length - 1;
        Button fieldView;
        for (int i = 0; i < mFieldViews_delete.length; i++) {
            fieldView = mFieldViews_delete[i];
            if (i != lastIndex || fieldView.getVisibility() == View.VISIBLE) {
                output.add(fieldView);
            }
        }
        return output.toArray(new Button[output.size()]);
    }

    public Button getFieldAt(int index) {
        return mFieldViews[index];
    }

    public boolean changeTo7Fields() {
        if (mFieldViews[7].getVisibility() != View.VISIBLE) {
            return false;
        }
        mFieldViews[7].setVisibility(View.GONE);
        mFieldViews[7].setText(null);
        mFieldViews[8].setVisibility(View.VISIBLE);
        mFieldViews[8].setText("+\r\n新能源");
        return true;
    }

    public boolean changeTo8Fields() {
        if (mFieldViews[7].getVisibility() == View.VISIBLE) {
            return false;
        }
        mFieldViews[7].setVisibility(View.VISIBLE);
        mFieldViews[7].setText(null);
        mFieldViews[8].setVisibility(View.GONE);
        return true;
    }

    public Button getLastField() {
        if (mFieldViews[7].getVisibility() == View.VISIBLE) {
            return mFieldViews[7];
        } else {
            return mFieldViews[6];
        }
    }

    public Button getFirstSelectedFieldOrNull() {
        for (Button field : getAvailableFields()) {
            if (field.isSelected()) {
                return field;
            }
        }
        return null;
    }

    public Button getLastFilledFieldOrNull() {
        final Button[] fields = getAvailableFields();
        for (int i = fields.length - 1; i >= 0; i--) {
            if (!TextUtils.isEmpty(fields[i].getText())) {
                return fields[i];
            }
        }
        return null;
    }

    public Button getFirstEmptyField() {
        final Button[] fields = getAvailableFields();
        Button out = fields[0];
        for (Button field : fields) {
            out = field;
            final CharSequence keyTxt = field.getText();
            if (TextUtils.isEmpty(keyTxt)) {
                break;
            }
        }
        Log.d(TAG, "[-- CheckEmpty --]: Btn.idx: " + out.getTag() + ", Btn.text: " + out.getText() + ", Btn.addr: " + out);
        return out;
    }

    public int getNextIndexOfField(Button target) {
        final Button[] fields = getAvailableFields();
        for (int i = 0; i < fields.length; i++) {
            if (target == fields[i]) {
                return Math.min(fields.length - 1, i + 1);
            }
        }
        return 0;
    }

    public boolean isAllFieldsFilled() {
        for (Button field : getAvailableFields()) {
            if (TextUtils.isEmpty(field.getText())) {
                return false;
            }
        }
        return true;
    }

    public String getText() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getAvailableFields().length - 1; i++) {
            sb.append(getAvailableFields()[i].getText());
        }
        return sb.toString();
    }

    public void setupAllFieldsTextSize(float size, float size_9) {
        for (int i = 0; i < mFieldViews.length - 1; i++) {
            mFieldViews[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
        mFieldViews[8].setTextSize(TypedValue.COMPLEX_UNIT_PX, size_9);
    }

    public void setAllButtonBackgroundColor(int color) {
        for (Button mFieldView : mFieldViews) {
            mFieldView.setBackgroundColor(color);
        }
    }

    public void setAllButtonTextColor(int color) {
        for (Button mFieldView : mFieldViews) {
            mFieldView.setTextColor(AppGlobals.get().getResources().getColor(color));
        }
    }

    public void setupAllFieldsOnClickListener(View.OnClickListener listener, View.OnClickListener listener_9) {
        for (int i = 0; i < mFieldViews.length - 1; i++) {
            mFieldViews[i].setOnClickListener(listener);
        }
        mFieldViews[8].setOnClickListener(listener_9);
    }

    public Button get9Button() {
        return mFieldViews[8];
    }
}
