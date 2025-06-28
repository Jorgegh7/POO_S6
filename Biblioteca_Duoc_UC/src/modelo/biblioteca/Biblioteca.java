package modelo.biblioteca;
import modelo.usuario.RegistroUsuario;
import modelo.usuario.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Biblioteca {

    protected static HashSet<Libro> inventarioLibros = new HashSet<>();

    public static boolean verificacionAccesoCuenta(String rut, int claveAcceso){
        return RegistroUsuario.getHashMapClaveAcceso().containsKey(rut) &&
                RegistroUsuario.getHashMapClaveAcceso().containsValue(claveAcceso);
    }

    public static boolean inicioSesion(String rut){
        Scanner scanner= new Scanner(System.in);
        boolean inicioSesion = false;

        System.out.println("Ingresa tu clave de acceso");
        int claveAcesso = scanner.nextInt();
        if(verificacionAccesoCuenta(Usuario.verificarRut(rut), claveAcesso)){
            System.out.println("Acceso permitirdo");
            inicioSesion = true;
        }else {
            System.out.println("Accedo denegado");
            inicioSesion = false;
        }
        return inicioSesion;

    }

    public static boolean inicioSesionCsv(String rut){
        Scanner scanner= new Scanner(System.in);
        boolean usuarioRegistrado = false;
        System.out.println("Ingresa tu clave de acceso");
        String clave = scanner.nextLine();

        try(BufferedReader reader = new BufferedReader(new FileReader("registro_usuario.txt"))){
            String linea;
            while((linea = reader.readLine()) != null){
                String[] campos = linea.split(",");

                if(campos.length>0 && campos[0].trim().toLowerCase().equals(rut.trim()) && campos[6].equals(clave)){
                    System.out.println("Usuario Registrado, clave correcta Acceso permitido");
                   usuarioRegistrado = true;
                   return usuarioRegistrado;
                }else{
                    usuarioRegistrado = false;
                }
            }
            if(!usuarioRegistrado){
                System.out.println("Las claves son incorrectas");
            }

        }catch (IOException e){
            System.out.println("No se ha encontraado el archivo: " +  e.getMessage());
        }
     return usuarioRegistrado;
    }

    public static boolean buscarLibro(String libroBuscado){
        boolean libroEncontrado= false;

        try(BufferedReader reader = new BufferedReader(new FileReader("libros_espanol_csv(100 Disponible).txt"))) {
            String linea;

            while((linea = reader.readLine()) != null){
                String[] campos = linea.split(",");

                if(campos.length>0 && campos[0].trim().toLowerCase().equals(libroBuscado)){
                    System.out.println("Libro encontrado");
                    System.out.println(linea);
                    libroEncontrado = true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se ha podido realizar la operacion por un problema de lectura de archivo");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
        if(!libroEncontrado){
            System.out.println("El libro no fue encontrado");
        }
        return libroEncontrado;
    }

    public static void librosPorAutor(String autorBuscado){
        boolean autorEncontrado = false;

        try(BufferedReader reader = new BufferedReader(new FileReader("libros_espanol_csv(100 Disponible).txt"))) {
            String linea;

            while((linea = reader.readLine()) != null){
                String[] campos = linea.split(",");

                if(campos.length>0 && campos[1].trim().toLowerCase().contains(autorBuscado)){
                    System.out.println("Autor encontrado");
                    System.out.println(linea);
                    autorEncontrado = true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se ha podido realizar la operacion por un problema de lectura de archivo");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if(!autorEncontrado){
            System.out.println("El Autor no fue encontrado");
        }
    }

    public static void inicializarListaLibros(){

        try(BufferedReader reader = new BufferedReader(new FileReader("libros_espanol_csv(100 Disponible).txt"))){
            String linea;

            while ((linea = reader.readLine()) != null){
                Libro libroInventario = new Libro();
                String campos[] = linea.split(",");

                libroInventario.setTitulo(campos[0]);
                libroInventario.setAutor(campos[1]);
                libroInventario.setGenero(campos[2]);
                libroInventario.setIsbn(campos[3]);
                libroInventario.setDisponible(campos[4]);
                setInventarioLibros().add(libroInventario);
            }

        } catch (IOException e) {
            System.out.println("El archivo no ha podido cargar la base de datos");
            System.out.println(e.getMessage());
        }
    }

    protected static HashSet<Libro> setInventarioLibros(){
        Libro libro = new Libro();
        return inventarioLibros;
    }

    public static void impresionInventario() throws IOException{

        System.out.println("\n===========================\n-     INVENTARIO LIBROS     -\n===========================");
        System.out.println();
        System.out.println("Titulo,Autor,Genero,ISBN,Estado");

        try(BufferedReader reader = new BufferedReader(new FileReader("libros_espanol_csv(100 Disponible).txt"))) {
            int i=0;
            String linea;

            while ((linea = reader.readLine()) != null) {
                i++;
                System.out.println(i+ ") " +linea);
            }
        }catch (FileNotFoundException e){
            System.out.println("El archivo no fue encontrado");
        }

       // Esto agrega a una lista pero al modificar el archivo CSV la lista no se actualiza
      /* int i = 1;
        System.out.println("\n===========================\n-     INVENTARIO LIBROS     -\n===========================");
        for(Libro libro: inventarioLibros){
            System.out.println((i++) +")" + " TITULO: " + libro.getTitulo() + ", AUTOR: " + libro.getAutor() + ",GENERO: " + libro.getGenero() + ", ISBN: " + libro.getIsbn() + ", ESTADO: " + libro.isDisponible());
        } */

    }

    public static void agregarTitulo(){
        // No agrega el Titulo al archivo CSV, solo lo agrega a la lista de registro. Al salir se resetea ya que el inventario se carga desde el archivo CSV.
        //Solucion: Agregar el titulo al archivo CSV

        Libro nuevoTitulo = new Libro();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n===========================\n-     NUEVO TITULO     -\n===========================");
        System.out.print("Ingresa el Titulo: ");
        nuevoTitulo.setTitulo(scanner.nextLine());
        System.out.print("Ingresa el Auto: ");
        nuevoTitulo.setAutor(scanner.nextLine());
        System.out.print("Ingresa el Género: ");
        nuevoTitulo.setGenero(scanner.nextLine());
        System.out.print("Ingresa el ISBN: ");
        nuevoTitulo.setIsbn(scanner.nextLine());
        System.out.print("Ingresa el Estado: ");
        nuevoTitulo.setDisponible(scanner.nextLine());

        inventarioLibros.add(nuevoTitulo);

    } // aun falta agregar al archivo CSV

    public static boolean inicioSesionAdministrador(){
        boolean acceso = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingresa el nombre de Administrador (admin)" );
        String nombreAdministrador = scanner.nextLine();
        System.out.println("Ingresa tu clave de acceso (1111)");
        String claveAccesoAdministrador = scanner.nextLine();

        if(nombreAdministrador.equals("admin") && claveAccesoAdministrador.equals("1111")) {
            acceso = true;
            System.out.println("Acceso permitido");
        }
        return acceso;
    }

    public static void eliminarLibro(String libroBuscado){
        List<String> nuevaLista = new ArrayList<>();
        Libro libroEliminar = new Libro();
        try(BufferedReader reader = new BufferedReader(new FileReader("libros_espanol_csv(100 Disponible).txt"))) {
            String linea;

            while((linea = reader.readLine()) != null){
                String[] campos = linea.split(",");
                if(campos.length>0 && campos[0].trim().toLowerCase().equals(libroBuscado)){
                    //Aqui se puede implementar la eliminacion de la lista inventarioLibros
                    for (Libro libro : inventarioLibros){
                        if (libroBuscado.toLowerCase().equals(libro.getTitulo().toLowerCase())){
                           libroEliminar = libro;
                        }
                    }inventarioLibros.remove(libroEliminar);
                    System.out.println("Libro encontrado");
                    System.out.println("Libro eliminado de la Lista de inventario");

                }else{
                    nuevaLista.add(linea);
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("Archivo no encontrado");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        try (FileWriter writer = new FileWriter("libros_espanol_csv(100 Disponible).txt")) {
            System.out.println("Inicio de SobreEscritura");

            for(String nuevaLinea: nuevaLista){
                writer.write(nuevaLinea +'\n');
                writer.flush();
            }

        } catch (IOException e) {
            System.out.println("El archivo no fue encontrado o no pudo ser modificado");
        }

        System.out.println("Libro Eliminado de forma Correcta");
        System.out.println("Base de datos actualizada");

    }

    public static HashSet<Libro> getInventarioLibros(){
        return inventarioLibros;
    }



}
