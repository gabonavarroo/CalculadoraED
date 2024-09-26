/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculadora;

/**
 *
 * @author EquipoBMNN
 */
public class PilaA<T> implements PilaADT<T>{
    private T[] datos;
    private int tope;
    private final int MAX=20;

    public PilaA() {
        datos =(T[]) new Object[MAX]; //TENGO QUE CASTEAR AL OBJETO a tipo T
        tope=-1; // pila vacia
    }
    
    public PilaA(int max){ //udpateo la longitud del arreglo
        datos =(T[]) new Object[max];
        tope=-1;
    }

    @Override
    public void push(T nuevoDato) {
        if(tope+1 == datos.length){ //verificar si esta llena
            expande();
        }else{
            tope++;
            datos[tope]=nuevoDato;
        }
    }
    
    private void expande(){ //fuera de la clase NADIE va a ver este metodo
        T[] masGrande= (T[]) new Object[datos.length*2];
        
        for(int i=0;i<=tope;i++){
            masGrande[i]=datos[i];
        }
        
        datos=masGrande;
    }

    @Override
    public T pop() {
        if(isEmpty()){
            throw new RuntimeException("La pila está vacía"); //lanza una exception y la ejecucion ya no sigue
        }
        T eliminado=datos[tope];
        datos[tope]=null;
        tope--;
        return eliminado;
    }

    @Override
    public boolean isEmpty() {
       return tope == -1; 
    }

    @Override
    public T peek() {
        if(isEmpty()){
            throw new RuntimeException("La pila está vacía"); //lanza una exception y la ejecucion ya no sigue
        }
        return datos[tope];
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<=tope;i++){
            sb.append(datos[i]).append(" ");
        }
        return sb.toString();
    }
    
    
    
    
    
}
