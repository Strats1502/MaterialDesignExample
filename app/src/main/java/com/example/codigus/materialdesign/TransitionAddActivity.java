package com.example.codigus.materialdesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import java.util.Random;

/**
 * Created by codigus on 07/08/2017.
 */

public class TransitionAddActivity extends AppCompatActivity{
    private TextView textViewLetraInicial;
    private EditText editTextNombre;
    private Button botonAgregar;
    private Intent intent;
    private int color;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_transition_add);

        intent = getIntent();
        textViewLetraInicial = (TextView) findViewById(R.id.textview_inicial);
        editTextNombre = (EditText) findViewById(R.id.edittext_nombre);
        botonAgregar = (Button) findViewById(R.id.boton_agregar);

        editTextNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    textViewLetraInicial.setText("");
                } else if (s.length() >= 1) {
                    textViewLetraInicial.setText(String.valueOf(s.charAt(0)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Random random = new Random();
        int[] colors = getResources().getIntArray(R.array.colores);
        color = colors[random.nextInt(colors.length)];

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = textViewLetraInicial.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    Snackbar.make(view, "Ingresa un nombre", Snackbar.LENGTH_LONG).show();
                } else {
                    intent.putExtra(MainActivity.EXTRA_NAME, String.valueOf(editTextNombre.getText()));
                    intent.putExtra(MainActivity.EXTRA_INITIAL, String.valueOf(textViewLetraInicial.getText()));
                    intent.putExtra(MainActivity.EXTRA_COLOR, color);
                    setResult(RESULT_OK, intent);
                    supportFinishAfterTransition();
                }
            }
        });

    }

}
