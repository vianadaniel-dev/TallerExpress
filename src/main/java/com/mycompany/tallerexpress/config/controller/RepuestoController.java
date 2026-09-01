
package com.mycompany.tallerexpress.config.controller;

import com.mycompany.tallerexpress.exceptions.CodigoRepuestoUnicoException;
import com.mycompany.tallerexpress.exceptions.StockMayorIgualCeroException;
import com.mycompany.tallerexpress.model.Repuesto;
import com.mycompany.tallerexpress.service.RepuestoService;
import java.util.List;

public class RepuestoController {

    private final RepuestoService repuestoService;

    public RepuestoController(RepuestoService repuestoService) {
        this.repuestoService = repuestoService;
    }

    public Repuesto guardar(Repuesto repuesto) throws CodigoRepuestoUnicoException, StockMayorIgualCeroException {
        return repuestoService.guardar(repuesto);
    }

    public boolean actualizar(Repuesto repuesto) throws CodigoRepuestoUnicoException, StockMayorIgualCeroException {
        return repuestoService.actualizar(repuesto);
    }

    public List<Repuesto> listar() {
        return repuestoService.listar();
    }

    public List<Repuesto> listarPorCategoria(String categoria) {
        return repuestoService.listarPorCategoria(categoria);
    }

    public List<Repuesto> listarPorProveedor(String proveedor) {
        return repuestoService.listarPorProveedor(proveedor);
    }
}
