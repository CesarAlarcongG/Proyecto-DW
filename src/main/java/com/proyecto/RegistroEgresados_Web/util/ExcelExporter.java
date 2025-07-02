package com.proyecto.RegistroEgresados_Web.util;

import com.proyecto.RegistroEgresados_Web.persistence.model.Egresado;
import com.proyecto.RegistroEgresados_Web.persistence.model.ExperienciaLaboral;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
    public byte[] exportarEgresadosYExperienciaLaboral(List<Egresado> egresados) throws IOException {
        Workbook workbook = new SXSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        // Estilo para fechas
        CellStyle fechaEstilo = workbook.createCellStyle();
        fechaEstilo.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        Sheet hoja = workbook.createSheet("EgresadosConExperienciaLaboral");

        // Fila 0: Títulos visuales
        Row tituloRow = hoja.createRow(0);
        tituloRow.createCell(0).setCellValue("EGRESADOS");
        hoja.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

        tituloRow.createCell(9).setCellValue("EXPERIENCIA LABORAL");
        hoja.addMergedRegion(new CellRangeAddress(0, 0, 9, 12));

        // Fila 1: Encabezados
        Row encabezado = hoja.createRow(1);
        encabezado.createCell(0).setCellValue("ID");
        encabezado.createCell(1).setCellValue("Nombre");
        encabezado.createCell(2).setCellValue("Apellido");
        encabezado.createCell(3).setCellValue("Email");
        encabezado.createCell(4).setCellValue("Carrera");
        encabezado.createCell(5).setCellValue("Fecha de Nacimiento");
        encabezado.createCell(6).setCellValue("Fecha de Ingreso");
        encabezado.createCell(7).setCellValue("Fecha de Egreso");
        encabezado.createCell(8).setCellValue("Ponderado");

        encabezado.createCell(9).setCellValue("Empresa");
        encabezado.createCell(10).setCellValue("Cargo");
        encabezado.createCell(11).setCellValue("Inicio Laboral");
        encabezado.createCell(12).setCellValue("Fin Laboral");

        int fila = 2; // Inicia después de los títulos y encabezados

        for (Egresado egresado : egresados) {
            List<ExperienciaLaboral> experiencias = egresado.getIdExperienciaLaboral();
            if (experiencias == null || experiencias.isEmpty()) {
                Row row = hoja.createRow(fila++);
                llenarDatosEgresado(row, egresado, fechaEstilo);
            } else {
                for (ExperienciaLaboral exp : experiencias) {
                    Row row = hoja.createRow(fila++);
                    llenarDatosEgresado(row, egresado, fechaEstilo);
                    row.createCell(9).setCellValue(exp.getEmpresa());
                    row.createCell(10).setCellValue(exp.getCargo());

                    Cell ingresoLab = row.createCell(11);
                    ingresoLab.setCellValue(exp.getFechaIngreso());
                    ingresoLab.setCellStyle(fechaEstilo);

                    if (exp.getFechaSalida() != null) {
                        Cell salidaLab = row.createCell(12);
                        salidaLab.setCellValue(exp.getFechaSalida());
                        salidaLab.setCellStyle(fechaEstilo);
                    }
                }
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }


    private void llenarDatosEgresado(Row row, Egresado egresado, CellStyle fechaEstilo) {
        CellStyle estiloFecha = fechaEstilo;

        row.createCell(0).setCellValue(egresado.getId());
        row.createCell(1).setCellValue(egresado.getNombre());
        row.createCell(2).setCellValue(egresado.getApellido());
        row.createCell(3).setCellValue(egresado.getEmail());
        row.createCell(4).setCellValue(egresado.getCarrera());

        Cell nacimiento = row.createCell(5);
        nacimiento.setCellValue(egresado.getFechaNacimiento());
        nacimiento.setCellStyle(estiloFecha);

        Cell ingreso = row.createCell(6);
        ingreso.setCellValue(egresado.getFechaIngreso());
        ingreso.setCellStyle(estiloFecha);

        Cell egreso = row.createCell(7);
        egreso.setCellValue(egresado.getFechaEgreso());
        egreso.setCellStyle(estiloFecha);

        row.createCell(8).setCellValue(egresado.getPonderado());
    }


}
