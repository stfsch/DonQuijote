package de.rwth_aachen.kbsg.drq;

import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import de.rwth_aachen.kbsg.dq.Color;
import de.rwth_aachen.kbsg.dq.Point;
import de.rwth_aachen.kbsg.dq.StateMachine;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

public class FieldView extends View {
	public static interface Listener {
		public void onPointSelected(Point p);
		public void onPointUnselected(Point p);
	}
	
	public static class Adapter implements Listener {
		@Override
		public void onPointSelected(Point p) {
		}

		@Override
		public void onPointUnselected(Point p) {
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
	
	private Collection<Pair<Point, Point>> lines = new Vector<Pair<Point, Point>>();
	private BlockingQueue<Point> selection = new ArrayBlockingQueue<Point>(1);
	
	private Listener listener = new Adapter();
	
	private StateMachine stateMachine = new StateMachine();
	
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
    
	public Listener getListener() {
		return listener;
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public void updateStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
		postInvalidate();
	}
	
	public Point waitForSelection() {
		try {
			return selection.take();
		} catch (InterruptedException e) {
			throw new GameInterruptedException(e);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
		dist = Math.min(width, height) * 85 / 100 / 2;
		radius = dist / 15;

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
		
		for (Point p : stateMachine.getState().pointsOfField()) {
			for (Point q : stateMachine.getState().pointsOfField()) {
				if (!p.equals(q)) {
					double x = toX(p) - toX(q);
					double y = toY(p) - toY(q);
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
		
		Paint textPaint = stateMachine.getActiveColor() == Color.WHITE ? whitePaint : blackPaint;
		canvas.drawText(stateMachine.getPhase().name(), width / 2, textPaint.getTextSize() * 2, textPaint);
		
		for (Point p : stateMachine.getState().pointsOfField()) {
			if (stateMachine.getState().isOccupiedBy(p, Color.WHITE)) {
				canvas.drawCircle(toX(p), toY(p), radius, whitePaint);
			} else if (stateMachine.getState().isOccupiedBy(p, Color.BLACK)) {
				canvas.drawCircle(toX(p), toY(p), radius, blackPaint);
			}
			canvas.drawCircle(toX(p), toY(p), radius, selection.contains(p) ? selectedPaint : fieldPaint);
		}
		
		for (Pair<Point, Point> line : lines) {
			canvas.drawLine(toX(line.first), toY(line.first), toX(line.second), toY(line.second), fieldPaint);
		}
	}

	private long lastEvent = -1;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {
			return false;
		}
		long now = SystemClock.elapsedRealtime();
		if (now - lastEvent < 500) {
			return false;
		}
		float x = event.getX();
		float y = event.getY();
		for (Point p : stateMachine.getState().pointsOfField()) {
			float r = minPointDist * 0.8f;
			if (toX(p) - r <= x && x <= toX(p) + r && toY(p) - r <= y && y <= toY(p) + r) {
				toggleSelect(p);
				lastEvent = now;
				return true;
			}
		}
		return false;
	}
	
	public void toggleSelect(Point p) {
		if (selection.remove(p)) {
			listener.onPointUnselected(p);
		} else {
			selection.add(p);
			listener.onPointSelected(p);
		}
		postInvalidate();
	}
	
	public void unselect(Point p) {
		if (selection.remove(p)) {
			listener.onPointUnselected(p);
		}
		postInvalidate();
	}

	public void unselectAll() {
		for (Point p : selection) {
			unselect(p);
		}
		selection.clear();
		postInvalidate();
	}
}