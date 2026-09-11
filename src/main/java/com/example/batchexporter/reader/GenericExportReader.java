package com.example.batchexporter.reader;

import com.example.batchexporter.service.ExportService;
import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;

public class GenericExportReader implements ItemReader<Object> {

    private final ExportService exportService;
    private Iterator<?> iterator;

    public GenericExportReader(ExportService exportService) {
        this.exportService = exportService;
    }

    @Override
    public Object read() {
        if (iterator == null) {
            List<?> data = exportService.fetchData();
            iterator = data.iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }
}
