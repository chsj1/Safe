package com.njupt.safe.activity;

import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.njupt.safe.R;
import com.njupt.safe.utils.BDVoiceConfig;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;

/**
 * Demo调整识别参数使用，开发者不须关注
 * 
 * @author yangliang02
 */
public class BDVoiceSettingActivity extends Activity implements OnCheckedChangeListener {

    private Spinner voiceTypeSpinner;

    private Spinner dialogThemeSpinner;

    private CheckBox startSoundCheckBox;

    private CheckBox endSoundCheckBox;

    private CheckBox showVolCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bdvoicesetting);

        startSoundCheckBox = (CheckBox) findViewById(R.id.cb_play_start_sound);
        startSoundCheckBox.setChecked(BDVoiceConfig.PLAY_START_SOUND);
        startSoundCheckBox.setOnCheckedChangeListener(this);
        endSoundCheckBox = (CheckBox) findViewById(R.id.cb_play_end_sound);
        endSoundCheckBox.setChecked(BDVoiceConfig.PLAY_END_SOUND);
        endSoundCheckBox.setOnCheckedChangeListener(this);
        showVolCheckBox = (CheckBox) findViewById(R.id.cb_show_vol);
        showVolCheckBox.setChecked(BDVoiceConfig.SHOW_VOL);
        showVolCheckBox.setOnCheckedChangeListener(this);

        voiceTypeSpinner = (Spinner) this.findViewById(R.id.voiceType);
        voiceTypeSpinner.setSelection(BDVoiceConfig.VOICE_TYPE);
        voiceTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            	BDVoiceConfig.VOICE_TYPE = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        int selection = 0;
        switch (BDVoiceConfig.DIALOG_THEME) {
            case BaiduASRDigitalDialog.THEME_BLUE_DEEPBG:
                selection = 0;
                break;
            case BaiduASRDigitalDialog.THEME_BLUE_LIGHTBG:
                selection = 1;
                break;
            case BaiduASRDigitalDialog.THEME_GREEN_DEEPBG:
                selection = 2;
                break;
            case BaiduASRDigitalDialog.THEME_GREEN_LIGHTBG:
                selection = 3;
                break;
            case BaiduASRDigitalDialog.THEME_ORANGE_DEEPBG:
                selection = 4;
                break;
            case BaiduASRDigitalDialog.THEME_ORANGE_LIGHTBG:
                selection = 5;
                break;
            case BaiduASRDigitalDialog.THEME_RED_DEEPBG:
                selection = 6;
                break;
            case BaiduASRDigitalDialog.THEME_RED_LIGHTBG:
                selection = 7;
                break;

            default:
                break;
        }
        dialogThemeSpinner = (Spinner) this.findViewById(R.id.dialogTheme);
        dialogThemeSpinner.setSelection(selection);
        dialogThemeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int result = BaiduASRDigitalDialog.THEME_BLUE_DEEPBG;
                switch (position) {
                    case 0:
                        result = BaiduASRDigitalDialog.THEME_BLUE_DEEPBG;
                        break;
                    case 1:
                        result = BaiduASRDigitalDialog.THEME_BLUE_LIGHTBG;
                        break;
                    case 2:
                        result = BaiduASRDigitalDialog.THEME_GREEN_DEEPBG;
                        break;
                    case 3:
                        result = BaiduASRDigitalDialog.THEME_GREEN_LIGHTBG;
                        break;
                    case 4:
                        result = BaiduASRDigitalDialog.THEME_ORANGE_DEEPBG;
                        break;
                    case 5:
                        result = BaiduASRDigitalDialog.THEME_ORANGE_LIGHTBG;
                        break;
                    case 6:
                        result = BaiduASRDigitalDialog.THEME_RED_DEEPBG;
                        break;
                    case 7:
                        result = BaiduASRDigitalDialog.THEME_RED_LIGHTBG;
                        break;

                    default:
                        break;
                }
                BDVoiceConfig.DIALOG_THEME = result;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == showVolCheckBox) {
            BDVoiceConfig.SHOW_VOL = isChecked;
        }
        if (buttonView == startSoundCheckBox) {
        	BDVoiceConfig.PLAY_START_SOUND = isChecked;
        }
        if (buttonView == endSoundCheckBox) {
        	BDVoiceConfig.PLAY_END_SOUND = isChecked;
        }
    }
}
