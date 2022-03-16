package com.libreria.servicios;

import com.libreria.entidades.Editorial;
import com.libreria.errores.ErrorServicio;
import com.libreria.repositorios.EditorialRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional(rollbackFor = {Exception.class})
    public void agregar(String nombre, Boolean alta) throws ErrorServicio {

        validar(nombre, alta);
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setAlta(alta);

        editorialRepositorio.save(editorial);
    }
    @Transactional(rollbackFor = {Exception.class})
    public void modificar(String id, String nombre, Boolean alta) throws ErrorServicio {

        validar(nombre, alta);
        Editorial editorial = buscarPorId(id);
        editorial.setNombre(nombre);
        editorial.setAlta(alta);
        editorialRepositorio.save(editorial);

    }
    @Transactional(rollbackFor = {Exception.class})
    public void eliminar(String id) throws ErrorServicio {

        Editorial editorial = buscarPorId(id);

        editorialRepositorio.delete(editorial);

    }
     @Transactional(readOnly = true)
    public Editorial buscarPorId(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("No se encontró la editorial solicitada");
        }

    }

    public void validar(String nombre, Boolean alta) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("Ingrese un nombre válido");
        }
    }
}
