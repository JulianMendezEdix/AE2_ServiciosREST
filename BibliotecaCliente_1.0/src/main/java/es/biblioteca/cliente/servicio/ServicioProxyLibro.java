package es.biblioteca.cliente.servicio;


import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.biblioteca.cliente.entidad.Libro;

//Con esta anotación damos de alta un objeto de tipo
//ServicioProxyLibro dentro del contexto de Spring
@Service
public class ServicioProxyLibro {

	//La URL base del servicio REST de libros
	public static final String URL = "http://localhost:8080/libros/";
	
	//Inyectamos el objeto de tipo RestTemplate que nos ayudará
	//a hacer las peticiones HTTP al servicio REST
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Método que obtiene un libro del servicio REST a partir de un id
	 * En caso de que el id no exita arrojaria una expcepción que se captura
	 * para sacar el codigo de respuesta
	 * 
	 * @param id que queremos obtener
	 * @return retorna el libro que estamos buscando, null en caso de que el
	 * libro no se encuentre en el servidor (devuelva 404) o haya habido algún
	 * otro error.
	 */
	public Libro obtener(int id){
		try {
			//Como el servicio trabaja con objetos ResponseEntity, nosotros 
			//tambien podemos hacerlo en el cliente
			//Ej http://localhost:8080/personas/1 GET
			ResponseEntity<Libro> re = restTemplate.getForEntity(URL + id, Libro.class);
			HttpStatus hs= re.getStatusCode();
			if(hs == HttpStatus.OK) {	
				//Si el libro existe, el libro viene en formato JSON en el body
				//Al ser el objeto ResponseEntity de tipo Libro, al obtener el 
				//body me lo convierte automaticamente a tipo Libro
				//(Spring utiliza librerías por debajo para pasar de JSON a objeto)
				System.out.println("");
			    System.out.println("ServicioProxyLibro => Codigo de respuesta: " + re.getStatusCode());
				return re.getBody();
			}else {
				System.out.println("");
				System.out.println("ServicioProxyLibro =>  Respuesta no contemplada");
				return null;
			}
		}catch (HttpClientErrorException e) {//Errores 4XX
			System.out.println("");
			System.out.println("ServicioProxyLibro => No existe ningún libre con id: " + id);
		    System.out.println("ServicioProxyLibro => Codigo de respuesta: " + e.getStatusCode());
		    return null;
		}
	}
	
	/**
	 * Método que da de alta una libro en el servicio REST
	 * 
	 * @param p la libro que vamos a dar de alta
	 * @return la libro con el id actualizado que se ha dado de alta en el
	 * servicio REST. Null en caso de que no se haya podido dar de alta
	 */
	public Libro alta(Libro l){
		try {
			//Para hacer un post de una entidad usamos el metodo postForEntity
			//El primer parametro la URL
			//El segundo parametros la libro que ira en body
			//El tercer parametro el objeto que esperamos que nos envie el servidor
			ResponseEntity<Libro> re = restTemplate.postForEntity(URL, l, Libro.class);
			System.out.println("");
			System.out.println("ServicioProxyLibro -> Libro dado de alta: ");
			System.out.println(l);
			System.out.println("ServicioProxyLibro -> Codigo de respuesta " + re.getStatusCode());
			return re.getBody();
		} catch (HttpClientErrorException e) {//Errores 4XX
			System.out.println("");
			System.out.println("ServicioProxyLibro -> El libro no se ha dado de alta (id o titulo ya existen)");
		    System.out.println("ServicioProxyLibro -> Codigo de respuesta: " + e.getStatusCode());
		    return null;
		}
	}
	
	/**
	 * 
	 * Modifica una libro en el servicio REST
	 * 
	 * @param p la libro que queremos modificar, se hara a partir del 
	 * id por lo que tiene que estar relleno.
	 * @return true en caso de que se haya podido modificar la libro. 
	 * false en caso contrario.
	 */
	public boolean modificar(Libro p){
		try {
			//El metodo put de Spring no devuelve nada
			//si no da error se ha dado de alta y si no daria una 
			//excepcion
			restTemplate.put(URL + p.getId(), p, Libro.class);
			return true;
		} catch (HttpClientErrorException e) {
			System.out.println("");
			System.out.println("ServicioProxyLibro => No existe ningún libre con id: " + p.getId());
		    System.out.println("ServicioProxyLibro =>  Codigo de respuesta: " + e.getStatusCode());
		    return false;
		}
	}
	
	/**
	 * 
	 * Borra una libro en el servicio REST
	 * 
	 * @param id el id de la libro que queremos borrar.
	 * @return true en caso de que se haya podido borrar la libro. 
	 * false en caso contrario.
	 */
	public boolean borrar(int id){
		try {
			//El metodo delete tampoco devuelve nada, por lo que si no 
			//ha podido borrar el id, daría un excepcion
			//Ej http://localhost:8080/personas/1 DELETE
			restTemplate.delete(URL + id);
			return true;
		} catch (HttpClientErrorException e) {
			System.out.println("");
			System.out.println("ServicioProxyLibro -> El libro no se ha borrado, id no existe: " + id);
		    System.out.println("ServicioProxyLibro -> Codigo de respuesta: " + e.getStatusCode());
		    return false;
		}
	}
	
	/**
	 * Metodo que devuelve todas las libros o todas las libros filtradas
	 * por nombre del web service
	 * 
	 * @param nombre en caso de ser distinto de null, devolvera el listado
	 * filtrado por el nombre que le hayamos pasado en este parametro. En caso
	 * de que sea null, el listado de las libros sera completo
	 * @return el listado de las libros segun el parametro de entrada o 
	 * null en caso de algun error con el servicio REST
	 */
		public List<Libro> listar(String aux){
			String queryParams = "";		
			if(aux != null) {
				queryParams += "?filtroTitulo=" + aux;
			}
			
			try {
				//Ej http://localhost:8080/personas?nombre=harry GET
				ResponseEntity<Libro[]> response =
						  restTemplate.getForEntity(URL + queryParams,Libro[].class);
				Libro[] arrayPersonas = response.getBody();
				return Arrays.asList(arrayPersonas);//convertimos el array en un ArrayList
			} catch (HttpClientErrorException e) {
				System.out.println("");
				System.out.println("listar -> Error al obtener la lista de personas");
			    System.out.println("listar -> Codigo de respuesta: " + e.getStatusCode());
			    return null;
			}
		}
	}
