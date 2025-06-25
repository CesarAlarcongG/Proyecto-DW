package com.proyecto.RegistroEgresados_Web.util;

import com.proyecto.RegistroEgresados_Web.persistence.model.Egresado;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ExcelExporter {

    public byte[] exportarEgresado(List<Egresado> egresados) throws IOException {
        Workbook workbook = new SXSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        CellStyle fechaEstilo = workbook.createCellStyle();
        fechaEstilo.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        Sheet hoja = workbook.createSheet("Egresados");

        Row encabezado = hoja.createRow(0);
        encabezado.createCell(0).setCellValue("egresado_id");
        encabezado.createCell(1).setCellValue("nombre");
        encabezado.createCell(2).setCellValue("apellido");
        encabezado.createCell(3).setCellValue("email");
        encabezado.createCell(4).setCellValue("carrera");
        encabezado.createCell(5).setCellValue("Fecha de nacimiento");
        encabezado.createCell(6).setCellValue("Fecha de ingreso");
        encabezado.createCell(7).setCellValue("Fecha de egreso");
        encabezado.createCell(8).setCellValue("ponderado");

        int fila = 1;

        for (Egresado egresado : egresados) {
            Row row = hoja.createRow(fila++);

            row.createCell(0).setCellValue(egresado.getId());
            row.createCell(1).setCellValue(egresado.getNombre());
            row.createCell(2).setCellValue(egresado.getApellido());
            row.createCell(3).setCellValue(egresado.getEmail());
            row.createCell(4).setCellValue(egresado.getCarrera());

            Cell fechaNacimientoCell = row.createCell(5);
            fechaNacimientoCell.setCellValue(egresado.getFechaNacimiento());
            fechaNacimientoCell.setCellStyle(fechaEstilo);

            Cell fechaIngresoCell = row.createCell(6);
            fechaIngresoCell.setCellValue(egresado.getFechaIngreso());
            fechaIngresoCell.setCellStyle(fechaEstilo);

            Cell fechaEgresoCell = row.createCell(7);
            fechaEgresoCell.setCellValue(egresado.getFechaEgreso());
            fechaEgresoCell.setCellStyle(fechaEstilo);

            row.createCell(8).setCellValue(egresado.getPonderado());
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }


}
