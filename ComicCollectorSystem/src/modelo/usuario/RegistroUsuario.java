package modelo.usuario;

import modelo.tienda.Libro;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class RegistroUsuario {
    static HashMap<String, Object> usuarioRegistradoPorRut = new HashMap<>();
    static HashMap<String, Integer> claveAccesoUsuarioPorRut = new HashMap<>();
    static HashMap<String, ArrayList<Libro>> reservasUsuarioPorRut = new HashMap<>();
    static HashMap<String, ArrayList<Libro>> librosCompradosPorRut = new HashMap<>();

    public static void registroUsuario(Usuario usuario) {
        Scanner scanner = new Scanner(System.in);
        boolean newUser = true;

        System.out.println("Ingresa tu rut con puntos y guion");
        String rut = scanner.nextLine();


        try(BufferedReader reader = new BufferedReader(new FileReader("registro_usuario.txt"))){
            String linea;
            while((linea = reader.readLine()) != null && newUser){
                String[] campos = linea.split(",");
                if(campos.length>0 && campos[0].toLowerCase().equals(rut)){
                    System.out.println("Este usuario ya se encuentra registrado en nustra base de datos");
                    newUser = false;

                }else{
                    newUser = true;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (newUser){
            usuario.setRut(Usuario.verificarRut(rut));
            System.out.print("Ingresa tu Nombre: ");
            usuario.setNombre(scanner.nextLine());
            System.out.print("Ingresa tu Apellido Paterno: ");
            usuario.setApellidoPaterno(scanner.nextLine());
            System.out.print("Ingresa tu Domicilio: ");
            usuario.setDomicilio(scanner.nextLine());
            System.out.print("Ingresa tu Comuna: ");
            usuario.setComuna(scanner.nextLine());
            usuario.setTelefono(usuario.registroTelefono());
            usuario.setClaveAcceso(usuario.claveVerificada());


            System.out.println();
            System.out.println("\n===========================\n-     NUEVO USUARIO     -\n===========================");
            usuario.mostrarInformacionUsuario(usuario);
            RegistroUsuario.usuarioRegistradoPorRut.put(usuario.getRut(), usuario);
            RegistroUsuario.claveAccesoUsuarioPorRut.put(usuario.getRut(), usuario.getClaveAcceso());

            System.out.println();
            archivarUsuario(usuario);
            System.out.println("Usuario registrado correctamente");
            System.out.println(usuario.getNombre() + " Bienvenido a la BIBLIOTECA DUOC UC");
            System.out.println();
            newUser = false;
        }

    }

    public static HashMap getHashMapClaveAcceso() {
        return claveAccesoUsuarioPorRut;
    }

    public static void archivarUsuario(Usuario usuario) {
        try (FileWriter writer = new FileWriter("registro_usuario.txt", true)) {

            String linea = (usuario.getRut() + ","+
                    usuario.getNombre() + "," +
                    usuario.getApellidoPaterno() + "," +
                    usuario.getDomicilio() + "," +
                    usuario.getComuna() + "," +
                    "+569" + usuario.getTelefono() + "," +
                    usuario.getClaveAcceso()) + '\n';
            writer.write(linea);
            writer.flush();
            System.out.println("Usuario almacenado en base de datos");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void setHashMapReservasUsuario(String rut, Libro libro) {
        if (!reservasUsuarioPorRut.containsKey(rut)) {
            reservasUsuarioPorRut.put(rut, new ArrayList<>());
        }
        reservasUsuarioPorRut.get(rut).add(libro);
    }

    public static void setHashMapReservasUsurio(String rut, ArrayList<Libro> lista){
        reservasUsuarioPorRut.put(rut,lista);
    }

    public static void eliminarReserva(String rut, Libro libro){
        reservasUsuarioPorRut.get(rut).remove(libro);
    }

    public static void getHashMapReservasUsuario(String rut){ //Este metodo hay que modificarlo para que lea el archivo arriendos_usuario
        try{
            List<Libro> librosUsuario = reservasUsuarioPorRut.get(rut);
            if (librosUsuario.isEmpty()){
                System.out.println("Aun no realizas ningun arriendo");
            }else {
                for(int i=0; i<librosUsuario.size(); i++) {
                    Libro libro = librosUsuario.get(i);
                    System.out.println("-------------------------");
                    System.out.println("Nombre: " + libro.getTitulo());         // Se van a rellenar desde los campos[]
                    System.out.println("Autor: " + libro.getAutor());
                    System.out.println("Género: " + libro.getGenero());
                    System.out.println("ISBN: " + libro.getIsbn());
                    System.out.println("Fecha Reserva: " + libro.getFecha());
                    System.out.println("-------------------------");
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Aun no realizas ningun arriendo");
        }
    }

    public static ArrayList<Libro> getArrayListReservasUsuario(String rut){
        ArrayList<Libro> arriendosUsuario = new ArrayList<>();
        arriendosUsuario= reservasUsuarioPorRut.get(rut);
        return arriendosUsuario;
    }

    public static HashMap getHashMapUsuarioRegistradoPorRut() {
        return usuarioRegistradoPorRut;
    }

    public static void inicializarListaUsuarios(){
        try(BufferedReader reader = new BufferedReader(new FileReader("registro_usuario.txt"))){
            String linea;
            while((linea = reader.readLine()) != null){
                String[] campos = linea.split(",");

                if(campos.length == 7){
                    String rut= campos[0];
                    String nombre = campos[1];
                    String apellido = campos[2];
                    String direccion = campos[3];
                    String comuna = campos[4];

                    long telefono = Integer.parseInt(campos[5].substring(4));
                    int claveAcceso = Integer.parseInt(campos[6]);

                    Usuario usuario = new Usuario(rut, nombre, apellido, direccion, comuna, telefono, claveAcceso);
                    usuarioRegistradoPorRut.put(rut, usuario);
                    claveAccesoUsuarioPorRut.put(rut, claveAcceso);
                }
            }

        }catch (FileNotFoundException e){
            System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
}

    public static void inicializarReservasUsuario(String rut){

        try(BufferedReader reader = new BufferedReader(new FileReader("reservas_usuario.txt"))){
            ArrayList<Libro> librosUsuario = new ArrayList<>();
            String linea;

            while ((linea = reader.readLine()) != null){
                String[] campos = linea.split(",");
                if(campos.length >= 7 && campos[1].equals(rut)){
                    Libro libro = new Libro();
                    //libro.setNombreArrendatario();
                    libro.setRutArrendatario(rut);
                    libro.setTitulo(campos[2]);
                    libro.setAutor(campos[3]);
                    libro.setGenero(campos[4]);
                    libro.setIsbn(campos[5]);
                    libro.setFecha(LocalDate.parse(campos[6]));

                    librosUsuario.add(libro);
                }
            }
            reservasUsuarioPorRut.put(rut, librosUsuario);
        }catch (IOException e){
            System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
        }



    }

    public static void setHashMapLibrosCompradosPorRut(String rut, Libro libro) {
        ArrayList<Libro> comprasUsuario = new ArrayList<>();
        comprasUsuario= reservasUsuarioPorRut.get(rut);
        comprasUsuario.add(libro);
    }

    public static void historiaComprasPorUsuario(String rut){

        try(BufferedReader reader = new BufferedReader(new FileReader("compras_usuario.txt"))){
            String linea;
            while((linea = reader.readLine()) != null){
                String[] campos = linea.split(",");
                if(campos.length>=7 && campos[1].equals(rut)){
                    System.out.println("-------------------------");
                    System.out.println("Nombre: " + campos[2]);
                    System.out.println("Autor: " + campos[3]);
                    System.out.println("Género: " + campos[4]);
                    System.out.println("ISBN: " + campos[5]);
                    System.out.println("Fecha Compra: " + campos[6]);
                    System.out.println("Valor Título: $" + campos[7]);
                    System.out.println("-------------------------");

                }


            }

        }catch (IOException e){
            System.out.println("El archivo no ha sido encontrado: " + e.getMessage());
        }
    }





}
