///*******************************************************************************
// * Copyright 2011-2014 Sergey Tarasevich
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *******************************************************************************/
//package fragments;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.provider.SyncStateContract;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//import android.support.v4.app.Fragment;
//
//import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
//import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
//import com.nostra13.universalimageloader.utils.StorageUtils;
//
//import java.io.File;
//
//import helix.profitkey.hotelapp.R;
////import com.nostra13.universalimageloader.sample.Constants;
////import com.nostra13.universalimageloader.sample.R;
//
///**
// * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
// */
//public class ImagePagerFragment extends Fragment {
//
//	public static final int INDEX = 2;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.fr_image_pager, container, false);
//		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
//		pager.setAdapter(new ImageAdapter(getActivity()));
//		pager.setCurrentItem(0);
//		return rootView;
//	}
//
//	private static class ImageAdapter extends PagerAdapter {
//
//		private static final String[] IMAGE_URLS = {"http://icons.iconarchive.com/icons/fasticon/essential-toolbar/32/small-icons-icon.png",
//				"http://icons.iconarchive.com/icons/robinweatherall/recycling/128/bin-small-icon.png",
//				"http://icons.iconarchive.com/icons/oxygen-icons.org/oxygen/128/Emotes-face-smile-icon.png",
//				"https://www.creativefreedom.co.uk/wp-content/uploads/2013/03/00-android-4-0_icons.png"};;
//
//		private LayoutInflater inflater;
//		private DisplayImageOptions options;
//
//		ImageAdapter(Context context) {
//			inflater = LayoutInflater.from(context);
//
//			ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
//
//			options = new DisplayImageOptions.Builder()
//					.showImageForEmptyUri(R.drawable.lock)
//					.showImageOnFail(R.drawable.lock)
//					.resetViewBeforeLoading(true)
//					.cacheOnDisk(true)
//					.imageScaleType(ImageScaleType.EXACTLY)
//					.bitmapConfig(Bitmap.Config.RGB_565)
//					.considerExifParams(true)
//					.displayer(new FadeInBitmapDisplayer(300))
//					.build();
//
////			File cacheDir = StorageUtils.getCacheDirectory(context);
////			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
////					.memoryCacheExtraOptions(480, 800) // default = device screen dimensions
////					//.discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75)
////					.taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
////					.taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)
////					.threadPoolSize(3) // default
////					.threadPriority(Thread.NORM_PRIORITY - 1) // default
////					.tasksProcessingOrder(QueueProcessingType.FIFO) // default
////					.denyCacheImageMultipleSizesInMemory()
////					.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // default
////					.memoryCacheSize(2 * 1024 * 1024)
////					//.discCache(new UnlimitedDiscCache(cacheDir)) // default
////					.discCacheSize(50 * 1024 * 1024)
////					.discCacheFileCount(100)
////					.discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
////					.imageDownloader(new BaseImageDownloader(context)) // default
////					.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
////					//.enableLogging()
////					.build();
//		}
//
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object) {
//			container.removeView((View) object);
//		}
//
//		@Override
//		public int getCount() {
//			return IMAGE_URLS.length;
//		}
//
//		@Override
//		public Object instantiateItem(ViewGroup view, int position) {
//			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
//			assert imageLayout != null;
//			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
//			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
//
//			ImageLoader.getInstance().displayImage(IMAGE_URLS[position], imageView, options, new SimpleImageLoadingListener() {
//				@Override
//				public void onLoadingStarted(String imageUri, View view) {
//					spinner.setVisibility(View.VISIBLE);
//				}
//
//				@Override
//				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//					String message = null;
//					switch (failReason.getType()) {
//						case IO_ERROR:
//							message = "Input/Output error";
//							break;
//						case DECODING_ERROR:
//							message = "Image can't be decoded";
//							break;
//						case NETWORK_DENIED:
//							message = "Downloads are denied";
//							break;
//						case OUT_OF_MEMORY:
//							message = "Out Of Memory error";
//							break;
//						case UNKNOWN:
//							message = "Unknown error";
//							break;
//					}
//					Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
//
//					spinner.setVisibility(View.GONE);
//				}
//
//				@Override
//				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//					spinner.setVisibility(View.GONE);
//				}
//			});
//
//			view.addView(imageLayout, 0);
//			return imageLayout;
//		}
//
//		@Override
//		public boolean isViewFromObject(View view, Object object) {
//			return view.equals(object);
//		}
//
//		@Override
//		public void restoreState(Parcelable state, ClassLoader loader) {
//		}
//
//		@Override
//		public Parcelable saveState() {
//			return null;
//		}
//	}
//}