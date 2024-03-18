package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVParserWriter;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args)throws ParserConfigurationException, IOException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String fileName2 = "data.xml";
        String fileName3 = "data1.json";
        String fileName4 = "data2.json";


        //Методы для выполнения первого задания.
        List<Employee> list = parseCSV(columnMapping, fileName);
        listToJson(list, fileName3);

        //Методы для выполнения второго задания.
        List<Employee> list1 = parseXML(fileName2);
        listToJson(list1, fileName4);

        //Методы для выполнения третьего задания.
        String result = readString(fileName3);
        List<Employee> list2 = jsonToList(result);
        System.out.println(list2);


    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void listToJson(List<Employee> list, String way) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {

        }.getType();
        String json = gson.toJson(list, listType);
        try (FileWriter fw = new FileWriter(way)) {
            fw.write(json);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String way) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(way));

            NodeList Nodelist = doc.getElementsByTagName("employee");

            for (int i = 0; i < Nodelist.getLength(); i++) {
                Node NodeEmployee = Nodelist.item(i);

                if (NodeEmployee.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) NodeEmployee;

                    NodeList fstNmElmntLst = element.getElementsByTagName("id");
                    Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
                    NodeList fstNm = fstNmElmnt.getChildNodes();
                    String ID = (fstNm.item(0)).getNodeValue();

                    NodeList lstNmElmntLst2 = element.getElementsByTagName("firstName");
                    Element lstNmElmnt2 = (Element) lstNmElmntLst2.item(0);
                    NodeList lstNm2 = lstNmElmnt2.getChildNodes();
                    String FirstName = (lstNm2.item(0)).getNodeValue();


                    NodeList lstNmElmntLst3 = element.getElementsByTagName("lastName");
                    Element lstNmElmnt3 = (Element) lstNmElmntLst3.item(0);
                    NodeList lstNm3 = lstNmElmnt3.getChildNodes();
                    String LastName = (lstNm3.item(0)).getNodeValue();


                    NodeList lstNmElmntLst4 = element.getElementsByTagName("country");
                    Element lstNmElmnt4 = (Element) lstNmElmntLst4.item(0);
                    NodeList lstNm4 = lstNmElmnt4.getChildNodes();
                    String Country = (lstNm4.item(0)).getNodeValue();


                    NodeList lstNmElmntLst5 = element.getElementsByTagName("age");
                    Element lstNmElmnt5 = (Element) lstNmElmntLst5.item(0);
                    NodeList lstNm5 = lstNmElmnt5.getChildNodes();
                    String Age = (lstNm5.item(0)).getNodeValue();


                    Employee employee = new Employee(Long.parseLong(ID), FirstName, LastName, Country, Integer.parseInt(Age));
                    list.add(employee);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static String readString(String way) {
        String read;
        String text = "";
        try (BufferedReader br = new BufferedReader(new FileReader(way))){
            while ((read = br.readLine()) != null) {
                text += read;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static List<Employee> jsonToList(String file) {
        List<Employee> list = new ArrayList<>();
        JsonParser jp = new JsonParser();
        JsonArray jsonArray = (JsonArray) jp.parse(file);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();

        for (int i = 0; i < jsonArray.size(); i++) {
            Employee employee = gson.fromJson(jsonArray.get(i), Employee.class);
            System.out.println(employee);
            list.add(employee);
        }
        return list;
    }
}