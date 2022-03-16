package com.libreria.servicios;

import com.libreria.entidades.Autor;
import com.libreria.entidades.Editorial;
import com.libreria.entidades.Libro;
import com.libreria.errores.ErrorServicio;
import com.libreria.repositorios.LibroRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {
//No usar repositorios de otros objetos, sólo del correspondiente

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @Transactional(rollbackFor = {Exception.class})
    public void agregar(String isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, Boolean alta, String idAutor, String idEditorial) throws Exception {

        Integer ejemplaresPrestado = 0;
        validar(isbn, titulo, anio, ejemplares, ejemplaresPrestados, alta, idAutor, idEditorial);

        Libro libro = new Libro();

        Autor autor = autorServicio.buscarPorId(idAutor);
        Editorial editorial = editorialServicio.buscarPorId(idEditorial);
        libro.setAutor(autor);

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(libro.getEjemplares() - libro.getEjemplaresPrestados());
        libro.setAlta(true);

        libroRepositorio.save(libro);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void modificar(String id, String isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, Boolean alta, String idAutor, String idEditorial) throws ErrorServicio {

        validar(isbn, idAutor, anio, ejemplares, ejemplaresPrestados, alta, idAutor, idEditorial);
        Libro libro = buscarPodId(id);
        Autor autor = autorServicio.buscarPorId(idAutor);
        Editorial editorial = editorialServicio.buscarPorId(idEditorial);
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(libro.getEjemplares() - libro.getEjemplaresPrestados());
        libro.setAlta(alta);

        libroRepositorio.save(libro);

    }

    @Transactional(rollbackFor = {Exception.class})
    public void eliminar(String id) throws ErrorServicio {
        Libro libro = buscarPorId(id);

        libroRepositorio.delete(libro);
    }
    @Transactional(readOnly = true)
    public Libro buscarPorId(String id) throws ErrorServicio {

        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("No se encontró el libro solicitado");
        }

    }

    public void validar(String isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Boolean alta, String idAutor, String idEditorial) throws ErrorServicio {
        if (isbn == null || isbn.isEmpty()) {
            throw new ErrorServicio("Ingrese un ISBN válido");
        }
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("Ingrese un título válido");
        }
        if (anio <= 0 || anio == null) {
            throw new ErrorServicio("Ingrese un anio válido");
        }
        if (ejemplares < 0 || ejemplares == null) {
            throw new ErrorServicio("Ingrese una cantidad de ejemplares válida");
        }
        if (ejemplaresPrestados < 0 || ejemplaresPrestados == null) {
            throw new ErrorServicio("Ingrese una cantidad de ejemplares prestados válida");
        }
        if (idAutor == null || idAutor.isEmpty()) {
            throw new ErrorServicio("Ingrese un ID del autor válido");
        }
        if (idEditorial == null || idEditorial.isEmpty()) {
            throw new ErrorServicio("Ingrese un ID de una editorial válido");
        }
    }

    @Transactional(readOnly = true)
    private Libro buscarPodId(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
