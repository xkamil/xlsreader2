package org.util;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.stream.Collectors;

public final class WorkbookReader {
    private static final Logger LOGGER = Logger.getLogger(WorkbookReader.class);


    public static Map<String, Workbook> getAllWorkbooks(File directory) throws IOException {
        Map<String, Workbook> workbooks = new HashMap<>();
        List<File> files = getAllFilesFrom(directory);

        files.forEach(f -> fileToWorkbook(f).ifPresent(w -> workbooks.put(f.getName(), w)));

        return workbooks;
    }


    private static List<File> getAllFilesFrom(File directory) throws IOException {
        return Files.list(directory.toPath())
                .map(Path::toFile)
                .collect(Collectors.toList());
    }

    private static Optional<Workbook> fileToWorkbook(File file) {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(file);
        } catch (Exception ex) {
            LOGGER.warn(ex.getMessage());
        }

        return Optional.ofNullable(workbook);
    }
}
