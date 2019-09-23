


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

// The game creates a 3 * 4 grid of buttons all with the same "cover" image, then secretly assigns each of 6 images to two random buttons. 
// when a button is clicked it reveals the assigned image, when a second button is clicked the game checks to see if they are assigned to the same image.
// If so the buttons remain showing the assigned image, if not they return to the cover image 
public class MemoryGame {

	public static final int WIDTH = 600;
	public static final int HEIGHT = 800;
	public static final int NUMBER_OF_CHAR = 200;
	public static final int NUMBER_OF_ROWS = 4;
	public static final int NUMBER_OF_COLUMNS = 3;
	public static final int numberOfPuzzleTiles = NUMBER_OF_ROWS * NUMBER_OF_COLUMNS;

	public static final ImageIcon defaultIcon = new ImageIcon("pictures/cover.jpg");

	public static JFrame gui;

	private JButton[] buttons;
	private ImageIcon icons[];

	public static ArrayList<Integer> images = new ArrayList<Integer>();
	public HashMap<Integer, Integer> iconMap = new HashMap<Integer, Integer>();// map of buttons to associated images 
	public HashMap<Integer, Boolean> flippedMap = new HashMap<Integer, Boolean>(); // map of buttons to boolean that is true if they are flipped
	boolean isFlipped = false; //boolean to track of if there is currently an unpaired button flipped 
	int currentButton;

	public MemoryGame() { //constructor 
		
		gui = new JFrame("MemoryGame");
		gui.setSize(WIDTH, HEIGHT );
		gui.setLayout(new GridLayout(NUMBER_OF_ROWS,NUMBER_OF_COLUMNS));



		randomiseImages();

		icons = new ImageIcon[numberOfPuzzleTiles];
		for (int i = 0; i < numberOfPuzzleTiles; i++) {
			icons[i] = new ImageIcon("pictures/" + String.valueOf(images.get(i)) + ".jpg");
			
		}

		buttons = new JButton[numberOfPuzzleTiles];

		for (int i = 0; i < numberOfPuzzleTiles; i++) {
			buttons[i] = new JButton(String.format("%d", i));
			buttons[i].setIcon(defaultIcon);
			gui.add(buttons[i]);
			flippedMap.put(i, false);
		}


		for (int i = 0; i < numberOfPuzzleTiles; i++) {
			buttons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String actionCommand = e.getActionCommand();
					toggleImage(actionCommand);
				}	
			});


		}

		gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gui.setResizable(false);
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);


	}

	// adds images to the images array and shuffles them, then adds them to the map
	private void randomiseImages() { 
		images.add(0); images.add(0);
		images.add(1); images.add(1);
		images.add(2); images.add(2);
		images.add(3); images.add(3);
		images.add(4); images.add(4);
		images.add(5); images.add(5);
		Collections.shuffle(images);

		for(int i = 0; i < images.size(); i++) {
			iconMap.put(i, images.get(i));
		}


	}

	// toggles the buttons to show the assigned image, calling reset if they are different
	private void toggleImage(String actionCommand) { 
		int i = Integer.parseInt(actionCommand);

		if(flippedMap.get(i) == false){
			buttons[i].setIcon(icons[i]);
			flippedMap.put(i, true);
			
			if(isFlipped) {
				if(iconMap.get(i) == iconMap.get(currentButton)) {
					isFlipped = false;
				}
				else {
					reset(i);
					
				}
					

			}else {
				isFlipped = true;
				currentButton = i;

			}
		}

	}
	
	// called if two buttons are revealed and do not show the same image, waits 1 second then resets to the cover image
	private void reset(int i) { 
		
		Thread thread = new Thread(new Runnable() {

			
			
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
				isFlipped = false;
				buttons[i].setIcon(defaultIcon);
				buttons[currentButton].setIcon(defaultIcon);
				flippedMap.put(i, false);
				flippedMap.put(currentButton, false);
			}
		});
		
		thread.start();
		
	}

	public static void main(String[] args){
		new MemoryGame();
	}
}
