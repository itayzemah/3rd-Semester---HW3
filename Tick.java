import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;

public class Tick implements Runnable {
	private int sleepTime = 1000;
	private ClockPane clockPane;
	private boolean resume = false;
	private boolean suspended = false;
	private Lock lock = new ReentrantLock();
	private Condition stop = lock.newCondition();
	private AnnounceTimeOnSeparateThread voice = new AnnounceTimeOnSeparateThread();
	public Tick(ClockPane clockPane) {
		this.clockPane = clockPane;
	}

	@Override
	public void run() {
		moveClock();
	}

	public void moveClock() {
		while (true) {
			lock.lock();
			if (suspended)//clock stop
				try {
					stop.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			else { // clock start again
				resume = true;
	        	 clockPane.setCurrentTime();
 	             Platform.runLater(() -> clockPane.paintClock());
				try {
					if(clockPane.getSecond() == 0) {
						voice.setHour(clockPane.getHour());
						voice.setMinute(clockPane.getMinute());
						new Thread(voice).start();
					}
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}
	}

	public synchronized void pause() {
		suspended = true;
	    resume = false; 
	}

	public void play() {
		if(resume) {
			return;
		}
		lock.lock();
		suspended = false;
		stop.signal();
		lock.unlock();
	}
}
