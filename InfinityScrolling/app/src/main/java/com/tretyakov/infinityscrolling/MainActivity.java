package com.tretyakov.infinityscrolling;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    InfinityScrolling infinityScrolling;
    ArrayList<String> rowsArrayList = new ArrayList<>();

    boolean isLoading = false;
    //создаем экземпляр адаптера
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        populateData();
        initAdapter();
        initScrollListener();


    }

    private void populateData() {
        int i = 0;
        while (i < 10) {
            rowsArrayList.add("Бесконечная прокрутка " + i);
            i++;
        }
    }

    private void initAdapter() {

        infinityScrolling = new InfinityScrolling(rowsArrayList);
        recyclerView.setAdapter(infinityScrolling);
    }
//В initScrollListener проверяем прокрученное состояние RecyclerView
// если виден самый нижний элемент
// то показываем представление загрузки
// и заполняем следующий список
    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        rowsArrayList.add(null);
        infinityScrolling.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                infinityScrolling.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Бесконечная прокрутка " + currentSize);
                    currentSize++;
                }

                infinityScrolling.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }
}
