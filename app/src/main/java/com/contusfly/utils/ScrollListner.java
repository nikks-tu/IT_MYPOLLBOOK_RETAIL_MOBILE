package com.contusfly.utils;

import androidx.recyclerview.widget.RecyclerView;


/**
 * The Class ScrollListner.
 */
public abstract class ScrollListner extends RecyclerView.OnScrollListener {

	/** The Constant HIDE_THRESHOLD. */
	private static final int HIDE_THRESHOLD = 20;

	/** The scrolled distance. */
	private int scrolledDistance = 0;

	/** The controls visible. */
	private boolean controlsVisible = true;

	/**
	 * On scrolled.
	 *
	 * @param recyclerView
	 *            the recycler view
	 * @param dx
	 *            the dx
	 * @param dy
	 *            the dy
	 */
	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		super.onScrolled(recyclerView, dx, dy);

		if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
			onHide();
			controlsVisible = false;
			scrolledDistance = 0;
		} else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
			onShow();
			controlsVisible = true;
			scrolledDistance = 0;
		}

		if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
			scrolledDistance += dy;
		}
	}

	/**
	 * On hide.
	 */
	public abstract void onHide();

	/**
	 * On show.
	 */
	public abstract void onShow();

}