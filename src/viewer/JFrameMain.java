/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 
package viewer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.*;

import entities.Word;
import viewer.Test.CheckBoxListener;
/* FrameDemo.java requires no other files. */
public class JFrameMain {

	static StringBuffer choices;
	static CheckBoxListener myListener = null;
	static ArrayList<JCheckBox> jcbList = new ArrayList<JCheckBox>();
	public static boolean RIGHT_TO_LEFT = false;
	final static JFrame frame = new JFrame("NLP Food Analysis");
	
	public static void addComponentsToPane(Container pane) {
		
		//Create and set up the window.
//				final JFrame frame = new JFrame("NLP Food Analysis");
//				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		if (RIGHT_TO_LEFT) {
			pane.setComponentOrientation(
					java.awt.ComponentOrientation.RIGHT_TO_LEFT);
		}

		JTabbedPane tabbedPane = new JTabbedPane();
		final JButton btn = new JButton("Search for food");
		final JPanel foodNamePanel= new JPanel();
		final JLabel lblFood = new JLabel("Enter food name:");
		final JTextField field = new JTextField(20);
		final JPanel foodCornerPanel = new JPanel();
		final JPanel jplCheckBox = new JPanel();
		
		//Search textbox section
		foodNamePanel.add(lblFood);
		foodNamePanel.add(field);
		foodNamePanel.add(btn);    
		foodNamePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		//Add search to tab's inner panel
		foodCornerPanel.add(foodNamePanel);
		
		
		jplCheckBox.setLayout(new GridLayout(10,5));		//0 rows, 1 Column
		foodCornerPanel.add(jplCheckBox);
		foodCornerPanel.setLayout(new BoxLayout(foodCornerPanel, BoxLayout.Y_AXIS));
		final JLabel plsSelect = new JLabel("Please select: ");

		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(field.getText().equals(""))return;
				btn.setEnabled(false);
				jplCheckBox.setBorder(
						BorderFactory.createLineBorder(Color.BLACK));
				jplCheckBox.add(plsSelect);
				String submitText = field.getText();
				HashSet<Word> adjSet = Viewer.searchByKeyWords(submitText);
				db.Db4oHelper.getInstance().db();
				for(JCheckBox jcbTmp:jcbList)
					jplCheckBox.remove(jcbTmp);
				jcbList.clear();
				if (adjSet!=null){
					for(Word w : adjSet){
						JCheckBox jcb = new JCheckBox(w.getWord());
						jplCheckBox.add(jcb);
						jcbList.add(jcb);
					}
					frame.pack();
				}
				db.Db4oHelper.getInstance().db().close();
				btn.setEnabled(true);
				foodCornerPanel.validate();
				frame.pack();
				frame.setExtendedState(Frame.MAXIMIZED_BOTH); 
			}
		});



		tabbedPane.add("Food Corners", foodCornerPanel);
		JPanel listAttr = new JPanel();
		JLabel lblAttr = new JLabel("Please select attributes:");
		listAttr.add(lblAttr);
		tabbedPane.add("Store Corners", listAttr);
		tabbedPane.setSelectedIndex(0);
		
		pane.add(tabbedPane);
		frame.pack();
		
	}



	private static void createAndShowGUI() {

		//Create and set up the window.
		//final JFrame frame = new JFrame("NLP Food Analysis");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToPane(frame.getContentPane());
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//Container contentPane = frame.getContentPane();


		//        JLabel emptyLabel = new JLabel("");
		//        emptyLabel.setPreferredSize(new Dimension(450, 400));
		//        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
		//        frame.setPreferredSize(new Dimension(500,300));
		//        final JTextField field = new JTextField(10);
		//        frame.add(field, BorderLayout.NORTH);
		//        
		//        JButton btn = new JButton();
		//        btn.setPreferredSize(new Dimension(40, 40));
		//        btn.setText("Search for food");
		//        btn.setBounds(600, 400, 220, 30);
		//        frame.add(btn);
		//        
		//        final JButton btn1 = new JButton();
		//        btn1.setPreferredSize(new Dimension(40, 40));
		//        btn1.setText("Search for stalls");
		//        frame.add(btn1);
		//        
		//     // Add an item listener for each of the check boxes.
		//    	// This is the listener class which contains business logic for the item events
		//        //myListener = new CheckBoxListener();
		//        
		//        // Create the check boxes with default selection true for all check boxes
		//        
		//        //jcbChin = new JCheckBox("Chin");
		//        //jcbChin.setMnemonic(KeyEvent.VK_C);			//Alt+C Checks/Unchecks the check Box 
		//        //jcbChin.setSelected(true);
		//        //jcbChin.addItemListener(myListener);
		//
		//        // Indicates what's on the geek.
		//        choices = new StringBuffer("cght");			//Default Image has all the parts.
		//
		//        // Set up the picture label
		//        //jlbPicture = new JLabel(new ImageIcon("geek-" + choices.toString().trim() + ".gif"));
		//        //jlbPicture.setToolTipText(choices.toString().trim());
		//
		//        // Put the check boxes in a column in a panel
		//        final JPanel jplCheckBox = new JPanel();
		//        jplCheckBox.setLayout(new GridLayout(0, 1));		//0 rows, 1 Column
		//
		//        //setLayout(new BorderLayout());
		//        frame.add(jplCheckBox, BorderLayout.WEST);
		//        //add(jlbPicture, BorderLayout.CENTER);
		//        //frame.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		//        
		//        
		//        btn1.addActionListener(new ActionListener() {
		//			
		//			@Override
		//			public void actionPerformed(ActionEvent arg0) {
		//				if(field.getText().equals(""))return;
		//				btn1.setEnabled(false);
		//				
		//				// TODO Auto-generated method stub
		//				String submitText = field.getText();
		//				HashSet<Word> adjSet = Viewer.searchByKeyWords(submitText);
		//				db.Db4oHelper.getInstance().db();
		//				for(JCheckBox jcbTmp:jcbList)
		//				jplCheckBox.remove(jcbTmp);
		//				jcbList.clear();
		//				if (adjSet!=null){
		//					for(Word w : adjSet){
		//						JCheckBox jcb = new JCheckBox(w.getWord());
		//						jplCheckBox.add(jcb);
		//						jcbList.add(jcb);
		//					}
		//				frame.pack();
		//				}
		//				db.Db4oHelper.getInstance().db().close();
		//				btn1.setEnabled(true);
		//			}
		//		});




		//        // add the listener to the jbutton to handle the "pressed" event
		//        showDialogButton.addActionListener(new ActionListener()
		//        {
		//          public void actionPerformed(ActionEvent e)
		//          {
		//            // display/center the jdialog when the button is pressed
		//            JDialog d = new JDialog(frame, "Hello", true);
		//            d.setLocationRelativeTo(frame);
		//            d.setVisible(true);
		//          }
		//        });
		//        
		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}