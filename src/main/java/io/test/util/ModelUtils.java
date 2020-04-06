package io.test.util;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.test.exception.BusinessException;
import io.test.exception.ErrorType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ModelUtils {

    private static ObjectMapper objectMapper;

    public static void setModelMapper(ObjectMapper objectMapper) {
        ModelUtils.objectMapper = objectMapper;
    }

    /**
     * 
     *<PRE>
     *<b>objectToJsonString</b>
     *</PRE>
     * method  : objectToJsonString
     * @param value
     * @return  String
     */ 
    public static String objectToJsonString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.warn(ExceptionUtils.getFullStackTrace(e));
            throw new BusinessException(ErrorType.UNCATHED_EXCEPTION);
        }
    }

    /**
     * 
     *<PRE>
     *<b>jsonStringToObject</b>
     *</PRE>
     * method  : jsonStringToObject
     * @param <T>
     * @param json
     * @param typeReference
     * @return  T
     */ 
    public static <T> T jsonStringToObject(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            log.warn(ExceptionUtils.getFullStackTrace(e));
            throw new BusinessException(ErrorType.UNCATHED_EXCEPTION);
        }
    }

    /**
     * 
     *<PRE>
     *<b>jsonStringToNode</b>
     *</PRE>
     * method  : jsonStringToNode
     * @param json
     * @return  JsonNode
     */ 
    public static JsonNode jsonStringToNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            log.warn(ExceptionUtils.getFullStackTrace(e));
            throw new BusinessException(ErrorType.UNCATHED_EXCEPTION);
        }
    }

    /**
     * 
     *<PRE>
     *<b>BeanUtils.copyProperties() </b>
     *</PRE>
     * method  : map
     * @param <T>
     * @param source
     * @param destinationType
     * @return  T
     */ 
    public static <T> T map(Object source, Class<T> destinationType) {
        try {
            T newTarget = destinationType.newInstance();
            BeanUtils.copyProperties(source, newTarget);
            return newTarget;
        } catch (InstantiationException|IllegalAccessException e) {
            log.warn(ExceptionUtils.getFullStackTrace(e));
            throw new BusinessException(ErrorType.UNCATHED_EXCEPTION);
        }
    }

    /**
     * 
     *<PRE>
     *<b>convertStringToXMLDocument</b>
     *</PRE>
     * method  : convertStringToXMLDocument
     * @param xmlString
     * @return  Document
     */ 
    public static Document convertStringToXMLDocument(String xmlString) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            log.error("ModelUtils convertStringToXMLDocument : " + ExceptionUtils.getFullStackTrace(e));
            return null;
        }
   }
}
