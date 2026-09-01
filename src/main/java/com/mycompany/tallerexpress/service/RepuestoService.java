
package com.mycompany.tallerexpress.service;

import com.mycompany.tallerexpress.exceptions.CodigoRepuestoUnicoException;
import com.mycompany.tallerexpress.exceptions.StockMayorIgualCeroException;
import com.mycompany.tallerexpress.model.Repuesto;
import java.util.List;

public interface RepuestoService {
    Repuesto guardar(Repuesto repuesto) throws CodigoRepuestoUnicoException, StockMayorIgualCeroException;
    boolean actualizar(Repuesto repuesto) throws CodigoRepuestoUnicoException, StockMayorIgualCeroException;
    List<Repuesto> listar();
    List<Repuesto> listarPorCategoria(String categoria);
    List<Repuesto> listarPorProveedor(String proveedor);
}