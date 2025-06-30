package servicio;
import modelo.tienda.Libro;
import modelo.usuario.RegistroUsuario;
import modelo.usuario.Usuario;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ServiciosTienda {
    public static Set<String> listaLibrosReservados = new HashSet<>();   // Lista que no permite duplicados en donde no importa el orden
    protected static Set<Libro> registroLibrosReservados = new TreeSet<>();// Entrega una lista ordenada que no permite repeticiones. En la clase libro está el override de la interfaz coparable para que pueda permitir usuarios con el mismo nombre

    public static Libro reservarTitulo(String rut, String libro) throws IOException {
        Libro libroReserva = new Libro(); // Este libro se utiliza para el usuario
        Libro registroLibro = new Libro(); // Ese objeto libro se utiliza para el registro de la Tienda
        boolean libroEncontrado = false;
        boolean conStock = true;
        boolean libroReservado = false;

        try(BufferedReader r = new BufferedReader(
                new InputStreamReader(new FileInputStream("comics_catalog.csv"), "UTF-8"))){
            String lineaStock;
            while((lineaStock = r.readLine()) != null){
                String[] campos = lineaStock.split(",");
                if (campos.length>= 7 && campos[0].toLowerCase().equals(libro.toLowerCase()) && Integer.parseInt(campos[6]) ==0){
                    System.out.println("No hay unidades disponibles.\nNo puedes reservar este Título.");
                    conStock = false;
                    libroEncontrado = true;
                }
            }
        }
        if(conStock){
            try(BufferedReader reader = new BufferedReader(new FileReader("reservas_usuario.txt"))){
                String linea;

                for(Libro libroR : registroLibrosReservados){
                    if(libroR.getTitulo().toLowerCase().equals(libro) && libroR.getRutArrendatario().equals(rut)){
                        System.out.println("Ya has realizado esta reserva");
                        libroReservado = true;
                        libroEncontrado = true;
                    }
                }
                while((linea = reader.readLine()) != null && !libroReservado && conStock){

                    String[] campos= linea.split(",");
                    BufferedReader reader2 = new BufferedReader(
                            new InputStreamReader(new FileInputStream("comics_catalog.csv"), "UTF-8"));
                    String nuevaLinea;
                    while ((nuevaLinea = reader2.readLine()) != null) {
                        String[] campos2 = nuevaLinea.split(",");
                        if (campos2.length >= 7 && campos2[0].toLowerCase().trim().equals(libro.toLowerCase())) {

                            libroEncontrado = true;

                            System.out.println("Reservaste de forma correcta el Título: " + libro);
                            libroReserva.setTitulo(campos2[0]);
                            libroReserva.setAutor(campos2[1]);
                            libroReserva.setGenero(campos2[2]);
                            libroReserva.setIsbn(campos2[3]);
                            libroReserva.setDisponible(campos2[4]);
                            libroReserva.setValor(Integer.parseInt(campos2[5]));

                            Usuario usurioIniciado = (Usuario) RegistroUsuario.getHashMapUsuarioRegistradoPorRut().get(rut);
                            String nombreArrendatario = usurioIniciado.getNombre() + " " + usurioIniciado.getApellidoPaterno();
                            registroLibro.setNombreArrendatario(nombreArrendatario);
                            registroLibro.setRutArrendatario(rut);
                            LocalDate fechaActual = LocalDate.now();
                            registroLibro.setFecha(fechaActual);
                            registroLibro.setTitulo(campos2[0]);
                            registroLibro.setAutor(campos2[1]);
                            registroLibro.setValor(Integer.parseInt(campos2[5]));

                            registroLibrosReservados.add(registroLibro);   // Esta lista permite tener un registro de los arriendo por usuario
                            RegistroUsuario.setHashMapReservasUsuario(rut, libroReserva); //Registro personal del usuario

                            try (FileWriter writer = new FileWriter("reservas_usuario.txt", true)) {
                                String lineaRegistro = (nombreArrendatario + "," +
                                        rut + "," +
                                        campos2[0] + "," +
                                        campos2[1] + "," +
                                        campos2[2] + "," +
                                        campos2[3] + "," +
                                        fechaActual + "," +
                                        campos2[5] + "," +
                                        campos2[6] + '\n');
                                writer.write(lineaRegistro);
                                writer.flush();
                                writer.close();
                                System.out.println("Libro reservado almacenado en la base de datos");

                                libroReservado = true;

                            } catch (IOException e) {
                                System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
                            }

                            System.out.println(libroReserva.toString());
                        }
                    }
                }
            }catch (IOException e){
                System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage());;
            }
        }
        if(libroEncontrado == false){
            System.out.println("El libro ingresado no ha sido encontrado. \nVerifica que la escriturea sea correcta respetando tildes");
        }
        return libroReserva;
    }

    public static void comprar(String rut, String libro) {
        boolean tituloEncontrado = false;
        Libro tituloBuscado = new Libro();
        String lineaCompra = "";

        ArrayList<Libro> listaReservasUsuario = RegistroUsuario.getArrayListReservasUsuario(rut);
        if (!listaReservasUsuario.isEmpty()){
            for(Libro libroReserva: listaReservasUsuario){
                if(libroReserva.getTitulo().toLowerCase().equals(libro.toLowerCase())){
                    tituloBuscado = libroReserva;
                    tituloEncontrado = true;
                }else{
                    System.out.println("No se ha encontrado el título ingresado en tu lista de reservas");
                }
            }
            if(tituloEncontrado){
                System.out.println("Tu compra ha sido realizada de forma correcta.");
                listaLibrosReservados.remove(libro);
                listaReservasUsuario.remove(tituloBuscado); // Se elimina de la lista que va a ser usada para reescribir el archivo
                RegistroUsuario.eliminarReserva(rut, tituloBuscado); // Se elimina de Hashmap de registro de reservas
                for (Libro libroRegistrado : registroLibrosReservados){
                    if (libroRegistrado.getTitulo().toLowerCase().equals(libro.toLowerCase())){
                        registroLibrosReservados.remove(libroRegistrado);
                        break;
                    }
                }
                if (listaReservasUsuario.isEmpty()) {
                    RegistroUsuario.setHashMapReservasUsurio(rut, new ArrayList<>());
                }
            }
        }else{
            System.out.println("Primero debes realizar la reserva del Título que deseas comprar");
        }

        if(tituloEncontrado){
            List<String> nuevaLista = new ArrayList<>();
            try(BufferedReader reader = new BufferedReader(new FileReader("reservas_usuario.txt"))) {
                String linea;

                while((linea = reader.readLine()) != null){
                    String[] campos = linea.split(",");
                    if(campos.length>=7 && campos[2].trim().toLowerCase().equals(libro.toLowerCase()) && campos[1].toLowerCase().equals(rut)){
                        lineaCompra = linea;
                    }else{
                        nuevaLista.add(linea);
                    }
                }
            }catch(FileNotFoundException e){
                System.out.println("Archivo no encontrado");
            }catch (IOException e){
                System.out.println(e.getMessage());
            }

            try (FileWriter writer = new FileWriter("reservas_usuario.txt")) {

                for(String nuevaLinea: nuevaLista){
                    writer.write(nuevaLinea +'\n');
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println("El archivo no fue encontrado o no pudo ser modificado");
            }

            try (FileWriter writer = new FileWriter("compras_usuario.txt", true)) {
                RegistroUsuario.getArrayListReservasUsuario(rut);
                writer.write('\n'+ lineaCompra);
                writer.flush();
            } catch (IOException e) {
                System.out.println("El archivo no fue encontrado o no pudo ser modificado");
            }
            System.out.println(tituloBuscado);
            System.out.println();
            System.out.println("Total compra: $" + tituloBuscado.getValor());

            RegistroUsuario.setHashMapLibrosCompradosPorRut(rut, tituloBuscado);

            reducirStock(libro);
        }
    }

    public static void listaReservasPorUsuario() {
        // Se puede implementar un iterator
        int i = 1;
        System.out.println("\n===========================\n-     INFORME DE ARRIENDOS     -\n===========================");
        for (Libro libro : registroLibrosReservados) {

            System.out.println((i++) + ")" + "ARRENDATARIO: " + libro.getNombreArrendatario() + ", RUT: " + libro.getRutArrendatario() + ", FECHA ARRIENDO: " + libro.getFecha() + ", TITULO LIBRO: " + libro.getTitulo() + ", AUTOR: " + libro.getAutor());
        }

    } // Este metodo fue reemplazado por la lectura directa del archivo CSV reservas_usuario.txt

    public static void inicializarRegistroLibrosReservados() {
        try(BufferedReader reader = new BufferedReader(new FileReader("reservas_usuario.txt"))){
            String linea;
            while((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 7 && !listaLibrosReservados.contains(campos[2])) {

                      // esto se puede modificar en el futuro actualizando el Estado (Disponible, Arrendado)

                    Libro libroArrendado = new Libro();
                    libroArrendado.setNombreArrendatario(campos[0]);
                    libroArrendado.setRutArrendatario(campos[1]);
                    libroArrendado.setTitulo(campos[2]);
                    libroArrendado.setAutor(campos[3]);
                    libroArrendado.setGenero(campos[4]);
                    libroArrendado.setIsbn(campos[5]);
                    libroArrendado.setFecha(LocalDate.parse(campos[6]));

                    registroLibrosReservados.add(libroArrendado);
                    listaLibrosReservados.add(libroArrendado.getTitulo().toLowerCase());
                }
            }
        }catch (IOException e){
            System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
        }

    }

    public static void reducirStock(String libro){
        String lineaNuevoStock ="";
        ArrayList <String> nuevaLista = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("comics_catalog.csv"), "UTF-8"))){
            String linea;
            while((linea = reader.readLine()) != null){
                String[] campos = linea.split(",");
                if(campos.length >=7 && campos[0].toLowerCase().equals(libro.toLowerCase())){
                    int nuevoStock = Integer.parseInt(campos[6]) -1;
                    lineaNuevoStock = campos[0] + "," +
                            campos[1] + "," +
                            campos[2] + "," +
                            campos[3] + "," +
                            campos[4] + "," +
                            campos[5] + "," +
                            nuevoStock;
                }else{
                    nuevaLista.add(linea);
                }
            }
            nuevaLista.add(lineaNuevoStock);

        }catch (IOException e){
            System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
        }
        try(FileWriter writer = new FileWriter("comics_catalog.csv")){
            for(String lineaNueva: nuevaLista){
                writer.append(lineaNueva+'\n');
            }
            System.out.println("Se ha actualizado de forma correcta el inventario");
            writer.flush();
        }catch (IOException e){
            System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
        }


    }

    public static boolean modificarStock(String libro, int nuevoStock){
        boolean libroEncontrado = false;
        String lineaNuevoStock ="";
        ArrayList <String> nuevaLista = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("comics_catalog.csv"), "UTF-8"))){
            String linea;
            while((linea = reader.readLine()) != null){
                String[] campos = linea.split(",");
                if(campos.length >=7 && campos[0].toLowerCase().equals(libro.toLowerCase())){
                    lineaNuevoStock = campos[0] + "," +
                            campos[1] + "," +
                            campos[2] + "," +
                            campos[3] + "," +
                            campos[4] + "," +
                            campos[5] + "," +
                            nuevoStock;
                    libroEncontrado = true;

                }else{
                    nuevaLista.add(linea);
                }
            }
            nuevaLista.add(lineaNuevoStock);

        }catch (IOException e){
            System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
        }
        try(FileWriter writer = new FileWriter("comics_catalog.csv")){
            for(String lineaNueva: nuevaLista){
                writer.append(lineaNueva+'\n');
            }
            if(libroEncontrado){
                System.out.println("Se ha actualizado de forma correcta el inventario");
                writer.flush();
            }

        }catch (IOException e){
            System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
        }
    return libroEncontrado;
    }
}