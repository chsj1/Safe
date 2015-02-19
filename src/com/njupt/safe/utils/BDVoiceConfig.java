package com.njupt.safe.utils;

import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;

/**
 * 临时保存参数信息，Demo演示使用。开发者不须关注
 * 
 * @author yangliang02
 */
public class BDVoiceConfig {

    /** 语音搜索模式 */
    public static int VOICE_TYPE = BDVoiceConfig.VOICE_TYPE_SEARCH;

    /** 对话框样式 */
    public static int DIALOG_THEME = BaiduASRDigitalDialog.THEME_BLUE_LIGHTBG;

    /** 语音搜索模式 */
    public static final int VOICE_TYPE_SEARCH = 0;

    /** 语音输入模式 */
    public static final int VOICE_TYPE_INPUT = 1;

    /**
     * 播放开始音
     */
    public static boolean PLAY_START_SOUND = true;

    /**
     * 播放结束音
     */
    public static boolean PLAY_END_SOUND = true;

    /**
     * 显示音量
     */
    public static boolean SHOW_VOL = true;
}
