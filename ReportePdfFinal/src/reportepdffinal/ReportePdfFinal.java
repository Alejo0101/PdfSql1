/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package reportepdffinal;



// 2. IMPORTS FALTANTES
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * @author Santiago
 */
public class ReportePdfFinal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Iniciando aplicación de reportes...");
        Connection conn = null;
        
        try {
            // 1. Obtener la conexión
            conn = ConexionDB.getConnection();
            
            if (conn != null) {
                // 2. Crear una instancia del generador
                GenerarReportes generador = new GenerarReportes();
                
                // 3. Llamar al método para crear el PDF
                generador.generarPDF(conn);
            } else {
                System.err.println("No se pudo establecer la conexión. El reporte no puede ser generado.");
            }

        } catch (Exception e) {
            System.err.println("Ocurrió un error en la aplicación principal:");
            e.printStackTrace();
        } finally {
            // 4. Cerrar la conexión, pase lo que pase
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Conexión cerrada.");
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión:");
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Aplicación finalizada.");
    }
    }
    

