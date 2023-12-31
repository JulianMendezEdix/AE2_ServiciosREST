package es.biblioteca.controlador;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.biblioteca.modelo.entidad.Libro;
import es.biblioteca.modelo.persistencia.DaoLibro;

//En este ejemplo vamos a realizar un CRUD completo contra la entidad
//Libro. La bbdd esta simulado en memoria.
@RestController
public class ControladorLibro {
	
	//Hay que tener en cuenta que normalmente tenemos objetos que dependen
	//de otros para hacer su trabajo. En este caso, el objeto de tipo
	//ControladorLibro que hemos dado de alta en el contexto de Spring
	//mediante la anotacion @RestController NECESITA un objeto de tipo
	//DaoLibro para realizar su trabajo (y que dimos de alta previamente
	//con la anotacion @Component)
	
	//La anotacion @Autowired se usa para hacer inyecciones de dependencias
	//de objetos dados de alta dentro del contexto de Spring. 
	//Cuando se cree este objeto (ControladorLibro) dentro del contexto
	//de Spring, mediante esta anotacion le diremos que inyecte un objeto
	//de tipo DaoLibro a la referencia "daoLibro", por lo que el objeto
	//Controlador libro quedará perfectamente formado.
	@Autowired
	private DaoLibro daoLibro;
	
	//GET LIBRO POR ID
	//En este primer ejemplo vamos a configurar endpoint(punto de acceso) para
	//devolver un libro por ID. Como nos marca REST, al ser una busqueda
	//por clave primaria, el ID debe ir como parte del PATH de la URL.
	//Esto lo hacemos por medio del atributo "path="libros/{id}"
	//Podemos obtener el ID usando la anotacion @PathVariable("id") dentro
	//de un parametro de entrada. El "id" se corresponde con el "{id}",
	//es decir, deben de llamarse igual.
	
	//Ademas, queremos que el resultado sea JSON. Spring Boot serializará
	//automaticamente el resultado a json a traves de las librerías Jackson
	//Esto lo hacemos mediante el atributo "produces". Recordemos que todas
	//las respuestas van dentro del BODY del mensaje HTTP.
	
	//Por ultimo, no nos olvidemos que tenemos que responder adecuadamente
	//con el codigo de respuesta apropieado, segun nos marca el protocolo HTTP.
	//Para ello usaremos la clase "ResponseEntity" que nos permite encapsular 
	//tanto el resultado en json como el codigo del mensaje. En este caso 
	//el codigo 200 "OK" si existe o 404 NOT FOUND si no existe
	
	//La URL para acceder a este metodo sería: 
	//"http://localhost:8080/libros/ID" y el metodo a usar seria GET
	//ID sería el identificador que queremos buscar
	@GetMapping(path="libros/{id}",produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<Libro> getLibro(@PathVariable("id") int id) {
		System.out.println("");
		System.out.println("ControladorLibro => Buscando libro con id: " + id);
		Libro l = daoLibro.get(id);
		if(l != null) {
			System.out.println("ControladorLibro => ENVIADO HttpStatus: 200 OK");
			System.out.println(l);
			return new ResponseEntity<Libro>(l,HttpStatus.OK);//200 OK
		}else {
			System.out.println("ControladorLibro => ENVIADO HttpStatus: 404 NOT_FOUND");
			System.out.println("ControladorLibro => Id de Libro no existe");
			return new ResponseEntity<Libro>(HttpStatus.NOT_FOUND);//404 NOT FOUND
		}
	}
	
	//POST 
	//En este caso vamos a dar de alta un libro, para ello usaremos
	//el metodo POST, vamos a producir tambien JSON (produces) y el 
	//formato que nos tiene que enviar el cliente tambien tiene que ser
	//JSON (consumes). El libro nos tiene que llegar sin ID, ya que
	//seremos nosotros quien le pongamos dicho ID.
	
	//Para obtener el libro que nos envie el cliente podemos usar
	//la anotacion @RequestBody en un parametro de entrada de tipo
	//Libro. Spring se encargará de deserializar automaticamente
	//el json.
	
	//En este caso devolveremos el libro creado (ya que seremos nosotros
	//los que le asignemos el ID) y el codigo de respuesta 201 CREATED
	
	//La URL para acceder a este metodo sería: 
	//"http://localhost:8080/libros" y el metodo a usar seria POST
	//Pasandole el libro dentro del body del HTTP request
	@PostMapping(path="libros",consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Libro> altaLibro(@RequestBody Libro l) {
		int creado = daoLibro.add(l);
		System.out.println("");
		System.out.println("ControladorLibro => Dando de alta libro...");
		if (creado==1) {
			System.out.println("ControladorLibro => ENVIADO HttpStatus: 200 OK");
            System.out.println("ControladorLibro => Libro añadido correctamente");
    		System.out.println(l);
			return new ResponseEntity<Libro>(l,HttpStatus.CREATED);//201 CREATED
		}
		else {
			System.out.println("ControladorLibro => ENVIADO HttpStatus: 400 BAD REQUEST");
            System.out.println("ControladorLibro => Un libro con el mismo título o ID ya existe.");
			return new ResponseEntity<Libro>(l,HttpStatus.BAD_REQUEST); // 400 BAD REQUEST
		}
			
			
	}
	
	//GET LISTA LIBROS
	//En este caso vamos a pedir todos los libros que tenemos almacenados
	//Tambien nos da la opcion de filtrar por titulo si nos pasa un parametro
	//que se llame "titulo". Mediante la anotacion @RequestParam que pondremos
	//en un atributo de entrada de tipo String. Con el atributo name="titulo"
	//establecemos el nombre del parametro y con el atributo required=false
	//le decimos que no es obligatorio que nos lo envie.
	//De esta manera si NO me viene el parametro "titulo" devolveremos
	//toda la lista de libros, en caso de que venga, haremos el filtrado
	//por dicho titulo.
	
	//La URL para acceder a este metodo en caso de querer todas los libros
	//sería: 
	//"http://localhost:8080/libros" y el metodo a usar seria GET
	//Si queremos filtrar por nombre entonces deberemos usar:
	//"http://localhost:8080/libros?filtroTitulo=TITULO_A_FILTRAR"
	@GetMapping(path="libros",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Libro>> listarLibros(
			@RequestParam(name="filtroTitulo",required=false) String filtroTitulo) {
		List<Libro> listaLibros = null;
		//Si no me viene el titulo, devolvemos toda la lista
		if(filtroTitulo == null) {
			System.out.println("");
			System.out.println("ControladorLibro => Listado de los libros. ");
			System.out.println("ControladorLibro => ENVIADO HttpStatus: 200 OK");
			listaLibros = daoLibro.list();			
		}else {
			System.out.println("");
			System.out.println("ControladorLibro => Filtrado de libros por titulo (" + filtroTitulo + "): ");
			System.out.println("ControladorLibro => ENVIADO HttpStatus: 200 OK");
			System.out.println("Coincidencias encontradas: ");
			listaLibros = daoLibro.listByTitulo(filtroTitulo);
			if (listaLibros.isEmpty()) { System.out.println("Ninguna coincidencia encontrada");}
				
		}
		System.out.println(listaLibros);
		return new ResponseEntity<List<Libro>>(listaLibros,HttpStatus.OK);
	}
	
	//PUT
	//En este caso vamos a hacer una modificación de libro por ID
	//Para seguir lo que nos marca REST, el ID lo recibiremos en el PATH
	//y los datos por JSON dentro del bodoy del mensaje HTTP.
	
	//Si todo ha ido bien devolvemos el codigo de respuesta de 200 OK,
	//si id del libro no existe devolvemos 404 NOT FOUND
	
	//La URL para acceder a este metodo sería: 
	//"http://localhost:8080/libros/ID" y el metodo a usar seria PUT
	//Pasandole el libro sin el ID dentro del body del HTTP request
	@PutMapping(path="libros/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Libro> modificarLibro(
			@PathVariable("id") int id, 
			@RequestBody Libro l) {
		System.out.println("");
		System.out.println("ControladorLibro => Id de libro a modificar: " + id);
		l.setId(id);
		Libro lUpdate = daoLibro.update(l);
		if(lUpdate != null) {
			System.out.println("ControladorLibro => ENVIADO HttpStatus: 200 OK");
            System.out.println("ControladorLibro => Libro modificado correctamente");
            System.out.println(lUpdate);
			return new ResponseEntity<Libro>(HttpStatus.OK);//200 OK
		}else {
			System.out.println("ControladorLibro => ENVIADO HttpStatus: 404 NOT FOUND");
			System.out.println("ControladorLibro => Id de Libro no existe");
			return new ResponseEntity<Libro>(HttpStatus.NOT_FOUND);//404 NOT FOUND
		}
	}
	
	//DELETE
	//Aqui vamos a borar un libro a traves de un ID que le pasemos en el
	//PATH.
	
	//Si todo ha ido bien devolvemos el codigo de respuesta de 200 OK y
	//devolvemos el libro que hemos borrado
	
	//La URL para acceder a este metodo sería: 
	//"http://localhost:8080/libros/ID" y el metodo a usar seria DELETE
	@DeleteMapping(path="libros/{id}")
	public ResponseEntity<Libro> borrarLibro(@PathVariable("id") int id) {
		System.out.println("");
		System.out.println("ControladorLibro => Libro a borrar con ID: " + id);
		Libro l = daoLibro.delete(id);
		if(l != null) {
			System.out.println("ControladorLibro => ENVIADO HttpStatus: 200 OK");
			System.out.println("ControladorLibro => Libro borrado");
			
			return new ResponseEntity<Libro>(l,HttpStatus.OK);//200 OK
		}else {
			System.out.println("ControladorLibro => ENVIADO HttpStatus: 404 NOT FOUND");
			System.out.println("ControladorLibro => Id de Libro no existe");
			return new ResponseEntity<Libro>(HttpStatus.NOT_FOUND);//404 NOT FOUND
		}
	}
}