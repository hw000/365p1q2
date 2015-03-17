import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;


public class p1q2 {
	static int firstPix;
	static int scanLines;
	static int width,height;
	static int stripsPerImage;
	static int offset;
	static byte[] arrOfBytes;
	static boolean little_endian;
	public static void main(String[] args) throws IOException {
		//file chooser///////////////////////////////////////////
		
		
		//uncomment later
		
		JButton open = new JButton();
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("."));
		fc.setDialogTitle("");
		
		
		if(fc.showOpenDialog(open)==JFileChooser.APPROVE_OPTION){
			//
		}
		
		
		String file = fc.getSelectedFile().getAbsolutePath();
		
		//////////////////////////////////////////////////////////

		
		//String file = "src/tiff_sample.tiff";
		
		
		arrOfBytes=getFileInfo(file);

		int byteOrder=getShort(0);
		if(byteOrder== 0x4949){
			little_endian=true;
		}else{
			little_endian=false;
		}
		

		offset=getInt(4);

		
		
		getIFD();
	
	

		int[][] red = new int[width][height];
		//load red
		int k=firstPix;
		for (int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				red[j][i]=(int)arrOfBytes[k]&0xff;
				k+=3;
			}
		}
		
		int[][] green = new int[width][height];
		//load green
		k=firstPix+1;
		for (int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				green[j][i]=(int)arrOfBytes[k]&0xff;
				k+=3;
			}
		}
		
		
		int[][] blue = new int[width][height];
		//load blue
		k=firstPix+2;
		for (int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				blue[j][i]=(int)arrOfBytes[k]&0xff;
				k+=3;
			}
		}
		
		int[][] grey = new int[width][height];
		for(int i =0;i<height;i++){
			for(int j = 0;j<width;j++) {
				grey[j][i]=(int) (0.21*red[j][i]+0.72*green[j][i]+.007*blue[j][i]);
			}
		}
		Gui test=new Gui(red,green,blue,grey,height,width);
		test.setVisible(true);
		
	}
	
	static byte[] getFileInfo(String fileName) throws IOException {
		int read;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
		
		byte[] buffer= new byte[500000];
		
		while((read=in.read(buffer))>0) {
			out.write(buffer,0,read);
						
		}
		out.flush();
		byte[] fileBytes=out.toByteArray();
		return fileBytes;
	}
	
	static int getInt(int i) {
		int b0=arrOfBytes[i+0]&0xFF;
		int b1=arrOfBytes[i+1]&0xFF;
		int b2=arrOfBytes[i+2]&0xFF;
		int b3=arrOfBytes[i+3]&0xFF;

		if(little_endian){
			return ((b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0));
		}else
			return ((b0 << 24) + (b1 << 16) + (b2 << 8) + b3);
	}
	static int getShort(int i) {
		int b0=arrOfBytes[i+0]&0xff;
		int b1=arrOfBytes[i+1]&0xff;
	

		
		if(little_endian) {
			return ((b1 << 8) + b0);
		}
		else{
			return ((b0 << 8) + b1);
		}
	}


	//double getRational
	static void getIFD(){
		int dir=offset+2;
		int numOfDir=getShort(offset);
		
		@SuppressWarnings("unused")
		int tag,type,count,val;

		for(int i=0;i<numOfDir;i++) {
			tag=getShort(dir);
			type=getShort((dir+2));
			count= getInt((dir+4));
			
			val= getVal(dir+8,type);


//			System.out.println("directory entry "+ i + " tag: " +tag);
//			System.out.println("directory entry "+ i + " type: " + type);
//			System.out.println("directory entry "+ i + " count: " + count);	// number of values
//			System.out.println("directory entry "+ i + " value/offset: " + val);
//			
//			System.out.println();
			
			dir=dir+12;
			
			if(tag==256){
				width=val;
			}
			if(tag==257){
				height=val;
			}
			if(tag==257){
				scanLines=val;
			}
			if(tag==273){
				firstPix=val;
			}
		
		}
		offset=offset+2+numOfDir*12;

		
	}
	static int getVal(int i,int type) {
		if(type==3){
			return getShort(i);
		}
		
		else{
			return getInt(i);
		}
	}

	


}
