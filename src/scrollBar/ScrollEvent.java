package scrollBar;

public class ScrollEvent {
	String source;
	int viewChange;
	ScrollEvent(String ID, int viewChanged) {
		this.source = ID;
		this.viewChange = viewChanged;
	}
	public String getSource() {
		return source;
	}
	public int getViewChanged() {
		return viewChange;
	}
}
