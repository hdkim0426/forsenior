package forsenior;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Weather {
	 
	 String[] temp = new String[3];
	 String[] wfKor = new String[3];
	 String[] hour1 = new String[3];
	 
	 public Weather() {
	 
	  try {
	   DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
	   DocumentBuilder parser = f.newDocumentBuilder();
	 
	   Document xmlDoc = null;
	   String url = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1121573000";
	   xmlDoc = parser.parse(url);
	 
	   Element root = xmlDoc.getDocumentElement();
	   // System.out.println(root.getTagName());
	   
	   //xml api에서 필요한 태그 골라서 파싱하기
	   for (int i = 0; i < temp.length; i++) {
	    Node xmlNode1 = root.getElementsByTagName("data").item(i);
	 
	    Node xmlNode21 = ((Element) xmlNode1).getElementsByTagName(
	      "temp").item(0);
	    Node xmlNode22 = ((Element) xmlNode1).getElementsByTagName(
	      "wfKor").item(0);
	    Node xmlNode23 = ((Element) xmlNode1).getElementsByTagName(
	      "hour").item(0);
	     
	    //파싱 결과를 데이터로 옮김
	    temp[i] = xmlNode21.getTextContent();
	    wfKor[i] = xmlNode22.getTextContent();
	    hour1[i] = Integer.parseInt(xmlNode23.getTextContent()) - 3 + "시부터  " + xmlNode23.getTextContent() + "시 까지";
	    
	    
	   }
	 
	  } catch (Exception e) {
	   System.out.println(e.getMessage());
	   System.out.println(e.toString());
	  }
	 }
	 
	 public String[] getTemp() {
	  return temp;
	 }
	 
	 public String[] getKor() {
	  return wfKor;
	 }
	 
	 public String[] getHour() {
	  return hour1;
	 }
	}