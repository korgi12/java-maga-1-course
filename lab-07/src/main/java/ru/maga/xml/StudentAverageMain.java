package ru.maga.xml;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public final class StudentAverageMain {
    private StudentAverageMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: StudentAverageMain input.xml output.xml");
        }
        Path input = Path.of(args[0]).toAbsolutePath().normalize();
        Path output = Path.of(args[1]).toAbsolutePath().normalize();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "file");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        var builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new DefaultHandler() {
            @Override
            public void error(SAXParseException exception) throws SAXException {
                throw exception;
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }
        });

        Document document = builder.parse(input.toFile());
        Element student = document.getDocumentElement();
        if (!"student".equals(student.getTagName())) {
            throw new IllegalArgumentException("Expected student element");
        }

        NodeList subjects = student.getElementsByTagName("subject");
        int count = subjects.getLength();
        if (count > 0) {
            int sum = 0;
            for (int i = 0; i < count; i++) {
                int mark = Integer.parseInt(((Element) subjects.item(i)).getAttribute("mark"));
                if (mark < 1 || mark > 5) {
                    throw new IllegalArgumentException("Invalid mark: " + mark);
                }
                sum += mark;
            }

            String actual = BigDecimal.valueOf(sum)
                    .divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .toPlainString();
            NodeList averages = student.getElementsByTagName("average");
            Element average;
            if (averages.getLength() == 0) {
                average = document.createElement("average");
                student.appendChild(average);
            } else {
                average = (Element) averages.item(0);
            }
            average.setTextContent(actual);
            System.out.println(actual);
        }

        Files.createDirectories(output.getParent());
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        var transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        if (document.getDoctype() != null && document.getDoctype().getSystemId() != null) {
            Path dtd = input.getParent().resolve(document.getDoctype().getSystemId()).normalize();
            String reference;
            try {
                reference = output.getParent().relativize(dtd).toString().replace('\\', '/');
            } catch (IllegalArgumentException exception) {
                reference = dtd.toUri().toString();
            }
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, reference);
        }
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(output.toFile()));
    }
}
