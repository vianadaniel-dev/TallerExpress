/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress;

import com.mycompany.tallerexpress.exceptions.CodigoRepuestoUnicoException;
import com.mycompany.tallerexpress.exceptions.StockMayorIgualCeroException;
import com.mycompany.tallerexpress.model.Repuesto;
import com.mycompany.tallerexpress.service.RepuestoService;
import com.mycompany.tallerexpress.service.RepuestoServiceImpl;
import java.util.List;

/**
 *
 * @author unknown
 */
public class RepuestoHttpLoggerDecorator implements RepuestoService {

    public RepuestoHttpLoggerDecorator(RepuestoServiceImpl repuestoServiceImpl) {
    }

    @Override
    public Repuesto guardar(Repuesto repuesto) throws CodigoRepuestoUnicoException, StockMayorIgualCeroException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean actualizar(Repuesto repuesto) throws CodigoRepuestoUnicoException, StockMayorIgualCeroException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Repuesto> listar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Repuesto> listarPorCategoria(String categoria) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Repuesto> listarPorProveedor(String proveedor) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
