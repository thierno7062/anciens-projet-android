package com.raj.sqlitetest;

public class NewListener {
	static PostListener postListener;
	public static void addNewListener(PostListener _listener) {
		NewListener.postListener = _listener;
	}
	public PostListener getListener() {
		return postListener;
	}
	public interface PostListener {
		public void madePost(int index);
	}
	public static void posted(int val1){
		postListener.madePost(val1);
	}
}