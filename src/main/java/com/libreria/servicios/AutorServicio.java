package com.libreria.servicios;

import com.libreria.entidades.Autor;
import com.libreria.errores.ErrorServicio;
import com.libreria.repositorios.AutorRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;
    @Transactional(rollbackFor = {Exception.class}) // Esta anotación lo que hace es que si todo sale bien corre el método, si salta alguna excepción, vuelve al último punto donde no hubo errores
    public void agregar(String nombre, Boolean alta) throws ErrorServicio {

        validar(nombre, alta);

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(alta);

        autorRepositorio.save(autor);
    }

    
    @Transactional(rollbackFor = {Exception.class})
    public void modificar(String id, String nombre, Boolean alta) throws ErrorServicio {

//        Optional<Autor> respuesta = autorRepositorio.findById(id);
//        if (respuesta.isPresent()) {
//            Autor autor = respuesta.get();
        Autor autor = buscarPorId(id);
        validar(nombre, alta);
        autor.setNombre(nombre);
        autor.setAlta(alta);

        autorRepositorio.save(autor);
//            }else {
//            throw new ErrorServicio("No se encontró el autor solicitado");
    }
    @Transactional(rollbackFor = Exception.class)
    public void eliminar(String id) throws ErrorServicio {

        Optional<Autor> respuesta = autorRepositorio.findById(id);

//        if (respuesta.isPresent()) {
        Autor autor = buscarPorId(id);

        autorRepositorio.delete(autor);
//        } else {
//            throw new ErrorServicio("No se encontró el autor solicitado");
//        }
    }

    public void validar(String nombre, Boolean alta) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("Ingrese un nombre válido");
        }

    }
    
    //Se crea un método para buscar por ID y no repetir el mismo código en los demás métodos que lo utilizan
     @Transactional(readOnly = true) //Esta anotación hace que sólo lea en la base de datos sin generar cambios
    public Autor buscarPorId(String idAutor) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(idAutor);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("No se encontró el autor solicitado");
        }
    }

}
