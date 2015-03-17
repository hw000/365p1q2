import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


@SuppressWarnings("serial")
public class Gui extends JFrame 
	implements ActionListener{
	
	protected int[][]red,green,blue,grey;
	protected int width,height;
	protected boolean greyb;
	protected int[] histogram=new int[256];
	private int count=0;
	private JButton b;
	private JPanel buttonPanel;
	private Draw drawPanel = new Draw();
	
	public Gui(int[][] red,int[][] green, int[][] blue, int[][] grey,int height,int width){
		super("");
		this.red=red;
		this.green=green;
		this.blue=blue;
		this.grey=grey;
		this.height=height;
		this.width=width;
		

		this.setPreferredSize(new Dimension(width*2,height*2));
		
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		buttonPanel=new JPanel();
		buttonPanel.setLayout(new GridLayout(1,1));
		
		b=new JButton("next");
		b.addActionListener(this);
		buttonPanel.add(b);
		
		Container contentPane = this.getContentPane();
		contentPane.add(buttonPanel,BorderLayout.SOUTH);
		add(drawPanel);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source =event.getSource();
		if (source ==b){
			count++;
			repaint();
		}
	
		
	}
	
	
	
	public class Draw extends JPanel{
	
		

			
		public void setHistZero(){
			for(int i = 0; i<256;i++){
				histogram[i]=0;
			}
		}
			

		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int locX=0;
			int locY=0;
			
			Color c;
			setHistZero();
			// histogram
			for(int i=0;i<height;i++) {
				for(int j=0;j<width;j++){
					histogram[grey[j][i]]++;
				}
			}
			
			// Color
			negateHist();
			if(count%4==0){
				for(int i=0;i<height;i++) {
					for(int j=0;j<width;j++){
							c=new Color(red[j][i],green[j][i],blue[j][i]);
					
						g.setColor(c);
						locX=j;
						locY=i;
						g.drawLine(locX,locY,locX,locY);
					}
					
				}
				
			// Greyscale
			}else if(count%4==1){
				for(int i=0;i<height;i++) {
					for(int j=0;j<width;j++){
						c=new Color(grey[j][i],grey[j][i],grey[j][i]);
						g.setColor(c);
						locX=j;
						locY=i;
						g.drawLine(locX,locY,locX,locY);
					}
				}
				
			// Histogram
			}else if(count%4==2){
				int x1,x2,y1,y2;

				g.setColor(Color.BLACK);
				for(int i = 0;i<255;i++){

					x1 = getWidth()*i/255;
					
					y1 = (getHeight()/8*histogram[i]/256+getHeight()-10);
					
					x2 = getWidth()*(i+1)/255;
				
					y2 = (getHeight()/8*histogram[i+1]/256+getHeight()-10);
					g.drawLine(x1, y1, x2, y2);
				}
				
			}
			// Ordered Dither
			else if(count %4==3){
				int[][] dither={{0,8,2,10},
								{12,4,14,6},
								{3,11,1,9},
								{15,7,13,5}};
				
				


				g.setColor(Color.BLACK);
				

				
				// 4x4
				for(int x=0;x<width;x++){
					for(int y=0;y<height;y++){
						int i = x%4;
						int j = y%4;
						if((grey[x][y]*16/256)<dither[i][j]){
							locX=x;
							locY=y;
							g.drawLine(locX,locY,locX,locY);
						}
					}
				}
			}
			

		}
		public void negateHist(){
			for(int i=0;i<256;i++){
				histogram[i]=histogram[i]*-1;
			}
		}

	}

}
