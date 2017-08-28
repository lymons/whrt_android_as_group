package com.mieasy.whrt_app_android_4.act.news;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.R.string;
import com.mieasy.whrt_app_android_4.util.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	private Bitmap bitmapImg;
	private View v;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();
		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});

		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = getResources().getString(string.download_error);
					break;
				case DECODING_ERROR:
					message = getResources().getString(string.pic_notShow);
					break;
				case NETWORK_DENIED:
					message = getResources().getString(string.network_error);
					break;
				case OUT_OF_MEMORY:
					message = getResources().getString(string.pic_sobig);
					break;
				case UNKNOWN:
					message = getResources().getString(string.other_error);
					break;
				}
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressBar.setVisibility(View.GONE);
				if(loadedImage!=null){
					bitmapImg = loadedImage;
				}
				mAttacher.update();
			}
		});
	}
	
	public void saveBitmapToSD() {
		if(bitmapImg!=null){
			BitmapUtil.saveImageToGallery(getActivity(), bitmapImg);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(!bitmapImg.isRecycled()){
			bitmapImg.recycle();
		}
	}
}