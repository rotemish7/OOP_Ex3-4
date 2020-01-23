package gameClient;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

	public void robot_kml(String pos,int id)
	{
		//initialize
		String temp = " <Style id=\"robot_icon\">\r\n" + 
				"      <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>https://img.icons8.com/color/128/000000/transformer.png</href>\r\n" + 
				"        </Icon>\r\n" + 
				"      </IconStyle>\r\n" + 
				"    </Style>";

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		buff_kml.append("<Folder>\r\n" + 
				"      <name>robot"+id+" folder</name>\r\n" + 
				"      <open>1</open>\r\n" + 
				"      <Style>\r\n" + 
				"        <ListStyle>\r\n" + 
				"          <listItemType>"+"robot-"+id+" path"+"</listItemType>\r\n" + 
				"        </ListStyle>\r\n" + 
				"      </Style>/n" +
				"      "
				+ "	   <Placemark>\r\n" + 
				"        <TimeStamp>\r\n" + 
				"          <when>"+now+"</when>\r\n" + 
				"        </TimeStamp>\r\n" + 
				"        <styleUrl>#robot_icon</styleUrl>\r\n" + 
				"        <Point>\r\n" + 
				"			<coordinates>"+ pos +"</coordinates>\r\n" + 
				"        </Point>\r\n" + 
				"      </Placemark>\n"
				);
	}
	
	public String stringTokml() 
	{
		this.buff_kml.append("</Folder>\r\n");

		return buff_kml.toString();
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

	try {

		PrintWriter p_w = new PrintWriter(file_name);
		p_w.println(buff_kml.toString());
		p_w.close();
		System.out.println("File saved");

	} catch (Exception e) {System.out.println("problem");}	
}
}
