package codestats;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;

public class XMLReportWriter {
    public void writeReport(ProjectReport report,
                            ProjectTree tree,
                            Mode mode,
                            boolean showReport) throws Exception {
        if (showReport) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            HashMap<String, String> stats = report.getStats();

            stats.forEach((key, value) -> {
                Element stat = doc.createElement(key.replace(' ', '_'));
                rootElement.appendChild(stat);
                stat.appendChild(doc.createTextNode(value));
            });


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("report.xml"));
            transformer.transform(source, result);
        }
    }
}
