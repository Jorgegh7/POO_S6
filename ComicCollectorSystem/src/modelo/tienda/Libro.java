package modelo.tienda;

import java.time.LocalDate;

public class Libro implements Comparable<Libro> {
    private String titulo;
    private String autor;
    private String genero;
    private String isbn;
    private String disponible;
    private String nombreArrendatario;
    private String rutArrendatario;
    private LocalDate fecha;
    private int valor;
    private int unidadesDisponibles;

    public Libro() {}

    public Libro(String titulo, String autor, String genero, String isbn, String disponible) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.isbn = isbn;
        this.disponible = disponible;
    }

    public Libro(String arrendatario, LocalDate fecha, String titulo, String autor) {
        this.nombreArrendatario = arrendatario;
        this.fecha = fecha;
        this.titulo = titulo;
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String isDisponible() {
        return disponible;
    }
    public void setDisponible(String disponible) {
        this.disponible = disponible;
    }
    public String getNombreArrendatario() {
        return nombreArrendatario;
    }
    public void setNombreArrendatario(String nombreArrendatario) {
        this.nombreArrendatario = nombreArrendatario;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public String getRutArrendatario() {
        return rutArrendatario;
    }
    public void setRutArrendatario(String rutArrendatario){this.rutArrendatario = rutArrendatario;}
    public int getValor() {
        return valor;
    }
    public void setValor(int valor) {
        this.valor = valor;
    }
    public int getUnidadesDisponibles() {
        return unidadesDisponibles;
    }
    public void setUnidadesDisponibles(int unidadesDisponibles) {
        this.unidadesDisponibles = unidadesDisponibles;
    }

    @Override
    public String toString() {
        return "\n===========================\n-     INFO LIBRO     -\n===========================" +'\n' +
                "Titulo: " + titulo + '\n' +
                "Autor: " + autor + '\n' +
                "Genero: " + genero + '\n' +
                "ISBN: " + isbn + '\n' +
                "Estado: " + disponible;
    }

    @Override
    public int compareTo(Libro libro) {
        int resultado = this.getNombreArrendatario().compareTo(libro.getNombreArrendatario());
        if (resultado == 0){ // si en la comparacion los nombres de los Arrendatarios son iguales va a comparar con el isbn
            resultado = this.getTitulo().compareTo(libro.getTitulo());
        }
        return resultado;
    }
}

