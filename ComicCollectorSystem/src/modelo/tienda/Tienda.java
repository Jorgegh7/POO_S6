package modelo.tienda;
import modelo.usuario.RegistroUsuario;
import modelo.usuario.Usuario;

import java.io.*;
import java.util.*;

public class Tienda {

    protected static HashSet<Libro> inventarioLibros = new HashSet<>();

    public static boolean verificacionAccesoCuenta(String rut, int claveAcceso){
        return RegistroUsuario.getHashMapClaveAcceso().containsKey(rut) &&
                RegistroUsuario.getHashMapClaveAcceso().containsValue(claveAcceso);
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

        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("comics_catalog.csv"), "UTF-8"))){
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

        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("comics_catalog.csv"), "UTF-8"))){
            String linea;

            while((linea = reader.readLine()) != null){
                String[] campos = linea.split(",");

                if(campos.length>=7 && campos[1].trim().toLowerCase().contains(autorBuscado)){
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

        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("comics_catalog.csv"), "UTF-8"))){
            String linea;

            while ((linea = reader.readLine()) != null){
                Libro libroInventario = new Libro();
                String[] campos = linea.split(",");
                if(campos.length >=7){
                    libroInventario.setTitulo(campos[0]);
                    libroInventario.setAutor(campos[1]);
                    libroInventario.setGenero(campos[2]);
                    libroInventario.setIsbn(campos[3]);
                    libroInventario.setDisponible(campos[4]);
                    libroInventario.setValor(Integer.parseInt(campos[5]));
                    libroInventario.setUnidadesDisponibles(Integer.parseInt(campos[6]));
                    setInventarioLibros().add(libroInventario);
                }
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

        System.out.println("\n===========================\n-     INVENTARIO TITULOS     -\n===========================");
        System.out.println();
        System.out.println("Titulo, Autor, Genero, ISBN, Estado, Valor, Unidades Disponibles");
        System.out.println();

        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("comics_catalog.csv"), "UTF-8"))){
            int i=0;
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (!linea.isEmpty()){
                    i++;
                    System.out.println(i+ ") " +linea);
                }

            }
        }catch (FileNotFoundException e){
            System.out.println("El archivo no fue encontrado");
        }
    }

    public static void agregarTitulo(){
        Libro nuevoTitulo = new Libro();
        String nuevaLinea = "";
        Scanner scanner = new Scanner(System.in);
        boolean enteros = false;

        System.out.println("\n===========================\n-     NUEVO TITULO     -\n===========================");
        System.out.print("Ingresa el Titulo: ");
        nuevoTitulo.setTitulo(scanner.nextLine());
        System.out.print("Ingresa el Autor: ");
        nuevoTitulo.setAutor(scanner.nextLine());
        System.out.print("Ingresa el Género: ");
        nuevoTitulo.setGenero(scanner.nextLine());
        System.out.print("Ingresa el ISBN: ");
        nuevoTitulo.setIsbn(scanner.nextLine());
        System.out.print("Ingresa el Estado: ");
        nuevoTitulo.setDisponible(scanner.nextLine());

        while(enteros == false){
            try{
                System.out.print("Ingresa el Valor: ");
                nuevoTitulo.setValor(scanner.nextInt());
                System.out.print("Ingresa el Número de unidades disponibles: ");
                nuevoTitulo.setUnidadesDisponibles(scanner.nextInt());
                enteros = true;
            }catch (InputMismatchException e){
                System.out.println("Has ingresado un valor incorrecto. Ingresa un valor entero");
                scanner.next();
            }
        }

        nuevaLinea =nuevoTitulo.getTitulo() + "," +
                nuevoTitulo.getAutor() + "," +
                nuevoTitulo.getGenero() + "," +
                nuevoTitulo.getIsbn() + "," +
                nuevoTitulo.isDisponible() + "," +
                nuevoTitulo.getValor() + "," +
                nuevoTitulo.getUnidadesDisponibles();

        try(FileWriter writer = new FileWriter("comics_catalog.csv", true)){
            writer.append(nuevaLinea);
            writer.flush();
            System.out.println();
            System.out.println("Has ingresado de forma correcta el nuevo Título");
        }catch (IOException e){
            System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
        }
        inventarioLibros.add(nuevoTitulo);
    }

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
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("comics_catalog.csv"), "UTF-8"))){
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
                    System.out.println("Título encontrado");
                    System.out.println("Título eliminado de la Lista de inventario");

                }else{
                    nuevaLista.add(linea);
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("Archivo no encontrado");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        try (FileWriter writer = new FileWriter("comics_catalog.csv")) {
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

    public static void informeReservas(){

        System.out.println("\n===========================\n-     INFORME RESERVAS     -\n===========================");
        System.out.println();

        try(BufferedReader reader = new BufferedReader(new FileReader("reservas_usuario.txt"))) {
            int i=0;
            String linea;
            reader.readLine();  // Leer y descartar la primera línea

            while ((linea = reader.readLine()) != null) {
                i++;
                System.out.println(i+ ") " +linea);
            }
            System.out.println("Total Reservas: " + i);
        }catch (IOException e){
            System.out.println("El archivo no fue encontrado");
        }
    }

    public static void informeVentas(){
        int totalVentas = 0;

        System.out.println("\n===========================\n-     INFORME COMPRAS     -\n===========================");
        System.out.println("Nombre Usuario, Rut Usuario, Nombre Tìtulo, Autor, Género, Estado, ISBN, Fecha Compra, Valor, Unidades en Stock");
        System.out.println();

        try(BufferedReader reader = new BufferedReader(new FileReader("compras_usuario.txt"))) {
            int i=0;
            String linea;

            while ((linea = reader.readLine()) != null) {
                i++;
                System.out.println(i+ ") " +linea);
                String[] campos = linea.split(",");
                if(campos.length>=7){
                    totalVentas += Integer.parseInt(campos[7]);
                }

            }
            System.out.println();
            System.out.println("Total Ventas: $" + totalVentas);
        }catch (IOException e){
            System.out.println("El archivo no fue encontrado");
        }
    }

    public static HashSet<Libro> getInventarioLibros(){
        return inventarioLibros;
    }



}
