package com.example.bfandemo.server.services;

import com.example.bfandemo.server.requestInfo.QueryObject;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QueryDeserializer {

  public static QueryObject deserialize(String requestBody) throws Exception {
    // String jsonString = "{\"activeResults\":{\"102017\":{\"type\":\"Alumn\",\"userId\":102017,\"name\":{\"firstName\":\"Scott\",\"lastName\":\"Camp\"},\"email\":\"camps1010@gmail.com\",\"phoneNumber\":\"2165380009\",\"location\":{\"country\":\"US\",\"state\":\"OH\",\"city\":\"Shaker Heights\"},\"graduationYear\":\"1993\",\"primaryPosition\":\"Quarterback\",\"linkedInPage\":\"Yes, Scott Camp\",\"favorites\":[],\"concentration\":\"Economics\",\"academicCertificates\":\"\",\"clubsOrActivities\":\"\",\"currentIndustry\":\"Finance - Asset Mgmt (pvt wealth)\",\"currentJob\":{\"title\":\"Portfolio Manager\",\"company\":\"Beese Fulmer Private Wealth Management \"},\"website\":\"Beese Fulmer\",\"gradDegree\":\"MBA\"},\"105031\":{\"type\":\"Alumn\",\"userId\":105031,\"name\":{\"firstName\":\"Mark\",\"lastName\":\"Callahan\"},\"email\":\"markfcallahan@gmail.com\",\"phoneNumber\":\"3152635707\",\"location\":{\"country\":\"US\",\"state\":\"MA\",\"city\":\"Boston\"},\"graduationYear\":\"2010\",\"primaryPosition\":\"O-Line\",\"linkedInPage\":\"https://www.linkedin.com/in/mfcallahan\",\"favorites\":[],\"concentration\":\"Business, Entrepreneurship, and Organizations\",\"academicCertificates\":\"\",\"clubsOrActivities\":\"\",\"currentIndustry\":\"Commercial Real Estate - Development\",\"currentJob\":{\"title\":\"Director of Development \",\"company\":\"Lincoln Property Company \"},\"website\":\"LPCBoston.com\",\"gradDegree\":\"MSRED\"}},\"query\":\"w\"}";

    ObjectMapper objectMapper = new ObjectMapper();
    QueryObject queryObject = objectMapper.readValue(requestBody, QueryObject.class);

    // Now you have your QueryObject
    // System.out.println(queryObject.activeResults);
    // System.out.println(queryObject.query);

    return queryObject;
  }
}
