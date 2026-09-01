
package com.mycompany.tallerexpress.exceptions;

import java.sql.SQLException;


public class PlacaUnicaException extends RuntimeException {
   public PlacaUnicaException(String message){
       super(message);
   }

    public PlacaUnicaException(String string, SQLException e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
