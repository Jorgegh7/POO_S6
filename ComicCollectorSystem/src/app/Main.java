package app;

import modelo.tienda.Tienda;
import modelo.usuario.RegistroUsuario;
import modelo.usuario.Usuario;
import servicio.ServiciosTienda;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){


        int opcionMenu;
        System.out.println("\n=====================================\n-     ComicCollectorSystem    -\n=====================================");

        do{
            RegistroUsuario.inicializarListaUsuarios();




            System.out.println("\n===========================\n-     MENÚ PRINCIPAL     -\n===========================");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrarme");
            System.out.println("3. Administración Tienda");
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

                    if(Tienda.inicioSesionCsv(rutInicioSesion)){
                        int opcionMenuUsuario=0;
                        RegistroUsuario.inicializarReservasUsuario(rutInicioSesion);
                        ServiciosTienda.inicializarRegistroLibrosReservados();

                        do{


                            System.out.println("\n===========================\n-     MENÚ USUARIO     -\n===========================");
                            System.out.println("1. Perfil");
                            System.out.println("2. Registro Reservas");
                            System.out.println("3. Reservar Título");
                            System.out.println("4. Buscar por Título");
                            System.out.println("5. Buscar por Autor");
                            System.out.println("6. Comprar Título");
                            System.out.println("7. Historial de compras");
                            System.out.println("8. Salir");
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

                                case 2: //Usuario: Registro Reserva
                                    RegistroUsuario.getHashMapReservasUsuario(rutInicioSesion);
                                    break;

                                case 3: //Usuario: Reservar
                                    System.out.println("Ingresa el Título que deseas buscar");
                                    Scanner scannerCompra = new Scanner(System.in);
                                    String libroReserva = scannerCompra.nextLine();
                                    try{
                                        ServiciosTienda.reservarTitulo(rutInicioSesion, libroReserva);
                                    }catch (IOException e){
                                        System.out.println("No se ha podido realizar la operacion por un problema de lectura de archivo");
                                        System.out.println(e.getMessage());
                                    }
                                    break;

                                case 4: //Usuario: Buscar por libro
                                    System.out.println("Ingresa el libro que deseas buscar");
                                    Scanner scannerLibro = new Scanner(System.in);
                                    String libroBuscado = scannerLibro.nextLine();
                                    Tienda.buscarLibro(libroBuscado.toLowerCase());
                                    break;

                                case 5://Usuario: Buscar por Autor
                                    System.out.println("Ingresa el Autor que deseas buscar");
                                    Scanner scannerAutor = new Scanner(System.in);
                                    String autorBuscado = scannerAutor.nextLine();
                                    Tienda.librosPorAutor(autorBuscado.toLowerCase());
                                    break;

                                case 6: // Usuario: Comprar Titulo
                                    System.out.println("Recuerda que para realizar tu compra primero debes hacer la reserva!");
                                    System.out.println("Ingresa el Título que deseas comprar");
                                    Scanner scannerLibroCompra = new Scanner(System.in);
                                    String libroCompra = scannerLibroCompra.nextLine();
                                    ServiciosTienda.comprar(rutInicioSesion, libroCompra.toLowerCase());
                                    break;

                                case 7:
                                    System.out.println("\n===========================\n-     HISTORIAL COMPRAS     -\n===========================");
                                    RegistroUsuario.historiaComprasPorUsuario(rutInicioSesion);
                                    break;

                                case 8: // Usuario: Salir Menu Usuario
                                    System.out.println("Cerrando Sessión");
                                    rutInicioSesion ="";
                                    break;


                                default:
                                    System.out.println("Opción no válida. Intente de nuevo.");
                            }
                        }while(opcionMenuUsuario !=8);
                    }
                    break;
                case 2: //Menu Inicio: Registro Usuario
                    RegistroUsuario.registroUsuario(new Usuario());
                    break;

                case 3://Administracion TiendaComics
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
                            if(Tienda.inicioSesionAdministrador()){
                                do {
                                    System.out.println("\n===========================\n-     MENÚ ADMINISTRACIÓN     -\n===========================");
                                    System.out.println("1. Inventario Titulos");
                                    System.out.println("2. Agregar Titulo");
                                    System.out.println("3. Eliminar Titulo");
                                    System.out.println("4. Modificar Stock");
                                    System.out.println("5. Informe de Reservas");
                                    System.out.println("6. Informe de Ventas");
                                    System.out.println("7. Salir");
                                    System.out.println("===========================");

                                    System.out.println("Selecciona tu opción:");
                                    Scanner scannerMenuAdministracion = new Scanner(System.in);

                                    try {
                                        opMenu2 = scannerMenuAdministracion.nextInt();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Ingresa un valor numerico correcto");
                                    }

                                    switch (opMenu2) {
                                        case 1: // Administracion TiendaComics:  Inventario Libros
                                            try {
                                                if(Tienda.getInventarioLibros().isEmpty()){
                                                    Tienda.inicializarListaLibros(); // Inicializa la lista desde un archivo CSV
                                                }
                                                Tienda.impresionInventario();   //Imprime la lista inializada
                                            } catch (IOException e) {
                                                System.out.println(e.getMessage());
                                            }
                                            break;
                                        case 2: // Administracion TiendaComics: Agregar Titulo
                                            Tienda.agregarTitulo();
                                            break;

                                        case 3:  // Administracion TiendaComics: Eliminar Titulo
                                            System.out.println("ingresa el libro que deseas eliminar");
                                            Scanner scannerEliminar = new Scanner(System.in);
                                            String elimninaLibro = scannerEliminar.nextLine().toLowerCase();
                                            Tienda.eliminarLibro(elimninaLibro);
                                            break;

                                        case 4: // Administracion TiendaComics: Modificar Stock
                                            Scanner scannerNuevoStock = new Scanner(System.in);
                                            System.out.println("Ingresa el nombre del libro");
                                            String libro = scannerNuevoStock.nextLine();
                                            System.out.println("Ingresa el nuevo Stock: ");
                                            try{
                                                int nuevoStock = scannerNuevoStock.nextInt();
                                                if(!ServiciosTienda.modificarStock(libro, nuevoStock)) {
                                                    System.out.println("El libro no ha sido encontrado");
                                                }
                                            }catch(InputMismatchException e){
                                                System.out.println("Ingresa un numero entero: " + e.getMessage());
                                            }
                                            break;

                                        case 5: // Administracion TiendaComics: Lista Arriendos
                                            Tienda.informeReservas();
                                            break;
                                        case 6:  // Administracion TiendaComics: Lista Ventas
                                            Tienda.informeVentas();
                                            break;
                                        case 7:
                                            break;
                                        default:
                                            System.out.println("Ingresa un número correcto");
                                    }
                                }while (opMenu2 != 7) ;

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



