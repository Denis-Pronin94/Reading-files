package quru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.pdftest.assertj.Assertions;
import com.codeborne.pdftest.matchers.ContainsExactText;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.hamcrest.MatcherAssert.assertThat;

public class WorksZip {

    ClassLoader classLoader = WorksZip.class.getClassLoader();

    @Test
    void zipParsingPDFTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/junit-user-guide-5.8.2pdf.zip"));
        try (ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("junit-user-guide-5.8.2pdf.zip"))) {
            ZipEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("junit-user-guide-5.8.2pdf.pdf");
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    PDF pdf = new PDF(inputStream);
                    Assertions.assertThat(pdf.numberOfPages).isEqualTo(166);
                    assertThat(pdf, new ContainsExactText("JUnit 5 User Guide"));
                }
            }
        }
    }

    @Test
    void zipParsingxlsTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/prajs_ot_2104xls.zip"));
        try (ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("prajs_ot_2104xls.zip"))) {
            ZipEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("prajs_ot_2104xls.xls");
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    XLS xls = new XLS(inputStream);
                            String stringCellValue = xls.excel.getSheetAt(0).getRow(31).getCell(2).getStringCellValue();
                    org.assertj.core.api.Assertions.assertThat(stringCellValue).contains("        Бумага для широкоформатных принтеров и чертежных работ");
                }
            }
        }
    }

    @Test
    void zipParsingCsvTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/business-price-indexes-december-2021-quarter-csv.zip"));
        try (ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("business-price-indexes-december-2021-quarter-csv.zip"))) {
            ZipEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("business-price-indexes-december-2021-quarter-csv.csv");
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        List<String[]> content = reader.readAll();
                        org.assertj.core.api.Assertions.assertThat(content).contains(new String[] {
                                "Series_reference",
                                "Period",
                                "Data_value",
                                "STATUS",
                                "UNITS",
                                "Subject",
                                "Group",
                                "Series_title_1",
                                "Series_title_2",
                                "Series_title_3",
                                "Series_title_4",
                                "Series_title_5"});
                    }
                }
            }
        }
    }
}

