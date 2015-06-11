/*package edu.hrbeu.myweather;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class PageControlView {

	private int count;


	private Context context;


	public void setCount(int count) {
	this.count = count;
	}


	public PageControlView(Context context, AttributeSet attrs) {
	super();
	this.init(context);
	}


	public PageControlView(Context context) {
	super();
	this.init(context);
	}


	private void init(Context context) {
	this.context=context;
	}



	public void callback(int currentIndex) {
	generatePageControl(currentIndex);
	}


	public void generatePageControl(int currentIndex) {
	this.removeAllViews();


	LayoutParams mParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	mParams.rightMargin=45;

	for (int i = 0; i < this.count; i++) {
	ImageView imageView = new ImageView(context);
	if (currentIndex == i) {
	imageView.setImageResource(R.drawable.page_indicator_focused);

	} else {
	imageView.setImageResource(R.drawable.page_indicator);
	}
	this.addView(imageView, mParams);
	}
	}


	private void removeAllViews() {
		// TODO Auto-generated method stub
		
	}
}
*/