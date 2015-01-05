import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditPlayerDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField north,south,east,west;
	private String[] names;
	private JButton confirm,cancel;
	private JPanel top,buttonPane;
	private JFrame frame;
	
	public EditPlayerDialog(JFrame frame, String[] names) {
		super(frame,"Edit Player Names",true);
		this.frame = frame;
		this.names = names;
		setLayout(new BorderLayout());
		south = new JTextField(names[0]);
		west = new JTextField(names[1]);
		north = new JTextField(names[2]);
		east = new JTextField(names[3]);
		confirm = new JButton("Confirm");
		cancel = new JButton("Cancel");
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		north.setSize(north.getWidth(),20);
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout(10,0));
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout(10,0));
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout(10,0));
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BorderLayout(10,0));
		JLabel southLabel = new JLabel("South");
		JLabel westLabel = new JLabel("West");
		JLabel northLabel = new JLabel("North");
		JLabel eastLabel = new JLabel("East");
		southLabel.setLabelFor(south);
		westLabel.setLabelFor(west);
		northLabel.setLabelFor(north);
		eastLabel.setLabelFor(east);
		
		//Lay out the top
		top = new JPanel();
		top.setLayout(new GridLayout(4,1));
		southPanel.add(southLabel,BorderLayout.WEST);
		southPanel.add(south);
		westPanel.add(westLabel,BorderLayout.WEST);
		westPanel.add(west);
		northPanel.add(northLabel,BorderLayout.WEST);
		northPanel.add(north);
		eastPanel.add(eastLabel,BorderLayout.WEST);
		eastPanel.add(east);
		top.add(southPanel);
		top.add(westPanel);
		top.add(northPanel);
		top.add(eastPanel);
		top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		add(top,BorderLayout.CENTER);

		
		//Lay out the buttons from left to right.
		buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(cancel);
		buttonPane.add(confirm);
		add(buttonPane,BorderLayout.SOUTH);
		setResizable(false);
	}
	
	public void showErrorLabel(String error) {
		JOptionPane.showMessageDialog(frame,error);
	}
	
	public String[] getNames() {
		return names;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == confirm) {
			names[0] = south.getText();
			names[1] = west.getText();
			names[2] = north.getText();
			names[3] = east.getText();
			for (String name : names) {
				if (name.trim().equals("")) {
					showErrorLabel("You may not leave a name blank.");
					return;
				}
			}
			dispose();
		} else if (e.getSource() == cancel) {
			names = null;
			dispose();
		}
	}
}