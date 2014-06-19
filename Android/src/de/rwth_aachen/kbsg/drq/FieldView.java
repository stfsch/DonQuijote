package de.rwth_aachen.kbsg.drq;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.rwth_aachen.kbsg.dq.Color;
import de.rwth_aachen.kbsg.dq.Phase;
import de.rwth_aachen.kbsg.dq.Point;
import de.rwth_aachen.kbsg.dq.State;
import de.rwth_aachen.kbsg.dq.UI;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class FieldView extends View implements UI {
	private static class PointInfo {
		public Paint paint;
		public boolean selected = false;
		public float x;
		public float y;
		
		public PointInfo(Paint paint, float x, float y) {
			this.paint = paint;
			this.x = x;
			this.y = y;
		}
	}
	
	private Paint bgPaint;
	private Paint fieldPaint;
	private Paint selectedPaint;
	private Paint whitePaint;
	private Paint blackPaint;
	
	private float width;
	private float height;
	private float dist;
	private float radius;
	private float minPointDist = Float.MAX_VALUE;
	
	private Map<Point, PointInfo> points = new HashMap<Point, PointInfo>();
	private Collection<Pair<Point, Point>> lines = new Vector<Pair<Point, Point>>();
	private BlockingQueue<Point> selection = new LinkedBlockingQueue<Point>(1);
	
	private Phase phase = Phase.OCCUPY;
	private Color active = Color.WHITE;
	private State state = new State();
	
    public FieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(getResources().getColor(R.color.background));

        fieldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fieldPaint.setStyle(Paint.Style.STROKE);
        fieldPaint.setColor(getResources().getColor(R.color.field));

        selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setColor(getResources().getColor(R.color.selected));
        selectedPaint.setStrokeWidth(5.0f);

        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setTextAlign(Paint.Align.CENTER);
        whitePaint.setTextSize(30.0f);
        whitePaint.setColor(getResources().getColor(R.color.white));

        blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackPaint.setStyle(whitePaint.getStyle());
		blackPaint.setTextAlign(whitePaint.getTextAlign());
        blackPaint.setTextSize(whitePaint.getTextSize());
        blackPaint.setColor(getResources().getColor(R.color.black));
    }

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
		dist = Math.min(width, height) * 85 / 100 / 2;
		radius = dist / 15;

		for (Point p : state.pointsOfField()) {
			Paint paint = state.isOccupiedBy(p, Color.BLACK) ? blackPaint : state.isOccupiedBy(p, Color.WHITE) ? whitePaint : null;
			points.put(p, new PointInfo(paint, toX(p), toY(p)));
		}
		
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 8; ++j) {
				Point p = new Point(i, j);
				Point q = new Point(i, (j+1) % 8);
				lines.add(new Pair<Point, Point>(p, q));
			}
		}
		for (int i = 0; i < 3-1; ++i) {
			for (int j = 0; j < 8; ++j) {
				if (j % 2 == 1) {
					Point p = new Point(i, j);
					Point q = new Point(i+1, j);
					lines.add(new Pair<Point, Point>(p, q));
				}
			}
		}
		
		for (Point p : state.pointsOfField()) {
			for (Point q : state.pointsOfField()) {
				if (!p.equals(q)) {
					PointInfo pi = points.get(p);
					PointInfo qi = points.get(q);
					double x = pi.x - qi.x;
					double y = pi.y - qi.y;
					double dist = Math.sqrt(x*x + y*y);
					if (dist < minPointDist) {
						minPointDist = (float) dist;
					}
				}
			}
		}
	}

	private float toX(Point p) {
		final int i = p.getIndex() == 0 || p.getIndex() == 6 || p.getIndex() == 7 ? -1 :
					  p.getIndex() == 2 || p.getIndex() == 3 || p.getIndex() == 4 ?  1 : 0;
		return width / 2 + i * dist * (3 - p.getFrame()) / 3;
	}

	private float toY(Point p) {
		final int i = p.getIndex() == 0 || p.getIndex() == 1 || p.getIndex() == 2 ? -1 :
					  p.getIndex() == 4 || p.getIndex() == 5 || p.getIndex() == 6 ?  1 : 0;
		return height / 2 + i * dist * (3 - p.getFrame()) / 3;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawPaint(bgPaint);
		
		Paint textPaint = active == Color.WHITE ? whitePaint : blackPaint;
		canvas.drawText(phase.name(), width / 2, textPaint.getTextSize() * 2, textPaint);
		
		for (Map.Entry<Point, PointInfo> e : points.entrySet()) {
			PointInfo pi = e.getValue();
			if (pi.paint != null) {
				canvas.drawCircle(pi.x, pi.y, radius, pi.paint);
			}
			canvas.drawCircle(pi.x, pi.y, radius, pi.selected ? selectedPaint : fieldPaint);
		}
		
		for (Pair<Point, Point> line : lines) {
			PointInfo pi = points.get(line.first);
			PointInfo qi = points.get(line.second);
			canvas.drawLine(pi.x, pi.y, qi.x, qi.y, fieldPaint);
		}
	}

	private long lastEvent = -1;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {
			return false;
		}
		long now = SystemClock.elapsedRealtime();
		if (now - lastEvent < 100) {
			return false;
		}
		float x = event.getX();
		float y = event.getY();
		for (Map.Entry<Point, PointInfo> e : points.entrySet()) {
			Point p = e.getKey();
			PointInfo pi = e.getValue();
			float r = minPointDist * 0.8f;
			if (pi.x - r <= x && x <= pi.x + r && pi.y - r <= y && y <= pi.y + r) {
				toggleSelect(p);
				lastEvent = now;
				return true;
			}
		}
		return false;
	}
	
	private void toggleSelect(Point p) {
		PointInfo pi = points.get(p);
		pi.selected = !pi.selected;
		selection.clear();
		if (pi.selected) {
			selection.add(p);
		}
		postInvalidate();
	}
	
	private void unselect(Point p) {
		PointInfo pi = points.get(p);
		pi.selected = false;
		selection.remove(p);
		postInvalidate();
	}

	private void unselectAll() {
		for (Point p : selection) {
			PointInfo pi = points.get(p);
			pi.selected = false;
		}
		selection.clear();
		postInvalidate();
	}

	@Override
	public Point getPoint(Color color) {
		try {
			Point p = selection.take();
			unselect(p);
//			showToast(p.toString());
			return p;
		} catch (InterruptedException exc) {
			throw new RuntimeException(exc);
		}
	}

	public void showToast(String msg) {
		showToast(msg, true);
	}
	
	public void showToast(final String msg, final boolean shortDuration) {
		((Activity) getContext()).runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(getContext(), msg, shortDuration ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void notifyIllegalMove(Color color, State oldState, State newState) {
		showToast("Illegal move by "+ color.name() +"!");
	}

	@Override
	public void notifyWin(Color color) {
		this.phase = Phase.WIN;
		this.active = color;
		showToast(color.name() +" wins!", false);
	}

	@Override
	public void notifyDraw() {
		this.phase = Phase.DRAW;
		showToast("Draw!", false);
	}

	@Override
	public void notifyPhase(Phase phase, Color color) {
		this.phase = phase;
		this.active = color;
		unselectAll();
	}

	@Override
	public void notifyState(State state) {
		this.state = state;
		unselectAll();
		for (Map.Entry<Point, PointInfo> e : points.entrySet()) {
			Point p = e.getKey();
			PointInfo pi = e.getValue();
			Color c = state.getOccupancy(p);
			pi.paint = c == Color.WHITE ? whitePaint : c == Color.BLACK ? blackPaint : null;
		}
		postInvalidate();
	}
}