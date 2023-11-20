package es.biblioteca.cliente;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import es.biblioteca.cliente.entidad.Libro;
import es.biblioteca.cliente.servicio.ServicioProxyLibro;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner{
	
	Scanner leer = new Scanner (System.in);

	//Primero inyectaremos todos los objetos que necesitamos para
	//acceder a nuestro ServicioRest, el ServicioProxyPersona y el
	//ServicioProxyMensaje

	@Autowired
	private ServicioProxyLibro spp;

	
	//También necesitaremos acceder al contexto de Spring para parar
	//la aplicación, ya que esta app al ser una aplicación web se
	//lanzará en un Tomcat. De esta manera le decimos a Spring que
	//nos inyecte su propio contexto.
	@Autowired
	private ApplicationContext context;
	
	//En este método daremos de alta un objeto de tipo RestTemplate que será
	//el objeto más importante de esta aplicación. Será usado por los 
	//objetos ServicioProxy para hacer las peticiones HTTP a nuestro
	//servicio REST. 
	//Como no podemos anotar la clase RestTemplate para dar un objeto
	//de este tipo, porque no la hemos creado nosotros, usaremos la anotación 
	//@Bean para decirle a Spring que cuando arranque la app ejecute este 
	//método y meta el objeto devuelto dentro del contexto de Spring con ID 
	//"restTemplate" (el nombre del método)
	@Bean
	private static RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	//Método main que lanza la aplicación
	public static void main(String[] args) {
		System.out.println("Cliente -> Cargando el contexto de Spring");
		SpringApplication.run(ClientApplication.class, args);

		//Nótese que como este método es estático no podemos acceder
		//a los métodos dinámicos de la clase, como el "spp" o "spm"
		//Para solucionar esto, haremos que nuestra clase implemente
		//"CommandLineRunner" e implementaremos el método "run"
		//Cuando se acabe de arrancar el contexto, se llamará automáticamente
		//al método run
	}
	
	//Este método es dinámico por la tanto ya podemos acceder a los atributos
	//dinámicos (spm y spp respectivamente)
	@Override
	public void run(String... args) throws Exception {

		
		System.out.println("****** Arrancando el cliente REST ******");
		
		menu();
		
		
		/*
		System.out.println("*********** ALTA LIBRO ***************");
		libro.setTitulo("Asterix");
		libro.setEditorial("Anagrama");
		libro.setNota("Pues eso");
		
		Libro pAlta = spp.alta(libro);
		System.out.println("run -> Libro dado de alta " + pAlta);
		
		System.out.println("************ GET LIBRO ***************");
		libro = spp.obtener(pAlta.getId());
		System.out.println("run -> Libro con id 5: " + libro);
		
		System.out.println("************ GET LIBRO ERRONEO ***************");
		libro = spp.obtener(20);
		System.out.println("run -> Libro con id 20: " + libro);
		
		System.out.println("********* MODIFICAR LIBRO *************");	
		Libro pModificar = new Libro();
		pModificar.setId(pAlta.getId());
		pModificar.setTitulo("Obelix");
		pModificar.setEditorial("Anagrama");
		pModificar.setNota("Pues aquello");
		boolean modificada = spp.modificar(pModificar);
		System.out.println("run -> libro modificado? " + modificada);
		
		System.out.println("********* MODIFICAR LIBRO ERRONEO*************");			
		pModificar.setTitulo("Panoramix");
		pModificar.setEditorial("El de la pocion magica");
		pModificar.setId(20);
		modificada = spp.modificar(pModificar);
		System.out.println("run -> libro modificado? " + modificada);
		
	
		
		System.out.println("********** LISTAR LIBROS ***************");
		List<Libro> libros = spp.listar(null);
		//Recorremos la lista y la imprimimos con funciones lambda
		//Tambien podríamos haber usado un for-each clásico de java
		libros.forEach((v) -> System.out.println(v));
		
		System.out.println("******* LISTAR LIBROS POR TÍTULO *******");
		libros = spp.listar("tHe");
		libros.forEach((v) -> System.out.println(v));
		*/

	}


	private void menu() {

		int opcion = 0;
		
		while (opcion!=7) {
		
			printMenu();
			
			try {
				
			opcion = leer.nextInt();
						
				switch (opcion) {
					case 1:
						altaLibro();
						break;
					case 2:
						bajaLibro();	
						break;
					case 3:
						modLibro();
						break;
					case 4:
						getLibroByID();
						break;
					case 5:
						listaLibros();
						break;
					case 6:
						listaLibrosFiltrada();
						break;
					case 7:
						//Mandamos parar nuestra aplicación Spring Boot
						pararAplicacion();
					default:
	                    System.out.println("Opción no válida. Por favor, introduce un número del 1 al 7.");	
				}
			}
			
			catch (InputMismatchException e) {
	            // Captura la excepción si se ingresa un valor que no es un entero
	            System.out.println("Entrada no válida. Por favor, introduce un número del 1 al 7.");
	            leer.next(); // Limpia la entrada incorrecta del Scanner   
			}
		
		}

		
	}

	public void printMenu () {	
		System.out.println("");
		System.out.println("******* MENU PRINCIPAL *******");
		System.out.println("1. Dar de alta un libro");
		System.out.println("2. Dar de baja un libro por ID");
		System.out.println("3. Modificar un libro por ID");
		System.out.println("4. Obtener un libro por ID");
		System.out.println("5. Listar todos los libros");
		System.out.println("6. Listar libros filtrando por palabra");	
		System.out.println("7. Salir");	
		System.out.println("Introduce Opción: ");
	}
	

	private void altaLibro() {
		
		Libro libro = new Libro();

		System.out.println("");
		System.out.println("*********** ALTA LIBRO ***************");
		System.out.println("Introduce id: ");
		int id = leer.nextInt();
		libro.setId(id);

		System.out.println("Introduce titulo: ");
		leer.nextLine(); // Consume the newline character left by leer.nextInt()
		String titulo = leer.nextLine();
		libro.setTitulo(titulo);

		System.out.println("Introduce editorial: ");
		String editorial = leer.nextLine();
		libro.setEditorial(editorial);

		System.out.println("Introduce nota: ");
		String nota = leer.nextLine();
		libro.setNota(nota);
		
		spp.alta(libro);

	}
	
	private void bajaLibro() {
		
		System.out.println("");
		System.out.println("********** BORRAR LIBROS **************");
		
		System.out.println("Introduce ID: ");
		int id = leer.nextInt();
		boolean borrada = spp.borrar(id);
		System.out.println("ClientApplication -> Libro con id " + id + " borrado? " + borrada);	
	}
	
	private void modLibro() {
		
		System.out.println("");
		System.out.println("********* MODIFICAR LIBRO *************");	
		
		Libro l = new Libro();
		System.out.println("Introduce id: ");
		int id = leer.nextInt();
		l.setId(id);

		System.out.println("Introduce titulo: ");
		leer.nextLine(); // Consume the newline character left by leer.nextInt()
		String titulo = leer.nextLine();
		l.setTitulo(titulo);

		System.out.println("Introduce editorial: ");
		String editorial = leer.nextLine();
		l.setEditorial(editorial);

		System.out.println("Introduce nota: ");
		String nota = leer.nextLine();
		l.setNota(nota);
		
		boolean modificado = spp.modificar(l);
		System.out.println("ClientApplication -> libro modificado? " + modificado);

		
	}

	private void getLibroByID() {
		
		System.out.println("");
		System.out.println("********** OBTENER LIBRO POR ID ***************");
		System.out.println("Introduce id: ");
		int id = leer.nextInt();
		Libro l = new Libro();
		l = spp.obtener(id);
		System.out.println("ClientApplication -> Libro seleccionado:");
		System.out.println(l);
	}
	

	private void listaLibros() {
		
		System.out.println("");
		System.out.println("********** LISTAR LIBROS ***************");
		List<Libro> libros = spp.listar(null);
		//Recorremos la lista y la imprimimos con funciones lambda
		//Tambien podríamos haber usado un for-each clásico de java
		libros.forEach((v) -> System.out.println(v));  // ESTE ESTÁ FENOMENAL.
		

	}

	private void listaLibrosFiltrada() {

		System.out.println("");
		System.out.println("******* LISTAR LIBROS POR TÍTULO *******");
		leer.nextLine();
		System.out.println("Introduce filtro para el título: ");
		String filtro = leer.nextLine();
		List<Libro> libros = spp.listar(filtro);
		if (!libros.isEmpty()) {
			System.out.println("ClientApplication -> Coincidencias encontradas: ");
			libros.forEach((v) -> System.out.println(v));
		}
		else
			System.out.println("ClientApplication -> Ningún resultado encontrado");
		
	}
	
	public void pararAplicacion() {
		//Esta aplicacion levanta un servidor web, por lo que tenemos que dar 
		//la orden de pararlo cuando acabemos. Para ello usamos el método exit, 
		//de la clase SpringApplication, que necesita tanto el contexto de 
		//Spring como un objeto que implemente la interfaz ExitCodeGenerator. 
		//Podemos usar la función lambda "() -> 0" para simplificar 
		
		System.out.println("");
		System.out.println("******************************************");		
		System.out.println("******** Parando el cliente REST *********");			
		SpringApplication.exit(context, () -> 0);
		
		//Podemos hacerlo también de una manera clásica, es decir, creando
		//la clase anónima a partir de la interfaz. 
		//El código de abajo sería equivalente al de arriba
		//(pero mucho más largo)
		/*
		SpringApplication.exit(context, new ExitCodeGenerator() {
			
			@Override
			public int getExitCode() {
				return 0;
			}
		});*/
	}
	

}