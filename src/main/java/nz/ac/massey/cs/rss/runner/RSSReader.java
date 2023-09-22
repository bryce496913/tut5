package nz.ac.massey.cs.rss.runner;

import com.example.myschema.Rss;
import com.example.myschema.RssChannel;
import com.example.myschema.RssItem;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class RSSReader {
    public static void main(String[] args) {
        try {
            File file = new File("herald.xml"); // Assuming the file is in the project's home directory
            JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Rss rss = (Rss) jaxbUnmarshaller.unmarshal(file);

            RssChannel channel = rss.getChannel();
            List<RssItem> items = channel.getItem();
            for (RssItem item : items) {
                List<Object> titleOrDescriptionOrLink = item.getTitleOrDescriptionOrLink();

                String title = "";
                String link = "";
                String description = "";

                for (Object obj : titleOrDescriptionOrLink) {
                    if (obj instanceof JAXBElement) {
                        JAXBElement<?> element = (JAXBElement<?>) obj;
                        String elementName = element.getName().getLocalPart();
                        Object elementValue = element.getValue();

                        if ("title".equals(elementName)) {
                            title = (String) elementValue;
                        } else if ("link".equals(elementName)) {
                            link = (String) elementValue;
                        } else if ("description".equals(elementName)) {
                            description = (String) elementValue;
                        }
                    }
                }

                // Format the description
                String[] descriptionLines = description.split("\\s+");
                description = String.join(" ", descriptionLines);

                System.out.println("Title: " + title);
                System.out.println("Link: " + link);
                System.out.println("Description: " + description);
                System.out.println();
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
