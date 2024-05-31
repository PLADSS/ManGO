package com.example.mango;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> book_id, book_title, book_author, book_pages, book_pdf;

    CustomAdapter(Context context, ArrayList<String> book_id, ArrayList<String> book_title, ArrayList<String> book_author, ArrayList<String> book_pages, ArrayList<String> book_pdf) {
        this.context = context;
        this.book_id = book_id;
        this.book_title = book_title;
        this.book_author = book_author;
        this.book_pages = book_pages;
        this.book_pdf = book_pdf;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.book_id_txt.setText(book_id.get(position));
        holder.book_title_txt.setText(book_title.get(position));
        holder.book_author_txt.setText(book_author.get(position));
        holder.book_pages_txt.setText(book_pages.get(position));
        String pdfPath = book_pdf.get(position);

        holder.edit_button.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("id", book_id.get(position));
            intent.putExtra("title", book_title.get(position));
            intent.putExtra("author", book_author.get(position));
            intent.putExtra("pages", book_pages.get(position));
            intent.putExtra("pdfUri", pdfPath);
            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            if (pdfPath != null && !pdfPath.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(pdfPath), "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "PDF Bulunamadı", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return book_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView book_id_txt, book_title_txt, book_author_txt, book_pages_txt;
        ImageButton edit_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id_txt = itemView.findViewById(R.id.book_id_txt);
            book_title_txt = itemView.findViewById(R.id.book_title_txt);
            book_author_txt = itemView.findViewById(R.id.book_author_txt);
            book_pages_txt = itemView.findViewById(R.id.book_pages_txt);
            edit_button = itemView.findViewById(R.id.edit_button);
        }
    }
}
