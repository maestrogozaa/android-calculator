package edu.upc.eetac.dsa.calculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // ESTADO DE LA CALCULADORA
    private double acumulado = 0;
    private String operacionPendiente = null;
    private boolean nuevoNumero = true;
    private boolean modoGrados = true;
    private boolean hayOperandoActual = true;

    // INTERFAZ

    private TextView pantallaResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        pantallaResultado = findViewById(R.id.pantallaResultado);

        /*
         * ============================================================
         * GRADOS / RADIANES
         * ============================================================
         */

        Switch switchGrados = findViewById(R.id.switchGrados);

        switchGrados.setChecked(modoGrados);
        switchGrados.setText("DEG");

        switchGrados.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    modoGrados = isChecked;

                    if (modoGrados) {
                        switchGrados.setText("DEG");
                    } else {
                        switchGrados.setText("RAD");
                    }
                }
        );

        /*
         * ============================================================
         * BOTONES NUMÉRICOS
         * ============================================================
         */

        int[] idsBotonesNumeros = {
                R.id.boton0,
                R.id.boton1,
                R.id.boton2,
                R.id.boton3,
                R.id.boton4,
                R.id.boton5,
                R.id.boton6,
                R.id.boton7,
                R.id.boton8,
                R.id.boton9
        };

        for (int id : idsBotonesNumeros) {

            Button boton = findViewById(id);

            boton.setOnClickListener(v -> {

                String digito = boton.getText().toString();

                introducirDigito(digito);
            });
        }

        /*
         * ============================================================
         * OPERADORES + - × ÷
         * ============================================================
         */

        int[] idsBotonesOperadores = {
                R.id.botonSumar,
                R.id.botonRestar,
                R.id.botonMultiplicar,
                R.id.botonDividir
        };

        for (int id : idsBotonesOperadores) {

            Button boton = findViewById(id);

            boton.setOnClickListener(v -> {

                String operador = boton.getText().toString();

                seleccionarOperacion(operador);
            });
        }

        /*
         * ============================================================
         * SIN COS TAN
         * ============================================================
         */

        int[] idsFuncionesTrigonometricas = {
                R.id.botonSin,
                R.id.botonCos,
                R.id.botonTan
        };

        for (int id : idsFuncionesTrigonometricas) {

            Button boton = findViewById(id);

            boton.setOnClickListener(v -> {

                String funcion = boton.getText().toString();

                aplicarTrigonometria(funcion);
            });
        }

        /*
         * ============================================================
         * =
         * ============================================================
         */

        Button botonCalcular = findViewById(R.id.botonCalcular);

        botonCalcular.setOnClickListener(v -> {
            calcularResultado();
        });

        /*
         * ============================================================
         * AC
         * ============================================================
         */

        Button botonReset = findViewById(R.id.botonAC);

        botonReset.setOnClickListener(v -> {
            limpiar();
        });
    }

    /*
     * ================================================================
     * INTRODUCIR DÍGITO
     * ================================================================
     */

    private void introducirDigito(String digito) {

        if (nuevoNumero) {

            pantallaResultado.setText(digito);

            nuevoNumero = false;

        } else {

            String numeroActual = pantallaResultado.getText().toString();

            pantallaResultado.setText(numeroActual + digito);
        }

        hayOperandoActual = true;
    }

    /*
     * ================================================================
     * SELECCIONAR OPERADOR
     * ================================================================
     */

    private void seleccionarOperacion(String operador) {

        if (operacionPendiente == null) {

            if (!hayOperandoActual) {
                return;
            }

            acumulado = leerPantalla();

        } else {

            if (hayOperandoActual) {

                double numeroActual = leerPantalla();

                acumulado = calcular(acumulado,numeroActual,operacionPendiente);

                mostrarNumero(acumulado);
            }
        }

        operacionPendiente = operador;

        nuevoNumero = true;

        hayOperandoActual = false;
    }

    /*
     * ================================================================
     * =
     * ================================================================
     */

    private void calcularResultado() {

        if (operacionPendiente == null) {
            return;
        }

        if (!hayOperandoActual) {
            return;
        }

        double numeroActual = leerPantalla();

        acumulado = calcular(acumulado,numeroActual,operacionPendiente);

        mostrarNumero(acumulado);

        operacionPendiente = null;

        nuevoNumero = true;

        hayOperandoActual = true;
    }

    /*
     * ================================================================
     * CÁLCULO BINARIO
     * ================================================================
     */

    private double calcular(double numero1,double numero2,String operador) {

        switch (operador) {

            case "+":
                return numero1 + numero2;

            case "-":
                return numero1 - numero2;

            case "×":
                return numero1 * numero2;

            case "÷":
                return numero1 / numero2;

            default:
                return numero2;
        }
    }


    /*
     * ================================================================
     * SIN / COS / TAN
     * ================================================================
     */

    private void aplicarTrigonometria(String funcion) {

        if (!hayOperandoActual) {
            return;
        }

        double numero = leerPantalla();

        double angulo = numero;

        if (modoGrados) {
            angulo = Math.toRadians(numero);
        }

        double resultado;

        switch (funcion) {

            case "SIN":
                resultado = Math.sin(angulo);
                break;

            case "COS":
                resultado = Math.cos(angulo);
                break;

            case "TAN":
                resultado = Math.tan(angulo);
                break;

            default:
                return;
        }

        mostrarNumero(resultado);

        hayOperandoActual = true;

        nuevoNumero = true;
    }

    /*
     * ================================================================
     * AC
     * ================================================================
     */

    private void limpiar() {

        acumulado = 0;

        operacionPendiente = null;

        nuevoNumero = true;

        hayOperandoActual = true;

        pantallaResultado.setText("0");
    }

    /*
     * ================================================================
     * LEER PANTALLA
     * ================================================================
     */

    private double leerPantalla() {

        String texto = pantallaResultado.getText().toString();

        return Double.parseDouble(texto);
    }

    /*
     * ================================================================
     * MOSTRAR NÚMERO
     * ================================================================
     */

    private void mostrarNumero(double numero) {

        if (numero == Math.rint(numero)) {

            pantallaResultado.setText(String.valueOf((long) numero));

        } else {

            pantallaResultado.setText(String.valueOf(numero));
        }
    }
}