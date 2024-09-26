/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package calculadora;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author EquipoBMNN
 * @version final.0
 */

/**
 * <pre>
 * Clase AppCalc
 * 
 * Contiene la interpretacion de la interfaz grafica de la calculadora
 * Opera con los datos proporcionados por el usuario
 * Contiene todos los métodos necesarios para calcular el resultado y evitar errores
 * Contiene las excepciones y fallos del programa
 * </pre>
 */
public class AppCalc extends javax.swing.JFrame {
    public String cadena;
    /**
     * Creates new form AppCalc
     */
    
    /**
     * Construye un objeto de tipo AppCalc para inicializar la calculadora
     */
    public AppCalc() {
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    /**
     * Método que revisa que una cadena recibida contenga un uso válido y ordenado de parentesis
     * Previene errores en desbalance de paréntesis
     * @param cad Cadena recibida del TextBox con expresion algebraica, de la cual se revisara su uso de parentesis
     * @return 
     * <ul>
     * <li> true: los parentesis estan balanceados y usados correctamente
     * <li> false: existen errores de validez y orden en los parentesis
     * </ul>
     */
    public boolean balanceParentesis(String cad){
        PilaADT<Character> pila = new PilaA();
        boolean resp = true;
        
        for(int i = 0; i < cad.length(); i++){
            if(cad.charAt(i) == '(')
                pila.push(cad.charAt(i));
            else
                if(cad.charAt(i) == ')')
                    if(!pila.isEmpty())
                        pila.pop();
                    else
                        resp = false;
        }
        if(!pila.isEmpty())
            resp = false;     
        return resp;
    }
     
    /** 
     * Método que analiza una cadena para verificar la validez de los signos matemáticos.
     * Se asegura de que no existan errores de operadores mal posicionados o paréntesis vacíos o signos en lugares equívocos. 
     * @param cad cadena texto que contiene una expresión matemática por revisar. 
     * @return <ul>
     * <li> true: si el uso de signos matemáticos fueron utilizados y escritos de manera correcta.
     * <li> false: si existe un operador al inicio de la cadena, excepto si es un signo negativo.
     * <li> false: si existen dos operadores seguidos, excepto cuando el segundo es un signo negativo (indicando un número negativo).
     * <li> false: Si hay paréntesis vacíos “()”.
     * <li> false: Si hay un operador después de un paréntesis de apertura “(“, excepto si es un signo negativo. 
     * <li> false: Si después de un signo sigue inmediatamente un paréntesis de cierre ')'. 
     * <li> false: Si el último valor de la cadena es un operador.
     * <li> false: Si hay un operador al final de la expresión.
     * <li> flase: Si solo se ingresa un punto decimalen la expresión.
     * </ul>
     */
    public boolean analisisSignos(String cad){
        boolean resp = true;
        if(esOperador(cad.charAt(0)) && cad.charAt(0) != '-')
            resp = false; 
        for (int i = 0; i < cad.length() - 1; i++) {
            char actual = cad.charAt(i);
            char siguiente = cad.charAt(i + 1);
            // Evitar considerar como error si el signo '-' es parte de un número negativo
            if (esOperador(actual) && esOperador(siguiente) && siguiente != '-') 
                resp = false; // Dos operadores seguidos
            if (actual == '(' && siguiente == ')') 
                resp = false; // Paréntesis vacíos
            if(actual == '(' && esOperador(siguiente) && siguiente != '-')
                resp = false; // No pueda haber un signo exactamente después de un parentesis (         
            if(esOperador(actual) && siguiente == ')')
                resp = false; // No puede haber un parentesis ) despues de un signo 
            /*if(actual == '.' && esOperador(siguiente))
                resp = false;*/
        }
        if(esOperador(cad.charAt(cad.length()-1)))
            resp = false;  
        if(cad.charAt(cad.length()-1) == '.'){
            resp = false;
            resp = cad.length() > 1 && Character.isLetterOrDigit(cad.charAt(cad.length() - 2));     
        }
        return resp;
    }
    
    /**
     * Revisa que una cadena que contiene un punto decimal esté bien posicionado
     * Protege de errores de sintaxis con respecto al uso de los puntos
     * @param cad cadena con expresion algebraica a la cual se le revisara su uso de decimales
     * @return <ul>
     * <li> true: si el uso de los puntos fue el adecuado, es decir, se usó para números decimales
     * <li> false: si existen errores en cuanto al uso de la puntuación
     * </ul>
     */
    public boolean revisaDecimal(String cad){
        PilaADT<Character> pila = new PilaA();
        boolean resp = true;
        
        for(int i = 0; i < cad.length(); i++){
            if(cad.charAt(i) == '.')
                if(pila.isEmpty())
                    pila.push(cad.charAt(i));
                else
                    resp = false;
            if(esOperador(cad.charAt(i)) && !pila.isEmpty())
                pila.pop();
        }
        return resp;
    }
    
    /**
     * Confirma que el análisis de los signos, balance de paréntesis y puntos decimales estén correctamente 
     * hechos. Así se comprueba la correcta escritura de la cadena para garantizar su ejecución.
     * Identifica errores de sintaxis en alguno de los 3 casos
     * @param cad cadena con expresion algebraica para evaluar su sintaxis
     * @return <ul>
     * <li> true: si la sintaxis de puntos, signos y paréntesis están realizadas correctamente
     * <li> false: si existe al menos un error en alguna de la sintaxis con respecto a la cadena dada
     * </ul>
     */
    public boolean analisisExpresion(String cad){
        boolean rSignos = analisisSignos(cad);
        boolean rParentesis = balanceParentesis(cad);
        boolean rDecimal = revisaDecimal(cad);
        return rSignos && rParentesis && rDecimal;
    }
    
    /**
     * Jerarquiza el orden de las operaciones aritméticas, dándole prioridad de acuerdo a las reglas de 
     * jerarquización aritmética.
     * Garantiza la correcta operación descrita por la cadena de caracteres.
     * @param op caracter que se evaluara su jerarquia como operador
     * @return <ul>
     * <li> 1: si se trata de la suma o resta, últimos en jerarquía de operaciones.
     * <li> 2: si se trata de la división o producto de operandos.
     * <li> 3: si se trata de la potencia de algún término puesto en la cadena.
     * <li> -1: si se trata de algún otro caracter u operación de la calculadora no considerada.
     * </ul>
     */
    public int jerarquia(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }
    
    /**
     * Verifica que el caracter dado sea un operador aritmético: suma, resta, división, producto o potencia
     * @param c caracter a evaluar si es o no un operador
     * @return <ul>
     * <li> true: si el caracter analizado es efectivamente un operador aritmético.
     * <li> false: si el caracter recibido es distinto a un operador aritmético.
     * </ul>
     */
    private boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }
    
    /**
     * Recibe una expresion algebraica escrita en notación infija, previamente revisada para prevenir errores, la cual va a convertir a una notación postfija
     * Itera a lo largo de la cadena para reacomodar la expresión en una lista con ayuda de pilas.
     * @param expresion Cadena recibida del TextBox la cual ha sido revisada por errores de sintaxis
     * @return Un arreglo de tipo String en el cual en cada uno de sus indices se encuentra un elemento de la expresion transformada en notación postfija
     */
    public String[] infijaAPostfija(String expresion) {
        PilaADT<Character> pila = new PilaA<>();
        List<String> post = new ArrayList<>();
        StringBuilder aux = new StringBuilder();

        for (int i = 0; i < expresion.length(); i++) {
            char charActual = expresion.charAt(i);

            // Manejo de números y caracteres (incluyendo números negativos)
            if (Character.isLetterOrDigit(charActual) || charActual == '.' || 
                (charActual == '-' && (i == 0 || expresion.charAt(i - 1) == '(' || esOperador(expresion.charAt(i - 1))))) {
                aux.append(charActual); // Agregar dígitos y signos negativos
            } else {
                // Si hay algo acumulado en `aux`, añadirlo como un número
                if (aux.length() > 0) {
                    post.add(aux.toString());
                    aux.setLength(0); // Limpiar el acumulador
                }
                // Si el carácter es un '(', lo empujamos a la pila
                if (charActual == '(' ) {
                    if(i != 0 && ((Character.isLetterOrDigit(expresion.charAt(i - 1))) || expresion.charAt(i - 1) == ')' )){
                        pila.push('*');
                    }
                    pila.push(charActual);
                }
                // Si el carácter es un ')', vaciamos la pila hasta el '('
                else if (charActual == ')') {
                    while (!pila.isEmpty() && pila.peek() != '(') {
                        post.add(String.valueOf(pila.pop()));
                    }                        
                    pila.pop(); // Sacamos el '('
                    if(i < expresion.length() - 1 && (Character.isLetterOrDigit(expresion.charAt(i + 1))))
                        pila.push('*');
                }
                // Si el carácter es un operador
                else if (esOperador(charActual)) {
                    while (!pila.isEmpty() && jerarquia(pila.peek()) >= jerarquia(charActual)) {
                        post.add(String.valueOf(pila.pop()));
                    }
                    pila.push(charActual);
                }   
            }
        }
        // Añadir el último número acumulado si queda algo en `aux`
        if (aux.length() > 0) {
            post.add(aux.toString());
        }
        // Vaciamos la pila en la salida
        while (!pila.isEmpty()) {
            post.add(String.valueOf(pila.pop()));
        }
        return post.toArray(new String[0]); // Convertir lista a arreglo
    }
    
    /**
     * Realiza las operaciones correspondientes a la expresión en notación postfija, con ayuda de una pila, para poder calcular el resultado de la expresión algebaica en infija 
     * Itera a lo largo del arreglo almacenando los valores y operando con ellos cuando un operador algebraíco salga
     * @param arre Arreglo con la expresion convertida a notacion postfija y separando numeros y operadores en sus indices
     * @return una variable double con el total calculado tras realizar todas las operaciones en la expresion, ordenada en postfija
     */
    public double calculaTotal(String arre[]){
        PilaADT<Double> pila = new PilaA<>(); // Pila para almacenar los números
        double n1, n2, resultado = 0; // Variables para los cálculos
        int i = 0; 

        while(i < arre.length && arre[i] != null){
            // Verificamos si el elemento es un número (positivo o negativo)
            try{
                pila.push(Double.parseDouble(arre[i]));
            } catch (NumberFormatException e) {
                // Si no es un número, entonces es un operador
                n2 = pila.pop(); // Primer número
                n1 = pila.pop(); // Segundo número

                // Realizamos la operación según el operador
                switch (arre[i].charAt(0)) {
                    case '+': 
                        resultado = n1 + n2;
                        break;
                    case '-': 
                        resultado = n1 - n2;
                        break;
                    case '*': 
                        resultado = n1 * n2;
                        break;
                    case '/': 
                        resultado = n1 / n2;
                        break;
                    case '^': 
                        resultado = Math.pow(n1, n2);
                        break;
                    default: 
                        resultado = 0; // Si el operador no es válido
                        break;
                }
                // Guardar el resultado en la pila
                pila.push(resultado);
            }
            i++;
        }
        // El último número en la pila será el resultado final
        return pila.pop();
    }
            
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        bCero = new javax.swing.JButton();
        bPunto = new javax.swing.JButton();
        bIgual = new javax.swing.JButton();
        bSuma = new javax.swing.JButton();
        bUno = new javax.swing.JButton();
        bDos = new javax.swing.JButton();
        bTres = new javax.swing.JButton();
        bResta = new javax.swing.JButton();
        bSiete = new javax.swing.JButton();
        bOcho = new javax.swing.JButton();
        bNueve = new javax.swing.JButton();
        bCuatro = new javax.swing.JButton();
        bCinco = new javax.swing.JButton();
        bSeis = new javax.swing.JButton();
        bMultiplica = new javax.swing.JButton();
        bDividir = new javax.swing.JButton();
        bParAbierto = new javax.swing.JButton();
        bParCerrado = new javax.swing.JButton();
        bElevar = new javax.swing.JButton();
        bBorrar = new javax.swing.JButton();
        casilla = new javax.swing.JLabel();
        bCambio = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setForeground(java.awt.Color.black);

        bCero.setText("0");
        bCero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCeroActionPerformed(evt);
            }
        });

        bPunto.setText(".");
        bPunto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPuntoActionPerformed(evt);
            }
        });

        bIgual.setText("=");
        bIgual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIgualActionPerformed(evt);
            }
        });

        bSuma.setText("+");
        bSuma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSumaActionPerformed(evt);
            }
        });

        bUno.setText("1");
        bUno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUnoActionPerformed(evt);
            }
        });

        bDos.setText("2");
        bDos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDosActionPerformed(evt);
            }
        });

        bTres.setText("3");
        bTres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTresActionPerformed(evt);
            }
        });

        bResta.setText("-");
        bResta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRestaActionPerformed(evt);
            }
        });

        bSiete.setText("7");
        bSiete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSieteActionPerformed(evt);
            }
        });

        bOcho.setText("8");
        bOcho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOchoActionPerformed(evt);
            }
        });

        bNueve.setText("9");
        bNueve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNueveActionPerformed(evt);
            }
        });

        bCuatro.setText("4");
        bCuatro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCuatroActionPerformed(evt);
            }
        });

        bCinco.setText("5");
        bCinco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCincoActionPerformed(evt);
            }
        });

        bSeis.setText("6");
        bSeis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSeisActionPerformed(evt);
            }
        });

        bMultiplica.setText("X");
        bMultiplica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMultiplicaActionPerformed(evt);
            }
        });

        bDividir.setText("/");
        bDividir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDividirActionPerformed(evt);
            }
        });

        bParAbierto.setText("(");
        bParAbierto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bParAbiertoActionPerformed(evt);
            }
        });

        bParCerrado.setText(")");
        bParCerrado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bParCerradoActionPerformed(evt);
            }
        });

        bElevar.setText("^");
        bElevar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bElevarActionPerformed(evt);
            }
        });

        bBorrar.setText("C");
        bBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBorrarActionPerformed(evt);
            }
        });

        casilla.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        casilla.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 255), 1, true));

        bCambio.setText("+/-");
        bCambio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCambioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bParAbierto, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(bParCerrado, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bElevar, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                            .addComponent(bCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(bCuatro, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(bUno, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                                        .addComponent(bCero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(bPunto, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                                            .addComponent(bDos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(bIgual, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(bTres, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(bSuma, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(bResta, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(bCinco, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(bSeis, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(bMultiplica, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(bSiete, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(bOcho, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(bNueve, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bDividir, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(casilla, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(casilla, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bElevar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCambio))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bParAbierto, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bParCerrado, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bSiete, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bOcho, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bNueve, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bDividir, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bCuatro, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bCinco, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSeis, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bMultiplica, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(bResta, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(55, 55, 55))
                        .addComponent(bSuma, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(bUno, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bDos, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bTres, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(10, 10, 10)
                            .addComponent(bCero, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(bPunto, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bIgual, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bTresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTresActionPerformed
        this.casilla.setText(casilla.getText()+"3");
    }//GEN-LAST:event_bTresActionPerformed

    private void bUnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUnoActionPerformed
        this.casilla.setText(casilla.getText()+"1");
    }//GEN-LAST:event_bUnoActionPerformed
    
    private void bCeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCeroActionPerformed
        this.casilla.setText(casilla.getText()+"0");
    }//GEN-LAST:event_bCeroActionPerformed

    private void bElevarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bElevarActionPerformed
        this.casilla.setText(casilla.getText()+"^");
    }//GEN-LAST:event_bElevarActionPerformed

    private void bCuatroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCuatroActionPerformed
        this.casilla.setText(casilla.getText()+"4");
    }//GEN-LAST:event_bCuatroActionPerformed

    private void bPuntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPuntoActionPerformed
        this.casilla.setText(casilla.getText()+".");
    }//GEN-LAST:event_bPuntoActionPerformed
    
    /**
     * <pre>
     * Tras ser ejecutado el '=' con un click del mouse, se toma la cadena escrita en el TextBox para poder ser traducida a notación postfija y posteriormente calcular el resultado 
     * Previo a los calculos, la cadena pasa por una revision de sintaxis
     * Refleja el resultado o mensaje de error en el text box
     * </pre>
     */
    private void bIgualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIgualActionPerformed
        this.cadena = this.casilla.getText();
        this.casilla.setText("");
        double resultado; 
        if(!cadena.equals("") && !cadena.equals("Syntax Error")){
            if(cadena.length() > 10 && (cadena.substring(0,10).equals("Math Error") || cadena.substring(0,12).equals("Syntax Error")))
                this.casilla.setText("");
            else{
                if (analisisExpresion(cadena)) {
                    String[] post = infijaAPostfija(cadena);
                    resultado = calculaTotal(post);
                    try {
                        if(resultado == 1.0/0)
                            this.casilla.setText("Math Error");
                        else
                            this.casilla.setText(calculaTotal(post) + "");
                    } catch (Exception e) {
                        this.casilla.setText("Error en el cálculo");
                    }
                } else
                    this.casilla.setText("Syntax Error");
            }
        }
    }//GEN-LAST:event_bIgualActionPerformed

    private void bSumaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSumaActionPerformed
        this.casilla.setText(casilla.getText()+"+");
    }//GEN-LAST:event_bSumaActionPerformed

    private void bRestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRestaActionPerformed
        this.casilla.setText(casilla.getText()+"-");
    }//GEN-LAST:event_bRestaActionPerformed

    private void bDosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDosActionPerformed
        this.casilla.setText(casilla.getText()+"2");
    }//GEN-LAST:event_bDosActionPerformed

    private void bCincoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCincoActionPerformed
        this.casilla.setText(casilla.getText()+"5");
    }//GEN-LAST:event_bCincoActionPerformed

    private void bSeisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSeisActionPerformed
        this.casilla.setText(casilla.getText()+"6");
    }//GEN-LAST:event_bSeisActionPerformed

    private void bSieteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSieteActionPerformed
        this.casilla.setText(casilla.getText()+"7");
    }//GEN-LAST:event_bSieteActionPerformed

    private void bOchoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOchoActionPerformed
        this.casilla.setText(casilla.getText()+"8");
    }//GEN-LAST:event_bOchoActionPerformed

    private void bNueveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNueveActionPerformed
        this.casilla.setText(casilla.getText()+"9");
    }//GEN-LAST:event_bNueveActionPerformed

    private void bParAbiertoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bParAbiertoActionPerformed
        this.casilla.setText(casilla.getText()+"(");
    }//GEN-LAST:event_bParAbiertoActionPerformed

    private void bParCerradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bParCerradoActionPerformed
        this.casilla.setText(casilla.getText()+")");
    }//GEN-LAST:event_bParCerradoActionPerformed

    private void bMultiplicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMultiplicaActionPerformed
        this.casilla.setText(casilla.getText()+"*");
    }//GEN-LAST:event_bMultiplicaActionPerformed

    private void bDividirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDividirActionPerformed
        this.casilla.setText(casilla.getText()+"/");
    }//GEN-LAST:event_bDividirActionPerformed

    private void bBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBorrarActionPerformed
        this.casilla.setText("");
    }//GEN-LAST:event_bBorrarActionPerformed

    private void bCambioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCambioActionPerformed
        try{
            this.casilla.setText((Double.parseDouble(casilla.getText()) * -1) + "");
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_bCambioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AppCalc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppCalc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppCalc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppCalc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppCalc().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBorrar;
    private javax.swing.JButton bCambio;
    private javax.swing.JButton bCero;
    private javax.swing.JButton bCinco;
    private javax.swing.JButton bCuatro;
    private javax.swing.JButton bDividir;
    private javax.swing.JButton bDos;
    private javax.swing.JButton bElevar;
    private javax.swing.JButton bIgual;
    private javax.swing.JButton bMultiplica;
    private javax.swing.JButton bNueve;
    private javax.swing.JButton bOcho;
    private javax.swing.JButton bParAbierto;
    private javax.swing.JButton bParCerrado;
    private javax.swing.JButton bPunto;
    private javax.swing.JButton bResta;
    private javax.swing.JButton bSeis;
    private javax.swing.JButton bSiete;
    private javax.swing.JButton bSuma;
    private javax.swing.JButton bTres;
    private javax.swing.JButton bUno;
    private javax.swing.JLabel casilla;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
