package de.amobico.example.java;
import java.io.IOException;

/**
 * This calls shows a separate thread is spawned for listing to keyboard
 * hits.
 * 
 * @author stefan
 *
 */
public class ThreadExample {
	static boolean continueCounting = true;

	public static void main(String[] args) {
		// Kick off first thread: Wait for user to hit a key on the keyboard to exit the program
		Thread waitForKeyboardPressThread = new Thread() {
		    public void run(){
				try {
					System.out.println("Press Enter to shutdown connection and exit.");
					System.in.read();
					continueCounting = false;					
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		 };
		 waitForKeyboardPressThread.start();
		
		 // Continue with main thread
		int counter = 0;
		while (continueCounting) {
			System.out.println("Main thread continues counting every second: " + counter++);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("You have hit the keyboard. We stop counting and the main thread.");
	}

}
