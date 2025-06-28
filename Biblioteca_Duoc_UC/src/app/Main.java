package app;

import modelo.biblioteca.Biblioteca;
import modelo.usuario.RegistroUsuario;
import modelo.usuario.Usuario;
import servicio.ServiciosBiblioteca;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){


        int opcionMenu;
        System.out.println("\n=====================================\n-     BIBLIOTECA DUOC UC    -\n=====================================");

        do{
            RegistroUsuario.inicializarListaUsuarios();




            System.out.println("\n===========================\n-     MENÚ PRINCIPAL     -\n===========================");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrarme");
            System.out.println("3. Administración Biblioteca");
            System.out.println("4. Salir");
            System.out.println("===========================");

            System.out.println("Selecciona tu opción:");

            opcionMenu=0;
            Scanner scanner = new Scanner(System.in);
            try{
                opcionMenu= scanner.nextInt();
            }catch (InputMismatchException e){
                System.out.println("Ingresa un numero correcto");
            }

            scanner.nextLine(); // limpiamos el buffer
            String rutInicioSesion;

            switch (opcionMenu) {
                case 1: //Menu Inicio: Inicio Sesion

                    System.out.println("\n===========================\n-     INICIO SESION     -\n===========================");
                    System.out.println();
                    System.out.println("Ingresa tu rut: ");
                    Scanner scannerRut = new Scanner(System.in);
                    rutInicioSesion=scannerRut.nextLine();

                    if(Biblioteca.inicioSesionCsv(rutInicioSesion)){
                        int opcionMenuUsuario=0;
                        RegistroUsuario.inicializarArriendosUsuario(rutInicioSesion);
                        ServiciosBiblioteca.inicializarRegistroLibrosArrendados();

                        do{


                            System.out.println("\n===========================\n-     MENÚ USUARIO     -\n===========================");
                            System.out.println("1. Perfil");
                            System.out.println("2. Registro Arriendos");
                            System.out.println("3. Arrendar");
                            System.out.println("4. Buscar por Libro");
                            System.out.println("5. Buscar por Autor");
                            System.out.println("6. Devolver Libro");
                            System.out.println("7. Salir");
                            System.out.println("===========================");

                            System.out.println("Selecciona tu opción:");
                            Scanner scannerMenuUsuario = new Scanner(System.in);
                            try{
                                opcionMenuUsuario= scannerMenuUsuario.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Ingresa un número válido");

                            }

                            switch (opcionMenuUsuario){

                                case 1: // Usuario: Perfil
                                    System.out.println();

                                    Usuario usuarioIniciado = (Usuario) RegistroUsuario.getHashMapUsuarioRegistradoPorRut().get(rutInicioSesion);
                                    System.out.println("\n===========================\n-     INFO USUARIO     -\n===========================");
                                    usuarioIniciado.mostrarInformacionUsuario(usuarioIniciado);
                                    break;

                                case 2: //Usuario: Registro Arriendos
                                    RegistroUsuario.getHashMapArriendosUsuario(rutInicioSesion);
                                    break;

                                case 3: //Usuario: Arrendar
                                    System.out.println("Ingresa el libro que deseas buscar");
                                    Scanner scannerLibroArriendo = new Scanner(System.in);
                                    String libroArriendo = scannerLibroArriendo.nextLine();
                                    try{
                                        ServiciosBiblioteca.prestarLibro(rutInicioSesion, libroArriendo);
                                    }catch (IOException e){
                                        System.out.println("No se ha podido realizar la operacion por un problema de lectura de archivo");
                                        System.out.println(e.getMessage());
                                    }
                                    break;

                                case 4: //Usuario: Buscar por libro
                                    System.out.println("Ingresa el libro que deseas buscar");
                                    Scanner scannerLibro = new Scanner(System.in);
                                    String libroBuscado = scannerLibro.nextLine();
                                    Biblioteca.buscarLibro(libroBuscado.toLowerCase());
                                    break;

                                case 5://Usuario: Buscar por Autor
                                    System.out.println("Ingresa el Autor que deseas buscar");
                                    Scanner scannerAutor = new Scanner(System.in);
                                    String autorBuscado = scannerAutor.nextLine();
                                    Biblioteca.librosPorAutor(autorBuscado.toLowerCase());
                                    break;

                                case 6: // Usuario: Devolver Libro
                                    System.out.println("Ingresa el libro que deseas devolver");
                                    Scanner scannerLibroDevolver = new Scanner(System.in);
                                    String libroDevolver = scannerLibroDevolver.nextLine(); 
                                    ServiciosBiblioteca.recibirLibroPrestado(rutInicioSesion, libroDevolver.toLowerCase());
                                    break;

                                case 7: // Usuario: Salir Menu Usuario
                                    System.out.println("Cerrando Inicio de Sessión");
                                    rutInicioSesion ="";
                                    break;

                                default:
                                    System.out.println("Opción no válida. Intente de nuevo.");
                            }
                        }while(opcionMenuUsuario !=7);
                    }
                    break;
                case 2: //Menu Inicio: Registro Usuario
                    RegistroUsuario.registroUsuario(new Usuario());
                    break;

                case 3://Administracion Biblioteca
                    int opMenu= 0;
                    System.out.println("\n===========================\n-     MENÚ ADMINISTRACIÓN     -\n===========================");
                    System.out.println("1. Iniciar sesión");
                    System.out.println("2. Salir");
                    System.out.println("===========================");
                    System.out.println("Selecciona tu opción:");

                    Scanner opcionMenuAdministracion = new Scanner(System.in);

                    try{
                        opMenu = opcionMenuAdministracion.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Ingresa un valor correcto");
                    }

                    switch (opMenu){
                        case 1: //Inicio de Session Administracion
                            int opMenu2 =0;
                            if(Biblioteca.inicioSesionAdministrador()){
                                do {
                                    System.out.println("\n===========================\n-     MENÚ ADMINISTRACIÓN     -\n===========================");
                                    System.out.println("1. Inventario Libros");
                                    System.out.println("2. Agregar Titulo");
                                    System.out.println("3. Eliminar Titulo");
                                    System.out.println("4. Informe de Arriendos");
                                    System.out.println("5. Salir");
                                    System.out.println("===========================");

                                    System.out.println("Selecciona tu opción:");
                                    Scanner scannerMenuAdministracion = new Scanner(System.in);

                                    try {
                                        opMenu2 = scannerMenuAdministracion.nextInt();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Ingresa un valor numerico correcto");
                                    }

                                    switch (opMenu2) {
                                        case 1: // Administracion Biblioteca:  Inventario Libros
                                            try {
                                                if(Biblioteca.getInventarioLibros().isEmpty()){
                                                    Biblioteca.inicializarListaLibros(); // Inicializa la lista desde un archivo CSV
                                                }
                                                Biblioteca.impresionInventario();   //Imprime la lista inializada
                                            } catch (IOException e) {
                                                System.out.println(e.getMessage());
                                            }
                                            break;
                                        case 2: // Administracion Biblioteca: Agregar Titulo
                                            Biblioteca.agregarTitulo();
                                            break;

                                        case 3:  // Administracion Biblioteca: Eliminar Titulo
                                            System.out.println("ingresa el libro que deseas eliminar");
                                            Scanner scannerEliminar = new Scanner(System.in);
                                            String elimninaLibro = scannerEliminar.nextLine().toLowerCase();
                                            Biblioteca.eliminarLibro(elimninaLibro);
                                            break;

                                        case 4: // Administracion Biblioteca: Lista Arriendos
                                            ServiciosBiblioteca.inicializarRegistroLibrosArrendados();
                                            ServiciosBiblioteca.listaArriendoPorUsuario();
                                            break;
                                        case 5:
                                            break;
                                        default:
                                            System.out.println("Ingresa un número correcto");
                                    }
                                }while (opMenu2 != 5) ;

                            }else{
                                System.out.println("Acceso denegado");
                            }

                        case 2://Salida Menu inicial Biblioteca
                            break;
                        default:
                            System.out.println("Ingresa un numero correcto");

                    }
                    break;


                case 4: //Menu Inicio: Salir del programa
                    System.out.println("Gracias por vistar BIBLIOTECA DUOC UC");
                    System.out.println("Hasta la proxima!");
                    break;


                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }while(opcionMenu!=4);
    }
}



