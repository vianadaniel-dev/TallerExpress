
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.exceptions.CodigoRepuestoUnicoException;
import com.mycompany.tallerexpress.model.Repuesto;
import java.util.List;


public interface RepuestoRepository {
    Repuesto guardar(Repuesto repuesto) throws CodigoRepuestoUnicoException;
    boolean actualizar(Repuesto repuesto) throws CodigoRepuestoUnicoException;
    List<Repuesto> listar();
    List<Repuesto> listarPorCategoria(String categoria);
    List<Repuesto> listarPorProveedor(String proveedor);
}
