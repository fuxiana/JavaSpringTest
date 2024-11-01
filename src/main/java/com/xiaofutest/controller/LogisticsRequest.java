package com.xiaofutest.controller;

import com.alibaba.fastjson2.JSON;
import com.xiaofutest.model.LogisticsDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :xs
 * @date : 2024/9/25
 */

@RestController
@RequestMapping("/logistics")
public class LogisticsRequest<IWorksheet> {

    @GetMapping("/list")
    public List<LogisticsDTO> list() throws ParserConfigurationException, IOException, SAXException {
        List<LogisticsDTO> list = new ArrayList<>();
        URL xmlPath = getClass().getClassLoader().getResource("requestTest.xml");
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlPath.openStream());
        Map<String, String> map = new HashMap<>();
        NodeList sonlist = doc.getElementsByTagName("book");
        for (int i = 0; i < sonlist.getLength(); i++){
            Element son = (Element)sonlist.item(i);
            for (Node node = son.getFirstChild(); node != null; node = node.getNextSibling()) {
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    String name = node.getNodeName();
                    String value = null != node.getFirstChild() ? node.getFirstChild().getNodeValue() : "";
                    map.put(name, value);
                }
            }
            LogisticsDTO logisticsDTO = JSON.parseObject(JSON.toJSONString(map), LogisticsDTO.class);
            list.add(logisticsDTO);
        }
        return list;
    }

    @PostMapping("/add")
    public boolean add(HttpServletRequest request, @RequestBody LogisticsDTO logisticsDTO ) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        URL xmlPath = getClass().getClassLoader().getResource("requestTest.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlPath.openStream());
        doc.getDocumentElement().normalize();

        NodeList sonlist = doc.getElementsByTagName("library");
        Node baseElement = sonlist.item(0);

        NodeList library = baseElement.getChildNodes();
        Node lastNode = library.item(library.getLength() - 1);

        DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
        Element root = doc.createElement("book");

        Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode(logisticsDTO.getTitle()));
        root.appendChild(title);

        Element author = doc.createElement("author");
        author.appendChild(doc.createTextNode(logisticsDTO.getAuthor()));
        root.appendChild(author);

        Element year = doc.createElement("year");
        year.appendChild(doc.createTextNode(logisticsDTO.getYear()));
        root.appendChild(year);

        if (lastNode != null) {
            baseElement.insertBefore(root, lastNode);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(baseElement);
            StreamResult streamResult = new StreamResult(String.valueOf(xmlPath));
            transformer.transform(domSource,streamResult);
            return true;
        }else {
            return false;
        }
    }

    @GetMapping("/getExcel")
    public List<List<String>> getExcel(HttpServletRequest request){
        URL xmlPath = getClass().getClassLoader().getResource("风险提醒清单Excel.xlsx");
        List<List<String>> lists = new ArrayList<>();
        try {
            InputStream is = new FileInputStream(URLDecoder.decode(xmlPath.getPath(),"UTF-8"));
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            // 获取sheet表
            Sheet sheet = workbook.getSheet("风险清单");
            for (Row row:sheet){
                List<String> objects = new ArrayList<>();
                for(Cell cell: row){
                    objects.add(cell.getStringCellValue());
                }
                lists.add(objects);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  lists;
    }
}
