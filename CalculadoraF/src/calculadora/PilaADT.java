/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package calculadora;

/**
 *
 * @author EquipoBMNN
 */
public interface PilaADT <T>{ // aviso que T va a ser el generico
    public void push(T dato);
    public T pop();
    public boolean isEmpty();
    public T peek();
}
