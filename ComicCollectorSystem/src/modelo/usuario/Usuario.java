package modelo.usuario;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Usuario {
    public  String rut;
    private String nombre;
    private String apellidoPaterno;
    private String domicilio;
    private String comuna;
    private long telefono;
    private int claveAcceso;


    public Usuario() {}

    public Usuario(String nombre, String apellidoPaterno, String domicilio, String comuna, long telefono, int claveAcceso) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.domicilio = domicilio;
        this.comuna = comuna;
        this.telefono = telefono;
        this.claveAcceso = claveAcceso;
    }

    public Usuario(String rut, String nombre, String apellidoPaterno, String domicilio, String comuna, long telefono, int claveAcceso) {
        this.rut = rut;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.domicilio = domicilio;
        this.comuna = comuna;
        this.telefono = telefono;
        this.claveAcceso = claveAcceso;
    }

    public  String getRut() {
        return rut;
    }
    public  void setRut(String rut) {this.rut = rut;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }
    public String getDomicilio() {
        return domicilio;
    }
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }
    public String getComuna() {
        return comuna;
    }
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }
    public long getTelefono() {
        return telefono;
    }
    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }
    public int getClaveAcceso() {
        return claveAcceso;
    }
    public void setClaveAcceso(int claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public long registroTelefono(){
        Scanner scanner = new Scanner(System.in);
        long numero = 0;
        boolean entradaValida = false;

        while (!entradaValida) {
            try {
                System.out.println("Ingresa tu número de Teléfono sin el código +569:");
                numero = scanner.nextLong();

                if (numero < 10000000) {
                    System.out.println("Número inválido. Debe tener al menos 8 dígitos.");
                    System.out.println("Ingresa un nuevo número:");
                    scanner.nextLine(); // Limpiar el buffer del scanner
                } else {
                    entradaValida = true; // Salir del bucle
                }

            } catch (InputMismatchException e) {
                System.out.println("Error: Debes ingresar solo números.");
                System.out.println("Mensaje de error: " + e.getMessage());
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
        }
        return numero;
    }

    public static String verificarRut(String rut) {
        Scanner scanner= new Scanner(System.in);

        if (rut.length() < 11 | rut.length() > 12) {
            while (rut.length() < 11 | rut.length() > 12) {
                System.out.println("Rut ingresado invalido");
                System.out.println("Ingresa tu rut nuevamente");
                rut = scanner.next();
            }
        }
        return rut;
    }

    public void mostrarInformacionUsuario(Usuario usuario) {
        System.out.println("Rut: " + usuario.getRut() + '\n' +           //Ojo aqui con la variable estatica
                "Nombre: " + nombre + '\n' +
                "Apellido Paterno: " + apellidoPaterno + '\n' +
                "Domicilio: " + domicilio + '\n' +
                "Comuna: " + comuna + '\n' +
                "Telefono: +569 " + telefono + '\n' +
                "Clave Acceso: " + claveAcceso);
    }

    public int  claveVerificada(){
        Scanner scanner = new Scanner(System.in);
        int clave=0;
        boolean entradaValida = false;

        while(!entradaValida) {
            try {
                System.out.println("Ingresa una clave que contenga 4 numero");
                clave = scanner.nextInt();
                if (clave > 9999) {
                    System.out.println("Debes ingresar una clave de 4 digitos");
                } else if (clave > 1000) {
                    entradaValida = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debes ingresar solo números.");
                System.out.println("Mensaje de error: " + e.getMessage());
                scanner.nextLine(); //limpiar el buffer para no caer en un loop infinito
            }
        }
        return clave;
    }

}
