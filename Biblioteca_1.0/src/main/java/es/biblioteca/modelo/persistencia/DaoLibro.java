package es.biblioteca.modelo.persistencia;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import es.biblioteca.modelo.entidad.Libro;


/**
 * Patron DAO (Data Access Object), objeto que se encarga de hacer las consultas
 * a algun motor de persistencia (BBDD, Ficheros, etc). En este caso vamos a 
 * simular que los datos estan guardados en una BBDD trabajando con una lista
 * de objetos cargada en memoria para simplificar el ejemplo.
 * 
 * Hay que tener en cuenta que para simplificar el ejemplo tambien se ha hecho
 * que el ID con el que se dan de alta las personas en la lista coincide exactamente
 * con la posicion del array que ocupan.
 * 
 * Mediante la anotacion @Component, damos de alta un unico objeto de esta clase
 * dentro del contexto de Spring, su ID sera el nombre de la case en notacion
 * lowerCamelCase
 * 
 */
@Component
public class DaoLibro {
	
	public List<Libro> libros;
	
	/**
	 * Cuando se cree el objeto dentro del contexto de Spring, se ejecutara
	 * su constructor, que creara las personas y las metera en una lista
	 * para que puedan ser consumidas por nuestros clientes
	 */
	public DaoLibro() {
		
		System.out.println("");
		System.out.println("DaoPersona -> Creando la lista de libros!");
		libros = new ArrayList<Libro>();
		Libro l1 = new Libro(1, "The Adventures of Java", "Coding House", "A classic in programming");
		Libro l2 = new Libro(2, "Programming in the Rain", "Tech World", "An inspiring journey into coding");
		Libro l3 = new Libro(3, "Code Chronicles", "Geeky Publications", "Unveiling the secrets of programming");
		Libro l4 = new Libro(4, "The Java Saga", "Code Masters", "A thrilling tale of software development");
		Libro l5 = new Libro(5, "Mastering SQL", "Database Wizards", "Unlocking the power of databases");
		libros.add(l1);
		libros.add(l2);
		libros.add(l3);
		libros.add(l4);
		libros.add(l5);
		System.out.println("DaoPersona -> Lista de libros creada");
		System.out.println("");
	}
	
	/**
	 * 
	 * Devuelve un libro a partir de su id
	 * @param id
	 * @return el libro con esa id, null en caso de
	 * que no exista
	 */
	public Libro get (int id) {
		
		try {
			int position = libros.indexOf(new Libro(id));
			return libros.get(position);
		} catch (IndexOutOfBoundsException iobe) {
			return null;
		}
	}
	
	/**
	 * Metodo que devuelve todos los libros del array
	 * @return una lista con todos los libros del array
	 */
	public List<Libro> list() {
		return libros;
	}
	
	/**
	 * Metodo que introduce un libro
	 * @param l el libro que queremos introducir
	 */
	public int add(Libro l) {
		
        if (!tituloLibroExiste(l) && libros.indexOf(l)==-1) {
            libros.add(l);
            return 1;
        }        
        else {
            return -1;
        }
	}
	
	/**
	 * Borramos un libro de una posicion del array
	 * @param posicion la posicion a borrar
	 * @return devolvemos el libro que hemos quitado del array, 
	 * o null en caso de que no exista.
	 */
	public Libro delete(int id) {
		
		int position = libros.indexOf(new Libro(id));
		try {
			return libros.remove(position);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	/**
	 * Metodo que modifica un libro de una posicion del array
	 * @param p contiene todos los datos que queremos modificar, pero 
	 * p.getId() contiene la posicion del array que queremos eliminar
	 * @return el libro modificado en caso de que exista, null en caso
	 * contrario
	 */
	public Libro update(Libro l) {
		
		try {
			int position = libros.indexOf(new Libro(l.getId()));
			Libro lAux = libros.get(position);
			lAux.setTitulo(l.getTitulo());
			lAux.setEditorial(l.getEditorial());
			lAux.setNota(l.getNota());
			return lAux;
		} catch (IndexOutOfBoundsException iobe) {
			return null;
		}
		/*
		int position = libros.indexOf(l);
		
		Libro lAux = libros.get(position);		
        if (position!=-1) {
			lAux.setTitulo(l.getTitulo());
			lAux.setEditorial(l.getEditorial());
			lAux.setNota(l.getNota());
        } else {
            System.out.println("Un libro con el mismo ID ya existe.");
        }

		return lAux;*/

	}
	
	/**
	 * Metodo que devuelve todos los libros que contienen
	 * las palabras introducidas en el título. Como puede
	 * haber varios libros con esas palabra tengo que
	 * devolver una lista con todas los libros encontrados.
	 * @param nombre representa las palabras por la que vamos a hacer la
	 * busqueda
	 * @return una lista con los libros coincidentes.
	 * La lista estará vacia en caso de que no hay coincidencias
	 */
	public List<Libro> listByTitulo(String titulo){
		
		List<Libro> librosAux = new ArrayList<Libro>();
		for(Libro l : libros) {
			if(l.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {//contains()
				librosAux.add(l);
			}
		}
		return librosAux;
	}
	
    /**
     * Verifica si el libro ya existe en la lista por título
     * @param libro el libro a verificar
     * @return true si el libro ya existe, false en caso contrario
     */
    private boolean tituloLibroExiste(Libro libro) {
        for (Libro l : libros) {
            if (l.getTitulo().equals(libro.getTitulo())) {
                return true;
            }
        }
        return false;
    }
}

