package gameClient;

import java.io.PrintWriter;

public class KML_Logger 
{
	StringBuffer buff_kml;
	private static KML_Logger kml;
	private int level;
	private int flag = 0;

	public KML_Logger(int level)
	{
		buff_kml = new StringBuffer();
		kml = this;
		this.level = level;
	}

	public void info_kml(String info)
	{
		if(flag  == 0)
		{
			buff_kml.append(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
							"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
							"  <Document>\r\n" + 
							"    <name>KML path</name>\r\n" + 
							"    <open>1</open>\r\n" + 
							"    <description>\r\n" + 
							info + " Level: "+level+ "\n" + 
							"    </description> \n" +
							" <LookAt>\r\n" + 
							"      <longitude>35.20734722222223</longitude>\r\n" + 
							"      <latitude>32.10453611111111</latitude>\r\n" + 
							"      <altitude>0</altitude>\r\n" + 
							"      <range>1131.110892010045</range>\r\n" + 
							"      <tilt>0</tilt>\r\n" + 
							"      <heading>-0.3840786059394472</heading>\r\n" + 
							"    </LookAt>\r\n"
					);
			flag++;
		}
		else
		{
			buff_kml.append(
							"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
							"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
							"  <Document>\r\n" + 
							"    <name>KML path</name>\r\n" + 
							"    <open>1</open>\r\n" + 
							"    <description>\r\n" + 
							"no info" +"\n"+ 
							"    </description> \n"+
							" <LookAt>\r\n" + 
							"      <longitude>35.20734722222223</longitude>\r\n" + 
							"      <latitude>32.10453611111111</latitude>\r\n" + 
							"      <altitude>0</altitude>\r\n" + 
							"      <range>1131.110892010045</range>\r\n" + 
							"      <tilt>0</tilt>\r\n" + 
							"      <heading>-0.3840786059394472</heading>\r\n" + 
							"    </LookAt>\r\n"
					);
		}

	}
	
	public void end_kml()
	{
		buff_kml.append("\n"+
						"	</Document>\n"+
						"</kml>"
		);
	}
	
	public void save_kml(String filename)
	{
		String file_name = filename;
		end_kml();
		
		//System.out.println(buff_kml);
		
		try {
			
			PrintWriter p_w = new PrintWriter(file_name);
			p_w.println(buff_kml.toString());
			p_w.close();
			System.out.println("File saved");
			
		} catch (Exception e) {System.out.println("problem");}	
	}


}
