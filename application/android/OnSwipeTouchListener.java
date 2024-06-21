package com.soiadmahedi.suicTh;

import android.content.Context;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;

public abstract class OnSwipeTouchListener implements View.OnTouchListener {

	private final GestureDetectorCompat gestureDetector;
	private final GestureListener gestureListener;


	protected OnSwipeTouchListener(Context context){
		gestureListener = new GestureListener();
		gestureDetector = new GestureDetectorCompat(context, gestureListener);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		gestureListener.onTouchEvent(event);
		return true;
	}
	public boolean onSwipeRight() {
		return true;
	}
	public boolean onSwipeLeft() {
		return true;
	}

    /**
    Soiad Mahedi 
    From Bangladesh 

    Soiad Mahedi's Profiles 
    Facebook : https://www.facebook.com/soiadmahediofficial
    Instagram : https://www.instagram.com/soiadmahedi
    Twitter : https://www.twitter.com/soiadmahedi
    LinkedIn : https://www.linkedin.com/in/soiadmahedi  

    */

	public boolean onSwipeTop() {
		return true;
	}

	public boolean onSwipeBottom() {
		return true;
	}

	public boolean onDoubleTap() {
		return true;
	}

	public boolean onSingleTap() {
		return true;
	}
	public abstract void onGestureDone();
	public abstract void adjustBrightness(double adjustPercent);
	public abstract void adjustVolumeLevel(double adjustPercent);
	public abstract void adjustVideoPosition(double adjustPercent, boolean forwardDirection);
	public abstract Rect viewRect();

	private enum SwipeEventType {
		NONE,
		BRIGHTNESS,
		VOLUME,
		SEEK,
		COMMENTS,
		DESCRIPTION
	}

    /**
    Soiad Mahedi 
    From Bangladesh 

    Soiad Mahedi's Profiles 
    Facebook : https://www.facebook.com/soiadmahediofficial
    Instagram : https://www.instagram.com/soiadmahedi
    Twitter : https://www.twitter.com/soiadmahedi
    LinkedIn : https://www.linkedin.com/in/soiadmahedi  

    Line : 4000

    */

	private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

		private static final int SWIPE_THRESHOLD = 50;
		private SwipeEventType swipeEventType = SwipeEventType.NONE;
		private MotionEvent startEvent;

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			return OnSwipeTouchListener.this.onDoubleTap();
		}


		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			return onSingleTap();
		}


		@Override
		public boolean onScroll(MotionEvent start, MotionEvent end,
								float distanceX, float distanceY) {

			double yDistance = end.getY() - start.getY();
			double xDistance = end.getX() - start.getX();

			if (swipeEventType == SwipeEventType.COMMENTS) {
				return onSwipeLeft();
			} else if (swipeEventType == SwipeEventType.DESCRIPTION) {
				return onSwipeTop();
			} else if (swipeEventType == SwipeEventType.BRIGHTNESS) {

				double percent = yDistance / (getBrightnessRect().height() / 2.0f) * -1.0f;
				adjustBrightness(percent);
			} else if (swipeEventType == SwipeEventType.VOLUME) {
				// use half of volumeRect's height to calculate percent.
				double percent = yDistance / (getVolumeRect().height() / 2.0f) * -1.0f;
				adjustVolumeLevel(percent);
			} else if (swipeEventType == SwipeEventType.SEEK) {
				double percent = xDistance / viewRect().width();
				adjustVideoPosition(percent, distanceX < 0);
			}

			return true;
		}

		private boolean isEventValid(MotionEvent event) {

			return event.getPointerCount() == 1;
		}

		private void onTouchEvent(MotionEvent event) {
			if (isEventValid(event)) {

				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						startEvent = MotionEvent.obtain(event);
						break;
					case MotionEvent.ACTION_MOVE:
						if (swipeEventType == SwipeEventType.NONE && startEvent != null) {
							swipeEventType = whatTypeIsIt(startEvent, event);
						}
						break;
					case MotionEvent.ACTION_UP:
						onGestureDone();
						swipeEventType = SwipeEventType.NONE;
						startEvent = null;
						break;
					default:
						break;
				}
			}

			gestureDetector.onTouchEvent(event);
		}


		/**
		 * Ok, swipe detected. What did user want? Let's find out
		 */
		private SwipeEventType whatTypeIsIt(MotionEvent startEvent, MotionEvent currentEvent) {
			float startX = startEvent.getX();
			float startY = startEvent.getY();
			float currentX = currentEvent.getX();
			float currentY = currentEvent.getY();
			float diffY = startY - currentY;
			float diffX = startX - currentX;
			
        /**
        Soiad Mahedi     
        From Bangladesh 

        Soiad Mahedi's Profiles 
        Facebook : https://www.facebook.com/soiadmahediofficial
        Instagram : https://www.instagram.com/soiadmahedi
        Twitter : https://www.twitter.com/soiadmahedi
        LinkedIn : https://www.linkedin.com/in/soiadmahedi  

        Line : 4034

        */

			if (Math.abs(currentX - startX) >= SWIPE_THRESHOLD && Math.abs(currentX - startX) > Math.abs(currentY - startY)) {
				if (getCommentsRect().contains((int) startX, (int) startY) && diffX > 0)
					return SwipeEventType.COMMENTS;

				return SwipeEventType.SEEK;
			} else if (Math.abs(currentY - startY) >= SWIPE_THRESHOLD) {
				if (getDescriptionRect().contains((int) startX, (int) startY) && diffY > 0) {
					return SwipeEventType.DESCRIPTION;
				} else if (getBrightnessRect().contains((int) startX, (int) startY)) {
					return SwipeEventType.BRIGHTNESS;
				} else if (getVolumeRect().contains((int) startX, (int) startY)) {
					return SwipeEventType.VOLUME;
				}
			}
			return SwipeEventType.NONE;
		}


		private Rect getBrightnessRect() {
			return new Rect(0, 0, viewRect().right / 2, viewRect().bottom);
		}


		private Rect getVolumeRect() {
			return new Rect(viewRect().right / 2, 0, viewRect().right, viewRect().bottom);
		}


		private Rect getCommentsRect() {
			return new Rect((int)(viewRect().right - Math.min(viewRect().bottom, viewRect().right) * 0.2), 0, viewRect().right, viewRect().bottom);
		}


		private Rect getDescriptionRect() {
			return new Rect(0, (int)(viewRect().bottom - Math.min(viewRect().bottom, viewRect().right) * 0.2), viewRect().right, viewRect().bottom);
		}

	}

}