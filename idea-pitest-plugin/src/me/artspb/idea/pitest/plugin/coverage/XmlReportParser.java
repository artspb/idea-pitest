package me.artspb.idea.pitest.plugin.coverage;

import com.intellij.rt.coverage.data.ClassData;
import com.intellij.rt.coverage.data.LineData;
import com.intellij.rt.coverage.data.ProjectData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
public class XmlReportParser {

    private static final String REPORT_FILENAME = "mutations.xml";

    private static final String MUTATION_TAG = "mutation";
    private static final String MUTATED_CLASS_TAG = "mutatedClass";
    private static final String DETECTED_ATTRIBUTE = "detected";
    private static final String LINE_NUMBER_TAG = "lineNumber";
    private static final String MUTATED_METHOD_TAG = "mutatedMethod";
    private static final String METHOD_DESCRIPTION_TAG = "methodDescription";

    public static void parseReport(ProjectData projectData, String reportDir) throws ParserConfigurationException, SAXException, IOException {
        File report = new File(reportDir, REPORT_FILENAME);
        if (report.exists()) {
            parseReport(projectData, report);
        }
    }

    public static void parseReport(ProjectData projectData, File report) throws ParserConfigurationException, SAXException, IOException {
        Map<String, Map<Integer, LineData>> classToLines = new HashMap<>();
        NodeList mutations = createDocument(report).getElementsByTagName(MUTATION_TAG);
        for (int i = 0; i < mutations.getLength(); i++) {
            Element mutation = (Element) mutations.item(i);
            parseMutation(classToLines, mutation);
        }
        populateProjectData(projectData, classToLines);
    }

    private static Document createDocument(File report) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(report);
    }

    private static void parseMutation(Map<String, Map<Integer, LineData>> classToLines, Element mutation) {
        String mutatedClass = getTextContent(mutation, MUTATED_CLASS_TAG);
        boolean detected = Boolean.parseBoolean(mutation.getAttribute(DETECTED_ATTRIBUTE));
        Integer lineNumber = Integer.valueOf(getTextContent(mutation, LINE_NUMBER_TAG));
        String mutatedMethod = getTextContent(mutation, MUTATED_METHOD_TAG);
        String methodDescription = getTextContent(mutation, METHOD_DESCRIPTION_TAG);
        LineData line = getLine(classToLines, mutatedClass, lineNumber, mutatedMethod, methodDescription);
        int hits = line.getHits();
        line.setHits(detected ? ++hits : hits);
    }

    private static String getTextContent(Element element, String tag) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

    private static LineData getLine(Map<String, Map<Integer, LineData>> classToLines, String mutatedClass,
                                    Integer lineNumber, String mutatedMethod, String methodDescription) {
        Map<Integer, LineData> lines = getLines(classToLines, mutatedClass);
        LineData line = lines.get(lineNumber);
        if (line == null) {
            line = new LineData(lineNumber, mutatedMethod + methodDescription);
            lines.put(lineNumber, line);
        }
        return line;
    }

    private static Map<Integer, LineData> getLines(Map<String, Map<Integer, LineData>> classToLines, String mutatedClass) {
        Map<Integer, LineData> lines = classToLines.get(mutatedClass);
        if (lines == null) {
            lines = new TreeMap<>();
            classToLines.put(mutatedClass, lines);
        }
        return lines;
    }

    private static void populateProjectData(ProjectData projectData, Map<String, Map<Integer, LineData>> classToLines) {
        for (Map.Entry<String, Map<Integer, LineData>> entry : classToLines.entrySet()) {
            ClassData classData = projectData.getOrCreateClassData(entry.getKey());
            classData.setLines(listToArray(entry.getValue().values()));
        }
    }

    private static LineData[] listToArray(Collection<LineData> list) {
        LineData[] lines = new LineData[maxLineNumber(list) + 1];
        for (LineData line : list) {
            lines[line.getLineNumber()] = line;
        }
        return lines;
    }

    private static int maxLineNumber(Collection<LineData> list) {
        int maxLineNumber = 0;
        for (LineData line : list) {
            int lineNumber = line.getLineNumber();
            maxLineNumber = maxLineNumber < lineNumber ? lineNumber : maxLineNumber;
        }
        return maxLineNumber;
    }
}
