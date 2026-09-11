package com.example.batchexporter.service;

import java.util.List;

public interface ExportService {

    String getName();

    List<?> fetchData();

    Object wrapForExport(List<?> items);
}
