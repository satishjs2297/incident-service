package com.service.api.controller;


import com.service.api.pdf.convert.Converter;
import com.service.api.pdf.convert.DocxToPDFConverter;
import com.service.api.utils.Docx4JSRUtil;
import lombok.extern.java.Log;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log
public class DocTemplateProcessController {


    @GetMapping(value= "/genpdf", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> pdfGenerator() {

        try {

            Map<String, String> placeholderMap = new HashMap<>();
            placeholderMap.put("${NAME}", "Rao Gadu");
            placeholderMap.put("${SURNAME}", "Thumsi");
            placeholderMap.put("${PLACE_OF_BIRTH}", "USA");
            placeholderMap.put("${JOB_DUTIES}", "Test123456");

            WordprocessingMLPackage sourceDocxDoc = WordprocessingMLPackage.load(
                    new File("C:/Users/satis/Downloads/incident-svc-api/src/main/resources/source2.docx"));
            Docx4JSRUtil.searchAndReplace(sourceDocxDoc, placeholderMap);

            ByteArrayOutputStream targetOutputStream = new ByteArrayOutputStream();
            sourceDocxDoc.save(targetOutputStream);

            ByteArrayInputStream inStream = new ByteArrayInputStream(targetOutputStream.toByteArray());

            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            Converter converter = new DocxToPDFConverter(inStream, pdfOutputStream, false, true);

            converter.convert();

            ByteArrayResource resource = new ByteArrayResource(pdfOutputStream.toByteArray());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

        } catch (Exception ex) {
            log.warning("Failed to generate pdf ::"+ ex);
        }

        return null;

    }

    protected static InputStream getInFileStream(String inputFilePath) throws FileNotFoundException {
        File inFile = new File(inputFilePath);
        FileInputStream iStream = new FileInputStream(inFile);
        return iStream;
    }

    protected static OutputStream getOutFileStream(String outputFilePath) throws IOException{
        File outFile = new File(outputFilePath);

        try{
            //Make all directories up to specified
            outFile.getParentFile().mkdirs();
        } catch (NullPointerException e){
            //Ignore error since it means not parent directories
        }

        outFile.createNewFile();
        FileOutputStream oStream = new FileOutputStream(outFile);
        return oStream;
    }
}
