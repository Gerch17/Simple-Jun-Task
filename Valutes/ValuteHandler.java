package Valutes;

import com.sun.jdi.Value;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class ValuteHandler {
    private HashMap<String, Float> last = new HashMap<>();
    private HashMap<String, Float> current = new HashMap<>();

    public void getValues(){
        getValutes(true);
        getValutes(false);
        List<String> keys = new ArrayList<>(current.keySet());
        HashMap<String, Float> temp = new HashMap<>();
        for(int i = 0; i < keys.size(); i++)
        {
            temp.put(keys.get(i), Math.abs(current.get(keys.get(i))-last.get(keys.get(i))));
        }
        for(Map.Entry<String, Float> forprint: getFiveMax(temp).entrySet()){
            System.out.println(forprint.getKey() + " - " + forprint.getValue());
        }
    }

    private HashMap<String, Float> getFiveMax(HashMap<String, Float> temp)
    {
        HashMap<String, Float> maxMap = new HashMap<>();
        for(int i = 0; i < 5; i++) {
            float maxValue = 0;
            String maxKey = "";
            for (Map.Entry<String, Float> m : temp.entrySet()) {
                if(maxValue < m.getValue())
                {
                    maxValue = m.getValue();
                    maxKey = m.getKey();
                }
            }
            maxMap.put(maxKey, maxValue);
            temp.remove(maxKey);
        }
        return maxMap;
    }

    private void getValutes(Boolean check)
    {
        try{
            String pre_apiURL = "http://www.cbr.ru/scripts/XML_daily.asp";
            if(check) {
                pre_apiURL = "http://www.cbr.ru/scripts/XML_daily.asp" + "?date_req=" + getDate();
            }
            URL url = new URL(pre_apiURL);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(url.openStream());
            NodeList valutes = doc.getElementsByTagName("Valute");
            for(int i = 0; i < valutes.getLength(); i++)
            {
                Node nameValute = valutes.item(i);
                Element element = (Element) nameValute;
                if(check){
                    last.put(getTagValue("Name", element), getTagValuefl("Value", element));
                } else {
                    current.put(getTagValue("Name", element), getTagValuefl("Value", element));
                }
            }
        }catch(Exception e){}
    }

    private String getTagValue(String tag, Element element)
    {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
    private float getTagValuefl(String tag, Element element)
    {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        float f = Float.parseFloat(node.getNodeValue().replaceAll(",", "."));;
        return f;
    }

   public String getDate(){
        LocalDate date = LocalDate.now().minusDays(1);
        String [] splittedDate = date.toString().split("\\-");
        String normalDateString = splittedDate[2] + "." + splittedDate[1] + "." + splittedDate[0];
        return normalDateString;
    }

}
