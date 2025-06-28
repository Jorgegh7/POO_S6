package servicio;
import modelo.biblioteca.Libro;
import modelo.usuario.RegistroUsuario;
import modelo.usuario.Usuario;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ServiciosBiblioteca {
    public static Set<String> listaLibrosArrendados = new HashSet<>();   // Lista que no permite duplicados en donde no importa el orden
    protected static Set<Libro> registroLibrosArrendados = new TreeSet<>();  // Entrega una lista ordenada que no permite repeticiones. En la clase libro está el override de la interfaz coparable para que pueda permitir usuarios con el mismo nombre

    public static Libro prestarLibro(String rut, String libro) throws IOException {
        Libro libroArriendo = new Libro(); // Este libro se utiliza para el usuario
        Libro registroLibro = new Libro(); // Ese ojeto libro se utiliza para el registro de la Biblioteca
        boolean libroEncontrado = false;


        try(BufferedReader reader = new BufferedReader(new FileReader("arriendos_usuario.txt"))){
            String linea;
            boolean libroArrendado = false;


            if(listaLibrosArrendados.contains(libro.toLowerCase())){
                System.out.println("Ya has arrendado este libro");
                libroArrendado = true;
                libroEncontrado = true;
            }

            while((linea = reader.readLine()) != null && !libroArrendado){

                String[] campos= linea.split(",");
                //System.out.println("Línea: " + linea);
                //System.out.println("Campos encontrados: " + campos.length);

                if(campos.length>= 7 &&campos[2].equalsIgnoreCase(libro) && !libroArrendado){
                    System.out.println("Este libro ya ha sido arrendado");
                    libroArrendado = true;
                    libroEncontrado = true;

                }else{
                    BufferedReader reader2 = new BufferedReader(new FileReader("libros_espanol_csv(100 Disponible).txt"));
                    String nuevaLinea;
                    while ((nuevaLinea = reader2.readLine()) != null) {
                        String[] campos2 = nuevaLinea.split(",");
                        if (campos2.length > 0 && campos2[0].toLowerCase().trim().equals(libro.toLowerCase())) {

                            libroEncontrado = true;

                            System.out.println("Arrendaste de forma correcta el libro " + libro);
                            libroArriendo.setTitulo(campos2[0]);
                            libroArriendo.setAutor(campos2[1]);
                            libroArriendo.setGenero(campos2[2]);
                            libroArriendo.setIsbn(campos2[3]);
                            libroArriendo.setDisponible(campos2[4]);

                            //Almacenamos el Objeto libro para el registro de la Biblioteca utilizando TreeSet
                            Usuario usurioIniciado = (Usuario) RegistroUsuario.getHashMapUsuarioRegistradoPorRut().get(rut);
                            String nombreArrendatario = usurioIniciado.getNombre() + " " + usurioIniciado.getApellidoPaterno();
                            registroLibro.setNombreArrendatario(nombreArrendatario);
                            registroLibro.setRutArrendatario(rut);
                            LocalDate fechaActual = LocalDate.now();
                            registroLibro.setFecha(fechaActual);
                            registroLibro.setTitulo(campos2[0]);
                            registroLibro.setAutor(campos2[1]);

                            registroLibrosArrendados.add(registroLibro);   // Esta lista permite tener un registro de los arriendo por usuario
                            listaLibrosArrendados.add(campos2[0].toLowerCase());
                            RegistroUsuario.setHashMapArriendosUsuario(rut, libroArriendo); //Registro personal del usuario

                            try (FileWriter writer = new FileWriter("arriendos_usuario.txt", true)) {
                                String lineaRegistro = (nombreArrendatario + "," +
                                        rut + "," +
                                        campos2[0] + "," +
                                        campos2[1] + "," +
                                        campos2[2] + "," +
                                        campos2[3] + "," +
                                        fechaActual + '\n');
                                writer.write(lineaRegistro);
                                writer.flush();
                                writer.close();
                                System.out.println("Libro arrendado almacenado en la base de datos");

                                libroArrendado = true;

                            } catch (IOException e) {
                                System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
                            }

                            System.out.println(libroArriendo.toString());

                        }
                    }
                }
            }
        }catch (IOException e){
            System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());;
        }
        if(libroEncontrado == false){
            System.out.println("El libro ingresado no ha sido encontrado. \nVerifica que la escriturea sea correcta respetando tildes");
        }
        return libroArriendo;
    }

    public static void recibirLibroPrestado(String rut, String libro) {

        if (listaLibrosArrendados.contains(libro.toLowerCase())) {

            listaLibrosArrendados.remove(libro);
            ArrayList<Libro> listaArriendosUsuario = RegistroUsuario.getArrayListArriendosUsuario(rut);

            for (int i = 0; i < listaArriendosUsuario.size(); i++) {
                Libro libroArriendo = (Libro) listaArriendosUsuario.get(i);

                if (libroArriendo.getTitulo().toLowerCase().contains(libro)) {
                    System.out.println("Has devuelto el libro de forma correcta.");
                    listaArriendosUsuario.remove(i);
                    RegistroUsuario.eliminarArriendo(rut, libroArriendo);

                    if (listaArriendosUsuario.isEmpty()) {
                        RegistroUsuario.setHashMapArriendosUsurio(rut, new ArrayList<>());
                    }
                }
            }
        } else {
            System.out.println("El libro: " + libro + "  no se encuentra entre los libros arrendados");
        }

        List<String> nuevaLista = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader("arriendos_usuario.txt"))) {
            String linea;

            while((linea = reader.readLine()) != null){
                String[] campos = linea.split(",");
                if(campos.length>=7 && campos[2].trim().toLowerCase().equals(libro)){
                    System.out.println("Libro encontrado en base de datos");

                }else{
                    nuevaLista.add(linea);
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("Archivo no encontrado");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        try (FileWriter writer = new FileWriter("arriendos_usuario.txt")) {
            System.out.println("Inicio de SobreEscritura base de datos");

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


    public static void listaArriendoPorUsuario() {
        // Se puede implementar un iterator
        int i = 1;
        System.out.println("\n===========================\n-     INFORME DE ARRIENDOS     -\n===========================");
        for (Libro libro : registroLibrosArrendados) {

            System.out.println((i++) + ")" + "ARRENDATARIO: " + libro.getNombreArrendatario() + ", RUT: " + libro.getRutArrendatario() + ", FECHA ARRIENDO: " + libro.getFecha() + ", TITULO LIBRO: " + libro.getTitulo() + ", AUTOR: " + libro.getAutor());
        }

    }


    public static void inicializarRegistroLibrosArrendados() {
        try(BufferedReader reader = new BufferedReader(new FileReader("arriendos_usuario.txt"))){
            String linea;
            while((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 7 && !listaLibrosArrendados.contains(campos[2])) {

                      // esto se puede modificar en el futuro actualizando el Estado (Disponible, Arrendado)

                    Libro libroArrendado = new Libro();
                    libroArrendado.setNombreArrendatario(campos[0]);
                    libroArrendado.setRutArrendatario(campos[1]);
                    libroArrendado.setTitulo(campos[2]);
                    libroArrendado.setAutor(campos[3]);
                    libroArrendado.setGenero(campos[4]);
                    libroArrendado.setIsbn(campos[5]);
                    libroArrendado.setFecha(LocalDate.parse(campos[6]));

                    registroLibrosArrendados.add(libroArrendado);
                    listaLibrosArrendados.add(libroArrendado.getTitulo().toLowerCase());
                }
            }
        }catch (IOException e){
            System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
        }

    }

}