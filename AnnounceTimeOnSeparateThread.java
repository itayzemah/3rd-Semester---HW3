import java.applet.*;


public class AnnounceTimeOnSeparateThread implements Runnable {

	
	private AudioClip[] hourAudio = new AudioClip[12];
	private AudioClip[] minuteAudio = new AudioClip[60];
	// Create audio clips for pronouncing am and pm
	private AudioClip amAudio = Applet.newAudioClip(this.getClass().getResource("/audio/am.au"));
	private AudioClip pmAudio = Applet.newAudioClip(this.getClass().getResource("/audio/pm.au"));
	private int hour, minute;
	
	public  AnnounceTimeOnSeparateThread() { // Create audio clips for pronouncing hours
		for (int i = 0; i < 12; i++)
			hourAudio[i] = Applet.newAudioClip(this.getClass().getResource("/audio/hour" + i + ".au"));
		// Create audio clips for pronouncing minutes
		for (int i = 0; i < 60; i++)
			minuteAudio[i] = Applet.newAudioClip(this.getClass().getResource("/audio/minute" + i + ".au"));
	}
	@Override
	public void run() {
		try { // Announce hour
			hourAudio[hour % 12].play();
			// Time delay to allow hourAudio play to finish
			Thread.sleep(1500);
			// Announce minute
			minuteAudio[minute].play();
			// Time delay to allow minuteAudio play to finish
			Thread.sleep(1500);
		} catch (InterruptedException ex) {
		}
		// Announce am or pm
		if (hour < 12)
			amAudio.play();
		else
			pmAudio.play();
	
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	

}
