package com.example.codigus.materialdesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by codigus on 07/08/2017.
 */

public class TransitionEditActivity extends AppCompatActivity {
    private TextView textViewLetraInicial;
    private Button botonActualizar;
    private Button botonEliminar;
    private EditText editTextNombre;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_edit);

        textViewLetraInicial = (TextView) findViewById(R.id.textview_letra_inicial);
        botonActualizar = (Button) findViewById(R.id.boton_actualizar);
        botonEliminar = (Button) findViewById(R.id.boton_borrar);
        editTextNombre = (EditText) findViewById(R.id.edittext_nombre);

        intent = getIntent();

        String nameExtra = intent.getStringExtra(MainActivity.EXTRA_NAME);
        final String initialExtra = intent.getStringExtra(MainActivity.EXTRA_INITIAL);
        int colorExtra = intent.getIntExtra(MainActivity.EXTRA_COLOR, 0);

        editTextNombre.setText(nameExtra);
        textViewLetraInicial.setText(initialExtra);
        textViewLetraInicial.setBackgroundColor(colorExtra);

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
                    intent.putExtra(MainActivity.EXTRA_UPDATE, true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        botonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textViewLetraInicial.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    Snackbar.make(v, "Ingresa un nombre", Snackbar.LENGTH_LONG).show();
                } else {
                    intent.putExtra(MainActivity.EXTRA_UPDATE, true);
                    intent.putExtra(MainActivity.EXTRA_NAME, String.valueOf(editTextNombre.getText()));
                    intent.putExtra(MainActivity.EXTRA_INITIAL, String.valueOf(editTextNombre.getText().charAt(0)));
                    setResult(RESULT_OK, intent);
                    supportFinishAfterTransition();
                }
            }
        });

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra(MainActivity.EXTRA_DELETE, true);
                setResult(RESULT_OK, intent);
                supportFinishAfterTransition();
            }
        });

    }
}
