package com.service.api.controller;


import com.service.api.pdf.convert.Converter;
import com.service.api.pdf.convert.DocxToPDFConverter;
import com.service.api.utils.Docx4JSRUtil;
import lombok.extern.java.Log;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log
public class DocTemplateProcessController {


    @GetMapping("/genpdf")
    public String pdfGenerator() {

        try {

            Map<String, String> placeholderMap = new HashMap<>();
            placeholderMap.put("${NAME}", "Rao Gadu");
            placeholderMap.put("${SURNAME}", "Thumsi");
            placeholderMap.put("${PLACE_OF_BIRTH}", "USA");
            placeholderMap.put("${JOB_DUTIES}", "Test123456");

            WordprocessingMLPackage sourceDocxDoc = WordprocessingMLPackage.load(
                    new File("C:/Users/satis/Downloads/incident-svc-api/src/main/resources/source2.docx"));
            Docx4JSRUtil.searchAndReplace(sourceDocxDoc, placeholderMap);

            File exportFile = new File("C:/Users/satis/Downloads/incident-svc-api/target2.docx");
            sourceDocxDoc.save(exportFile);

            InputStream inStream = getInFileStream("C:/Users/satis/Downloads/incident-svc-api/target2.docx");
            OutputStream outStream = getOutFileStream("C:/Users/satis/Downloads/incident-svc-api/output.pdf");

            Converter converter = new DocxToPDFConverter(inStream, outStream, false, true);

            converter.convert();

        } catch (Exception ex) {
            log.warning("Failed to generate pdf ::"+ ex);
        }


        return "Pdf successfully generated.";
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
