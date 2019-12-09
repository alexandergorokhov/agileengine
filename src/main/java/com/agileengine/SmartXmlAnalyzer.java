package com.agileengine;

import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Log
public class SmartXmlAnalyzer {
    private static Logger LOGGER = Logger.getLogger(String.valueOf(SmartXmlAnalyzer.class));
    private static String CHARSET_NAME = "utf8";
    private static final String targetElementId = "make-everything-ok-button";

    public static void main(String[] args) {

        String resourcePathOrigin = args[0];
        String resourcePathNext = args[1];

        Optional<Document> doc = getDocument(new File(resourcePathOrigin));
        Optional<Element> result = Optional.empty();

        if (doc.isPresent()) {
            Element buttonOpt = Optional.ofNullable(doc.get().getElementById(targetElementId)).orElseThrow(IllegalArgumentException::new);
            result = findElementInAnotherPage(new File(resourcePathNext), buttonOpt);
        }

        System.out.println(result.get());
    }

    private static Optional<Element> findElementInAnotherPage(File htmlFile, Element buttonOpt) {
        try {
            Document doc = getDocument(htmlFile).orElseThrow(IOException::new);
            Elements anchors = doc.select("a");
            List<Element> result = anchors
                    .stream()
                    .filter(element ->
                            isRelPresentAndSameText(element)
                                    ||
                                    isSameClassAndParentParentClass(element, buttonOpt))
                    .collect(Collectors.toList());

            return Optional.of(result.get(0));

        } catch (IOException e) {

            LOGGER.warning("Error reading [{}] file" + htmlFile.getAbsolutePath().toString() + e.getMessage());
            return Optional.empty();
        }

    }

    private static boolean isRelPresentAndSameText(Element element) {
        return (!element.attr("rel").equals("")
                &&
                element.childNodes().get(0).toString().equals(" Make everything OK ")
        );
    }

    private static boolean isSameClassAndParentParentClass(Element element, Element buttonOpt) {
        return element.attr("class").equals(buttonOpt.attr("class"))
                &&
                element.parent().parent().attr("class")
                        .equals(buttonOpt.parent().parent().attr("class"));
    }

    private static Optional<Document> getDocument(File htmlFile) {
        Document doc = null;
        try {
            doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());
            return Optional.of(doc);
        } catch (IOException e) {
            LOGGER.warning("Error reading [{}] file" + htmlFile.getAbsolutePath().toString() + e.getMessage());
            return Optional.empty();
        }
    }

}
