package com.mycompany.tallerexpress;

import com.mycompany.tallerexpress.config.controller.OrdenServicioController;
import com.mycompany.tallerexpress.config.controller.RepuestoController;
import com.mycompany.tallerexpress.config.controller.UsuarioController;
import com.mycompany.tallerexpress.decorator.HttpLoggerDecorator;
import com.mycompany.tallerexpress.decorator.UsuarioDefaultValuesDecorator;
import com.mycompany.tallerexpress.repository.OrdenServicioRepository;
import com.mycompany.tallerexpress.repository.OrdenServicioRepositoryImpl;
import com.mycompany.tallerexpress.repository.RepuestoRepository;
import com.mycompany.tallerexpress.repository.RepuestoRepositoryImpl;
import com.mycompany.tallerexpress.repository.UsuarioRepository;
import com.mycompany.tallerexpress.repository.UsuarioRepositoryImpl;
import com.mycompany.tallerexpress.service.OrdenServicioService;
import com.mycompany.tallerexpress.service.OrdenServicioServiceImpl;
import com.mycompany.tallerexpress.service.RepuestoService;
import com.mycompany.tallerexpress.service.RepuestoServiceImpl;
import com.mycompany.tallerexpress.service.UsuarioService;
import com.mycompany.tallerexpress.service.UsuarioServiceImpl;
import com.mycompany.tallerexpress.view.OrdenServicioView;


public class TallerExpress {
    public static void main(String[] args) {
        // 1. Repositorios
        UsuarioRepository usuarioRepo = new UsuarioRepositoryImpl();
        RepuestoRepository repuestoRepository = new RepuestoRepositoryImpl();
        OrdenServicioRepository ordenRepo = new OrdenServicioRepositoryImpl();

        // 2. Servicios con Decoradores
        UsuarioService usuarioService = new HttpLoggerDecorator(
            new UsuarioDefaultValuesDecorator(
                new UsuarioServiceImpl(usuarioRepo)
            )
        );
        
        RepuestoService repuestoService = new RepuestoHttpLoggerDecorator(
            new RepuestoServiceImpl(repuestoRepository)
        );
        
        OrdenServicioService ordenService = new OrdenServicioServiceImpl(ordenRepo);

        // 3. Controllers
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        RepuestoController repuestoController = new RepuestoController(repuestoService);
        OrdenServicioController ordenController = new OrdenServicioController(ordenService);

        // 4. Vista (Pasas los controllers necesarios a las vistas)
        LoginView loginView = new LoginView(usuarioController);
        
        // Si el login es exitoso, abres la vista principal de órdenes
        boolean autenticado = loginView.mostrarPantallaLogin();
        
        if (autenticado) {
            OrdenServicioView vista = new OrdenServicioView(ordenController, repuestoController);
            vista.mostrarMenuOrdenes();
        } else {
            System.out.println("Acceso denegado. Cerrando el sistema...");
        }
    }
}