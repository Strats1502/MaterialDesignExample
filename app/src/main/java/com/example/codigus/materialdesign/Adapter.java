package com.example.codigus.materialdesign;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by codigus on 07/08/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterViewHolder> {
    private Context context;
    private ArrayList<Tarjeta> tarjetas;

    public Adapter(Context context, ArrayList<Tarjeta> tarjetas) {
        this.context = context;
        this.tarjetas = tarjetas;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_tarjeta, parent, false));
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder holder, int position) {
        holder.textViewLetraInicial.setText(String.valueOf(tarjetas.get(position).getNombre().charAt(0)));
        holder.textViewLetraInicial.setBackgroundColor(tarjetas.get(position).getColor());
        holder.textViewNombre.setText(tarjetas.get(position).getNombre());
    }

    @Override
    public void onViewAttachedToWindow(AdapterViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        animateCircularReveal(viewHolder.itemView);
    }

    @Override
    public int getItemCount() {
        if (tarjetas != null) {
            return tarjetas.size();
        } else {
            return 0;
        }
    }

    private void animateCircularReveal(View view) {
        int centerX = 0;
        int centerY = 0;
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(), view.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
        view.setVisibility(View.VISIBLE);
        animation.start();
    }

    private void animateCircularDelete(final View view, final int listPosition) {
        int centerX = view.getWidth();
        int centerY = view.getHeight();
        int startRadius = view.getWidth();
        int endRadius = 0;
        Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                view.setVisibility(View.INVISIBLE);
                tarjetas.remove(listPosition);
                notifyItemRemoved(listPosition);
            }
        });

        animator.start();
    }

    public void addCard(String name, int color) {
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setId(getItemCount());
        tarjeta.setNombre(name);
        tarjeta.setColor(color);
        tarjetas.add(tarjeta);
        ((MainActivity) context).doSmoothScroll(getItemCount());
        notifyItemInserted(getItemCount());
    }

    public void updateCard(String name, int listPosition) {
        tarjetas.get(listPosition).setNombre(name);
        notifyItemChanged(listPosition);
    }

    public void deleteCard(View view, int listPosition) {
        animateCircularDelete(view, listPosition);
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLetraInicial;
        TextView textViewNombre;
        Button botonBorrar;

        AdapterViewHolder(View item) {
            super(item);

            textViewLetraInicial = (TextView) item.findViewById(R.id.textview_letra_inicial);
            textViewNombre = (TextView) item.findViewById(R.id.textview_nombre);
            botonBorrar = (Button) item.findViewById(R.id.boton_borrar);

            botonBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCard(itemView, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pair<View, String> pair1 = Pair.create((View) textViewLetraInicial, MainActivity.TRANSITION_INITIAL);
                    Pair<View, String> pair2 = Pair.create((View) textViewNombre, MainActivity.TRANSITION_NAME);
                    Pair<View, String> pair3 = Pair.create((View) botonBorrar, MainActivity.TRANSITION_DELETE_BUTTON);

                    ActivityOptionsCompat optionsCompat;
                    Activity activity = (AppCompatActivity) context;
                    optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair1, pair2, pair3);

                    int requestCode = getAdapterPosition();
                    String name = tarjetas.get(requestCode).getNombre();
                    int color = tarjetas.get(requestCode).getColor();

                    Intent transitionIntent = new Intent(context, TransitionEditActivity.class);
                    transitionIntent.putExtra(MainActivity.EXTRA_NAME, name);
                    transitionIntent.putExtra(MainActivity.EXTRA_INITIAL, Character.toString(name.charAt(0)));
                    transitionIntent.putExtra(MainActivity.EXTRA_COLOR, color);
                    transitionIntent.putExtra(MainActivity.EXTRA_UPDATE, false);
                    transitionIntent.putExtra(MainActivity.EXTRA_DELETE, false);
                    ((AppCompatActivity) context).startActivityForResult(transitionIntent, requestCode, optionsCompat.toBundle());

                }
            });

        }

    }
}
