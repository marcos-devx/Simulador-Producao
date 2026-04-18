package com.marcos.meuapp;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText edtQuantidade, edtHorasDia, edtCustoHora;
    private EditText edtTempoM1, edtEfM1, edtQtdM1;
    private EditText edtTempoM2, edtEfM2, edtQtdM2;

    private TextView txtResultado;
    private Button btnCalcular;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializar();

        btnCalcular.setOnClickListener(v -> calcular());
    }

    private void inicializar() {

        edtQuantidade = findViewById(R.id.edtQuantidade);
        edtHorasDia = findViewById(R.id.edtHorasDia);
        edtCustoHora = findViewById(R.id.edtCustoHora);

        edtTempoM1 = findViewById(R.id.edtTempoM1);
        edtEfM1 = findViewById(R.id.edtEfM1);
        edtQtdM1 = findViewById(R.id.edtQtdM1);

        edtTempoM2 = findViewById(R.id.edtTempoM2);
        edtEfM2 = findViewById(R.id.edtEfM2);
        edtQtdM2 = findViewById(R.id.edtQtdM2);

        txtResultado = findViewById(R.id.txtResultado);
        btnCalcular = findViewById(R.id.btnCalcular);
        barChart = findViewById(R.id.barChart);
    }

    private void calcular() {

        if (camposInvalidos()) {
            txtResultado.setText("Preencha todos os campos.");
            return;
        }

        // Dados gerais
        double Q = Double.parseDouble(edtQuantidade.getText().toString());
        double H = Double.parseDouble(edtHorasDia.getText().toString());
        double C = Double.parseDouble(edtCustoHora.getText().toString());

        // Linha 1 (operadores)
        double t1 = Double.parseDouble(edtTempoM1.getText().toString());
        double e1 = Double.parseDouble(edtEfM1.getText().toString()) / 100;
        double op1 = Double.parseDouble(edtQtdM1.getText().toString());

        // Linha 2 (operadores)
        double t2 = Double.parseDouble(edtTempoM2.getText().toString());
        double e2 = Double.parseDouble(edtEfM2.getText().toString()) / 100;
        double op2 = Double.parseDouble(edtQtdM2.getText().toString());

        // Tempo disponível
        double tempoDia = H * 60;

        // Produção diária
        double prod1 = (tempoDia * e1 * op1) / t1;
        double prod2 = (tempoDia * e2 * op2) / t2;

        // Tempo total estimado (usando a melhor linha)
        double melhorProd = Math.max(prod1, prod2);
        double dias = Q / melhorProd;

        // Custo
        double custo = dias * H * C;

        // Comparação
        String melhor;
        if (prod1 > prod2) {
            melhor = "Linha 1 é mais produtiva";
        } else if (prod2 > prod1) {
            melhor = "Linha 2 é mais produtiva";
        } else {
            melhor = "As duas linhas têm mesma produtividade";
        }

        // Resultado final
        String resultado =
                "Produção Linha 1: " + (int) prod1 + " peças/dia\n" +
                        "Produção Linha 2: " + (int) prod2 + " peças/dia\n\n" +
                        melhor + "\n\n" +
                        "Dias necessários: " + String.format("%.2f", dias) + "\n" +
                        "Custo total: R$ " + String.format("%.2f", custo);

        txtResultado.setText(resultado);

        gerarGrafico(prod1, prod2);
    }

    private void gerarGrafico(double prod1, double prod2) {

        ArrayList<BarEntry> entradas = new ArrayList<>();
        entradas.add(new BarEntry(1, (float) prod1));
        entradas.add(new BarEntry(2, (float) prod2));

        BarDataSet dataSet = new BarDataSet(entradas, "Produção por Linha");

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.invalidate();
    }

    private boolean camposInvalidos() {
        return edtQuantidade.getText().toString().isEmpty() ||
                edtHorasDia.getText().toString().isEmpty() ||
                edtCustoHora.getText().toString().isEmpty() ||
                edtTempoM1.getText().toString().isEmpty() ||
                edtEfM1.getText().toString().isEmpty() ||
                edtQtdM1.getText().toString().isEmpty() ||
                edtTempoM2.getText().toString().isEmpty() ||
                edtEfM2.getText().toString().isEmpty() ||
                edtQtdM2.getText().toString().isEmpty();
    }
}