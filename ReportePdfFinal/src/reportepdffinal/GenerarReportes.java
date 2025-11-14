package reportepdffinal;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

// Librería iText 5
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

public class GenerarReportes {

    public void generarPDF(Connection conn) {
        Document document = new Document(PageSize.A4);

        try {
            // Archivo de salida
            PdfWriter.getInstance(document, new FileOutputStream("ReporteAdmitidos.pdf"));
            document.open();

            // Fuentes
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font fontCuerpo = FontFactory.getFont(FontFactory.HELVETICA, 10);

            // ----- TÍTULO -----
            Paragraph titulo = new Paragraph("Reporte de Admitidos Unillanos", fontTitulo);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" "));

            // ================ REPORTE 1 ==================
            Paragraph subtitulo1 = new Paragraph("Reporte #1: Admitidos por Programa", fontSubtitulo);
            subtitulo1.setSpacingAfter(10);
            document.add(subtitulo1);

            PdfPTable tabla1 = new PdfPTable(2);
            tabla1.setWidthPercentage(100);

            tabla1.addCell(new PdfPCell(new Phrase("Programa", fontHeader)));
            tabla1.addCell(new PdfPCell(new Phrase("Cantidad", fontHeader)));

            String sql1 = "SELECT PROGRAMA, COUNT(*) AS Cantidad " +
                          "FROM admitidos " +
                          "GROUP BY PROGRAMA " +
                          "ORDER BY Cantidad DESC";

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql1)) {

                while (rs.next()) {
                    tabla1.addCell(new Phrase(rs.getString("PROGRAMA"), fontCuerpo));
                    tabla1.addCell(new Phrase(String.valueOf(rs.getInt("Cantidad")), fontCuerpo));
                }
            }

            document.add(tabla1);

          // ================ REPORTE 2 ==================
document.add(new Paragraph(" "));
Paragraph subtitulo2 = new Paragraph("Reporte #2: Hombres y Mujeres por Facultad", fontSubtitulo);
subtitulo2.setSpacingAfter(10);
document.add(subtitulo2);

PdfPTable tabla2 = new PdfPTable(3);
tabla2.setWidthPercentage(100);

tabla2.addCell(new PdfPCell(new Phrase("Facultad", fontHeader)));
tabla2.addCell(new PdfPCell(new Phrase("Hombres", fontHeader)));
tabla2.addCell(new PdfPCell(new Phrase("Mujeres", fontHeader)));

String sql2 = 
    "SELECT " +
    "CASE " +
    "   WHEN FACULTAD IN ('CIENCIAS AGROP. Y REC. NATURALES', 'CIENCIAS AGROPECUARIAS Y RECURSOS NATURALES') " +
    "       THEN 'CIENCIAS AGROPECUARIAS Y RECURSOS NATURALES' " +
    "   ELSE FACULTAD " +
    "END AS FACULTAD_UNIFICADA, " +
    "SUM(CASE WHEN GENERO = 'MASCULINO' THEN 1 ELSE 0 END) AS Hombres, " +
    "SUM(CASE WHEN GENERO = 'FEMENINO' THEN 1 ELSE 0 END) AS Mujeres " +
    "FROM admitidos " +
    "GROUP BY FACULTAD_UNIFICADA " +
    "ORDER BY FACULTAD_UNIFICADA";

try (Statement st = conn.createStatement();
     ResultSet rs = st.executeQuery(sql2)) {

    while (rs.next()) {
        tabla2.addCell(new Phrase(rs.getString("FACULTAD_UNIFICADA"), fontCuerpo));
        tabla2.addCell(new Phrase(String.valueOf(rs.getInt("Hombres")), fontCuerpo));
        tabla2.addCell(new Phrase(String.valueOf(rs.getInt("Mujeres")), fontCuerpo));
    }
}

document.add(tabla2);


            System.out.println("PDF 'ReporteAdmitidos.pdf' generado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
