
package com.mycompany.tallerexpress.exceptions;

import java.sql.SQLException;


public class CodigoRepuestoUnicoException extends RuntimeException{
    public CodigoRepuestoUnicoException(String message){
        super(message);
    }

    public CodigoRepuestoUnicoException(String string, SQLException e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
