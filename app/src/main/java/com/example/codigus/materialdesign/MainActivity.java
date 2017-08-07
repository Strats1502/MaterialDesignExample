package com.example.codigus.materialdesign;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int[] colores;
    private String[] nombres;
    private ArrayList<Tarjeta> tarjetas;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private FloatingActionButton floatingActionButton;

    public static final String EXTRA_UPDATE = "extra_update";
    public static final String EXTRA_DELETE = "extra_delete";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_INITIAL = "extra_initial";
    public static final String EXTRA_COLOR = "extra_color";

    public static final String TRANSITION_FAB = "fab_transition";
    public static final String TRANSITION_INITIAL = "initial_transition";
    public static final String TRANSITION_NAME = "name_transition";
    public static final String TRANSITION_DELETE_BUTTON = "delete_button_transition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colores = getResources().getIntArray(R.array.colores);
        nombres = getResources().getStringArray(R.array.nombres);
        tarjetas = new ArrayList<>();

        generarTarjetas();

        adapter = new Adapter(this, tarjetas);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_tarjetas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingactionbutton_añadir);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair<View, String> pair = Pair.create(view.findViewById(R.id.floatingactionbutton_añadir), "fab_transition");
                ActivityOptionsCompat options;
                Activity activity = MainActivity.this;
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair);

                Intent transitionIntent = new Intent(activity, TransitionAddActivity.class);
                activity.startActivityForResult(transitionIntent, adapter.getItemCount(), options.toBundle());
            }
        });

    }

    private void generarTarjetas() {
        for (int i = 0; i < colores.length; i++) {
            Tarjeta tarjeta = new Tarjeta();
            tarjeta.setId((long) i);
            tarjeta.setNombre(nombres[i]);
            tarjeta.setColor(colores[i]);
            tarjetas.add(tarjeta);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == adapter.getItemCount()) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra(EXTRA_NAME);
                int color = data.getIntExtra(EXTRA_COLOR, 0);
                adapter.addCard(name, color);
            }
        } else {
            if (resultCode == RESULT_OK) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(requestCode);
                if (data.getExtras().getBoolean(EXTRA_DELETE, false)) {
                    adapter.deleteCard(viewHolder.itemView, requestCode);
                } else if (data.getExtras().getBoolean(EXTRA_UPDATE)) {
                    String name = data.getStringExtra(EXTRA_NAME);
                    viewHolder.itemView.setVisibility(View.INVISIBLE);
                    adapter.updateCard(name, requestCode);
                }
            }
        }
    }

    public void doSmoothScroll(int position) {
        recyclerView.smoothScrollToPosition(position);
    }
}
