import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
public class AprioriUI extends JFrame implements ActionListener {
	
	private JLabel support,confidence;
	private JButton apriori,frequentPattern,cancel,rulesGeneration;
	private TextField textSupport,textConfidence;
	public AprioriUI() {
		setLayout(null);
		support = new JLabel("Support");
		support.setBounds(50, 55, 70, 20);
		add(support);
		
		textSupport = new TextField(20);
		textSupport.setBounds(150,50,200,30);
		add(textSupport);
		
		confidence = new JLabel("Confidence");
		confidence.setBounds(50,95,70,20);
		add(confidence);
				
		textConfidence = new TextField(20);
		textConfidence.setBounds(150,90,200,30);
		add(textConfidence);
		
		apriori = new JButton("Start Apriori Process");
		apriori.setBounds(50,200,300,35);
		add(apriori);
		
		
		frequentPattern = new JButton("Frequent Pattern");
		frequentPattern.setBounds(120,430,130,30);
		add(frequentPattern);
		
		rulesGeneration = new JButton("Generate Rules");
		rulesGeneration.setBounds(120,380,200,30);
		add(rulesGeneration);
		
		cancel = new JButton("Cancel");
		cancel.setBounds(270,430,100,30);
		add(cancel);
		
		//Add ActionListeners to all the Buttons
		apriori.addActionListener(this);
		frequentPattern.addActionListener(this);	
		rulesGeneration.addActionListener(this);
	}
	public static void main(String[] args){
		AprioriUI a = new AprioriUI();
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		a.setSize(500,500);
		a.setTitle("Apriori Algorithm");
		a.setResizable(false);
		a.setVisible(true);
	}
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource() == apriori) {
			System.out.println(textSupport.getText());
			if (textSupport.getText().equals("")) {
				JOptionPane.showMessageDialog(null,
					"Please Enter a valid support",
					"Invalid Parameters",
					JOptionPane.ERROR_MESSAGE);
			}
			if (textConfidence.getText().equals("")){
				JOptionPane.showMessageDialog(null,
					"Please Enter a valid Confidence",
					"Invalid Parameters",
					JOptionPane.ERROR_MESSAGE);
			}
			
			else {
				String config = "f1.dat"+" "+textSupport.getText()+" "+textConfidence.getText();
				String[] args = config.split(" ");
				try{
					Apriori ap =  new Apriori(args);
				}
				catch(Exception e)
				{
				}
			}
	}	
		if(ae.getSource() == frequentPattern) {
				/*OptionPane.showMessageDialog(null,
					"Please Enter a valid Confidence, \n do sdfsdgsfdghdfgv sdsjhfb udshf  sjdfh kjsd f ds fksdh fksd f dsf sduf sdf sd fusd f sdf ",
					"Invalid Parameters",
					JOptionPane.ERROR_MESSAGE);*/
					JFrame a = new JFrame();
					//a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					try {
        
			FileInputStream fstream = new FileInputStream("output.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
        	BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			StringBuffer str=new StringBuffer();
			//Read File Line By Line
			while ((strLine = br.readLine()) != null){
				// Print the content on the console
				str.append(strLine+"\n");
				System.out.println(str);
	
				 
		}
			//Close the input stream
					in.close();
					JTextArea area = new JTextArea();
					area.setText(str.toString());			
					a.add(area);
					a.setSize(750,750);
					a.setTitle("Frequent Patterns");
					a.setResizable(true);
					a.setVisible(true);
		}
	catch (Exception e) {
            System.out.println("Exception ");
        }			
			
			}

	if(ae.getSource() == rulesGeneration) {
				/*OptionPane.showMessageDialog(null,
					"Please Enter a valid Confidence, \n do sdfsdgsfdghdfgv sdsjhfb udshf  sjdfh kjsd f ds fksdh fksd f dsf sduf sdf sd fusd f sdf ",
					"Invalid Parameters",
					JOptionPane.ERROR_MESSAGE);*/
					JFrame a = new JFrame();
					//a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					try {
        
			FileInputStream fstream = new FileInputStream("rules.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
        	BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			StringBuffer str=new StringBuffer();
			//Read File Line By Line
			while ((strLine = br.readLine()) != null){
				// Print the content on the console
				str.append(strLine+"\n");
				//System.out.println(str);\
	
				 
		}
			//Close the input stream
					in.close();
					JTextArea area = new JTextArea();
					area.setText(str.toString());			
					a.add(area);					
					a.setSize(750,750);
					a.setTitle("Genrerated Rules");
					a.setResizable(true);
					a.setVisible(true);
		}
	catch (Exception e) {
            System.out.println("Exception ");
        }			
			
			}
	}
	}

	
