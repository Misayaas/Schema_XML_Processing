import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

public class XMLValidate {
    public static void main(String[] args) {
        String xml1 ="src/main/xmls/xml1.xml";
        String xsd1 = "src/main/xsds/studentMessage.xsd";
        String xml2 = "src/main/xmls/xml2.xml";
        String xsd2 = "src/main/xsds/present.xsd";
        validateXMLByXSD(xml1,xsd1);
        validateXMLByXSD(xml2,xsd2);
    }

    public static void validateXMLByXSD(String xml, String xsd){
        String xmlFileName = xml;
        String xsdFileName = xsd;
        try {
            XMLErrorHandler errorHandler = new XMLErrorHandler();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // 解析器验证XML内容
            factory.setValidating(true);
            factory.setNamespaceAware(true);

            SAXParser parser = factory.newSAXParser();
            SAXReader xmlReader = new SAXReader();
            Document xmlDocument = (Document) xmlReader.read(new File(xmlFileName));
            parser.setProperty(
                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
            parser.setProperty(
                    "http://java.sun.com/xml/jaxp/properties/schemaSource",
                    "file:" + xsdFileName);
            SAXValidator validator = new SAXValidator(parser.getXMLReader());
            validator.setErrorHandler(errorHandler);
            validator.validate(xmlDocument);

            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            if (errorHandler.getErrors().hasContent()) {
                System.out.println("XML文件" + xmlFileName + "通过XSD文件校验失败");
                writer.write(errorHandler.getErrors());
            } else {
                System.out.println("XML文件" + xmlFileName + "通过XSD文件校验成功");
            }
        } catch (Exception e) {
            System.out.println("XML文件" + xmlFileName + "通过XSD文件校验失败");
            e.printStackTrace();
        }
    }
}
