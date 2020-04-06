package io.test.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;

import io.test.exception.BusinessException;
import io.test.exception.ErrorType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {

    public static ResponseEntity<Object> createSuccessResponseEntity(Object data) {

        HttpStatus httpStatus = HttpStatus.OK;

        // @formatter:off
        ResBody<Object> resBody = ResBody.builder()
                .message(httpStatus.getReasonPhrase())
                .status(httpStatus.value())
                .data(data)
                .build();
        // @formatter:on

        return new ResponseEntity<Object>(resBody, httpStatus);
    }

    public static ResponseEntity<Object> createErrorResponseEntity(HttpStatus httpStatus, ErrorType errorType) {
        return ResponseUtils.createErrorResponseEntity(httpStatus, errorType, null);
    }

    public static ResponseEntity<Object> createErrorResponseEntity(HttpStatus httpStatus, ErrorType errorType,
            Object data) {

        // @formatter:off
        ResBody<Object> resBody = ResBody.builder()
                .code(errorType.getErrorCode())
                .data(data)
                .message(errorType.getMessage())
                .status(httpStatus.value())
                .build();
        // @formatter:on

        return new ResponseEntity<Object>(resBody, httpStatus);
    }

    public static void createErrorResponseEntityAndPopulate(HttpStatus httpStatus, ErrorType errorType,
            HttpServletResponse servletResponse) throws IOException {
        ResponseUtils.createErrorResponseEntityAndPopulate(httpStatus, errorType, null, servletResponse);
    }

    public static void createErrorResponseEntityAndPopulate(HttpStatus httpStatus, ErrorType errorType, Object data,
            HttpServletResponse servletResponse) throws IOException {

        // @formatter:off
        ResBody<Object> resBody = ResBody.builder()
                .code(errorType.getErrorCode())
                .data(data)
                .message(errorType.getMessage())
                .status(httpStatus.value())
                .build();
        // @formatter:on

        ResponseUtils.populateResponse(new ResponseEntity<String>(ModelUtils.objectToJsonString(resBody), httpStatus),
                servletResponse);
    }

    public static void populateResponse(ResponseEntity<String> responseEntity, HttpServletResponse servletResponse)
            throws IOException {

        if (servletResponse.isCommitted()) {
            throw new BusinessException(ErrorType.UNCATHED_EXCEPTION);
        }

        for (Map.Entry<String, List<String>> header : responseEntity.getHeaders().entrySet()) {
            String chave = header.getKey();
            for (String valor : header.getValue()) {
                servletResponse.addHeader(chave, valor);
            }
        }

        servletResponse.setStatus(responseEntity.getStatusCodeValue());
        servletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter printWriter = servletResponse.getWriter();
        printWriter.write(responseEntity.getBody());
        printWriter.flush();
        printWriter.close();
    }

    public static ResponseEntity<byte[]> downloadableResponse (byte[] bytes, String fileName) throws IOException {

        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    public static ResponseEntity<byte[]> downloadableResponseFromFile(File file) {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){

            byte[] bytes = Files.toByteArray(file);

            file.delete();

            String fileName = URLEncoder.encode(file.getName(), "UTF-8").replaceAll("\\+", "%20");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", fileName);

            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);

        } catch (IOException e) {
            log.error("ResponseUtils downloadableResponseFromFile() : " + ExceptionUtils.getFullStackTrace(e));
            throw new BusinessException(ErrorType.UNCATHED_EXCEPTION);
        }

    }
}
