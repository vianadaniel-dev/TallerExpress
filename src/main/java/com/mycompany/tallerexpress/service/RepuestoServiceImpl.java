
package com.mycompany.tallerexpress.service;

import com.mycompany.tallerexpress.exceptions.CodigoRepuestoUnicoException;
import com.mycompany.tallerexpress.exceptions.StockMayorIgualCeroException;
import com.mycompany.tallerexpress.model.Repuesto;
import com.mycompany.tallerexpress.repository.RepuestoRepository;
import java.util.List;


public class RepuestoServiceImpl implements RepuestoService {

    private final RepuestoRepository repuestoRepository;

    public RepuestoServiceImpl(RepuestoRepository repuestoRepository) {
        this.repuestoRepository = repuestoRepository;
    }

    @Override
    public Repuesto guardar(Repuesto repuesto) throws CodigoRepuestoUnicoException, StockMayorIgualCeroException {
        // Validar regla de negocio de stock
        if (repuesto.getStockTotal() < 0 || repuesto.getStockDisponible() < 0) {
            throw new StockMayorIgualCeroException("El stock no puede ser un valor negativo.");
        }
        return repuestoRepository.guardar(repuesto);
    }

    @Override
    public boolean actualizar(Repuesto repuesto) throws CodigoRepuestoUnicoException, StockMayorIgualCeroException {
        if (repuesto.getStockTotal() < 0 || repuesto.getStockDisponible() < 0) {
            throw new StockMayorIgualCeroException("El stock no puede ser un valor negativo.");
        }
        return repuestoRepository.actualizar(repuesto);
    }

    @Override
    public List<Repuesto> listar() {
        return repuestoRepository.listar();
    }

    @Override
    public List<Repuesto> listarPorCategoria(String categoria) {
        return repuestoRepository.listarPorCategoria(categoria);
    }

    @Override
    public List<Repuesto> listarPorProveedor(String proveedor) {
        return repuestoRepository.listarPorProveedor(proveedor);
    }
}