package com.njupt.safe.activity;

import com.baidu.voicerecognition.android.Candidate;
import com.baidu.voicerecognition.android.VoiceRecognitionClient;
import com.baidu.voicerecognition.android.VoiceRecognitionClient.VoiceClientStatusChangeListener;
import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.njupt.safe.R;
import com.njupt.safe.utils.BDVoiceConfig;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 开发用例，含有一次语音识别的全过程。
 * 
 * @author zhanghongchi
 */
public class BDVoiceRecognitionActivity extends Activity implements
		OnClickListener {
//	private static final String API_KEY = "8MAxI5o7VjKSZOKeBzS4XtxO";这个是Demo中的AK
//	private static final String SECRET_KEY = "Ge5GXVdGQpaxOmLzc8fOM8309ATCz9Ha"; 这个是Demo中的SK
	private static final String API_KEY = "f7Q2MxcTmYdprGIsjewHVyfx";
	private static final String SECRET_KEY = "KWXUOoev1BXfbT6HCIw3bGIU0UCyeKpk";
	/** 取消按钮 */
	private Button mCancelButton = null;

	/** 完成和重试按钮 */
	private Button mFinishButton = null;

	/** 用于显示当前录音音量的条形bar */
	private ProgressBar mVolumeBar = null;

	/** 状态显示 */
	private TextView mStatusTextView = null;

	/**
	 * 结果展示
	 */
	private EditText mResult = null;

	/**
	 * 状态区域
	 */
	private View mStatusPanel = null;

	/** 正在识别中 */
	private boolean isRecognition = false;

	/** 主线程Handler */
	private Handler mHandler;

	/** 音量更新间隔 */
	private static final int POWER_UPDATE_INTERVAL = 100;

	/**
	 * 当前识别模式
	 */
	private int currentVoiceType = BDVoiceConfig.VOICE_TYPE_SEARCH;

	/** 识别回调接口 */
	private MyVoiceRecogListener mListener = new MyVoiceRecogListener();

	private BaiduASRDigitalDialog mDialog = null;

	private DialogRecognitionListener mRecognitionListener;

	/**
	 * 音量更新任务
	 */
	private Runnable mUpdateVolume = new UpdateVolum();

	private VoiceRecognitionClient mClient;

	private int mCurrentTheme = BDVoiceConfig.DIALOG_THEME;

	//用来随机产生回答
	Random r;
	String[] strs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bdvoicedemo);
		mClient = VoiceRecognitionClient.getInstance(this);
		mClient.setTokenApis(API_KEY, SECRET_KEY);
		mHandler = new Handler();
		mCancelButton = (Button) this.findViewById(R.id.cancelButton);
		mCancelButton.setOnClickListener(this);
		mCancelButton.setEnabled(false);
		mFinishButton = (Button) this.findViewById(R.id.start);
		mFinishButton.setOnClickListener(this);
		mVolumeBar = (ProgressBar) this.findViewById(R.id.volumeProgressbar);
		mStatusTextView = (TextView) this.findViewById(R.id.statusBodyTextView);
		mStatusPanel = findViewById(R.id.status_panel);
		mResult = (EditText) findViewById(R.id.recognition_text);


		mRecognitionListener = new DialogRecognitionListener() {

			@Override
			public void onResults(Bundle results) {
				ArrayList<String> rs = results != null ? results
						.getStringArrayList(RESULTS_RECOGNITION) : null;
				if (rs != null && rs.size() > 0) {
					mResult.setText(rs.get(0));
				}

			}
		};

	    r = new Random();
		strs = new String[]{
				"你是问我你是谁吗???我哪知道你是谁....不过我听过一个叫王烨的二货...",
				"别老问这个问题好吗????",
				"你好烦~~~~~",
				"好吧,告诉你,其实我还认识一个叫王硕的帅小伙....",
				"我困了,要去睡觉了~~~~"
		};
	}

	@Override
	protected void onPause() {
		if (isRecognition) {
			mClient.stopVoiceRecognition(); // 取消识别
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		VoiceRecognitionClient.releaseInstance(); // 释放识别库
		super.onDestroy();
	}

	/**
	 * 重写用于处理语音识别回调的监听器
	 * 
	 * @author zhanghongchi
	 */
	class MyVoiceRecogListener implements VoiceClientStatusChangeListener {

		@Override
		public void onClientStatusChange(int status, Object obj) {
			switch (status) {
			// 语音识别实际开始，这是真正开始识别的时间点，需在界面提示用户说话。
			case VoiceRecognitionClient.CLIENT_STATUS_START_RECORDING:
				isRecognition = true;
				mVolumeBar.setVisibility(View.VISIBLE);
				mFinishButton.setEnabled(true);
				mFinishButton.setText(R.string.tofinish);
				mStatusTextView.setText(R.string.please_speak);
				mHandler.removeCallbacks(mUpdateVolume);
				mHandler.postDelayed(mUpdateVolume, POWER_UPDATE_INTERVAL);
				break;
			case VoiceRecognitionClient.CLIENT_STATUS_SPEECH_START: // 检测到语音起点
				mStatusTextView.setText(R.string.speaking);
				break;
			// 已经检测到语音终点，等待网络返回
			case VoiceRecognitionClient.CLIENT_STATUS_SPEECH_END:
				mStatusTextView.setText(R.string.in_recog);
				mFinishButton.setEnabled(false);
				mVolumeBar.setVisibility(View.INVISIBLE);
				break;
			// 语音识别完成，显示obj中的结果
			case VoiceRecognitionClient.CLIENT_STATUS_FINISH:
				mStatusTextView.setText(null);
				updateRecognitionResult(obj);
				isRecognition = false;
				resetUI();
				break;
			// 处理连续上屏
			case VoiceRecognitionClient.CLIENT_STATUS_UPDATE_RESULTS:
				updateRecognitionResult(obj);
				break;
			// 用户取消
			case VoiceRecognitionClient.CLIENT_STATUS_USER_CANCELED:
				mStatusTextView.setText(R.string.is_canceled);
				isRecognition = false;
				resetUI();
				break;
			default:
				break;
			}

		}

		@Override
		public void onError(int errorType, int errorCode) {
			isRecognition = false;
			mResult.setText(getString(R.string.error_occur,
					Integer.toHexString(errorCode)));
			resetUI();
		}

		@Override
		public void onNetworkStatusChange(int status, Object obj) {
			// 这里不做任何操作不影响简单识别
		}

	}

	/**
	 * 将识别结果更新到UI上，搜索模式结果类型为List<String>,输入模式结果类型为List<List<Candidate>>
	 * 
	 * @param result
	 */
	private void updateRecognitionResult(Object result) {
		if (result != null && result instanceof List) {
			List results = (List) result;
			if (results.size() > 0) {
				if (currentVoiceType == BDVoiceConfig.VOICE_TYPE_SEARCH) {
					
					String text = results.get(0).toString();
					
					
					if (text.equals("开启锁屏功能")) {
						Toast.makeText(getApplicationContext(), "你开启了锁屏功能...",
								1).show();
					} else if (text.equals("打电话")) {
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_DIAL);
						// intent.setData(Uri.parse("tel:" + number));
						startActivity(intent);
					} else if (text.equals("启动照相机")) {
						Intent intent = new Intent(
								"android.media.action.STILL_IMAGE_CAMERA");
						startActivity(intent);
					} else if (text.equals("启动音乐播放器")) {//
//						Intent intent = new Intent(
//								"android.intent.action.MUSIC_PLAYER");
//						startActivity(intent);
						
					} else if (text.equals("启动图片列表")) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setType("vnd.android.cursor.dir/image");// 图片列表
						startActivity(intent);
					} else if (text.equals("启动视频列表")) {//
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setType("vnd.android.cursor.dir/video");// 视频列表
						startActivity(intent);
					} else if (text.equals("打开短信列表")) {
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_DEFAULT);
						intent.setType("vnd.android-dir/mms-sms");
						startActivity(intent);
					} else if (text.equals("打开通话记录")) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setType("vnd.android.cursor.dir/calls");
						startActivity(intent);
					} else if (text.equals("打开壁纸设置")) {
						Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
						startActivity(intent);
					} else if (text.equals("打开设置主界面")) {
						Intent intent = new Intent(
								android.provider.Settings.ACTION_SETTINGS); // 系统设置
						startActivityForResult(intent, 0);
					} else if (text.equals("返回桌面")) {
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.addCategory(Intent.CATEGORY_HOME);
						startActivity(intent);
					} else if(text.equals("打开录音机")){
						Intent mi = new Intent(Media.RECORD_SOUND_ACTION); 
			            startActivity(mi); 
					} else if(text.equals("显示应用详细列表")){
						Uri uri = Uri.parse("market://details?id=com.njupt.safe");         
						Intent it = new Intent(Intent.ACTION_VIEW, uri);         
						startActivity(it);  
					}else if(text.equals("上网")){
						Uri uri = Uri.parse("http://www.google.com"); 
						Intent it  = new Intent(Intent.ACTION_VIEW,uri); 
						startActivity(it); 
					}else if(text.equals("规划路径")){
						Uri uri = Uri.parse("http://maps.google.com/maps?f=dsaddr=startLat%20startLng&daddr=endLat%20endLng&hl=en"); 
						Intent it = new Intent(Intent.ACTION_VIEW,uri); 
						startActivity(it); 
					}else if(text.equals("当前位置")){
						Intent intent = new Intent(this,LocationActivity.class);
					    startActivity(intent);
					}else if(text.equals("我是谁")){
						mResult.setEnabled(false);
						mResult.setText(strs[r.nextInt(5)]);
					}else if(text.equals("我爱你")){
						mResult.setEnabled(false);
						mResult.setText("你是想说你爱我吗????但是我只爱那个英俊无比的超级无敌帅气的黄俊东。可是英俊无比的俊东大神已经有喜欢的人了....");
					}else if(text.equals("我是周晓华")){
						mResult.setEnabled(false);
						mResult.setText("晓华,我知道,或许我配不上你,但是我是真的喜欢你,我是认真的。或许你有很多的顾虑,如果有什么顾虑能和我说一下吗???只要真心在一起,又有什么是解决不了的呢...");
					
					}else{
						mResult.setEnabled(false);
						mResult.setText(text);
					}

				} else if (currentVoiceType == BDVoiceConfig.VOICE_TYPE_INPUT) {
					List<List<Candidate>> sentences = (List<List<Candidate>>) result;
					StringBuffer sb = new StringBuffer();
					for (List<Candidate> candidates : sentences) {
						if (candidates != null && candidates.size() > 0) {
							sb.append(candidates.get(0).getWord());
						}
					}
					mResult.setEnabled(true);
					mResult.setText(sb.toString());
				}
			}
		}
	}

	/**
	 * 重置UI，并且终止更新音量的线程
	 */
	void resetUI() {
		mStatusPanel.setVisibility(View.GONE);
		mFinishButton.setEnabled(true); // 可以开始重试
		mFinishButton.setText(R.string.try_again);
		mCancelButton.setEnabled(false); // 还没开始不能取消
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelButton:
			mClient.stopVoiceRecognition();
			break;
		case R.id.start:
			if (isRecognition) { // 用户说完
				mClient.speakFinish();
			} else { // 用户重试，开始新一次语音识别
				mResult.setText(null);
				// 需要开始新识别,首先设置参数
				VoiceRecognitionConfig config = new VoiceRecognitionConfig();
				currentVoiceType = BDVoiceConfig.VOICE_TYPE;
				if (currentVoiceType == BDVoiceConfig.VOICE_TYPE_INPUT) {
					config.setSpeechMode(VoiceRecognitionConfig.SPEECHMODE_MULTIPLE_SENTENCE);
				} else {
					config.setSpeechMode(VoiceRecognitionConfig.SPEECHMODE_SINGLE_SENTENCE);
				}
				config.enableVoicePower(BDVoiceConfig.SHOW_VOL); // 音量反馈。
				if (BDVoiceConfig.PLAY_START_SOUND) {
					config.enableBeginSoundEffect(R.raw.bdspeech_recognition_start); // 设置识别开始提示音
				}
				if (BDVoiceConfig.PLAY_END_SOUND) {
					config.enableEndSoundEffect(R.raw.bdspeech_speech_end); // 设置识别结束提示音
				}

				// 下面发起识别
				int code = VoiceRecognitionClient.getInstance(this)
						.startVoiceRecognition(mListener, config);
				if (code == VoiceRecognitionClient.START_WORK_RESULT_WORKING) { // 能够开始识别，改变界面
					mFinishButton.setEnabled(false);
					mFinishButton.setText(R.string.tofinish);
					mCancelButton.setEnabled(true);
					mStatusPanel.setVisibility(View.VISIBLE);
				} else {
					mResult.setText(getString(R.string.error_start, code));

				}
			}
			break;
		case R.id.start_diolog:
			mResult.setText(null);
			if (mDialog == null || mCurrentTheme != BDVoiceConfig.DIALOG_THEME) {
				mCurrentTheme = BDVoiceConfig.DIALOG_THEME;
				if (mDialog != null) {
					mDialog.dismiss();
				}
				Bundle params = new Bundle();
				params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, API_KEY);
				params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY,
						SECRET_KEY);
				params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME,
						BDVoiceConfig.DIALOG_THEME);
				mDialog = new BaiduASRDigitalDialog(this, params);
				mDialog.setDialogRecognitionListener(mRecognitionListener);
			}
			mDialog.setSpeechMode(BDVoiceConfig.VOICE_TYPE == BDVoiceConfig.VOICE_TYPE_SEARCH ? BaiduASRDigitalDialog.SPEECH_MODE_SEARCH
					: BaiduASRDigitalDialog.SPEECH_MODE_INPUT);
			mDialog.show();
			break;
		case R.id.setting:
			Intent setting = new Intent(this, BDVoiceSettingActivity.class);
			startActivity(setting);
			break;
		default:
			break;
		}

	}

	class UpdateVolum implements Runnable {
		public void run() {
			if (isRecognition) {
				long vol = VoiceRecognitionClient.getInstance(
						BDVoiceRecognitionActivity.this)
						.getCurrentDBLevelMeter();
				mVolumeBar.setProgress((int) vol);
				mHandler.removeCallbacks(mUpdateVolume);
				mHandler.postDelayed(mUpdateVolume, POWER_UPDATE_INTERVAL);
			}
		}
	};
}
